# SystemHelper 3.0.1 安全与隐私分析报告

**样本** `SystemHelper_3.0.1-20250311.apk`
**SHA-256** `5267008ede808c467988df588915791d21a6f1f3fbe219c57b125a235000df4f`
**包名** `com.mobiletools.systemhelper` · versionCode 31 · minSdk 31 · targetSdk 33
**分析日期** 2026-11-05
**方法** 静态逆向（jadx 1.5.6）、清单与资源检查、签名证书分析、上报协议离线复现、端点非侵入式指纹识别。分析期间无可用真机，未做动态插桩。
**样本性质** 中国联通设备管理（DM）客户端，系统预装应用，无启动图标，无用户可见的授权流程。

> 本文件为 `REPORT.md` 的中文对照版。技术名词、字段名、代码路径保留英文原文以便核对。

---

## 0. 结论摘要

该应用是运营商设备管理平台的静默遥测代理。它采集硬件标识、用户标识（IMEI、IMSI、ICCID）、无线与小区数据、精确经纬度，以及完整的已安装应用清单（含每应用使用时长与流量），然后通过单一硬编码明文 HTTP 端点上报。

七条判断决定整体风险：

1. **没有客户端身份验证。** `sign` 参数并非签名，而是用 APK 内置公钥对 `MD5(payload)` 做 RSA 加密。任何持有该 APK 的一方可构造格式合法的上报。服务端无法区分真机与伪造客户端。
2. **一个导出的、无权限保护的 Binder 服务允许任意已安装应用改写上报地址**（`serverAddress`）、修改采集周期、打开 IMEI/IMSI/坐标的详细日志、清空本地队列。配合一个可被显式 Intent 触发的导出广播接收器，构成可按需触发的数据外流通道。
3. **传输为明文 HTTP**（18080 端口），应用级 `android:usesCleartextTraffic="true"`。型号、厂商、协议版本、源 IP、时序等元数据明文暴露；报文体有加密信封，但信封本身强度不足（见 §9）。
4. **"同意"是推断出来的，不是取得的。** 除 vivo 外，所有厂商分支的判定条件是 `Settings.Global.device_provisioned == 1`，即"开机向导已完成"；运营商自身的 `operator_service_improve_plan` 开关只在 vivo 上生效。APK 内不存在隐私声明、授权界面或政策链接。
5. **报文保护使用 MD5 作 KDF、AES-128-ECB、RSA-1024。** 这只是一个保密信封，不构成任何认证机制，且不符合现代密码学标准。
6. **无法通过应用协议从端点读回数据。** 客户端只会执行 `POST /register`，不存在查询接口；源站前置 WAF 对来自家庭宽带的全部未认证请求以 `reason:UrlACL` 拒绝。该结论基于客户端行为与边缘行为，不是服务端审计。
7. **流量可被截获、抑制与重放。** 被动中间人可取得全部元数据，但在缺少额外信息时无法解出报文体；主动攻击者可用 `200` 静默吞掉一次上报，或原样重放已捕获的请求。

---

## 1. 范围与方法

| 项目 | 内容 |
|---|---|
| 目标 | `SystemHelper_3.0.1-20250311.apk`（3,261,815 字节） |
| 来源 | 送检方提供的、从小米（MIUI）手机提取的 APK 镜像；与 MIUI 平台签名、小米专有代码路径、无启动入口等特征一致 |
| 反编译器 | jadx 1.5.6（内置 JRE 25） |
| 还原类数 | 585 个 Java 文件；20 个业务类（`com.mobiletools.*`）+ 28 个混淆支撑类（`c.d.a`） |
| 原生库 | `liblocSDK8b.so`（368,568 B）、`libindoor.so`（1,596,896 B），均为百度 LBS，已裁剪/加固 |
| 动态测试 | 无（无真机/模拟器） |
| 网络测试 | DNS、TCP 连接、`GET /`、`OPTIONS /register`、`HEAD /register` 各一次；**未发送 POST，未提交载荷，未使用凭据，未做路径枚举** |
| 证据 | `evidence/`：清单、反编译源码、字符串表、证书数据、探测记录、复现请求 |

复现步骤见 `docs/METHOD.md`；本文中的 `文件:行号` 指向 `evidence/decompiled/`。

---

## 2. 应用画像

### 2.1 身份与部署位置

- 无 `LAUNCHER` intent-filter：**桌面无图标、无入口**。唯一 Activity 是导出的诊断界面 `Activity.TestActivity`。
- `android:sharedUserId="com.mobiletools.systemhelper.uid"`，预期与同族应用共享 UID。
- 申请 `READ_PRIVILEGED_PHONE_STATE`、`PACKAGE_USAGE_STATS`、`QUERY_ALL_PACKAGES`、`READ_PRECISE_PHONE_STATE`：这些权限只授予特权/系统应用或需特殊访问授权。结合下述签名，可判定为**系统预装应用**，而非用户自行安装。

### 2.2 签名证书

| 字段 | 值 |
|---|---|
| Subject / Issuer | `EMAILADDRESS=miui@xiaomi.com, CN=MIUI, OU=MIUI, O=Xiaomi, L=Beijing, ST=Beijing, C=CN` |
| 序列号 | `0xe552a8ecb9011b7c` |
| 有效期 | 2011-12-06 → 2039-04-23 |
| 密钥 | RSA 2048，`sha1WithRSAEncryption` |
| SHA-1 | `7B:6D:C7:07:9C:34:73:9C:E8:11:59:71:9F:B5:EB:61:D2:A0:32:25` |
| SHA-256 | `C9:00:9D:01:EB:F9:F5:D0:30:2B:C7:1B:2F:E9:AA:9A:47:A4:32:BB:A1:73:08:A3:11:1B:75:D7:B2:14:90:25` |

这是 MIUI 平台证书，也用于小米系统应用。其 SHA-1 已与 Mi Share、CIT 等应用的公开签名记录交叉核对，摘要一致。两点推论：

- 在 MIUI 系 ROM 上，该 APK 可通过平台签名校验，签名级权限与 `sharedUserId` 均生效，实际权限远高于普通三方应用。
- 同一包名内包含小米、OPPO、vivo、三星、realme、中兴、努比亚、一加、WIKO、PTAC、TDTECH、"Liantong" 等多厂商适配器。单一签名密钥不可能是所有这些厂商的平台密钥，因此**本样本是同一套多厂商代码中的某一个厂商构建**。信任锚是厂商相关的，不能泛化。

### 2.3 组件

完整表格见 `evidence/components.csv`。

| 组件 | 类型 | 导出 | 权限 | 说明 |
|---|---|---|---|---|
| `Activity.TestActivity` | activity | **是** | 无 | 在屏幕上渲染完整设备信息 JSON（IMEI、IMSI、ICCID、小区、经纬度） |
| `control.OnBootReceiver` | receiver | **是** | 无 | 接收 `BOOT_COMPLETED`；**任意其他 action 的显式 Intent 会落入完整上报流程** |
| `control.SIMStateReceiver` | receiver | **是** | 无 | `SIM_STATE_CHANGED`；其他 action 的显式 Intent 同样进入上报路径 |
| `control.ConnectStateReceiver` | receiver | **是** | 无 | 受保护广播，风险低 |
| `service.RegisterHelperService` | service | **是** | 无 | 未鉴权 AIDL 接口，见 F-02 |
| `ScheduleRegisterReceiver`、`AppInfoRegisterReceiver`、`SignalInfoRegisterReceiver`、`GetSignalInfoReceiver` | receiver | 否 | 无 | 内部闹钟目标 |
| `service.RegisterIntentService`、`RegisterJobService` | service | 否 | — / `BIND_JOB_SERVICE` | 内部 |
| `com.baidu.location.f` | service | 否 | 无 | 内置 SDK，`:remote` 进程 |

### 2.4 权限（17 项）

```
ACCESS_BACKGROUND_LOCATION   ACCESS_COARSE_LOCATION   ACCESS_FINE_LOCATION
ACCESS_LOCATION_EXTRA_COMMANDS  ACCESS_NETWORK_STATE  ACCESS_WIFI_STATE
CHANGE_WIFI_STATE  FOREGROUND_SERVICE  INTERNET  PACKAGE_USAGE_STATS
QUERY_ALL_PACKAGES  READ_EXTERNAL_STORAGE  READ_PHONE_STATE
READ_PRECISE_PHONE_STATE  READ_PRIVILEGED_PHONE_STATE
RECEIVE_BOOT_COMPLETED  SCHEDULE_EXACT_ALARM
```

后台定位 + 特权电话状态 + 使用统计 + 全量包可见性，恰好是构建"用户标识 + 行为画像"所需的完整组合。

---

## 3. 采集内容

逐字段映射（来源 API、上传前处理）见 `evidence/data-fields.csv`。概要：

### 3.1 设备与用户标识

| 数据 | API | 上传前处理 |
|---|---|---|
| 厂商、型号、硬件版本、ROM/构建版本、OS 版本 | `Build.*`、`ro.*` 系统属性 | 无 |
| **IMEI 1 / IMEI 2** | `TelephonyManager.getImei(0/1)`；小米机型经反射调用 `miui.telephony.TelephonyManager.getImeiForSlot` | **无——完整 15 位** |
| **ICCID 1 / ICCID 2** | `getSimSerialNumber()` | 保留前 10 位，其余替换为 `*` |
| **IMSI 1 / IMSI 2** | `getSubscriberId()` | 保留前 10 位，其余替换为 `*` |
| MNC、网络制式、TAC、CID/NCI、PCI、ARFCN、频段、带宽 | `ServiceState`、`CellIdentityLte/Nr` | 无 |
| VoLTE / 5G / VoNR 开关、当前数据卡槽 | `ImsManager`、厂商 AIDL、系统属性 | 无 |
| IPv6 地址 | 枚举 `NetworkInterface` | 无（IPv4 留空） |
| RAM / ROM / 可用存储 | `ActivityManager`、`StatFs`、`ro.boot.dram_info` | 归档为容量档位 |

ICCID/IMSI 的遮蔽是有条件的：当 IMSI 以联通测试号段（`46001/46006/46009/46010`）开头时跳过遮蔽，直接上传完整值（`c/d/a/a/n.java:247-280`）。即便遮蔽，ICCID 保留 10 位，对于 19–20 位的 ICCID 而言仍有约一半明文留在报文中。

### 3.2 无线与位置遥测

每小时一次的采样任务驱动百度 LBS SDK（高精度、BD09LL），按卡槽记录：

`Time, IMEI, ICCID, IMSI, MNC, TAC, CID, PCI, NetworkType, ARFCN, Bands, Bandwidth, CQITableIndex, CQIIndex, RSRP, RSRQ, SINR, CSIRSRP, CSIRSRQ, CSISINR, Uspeed, Dspeed, CallDisconnectCause, DataFailCause, EPSFallback, LONGITUDE, LATITUDE, ALTITUDE, SPEED, BEARING`

（`c/d/a/b/n.java:52-102`、`control/d.java`、`control/GetSignalInfoReceiver.java`）这是一份与持久硬件标识绑定的完整 RF 路测记录，并包含用户精确位置——本应用中最敏感的数据集。

### 3.3 已安装应用清单与使用情况

`control/f.java` 枚举全部已安装应用，逐条记录：

`appLabel, pkgName, Time, runTime（前台分钟数）, tx1/rx1（移动数据）, tx2/rx2（第二卡槽）, totalData1, totalData2, totalDataWiFi`

数据来自 `UsageStatsManager.queryAndAggregateUsageStats()` 与按 UID 的 `NetworkStatsManager.querySummary()`。包名以 `com.android` 开头的应用被过滤（`c/d/a/a/n.java:37`），其余全部上传。这构成行为指纹：用户装了哪些应用、各用多久、各消耗多少流量。

### 3.4 本地持久化

应用私有目录下两个 SQLite 库：

| 库 | 表 | 上限 | 行为 |
|---|---|---|---|
| `systemhelper` | `dmregister(jsonString)` | 10 行 | 上报失败队列；`control/e.java` 取最早一条补报 |
| `signalhelper` | `signal(jsonString)` | 100 行 | RF/位置样本；以 `RegType=R` 批量上报，成功后清空 |

两个库均以**明文**落盘，包含 IMEI 与经纬度。虽在应用沙箱内，但 root 或同 UID 进程可直接读取——而 `sharedUserId` 恰恰允许同 UID 进程。

---

## 4. 采集与上报时机

```
BOOT_COMPLETED ──► OnBootReceiver ──(240 秒)──► OnBootReceiver（无 action）
                                                    ├─ 注册动态接收器（SIM / 网络）
                                                    ├─ RegType B（首次开机为 F）→ 完整设备上报
                                                    └─ 设置精确闹钟：
                                                         GetSignalInfo    每 ~12 小时（+随机偏移）
                                                         SignalInfo       每 ~12 小时（+随机偏移）→ RegType R
                                                         Schedule         每 ~24 小时（+随机偏移）→ RegType S
                                                         AppInfo          每 ~24 小时（+随机偏移）→ RegType A
SIM_STATE_CHANGED ──► SIMStateReceiver ──(180 秒)──► 比对 ICCID，仅在换卡时 RegType T
显式 Intent 指向 OnBootReceiver / SIMStateReceiver ──► RegType B / T，不受周期约束
```

默认周期硬编码于 `c/d/a/b/d.java:27-36`，并可在运行时经导出服务改写。RF/位置采样每小时一次；批量上报每日两次。

`RegType` 语义（`c/d/a/a/n.java:118-170`）：

| 代码 | 含义 | 报文段 |
|---|---|---|
| `F` | 安装后首次开机 | 设备信息 +（有权限时）应用清单 |
| `B` | 普通开机 | 设备信息 |
| `S` | 周期（24 小时） | 设备信息 |
| `T` | 检测到换卡 | 设备信息，仅当 ICCID 变化时发送 |
| `R` | 周期 RF/位置批量 | 设备信息 + `SigInfo[]` |
| `A` | 周期应用清单 | 设备信息 + `AppInfo[]` |

无论哪种类型，报文都携带完整设备身份块——即便"信号"上报也会重新声明 IMEI 与用户标识。

---

## 5. 上报机制

### 5.1 处理链路

```
b.a()                       构造 JSON（control/b.java）
  └─ MD5(json)               c.d.a.b.c.a(String)            → 16 字节 AES 密钥
  └─ AES/ECB/PKCS5(json)     c.d.a.b.c.a(String, byte[])    → 请求体
  └─ RSA/ECB/PKCS1(MD5)      c.d.a.b.c.a(Context, byte[])   → "sign"（Base64）
  └─ POST http://dm.wo.com.cn:18080/register
        ?ver=5.0&model=…&manuf=…&sign=…
        Content-Type: application/encrypted-json
```

实现位置：`control/g.java:29-63`（实时上报）、`control/e.java:21-62`（队列补报）、`c/d/a/b/f.java:24-94`（HTTP 客户端）、`c/d/a/b/c.java`（密码学原语）、`c/d/a/b/j.java`（RSA）。

### 5.2 线路格式（已离线复现验证）

`tools/repro_crypto.py` 使用 APK 中提取的公钥重建完整请求，并验证 AES 往返一致。输出见 `evidence/network/sample-request.txt`。**该请求从未发送。**

```
endpoint   : http://dm.wo.com.cn:18080/register
public key : CN=UNICOM / 1024-bit RSA
md5(json)  : b15a52c816eeb587d6cebb370e2d6b93   （AES-128 密钥）
aes mode   : AES/ECB/PKCS5Padding
body bytes : 688
sign bytes : 128                                  （单个 RSA 分组）
```

由复现可直接得到两个性质：

- 报文对同一明文是确定性的（ECB、无 IV、无 nonce）。
- `sign` 同样是确定性的（RSA PKCS#1 v1.5）。同一载荷永远产生同一 `sign`，观察者无需解密即获得稳定的关联标识。

### 5.3 响应处理

`c/d/a/b/f.java:43-50` 将 HTTP `200` 视为成功；调用方进一步把 `416` 也当作成功（`control/g.java:48-50`）。不解析响应体、不校验签名或 MAC；除重新入队 `dmregister` 外无重试策略。把 `416 Requested Range Not Satisfiable` 解释为"已送达"是逻辑缺陷：服务端拒绝被静默计为上报成功。

---

## 6. 端点

| 主机 | 端口 / 协议 | 用途 | 来源 |
|---|---|---|---|
| `dm.wo.com.cn` | **18080 / HTTP** | 设备注册与遥测上报 | 硬编码，`c/d/a/b/d.java:26` |
| `loc.map.baidu.com`、`ofloc.map.baidu.com`、`api.map.baidu.com`、`daup.map.baidu.com`、`itsdata.map.baidu.com`、`parking.baidu.com` | 443 / HTTPS | 百度 LBS 定位与配置 | 内置 SDK（`liblocSDK8b.so`、`libindoor.so`、`com.baidu.location.*`） |

一手端点**只有一个**。其地址由 `l.h(context)` 返回：默认 `d.l`，若 `serverAddress` 偏好已被写入则被覆盖——而该偏好只能通过导出服务写入（F-02）。

### 6.1 端点实测

```
dm.wo.com.cn  →  CNAME 63ce5fd2be92dcdf.icloudv6.com（联通 CDN）
               →  180.97.168.69 / 124.232.185.44 / 61.174.41.35
TCP/18080      →  三个地址均开放
GET /          →  403 Forbidden, WZWS-RAY: … reason:UrlACL
OPTIONS /register → 403 Forbidden, reason:UrlACL
HEAD /register → 403 Forbidden
```

`WZWS` 为 360 网站卫士 WAF；`UrlACL` 表示请求在到达应用之前即被路径级访问控制拒绝。来自家庭宽带地址的全部未认证请求均在边缘被拒。客户端只发 `POST /register`，因此该 ACL 很可能按方法/路径和/或蜂窝来源网段设限；无真机无法确认，且未做任何暴力枚举。原始记录：`evidence/network/probe-log.txt`。

---

## 7. 身份验证

### 7.1 客户端 → 服务端：无

请求中没有 `Authorization` 头、没有 token、没有 API key、没有设备证书、没有 TLS 客户端证书、没有基于共享密钥的 HMAC。唯二的设备相关值（IMEI、IMSI、ICCID）位于报文**内部**，属自证。

### 7.2 `sign` 的实际含义

```
key   = MD5(plaintext JSON)              // 16 字节
body  = AES-128-ECB(key, plaintext)
sign  = Base64(RSA-1024-PKCS1v1.5(pub.cer, key))
```

`pub.cer` 是随 `assets/` 分发的 1024 位 RSA 证书（`CN=UNICOM`，2019→2119）。用**公钥**加密 MD5 摘要意味着：

- AES 密钥的保密性完全取决于联通私钥的保密性；
- **完整性与真实性根本未被建立**——任何人可自行计算 `MD5(json)`、用公钥加密、构造合法请求；
- 摘要算法是 MD5，即便服务端做完整性校验也偏弱。

这是加密，不是签名。把参数命名为 `sign` 具有误导性；从评估角度，该方案**不提供任何客户端认证与不可否认性**。

### 7.3 结论表

| 属性 | 状态 |
|---|---|
| 客户端认证 | 缺失 |
| 报文真实性 | 缺失（任何持有 APK 者可伪造） |
| 重放防护 | 缺失（无 nonce；客户端不校验时间戳，服务端行为未知） |
| 报文机密性 | 取决于 RSA-1024 私钥与明文熵 |
| 传输完整性 | 缺失（无 TLS、无 MAC） |
| 服务端认证 | 缺失（HTTP） |

---

## 8. 能否从端点获取数据

**通过应用协议：不能。** 客户端只执行一种操作——`POST /register`。48 个一手类中不存在任何读取、查询、列表或导出调用；DEX 与原生库中也没有备用主机、管理路径或调试端点。

**通过公网边缘：未观察到。** 所有来自非蜂窝地址的未认证请求均被 WAF 以 `UrlACL` 在应用层之前拒绝。未做路径枚举，因此本报告**不**声称服务端不存在脆弱接口，只说明客户端未暴露任何读取接口，且已发出的请求被边缘拦截。

**但写入侧不受控。** 由于任何人持 APK 即可生成 `sign`（§7），能到达端点的一方（例如处于允许网段内）可提交任意设备记录。现实影响是污染运营商设备库、消耗资源、探测端点解析器。本次未尝试。

**服务端存储不在范围内。** 记录是否留存、留存多久、访问控制如何、是否存在可导出的内部控制台，均无法从 APK 判定。

---

## 9. 流量能否被截取

### 9.1 被动截获

传输为明文 HTTP，同网 Wi-Fi、ISP、骨干网或恶意热点的中间人可完整捕获请求。

| 元素 | 暴露情况 |
|---|---|
| `model`、`manuf`、`ver=5.0` | **查询串明文** |
| 源 IP、目的地址、时间、报文长度 | 明文 |
| `sign` | 密文，但确定性 → 同一载荷恒定 |
| 报文体 | AES-128-ECB 密文 |

在缺少 AES 密钥时无法读取报文体，而密钥以 RSA 加密后只送往服务端。但有两点削弱该保护：

1. **密钥由明文派生。** `key = MD5(plaintext)`。若攻击者可枚举明文——对特定机型大量字段是常量，`Time` 精度仅到秒——即可离线暴力求得密钥并解密。对于属性基本已知的目标设备，搜索空间很小。
2. **1024 位 RSA。** 信封的唯一秘密是一个 1024 位模数，低于所有现行建议（NIST SP 800-57：2030 年前应 ≥2048 位）。分解并非随手可行，但对于包含用户标识与经纬度的数据集，安全边际过薄。

### 9.2 主动截获

由于无 TLS、无响应认证，路径上的攻击者可以：

- **抑制**上报：返回 `HTTP/1.1 200 OK` 即可（客户端把 200 与 416 都当成功，并丢弃队列记录）；
- **重放**任何已捕获请求（无 nonce、无过期）；
- **改写目的地**：通过导出服务写入 `serverAddress`（F-02），或进行 DNS 欺骗；
- **关联**多次上报：借助确定性的 `sign`，无需解密。

### 9.3 实际结论

元数据与"是否上报"这一事实会暴露；报文体由一个设计偏弱但并非随手可破的信封保护。更严重的暴露不是机密性而是**控制权**：位于路径上的对手可让一次设备管理上报永远不到达，而客户端会认为自己成功。

---

## 10. 发现项

严重性为五级制；CVSS v3.1 向量为指示性，假设应用以系统应用身份安装于手机。

### F-01 — 遥测端点无客户端认证 · **高** · CVSS 7.5 `AV:N/AC:L/PR:N/UI:N/S:U/C:N/I:H/A:N`

`sign` 是用 APK 内置公钥对 `MD5(body)` 做 RSA 加密的结果；不存在共享密钥、token 或证书。任何持 APK 者可构造合法上报。
**CWE-306** · **MASVS-NETWORK-2**
**影响** 伪造设备记录、污染设备库、对 DM 平台发起未认证负载。
**修复** 采用双向 TLS，并在出厂时下发每设备凭据；或对规范化载荷做基于每设备密钥的 HMAC（密钥存于 TEE/Keystore）；增加服务端下发的 nonce。

### F-02 — 导出 Binder 服务可改写上报地址与运行状态 · **高** · CVSS 7.8 `AV:L/AC:L/PR:N/UI:N/S:U/C:H/I:H/A:L`

`RegisterHelperService` 声明 `android:exported="true"` 且无 `android:permission`。其 AIDL 接口（`service/a.java`）暴露：

| 事务号 | 方法 | 效果 |
|---|---|---|
| 1 | `a(String)` | 写入 `serverAddress` 偏好 → **此后每次上报都发往任意主机** |
| 2 | `a(int)` | 改写全部采集/上报周期 |
| 3 | `b()` | 删除 SharedPreferences 文件 |
| 4 | `a(boolean)` | 开/关详细日志 |
| 5 | `a()` | 读取日志开关 |

`c/d/a/b/l.java:241-243` 是 `serverAddress` 的唯一读取点，而 `l.a(Context,String)` 别无调用者。配合 F-03（任意应用可显式调用 `OnBootReceiver` 启动采集周期），构成**按需外流原语**：一个无任何权限的已安装应用即可绑定、把代理指向自己的服务器、触发上报，并收到信封报文与全部元数据。
**CWE-926** · **MASVS-PLATFORM-1**
**修复** `android:exported="false"`；若厂商组件确需外部调用，使用签名级权限保护。

### F-03 — PII 走明文 HTTP，且全局允许明文 · **高** · CVSS 6.5 `AV:N/AC:L/PR:N/UI:N/S:U/C:L/I:L/A:N`

`http://dm.wo.com.cn:18080/register`，`android:usesCleartextTraffic="true"`，无 `networkSecurityConfig`，无 pinning。元数据明文；报文有信封但无认证；响应无认证。
**CWE-319** · **MASVS-NETWORK-1**
**修复** 改用 HTTPS + 现代套件 + 证书 pinning；`cleartextTrafficPermitted="false"`；下线 18080。

### F-04 — 采集在缺乏实质同意的情况下进行 · **高**

`c/d/a/a/a.java:546-551` 返回 `device_provisioned == 1`；仅 vivo 额外读取 `Settings.Secure.operator_service_improve_plan`（`c/d/a/b/l.java:97-106`）。在小米、OPPO、三星、realme、中兴、努比亚、一加、WIKO、PTAC、TDTECH 构建上，**用户的"运营商服务改进计划"选择从未被查询**。应用无桌面入口、无授权界面、无隐私声明、无政策链接；DEX 中仅有的隐私相关字符串来自百度 SDK。
数据包含《个人信息保护法》第 28 条意义上的敏感个人信息（用户标识、行踪、应用使用），且为定位目的向第三方（百度）披露。
**CWE-359** · **MASVS-PRIVACY**
**修复** 以系统记录的、可撤回的、按目的授权的同意作为采集前置条件；提供用户可见的开关；提供隐私告知；最小化标识（去除 IMEI，对 ICCID/IMSI 做哈希）。

### F-05 — 密码学信封强度不足 · **中** · CVSS 5.9

MD5 作 KDF、AES-128-ECB、RSA-1024 PKCS#1 v1.5、无 nonce/IV、输出确定性。
**CWE-327 / CWE-330 / CWE-326**
**影响** 明文可猜时密钥可离线恢复；ECB 泄露分组级模式；相同载荷可关联；1024 位密钥是单点故障。
**修复** 以 TLS 1.3 取代该信封；若仍需信封，使用 AES-256-GCM + 随机 IV + HKDF-SHA-256、RSA-3072 或 ECDH P-256。

### F-06 — 敏感标识写入 logcat · **中**

`c/d/a/b/h.java` 在 `d.f1331a` 置位时写 `Log.d/Log.e`；调用点记录 IMEI（`control/d.java:103`）、ICCID/IMSI（`control/d.java:55-63`）、经纬度（`GetSignalInfoReceiver.java:88`）与完整 JSON（`control/g.java:37`、`c/d/a/a/n.java:376`）。该标志可经 F-02 由任意应用打开。
**CWE-532**
**修复** 发布版本移除标识日志；不要把日志开关暴露在 IPC 上。

### F-07 — 导出的诊断界面展示完整设备身份 · **中**

`Activity.TestActivity` 以自定义 action 导出且无权限。运行期权限已授予时，它渲染含 IMEI、IMSI、ICCID、小区与经纬度的 JSON。任意应用可拉起；在锁屏设备上可用于信息泄露或叠加/点击劫持准备。
**CWE-926**
**修复** 从发布版本移除，或加签名级权限并 `android:exported="false"`。

### F-08 — 内置第三方定位 SDK 与硬编码 API Key · **中**

百度 LBS（版本 9.5.2.x，依据 SDK 任务标签 `com.baidu.location.autonotifyloc_9.5.2.1` 推断；`liblocSDK8b.so`、`libindoor.so`、18 个 Java 壳类）以独立 `:remote` 进程运行，具备后台定位，访问 `*.map.baidu.com`。LBS Key `IGY6EFGRsixbYqk7GeZdAOD2Od3Rj7iF` 硬编码于清单。该 SDK 自身的数据流不在运营商隐私告知范围内，也不在本次审计范围内（原生代码已加固）。
**CWE-798 / CWE-359**
**修复** 与 SDK 厂商签署数据处理协议；按包名/签名限制 Key；在隐私告知中披露该第三方数据流；评估不依赖第三方的端侧定位方案。

### F-09 — 权限过度与隐蔽运行 · **中**

系统签名 + `sharedUserId` + `READ_PRIVILEGED_PHONE_STATE` + `PACKAGE_USAGE_STATS` + `QUERY_ALL_PACKAGES`，同时无桌面图标、无用户可见界面。应用能读取普通应用无法触及的标识与使用数据，而用户无从发现其存在。
**CWE-250 / CWE-269**
**修复** 按数据目的做最小授权；若只需白名单则去掉 `QUERY_ALL_PACKAGES`；在系统设置中暴露该代理并提供采集开关。

### F-10 — 把 HTTP 416 当作送达成功 · **低**

`control/g.java:48-50`。服务端拒绝被记为上报成功，破坏运营商对设备合规状态的判断。
**CWE-393**
**修复** 仅 2xx 视为成功，非 2xx 进入退避队列。

### F-11 — 跨 OEM 代码复用与残留产物 · **信息**

`c/c/a/a.java` 仍声明 `com.gree.telephonymanager.IGreeTelephonyManager`；11 个厂商适配器共用同一包名。代码来源因此不清晰，为某一 OEM 修复的缺陷可能在另一构建中继续存在。
**建议** 建立单一上游 + 按厂商配置，而非分厂商分支。

---

## 11. 隐私与合规观察

本节描述可观测行为，不构成法律意见。

1. **目的限制。** 一份以 IMEI 为键、含精确经纬度的 RF 路测记录，若缺少明确目的与留存期限，很难用"网络优化"解释。
2. **同意。** PIPL 对敏感个人信息处理与第三方提供均要求单独同意；本实现除 vivo 外以"设备已配置"替代同意。
3. **透明性。** 无隐私告知、无政策链接、无采集指示、无退出方式。该应用对用户完全不可见。
4. **第三方提供。** 定位经百度 LBS 解析；SDK 自身的遥测是独立的处理活动，告知中必须覆盖。
5. **最小化。** IMEI 完整上传且从不遮蔽；ICCID/IMSI 保留 10 位。二者均超出设备管理计费所需。
6. **留存。** 设备端保留 10 条设备记录与 100 条信号记录，直到上传成功；服务端留存未知。

---

## 12. 检测与缓解

**对企业或发现该应用的用户**

- 枚举：`adb shell pm list packages -s | grep mobiletools`、`adb shell dumpsys package com.mobiletools.systemhelper`。
- 校验签名摘要是否与 §2.2 一致；不一致说明是重打包版本。
- 查看同意开关：`adb shell settings get secure operator_service_improve_plan`。
- 若设备不由运营商管理，在网络边缘阻断 `dm.wo.com.cn:18080` 与 `*.map.baidu.com`。
- 卸载系统应用需要特权；在受管机群中优先用策略禁用（`pm disable-user --user 0`）。

**对厂商**

1. 用 TLS 1.3 + 证书 pinning 取代现有信封，下线 HTTP/18080。
2. 在 TEE 中引入每设备凭据与双向认证（F-01）。
3. 对所有无需外部访问的组件设 `exported="false"`（F-02、F-07）。
4. 对所有厂商（而非仅 vivo）以显式同意作为采集前置（F-04）。
5. 移除标识日志与 IPC 日志开关（F-06）。
6. 减少标识：去除 IMEI，ICCID/IMSI 以服务端盐值哈希（F-04）。
7. 发布隐私告知与数据留存计划。

---

## 13. 局限性

- **无动态分析。** 无真机与模拟器，运行期行为（实际权限授予、真实上报时序、服务端响应）未观测。所有行为结论均来自代码路径。
- **未测试服务端。** 未发送 POST、未使用凭据、未做路径枚举。关于端点的陈述仅限于边缘对未认证请求的返回。
- **未逆向原生 SDK。** `liblocSDK8b.so` 与 `libindoor.so` 已加固；百度数据流依据 Java 壳类、清单元数据与字符串证据推断。
- **混淆。** 一手代码已名称混淆（`c.d.a.*`），本文映射为人工重建并与调用点交叉验证。
- **单一产物。** 结论针对 `3.0.1-20250311`、使用 MIUI 平台证书签名的这一构建；同包名的其他厂商构建可能不同。

---

## 附录 A — 指标

```
package      com.mobiletools.systemhelper
sha256       5267008ede808c467988df588915791d21a6f1f3fbe219c57b125a235000df4f
signer sha1  7B:6D:C7:07:9C:34:73:9C:E8:11:59:71:9F:B5:EB:61:D2:A0:32:25
host         dm.wo.com.cn:18080            (HTTP, /register)
host         loc.map.baidu.com             (HTTPS, LBS)
host         ofloc.map.baidu.com           (HTTPS, LBS)
host         api.map.baidu.com             (HTTPS, LBS key verification)
host         daup.map.baidu.com            (HTTPS, LBS)
host         itsdata.map.baidu.com         (HTTPS, LBS)
prefs        com_mobiletools_systemhelper_preference（serverAddress、周期、firstPowerOn）
db           /data/data/com.mobiletools.systemhelper/databases/systemhelper
db           /data/data/com.mobiletools.systemhelper/databases/signalhelper
process      com.mobiletools.systemhelper:remote（百度 SDK）
```

## 附录 B — 证据索引

```
evidence/AndroidManifest.xml            解码后的清单
evidence/manifest-summary.txt           包名、权限、sharedUserId
evidence/components.csv                 组件导出矩阵
evidence/data-fields.csv                逐字段采集映射
evidence/hashes.txt                     哈希与签名证书
evidence/unicom-pub.cer                 从 assets/ 提取的公钥
evidence/strings/dex-strings.txt        完整可打印字符串表（11,175 条）
evidence/strings/urls.txt               DEX 中全部 URI
evidence/decompiled/                    34 个一手与支撑类反编译源码
evidence/network/probe-log.txt          端点探测记录
evidence/network/sample-request.txt     重建的请求（从未发送）
tools/repro_crypto.py                   协议离线复现
docs/WIRE-FORMAT.md                     协议规范
docs/DATA-FLOWS.md                      从采集到上报的数据流
docs/COMPONENTS.md                      导出组件的攻击面
docs/METHOD.md                          工具与复现步骤
```

---

*本报告基于单一送检 APK。分析过程中未修改任何生产系统、未使用任何凭据、未向报告所述任何端点提交数据。*
