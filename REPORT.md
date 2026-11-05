# SystemHelper 3.0.1 — Security and Privacy Analysis

**Sample** `SystemHelper_3.0.1-20250311.apk`
**SHA-256** `5267008ede808c467988df588915791d21a6f1f3fbe219c57b125a235000df4f`
**Package** `com.mobiletools.systemhelper` · versionCode 31 · minSdk 31 · targetSdk 33
**Analysis date** 2026-11-05
**Method** static reverse engineering (jadx 1.5.6), manifest/resource inspection, certificate analysis, offline reconstruction of the wire protocol, non-intrusive endpoint fingerprinting. No device was available; no dynamic instrumentation was performed.
**Nature of the artefact** China Unicom device-management (DM) client, pre-installed system application, no launcher icon, no user-facing consent flow.

---

## 0. Bottom line

The application is a silent telemetry agent for a carrier device-management platform. It collects hardware identifiers, subscriber identifiers (IMEI, IMSI, ICCID), radio and cell data, precise geographic coordinates, and a full inventory of installed applications with per-app usage time and traffic volume, then posts them to a single hard-coded plaintext HTTP endpoint.

Seven judgements drive the risk assessment:

1. **There is no client authentication.** The `sign` parameter is not a signature; it is RSA encryption of `MD5(payload)` under a public key shipped inside the APK. Any party holding the APK can mint a syntactically valid submission. The server cannot distinguish a genuine handset from a forged client.
2. **An exported, permission-less Binder service lets any installed application redirect the upload destination** (`serverAddress`), change collection intervals, switch on verbose logging of IMEI/IMSI/coordinates, or wipe the local queue. Combined with an exported broadcast receiver that starts a full collection cycle on demand, this is a practical on-demand exfiltration channel.
3. **Transport is plaintext HTTP** on port 18080 with `android:usesCleartextTraffic="true"` application-wide. Metadata (model, manufacturer, protocol version, source IP, timing) is exposed in clear; the body is enveloped but the envelope is cryptographically weak (§9).
4. **Consent is asserted, not obtained.** On every vendor except vivo the gate is `Settings.Global.device_provisioned == 1`, i.e. "the setup wizard has been completed". The carrier's own `operator_service_improve_plan` flag is honoured only on vivo. No privacy notice, consent screen, or policy URL exists in the APK.
5. **The payload is protected by MD5-as-KDF, AES-128-ECB and RSA-1024.** This is a confidentiality envelope, not an authentication mechanism, and it fails modern cryptographic standards.
6. **Data cannot be read back from the endpoint through the application protocol.** The client only ever performs `POST /register`; there is no query interface; the origin is fronted by a WAF that rejected every unauthenticated request from a residential address with `reason:UrlACL`. This is an assessment based on client behaviour and edge behaviour, not a server-side audit.
7. **Traffic can be intercepted, suppressed and replayed.** A passive on-path observer obtains full metadata but not the plaintext body without additional knowledge; an active attacker can silently suppress a report by answering `200`, or replay a captured request.

---

## 1. Scope and method

| Item | Value |
|---|---|
| Target | `SystemHelper_3.0.1-20250311.apk` (3,261,815 bytes) |
| Decompiler | jadx 1.5.6 (bundled JRE 25) |
| Classes recovered | 585 Java files; 20 application classes (`com.mobiletools.*`) + 28 support classes in the obfuscated `c.d.a` package |
| Native code | `liblocSDK8b.so` (368,568 B), `libindoor.so` (1,596,896 B) — both Baidu LBS, stripped/packed |
| Dynamic testing | none (no device/emulator attached) |
| Network testing | DNS, TCP connect, one `GET /`, one `OPTIONS /register`, one `HEAD /register`; **no POST, no payload, no credential, no path enumeration** |
| Evidence | `evidence/` — manifest, decompiled sources, string dumps, certificate data, probe log, reconstructed request |

Reproduction steps are in `docs/METHOD.md`; the file:line references below point into `evidence/decompiled/`.

---

## 2. Application profile

### 2.1 Identity and placement

- No `LAUNCHER` intent filter: the app has **no icon and no entry point in the launcher**. The only activity is an exported diagnostic screen (`Activity.TestActivity`).
- `android:sharedUserId="com.mobiletools.systemhelper.uid"` — the app expects to share a UID with other packages of the same family.
- Requests `READ_PRIVILEGED_PHONE_STATE`, `PACKAGE_USAGE_STATS`, `QUERY_ALL_PACKAGES`, `READ_PRECISE_PHONE_STATE`: these are only granted to privileged/system applications or through special app access. Their presence, together with the signature below, indicates a **pre-installed system app**, not a user-installed one.
- Declares `FOREGROUND_SERVICE` but never calls `startForeground()` — an over-declaration with no functional use.

### 2.2 Signing certificate

| Field | Value |
|---|---|
| Subject / Issuer | `EMAILADDRESS=miui@xiaomi.com, CN=MIUI, OU=MIUI, O=Xiaomi, L=Beijing, ST=Beijing, C=CN` |
| Serial | `0xe552a8ecb9011b7c` |
| Validity | 2011-12-06 → 2039-04-23 |
| Key | RSA 2048, `sha1WithRSAEncryption` |
| SHA-1 | `7B:6D:C7:07:9C:34:73:9C:E8:11:59:71:9F:B5:EB:61:D2:A0:32:25` |
| SHA-256 | `C9:00:9D:01:EB:F9:F5:D0:30:2B:C7:1B:2F:E9:AA:9A:47:A4:32:BB:A1:73:08:A3:11:1B:75:D7:B2:14:90:25` |

This is the MIUI platform certificate also used for Xiaomi system applications (e.g. Mi Share, CIT). Two consequences:

- On a MIUI-derived ROM the APK passes platform signature checks, so signature-level permissions and the `sharedUserId` are effective. The app therefore operates with privileges far above a normal third-party application.
- The same package name (`com.mobiletools.systemhelper`) contains adapters for Xiaomi, OPPO, vivo, Samsung, realme, ZTE, nubia, OnePlus, WIKO, PTAC, TDTECH and "Liantong". A single signing key cannot be the platform key of all those vendors, so this specific artefact is **one vendor's build of a multi-OEM codebase**. The trust anchor is therefore vendor-specific and cannot be generalised.

### 2.3 Components

Full table: `evidence/components.csv`.

| Component | Type | Exported | Permission | Note |
|---|---|---|---|---|
| `Activity.TestActivity` | activity | **yes** | none | renders the complete device-info JSON (IMEI, IMSI, ICCID, cell, coordinates) on screen |
| `control.OnBootReceiver` | receiver | **yes** | none | `BOOT_COMPLETED`; **any explicit intent with another action falls through to a full registration cycle** |
| `control.SIMStateReceiver` | receiver | **yes** | none | `SIM_STATE_CHANGED`; explicit intent with another action also reaches the registration path |
| `control.ConnectStateReceiver` | receiver | **yes** | none | protected broadcast, low risk |
| `service.RegisterHelperService` | service | **yes** | none | unauthenticated AIDL interface, see F-02 |
| `ScheduleRegisterReceiver`, `AppInfoRegisterReceiver`, `SignalInfoRegisterReceiver`, `GetSignalInfoReceiver` | receiver | no | none | internal alarm targets |
| `service.RegisterIntentService`, `RegisterJobService` | service | no | — / `BIND_JOB_SERVICE` | internal |
| `com.baidu.location.f` | service | no | none | bundled SDK, `:remote` process |

### 2.4 Permissions (16)

```
ACCESS_BACKGROUND_LOCATION   ACCESS_COARSE_LOCATION   ACCESS_FINE_LOCATION
ACCESS_LOCATION_EXTRA_COMMANDS  ACCESS_NETWORK_STATE  ACCESS_WIFI_STATE
CHANGE_WIFI_STATE  FOREGROUND_SERVICE  INTERNET  PACKAGE_USAGE_STATS
QUERY_ALL_PACKAGES  READ_EXTERNAL_STORAGE  READ_PHONE_STATE
READ_PRECISE_PHONE_STATE  READ_PRIVILEGED_PHONE_STATE
RECEIVE_BOOT_COMPLETED  SCHEDULE_EXACT_ALARM
```

The combination of background location, privileged phone state, usage statistics and full package visibility is exactly the set required to build a persistent subscriber-and-behaviour profile.

---

## 3. What is collected

Field-level mapping with source APIs and pre-upload transforms: `evidence/data-fields.csv`. Summary:

### 3.1 Device and subscriber identity

| Data | API | Transform before upload |
|---|---|---|
| Manufacturer, model, hardware version, build/ROM version, OS version | `Build.*`, `ro.*` system properties | none |
| **IMEI 1 / IMEI 2** | `TelephonyManager.getImei(0/1)`; on Xiaomi `miui.telephony.TelephonyManager.getImeiForSlot` via reflection | **none — full 15 digits** |
| **ICCID 1 / ICCID 2** | `getSimSerialNumber()` | first 10 digits kept, remainder replaced by `*` |
| **IMSI 1 / IMSI 2** | `getSubscriberId()` | first 10 digits kept, remainder replaced by `*` |
| MNC, network type, TAC, CID/NCI, PCI, ARFCN, bands, bandwidth | `ServiceState`, `CellIdentityLte/Nr` | none |
| VoLTE / 5G / VoNR enablement, active data slot | `ImsManager`, vendor AIDL, system properties | none |
| IPv6 address | `NetworkInterface` enumeration | none (IPv4 is left empty) |
| RAM / ROM / free storage | `ActivityManager`, `StatFs`, `ro.boot.dram_info` | rounded to a size class |

The masking of ICCID/IMSI is conditional: it is skipped when the IMSI starts with a China Unicom test prefix (`46001/46006/46009/46010`), in which case the full value is transmitted (see `c/d/a/a/n.java:247-280`). Masking ICCID to ten digits still leaves roughly half of a 19–20 digit identifier in clear inside the payload.

### 3.2 Radio and location telemetry

A one-hour sampling task drives the Baidu LBS SDK in high-accuracy BD09LL mode, then records, per SIM slot:

`Time, IMEI, ICCID, IMSI, MNC, TAC, CID, PCI, NetworkType, ARFCN, Bands, Bandwidth, CQITableIndex, CQIIndex, RSRP, RSRQ, SINR, CSIRSRP, CSIRSRQ, CSISINR, Uspeed, Dspeed, CallDisconnectCause, DataFailCause, EPSFallback, LONGITUDE, LATITUDE, ALTITUDE, SPEED, BEARING`

(`c/d/a/b/n.java:52-102`, `com/mobiletools/systemhelper/control/d.java`, `control/GetSignalInfoReceiver.java`). This is a full RF-survey record tied to a persistent hardware identifier, including the subscriber's precise position — the most sensitive dataset in the application.

### 3.3 Installed application inventory and usage

`control/f.java` enumerates every installed application and, for each, records:

`appLabel, pkgName, Time, runTime (minutes in foreground), tx1/rx1 (mobile), tx2/rx2 (second subscription), totalData1, totalData2, totalDataWiFi`

using `UsageStatsManager.queryAndAggregateUsageStats()` and `NetworkStatsManager.querySummary()` per UID. Applications whose package starts with `com.android` are filtered out (`c/d/a/a/n.java:37`), everything else is sent. This yields a behavioural fingerprint: which apps a user has, how long they use them, and how much traffic each consumes.

### 3.4 Local persistence

Two SQLite databases under the app's private directory:

| DB | Table | Cap | Behaviour |
|---|---|---|---|
| `systemhelper` | `dmregister(jsonString)` | 10 rows | queue for failed device reports; the oldest row is drained and posted by `control/e.java` |
| `signalhelper` | `signal(jsonString)` | 100 rows | RF/location samples; batch-uploaded with `RegType=R` and cleared on success |

Both databases store the payload **in clear text on disk**, including IMEI and coordinates. They are inside the app sandbox, so readable only with root or a same-UID process — which is exactly what `sharedUserId` permits.

---

## 4. When it collects and reports

```
BOOT_COMPLETED ──► OnBootReceiver ──(240 s)──► OnBootReceiver (no action)
                                                    ├─ register dynamic receivers (SIM / connectivity)
                                                    ├─ RegType B  (first boot: F)  → full device report
                                                    └─ schedule exact alarms:
                                                         GetSignalInfo    every  ~12 h (+ random offset)
                                                         SignalInfo       every  ~12 h (+ random offset) → RegType R
                                                         Schedule         every  ~24 h (+ random offset) → RegType S
                                                         AppInfo          every  ~24 h (+ random offset) → RegType A
SIM_STATE_CHANGED ──► SIMStateReceiver ──(180 s)──► ICCID comparison → RegType T only if the SIM changed
explicit intent to OnBootReceiver / SIMStateReceiver ──► RegType B / T, no schedule constraint
```

Interval defaults are hard-coded in `c/d/a/b/d.java:27-36` and can be overwritten at runtime through the exported service. Sampling of RF/location runs hourly; batched upload runs twice a day.

`RegType` semantics (from `c/d/a/a/n.java:118-170`):

| Code | Meaning | Payload sections |
|---|---|---|
| `F` | first power-on after install | device + (app inventory if permitted) |
| `B` | ordinary boot | device |
| `S` | scheduled (24 h) | device |
| `T` | SIM change detected | device, only if ICCID changed |
| `R` | periodic RF/location batch | device + `SigInfo[]` |
| `A` | periodic application inventory | device + `AppInfo[]` |

Every payload carries the full device identity block regardless of type, so even the "signal" upload re-asserts IMEI and subscriber identifiers.

---

## 5. How it reports

### 5.1 Pipeline

```
b.a()                        build JSON (control/b.java)
  └─ MD5(json)               c.d.a.b.c.a(String)            → 16-byte AES key
  └─ AES/ECB/PKCS5(json)     c.d.a.b.c.a(String, byte[])    → request body
  └─ RSA/ECB/PKCS1(MD5)      c.d.a.b.c.a(Context, byte[])   → "sign" (Base64)
  └─ POST http://dm.wo.com.cn:18080/register
        ?ver=5.0&model=…&manuf=…&sign=…
        Content-Type: application/encrypted-json
```

Implementation: `control/g.java:29-63` (live report), `control/e.java:21-62` (queued history report), `c/d/a/b/f.java:24-94` (HTTP client), `c/d/a/b/c.java` (crypto primitives), `c/d/a/b/j.java` (RSA).

### 5.2 Wire format (reconstructed and verified offline)

`tools/repro_crypto.py` rebuilds a complete request from the public key extracted from the APK and verifies the AES round-trip. Output is in `evidence/network/sample-request.txt`. **The request was never transmitted.**

```
endpoint   : http://dm.wo.com.cn:18080/register
public key : CN=UNICOM / 1024-bit RSA
md5(json)  : b15a52c816eeb587d6cebb370e2d6b93   (AES-128 key)
aes mode   : AES/ECB/PKCS5Padding
body bytes : 688
sign bytes : 128                                  (single RSA block)
```

Two properties follow directly from the reconstruction:

- The body is deterministic for a given plaintext (ECB, no IV, no nonce).
- The `sign` value is deterministic as well (RSA PKCS#1 v1.5). The same payload always produces the same `sign`, which gives an observer a stable correlation handle without any decryption.

### 5.3 Response handling

`c/d/a/b/f.java:43-50` treats HTTP `200` as success and, in the caller, `416` is also treated as success (`control/g.java:48-50`). No response body is parsed, no signature or MAC is verified, and there is no retry policy beyond re-queueing into `dmregister`. A `416 Requested Range Not Satisfiable` being interpreted as "delivered" is a logic defect: a server-side rejection is silently counted as a successful report.

---

## 6. Endpoints

| Host | Port / scheme | Purpose | Source |
|---|---|---|---|
| `dm.wo.com.cn` | **18080 / HTTP** | device registration and telemetry upload | hard-coded, `c/d/a/b/d.java:26` |
| `loc.map.baidu.com`, `ofloc.map.baidu.com`, `api.map.baidu.com`, `daup.map.baidu.com`, `itsdata.map.baidu.com`, `parking.baidu.com` | 443 / HTTPS | Baidu LBS positioning and configuration | bundled SDK (`liblocSDK8b.so`, `libindoor.so`, `com.baidu.location.*`) |

There is exactly **one first-party endpoint**. It is constructed as `l.h(context)` which returns `d.l` unless the `serverAddress` preference has been set — and that preference is only writable through the exported service (F-02).

### 6.1 Endpoint posture (observed)

```
dm.wo.com.cn  →  CNAME 63ce5fd2be92dcdf.icloudv6.com  (China Unicom CDN)
               →  180.97.168.69 / 124.232.185.44 / 61.174.41.35
TCP/18080      →  open on all three addresses
GET /          →  403 Forbidden, WZWS-RAY: … reason:UrlACL
OPTIONS /register → 403 Forbidden, reason:UrlACL
HEAD /register → 403 Forbidden
```

`WZWS` is the 360 WangZhan WeiShi WAF. `UrlACL` indicates path-level access control applied before the request reaches the application. From a residential broadband address, every unauthenticated request was rejected at the edge. The client only ever issues `POST /register`, so the ACL is plausibly scoped to that method/path and/or to cellular source ranges; this could not be confirmed without a handset and was deliberately not brute-forced. Raw transcript: `evidence/network/probe-log.txt`.

---

## 7. Authentication

### 7.1 Client → server: none

The request contains no `Authorization` header, no token, no API key, no device certificate, no TLS client certificate, no HMAC over a shared secret. The only device-specific values (IMEI, IMSI, ICCID) travel **inside** the body and are self-asserted.

### 7.2 What `sign` actually is

```
key   = MD5(plaintext JSON)              // 16 bytes
body  = AES-128-ECB(key, plaintext)
sign  = Base64(RSA-1024-PKCS1v1.5(pub.cer, key))
```

`pub.cer` is a 1024-bit RSA certificate (`CN=UNICOM`, valid 2019→2119) shipped in `assets/`. Using a **public** key to encrypt the MD5 digest means:

- confidentiality of the AES key depends solely on the secrecy of UNICOM's private key;
- **integrity and authenticity are not established at all** — anyone can compute `MD5(json)`, encrypt it with the public key, and produce a well-formed request;
- the digest is MD5, so even a server-side integrity check is weak.

This is encryption, not signing. Calling the parameter `sign` is misleading and, from an assessment standpoint, the scheme provides **no client authentication and no non-repudiation**.

### 7.3 Consequences

| Property | Status |
|---|---|
| Client authentication | absent |
| Message authenticity | absent (forgeable by anyone with the APK) |
| Replay protection | absent (no nonce, no timestamp validation on the client side; server behaviour unknown) |
| Confidentiality of body | depends on RSA-1024 private key + plaintext entropy |
| Integrity in transit | absent (no TLS, no MAC) |
| Server authentication | absent (HTTP) |

---

## 8. Can data be obtained from the endpoint?

**Through the application protocol: no.** The client performs exactly one operation — `POST /register`. There is no read, query, list or export call anywhere among the 48 first-party classes. No alternate host, no admin path, no debug endpoint appears in the DEX or in the native libraries.

**Through the public edge: not observed.** Every unauthenticated request from a non-cellular address was rejected by the WAF with `UrlACL` before reaching the application. No path enumeration was attempted; consequently this report does **not** claim that the server is free of vulnerable interfaces, only that the client exposes none and the edge blocked the requests that were sent.

**Write access, however, is not controlled.** Because `sign` can be produced by anyone holding the APK (§7), a party that can reach the endpoint (for example from an allowed network range) can submit arbitrary device records. The realistic impacts are data poisoning of the carrier's device database, resource consumption, and the ability to test the endpoint's parser. This was not attempted.

**Server-side storage is out of scope.** Whether records are retained, for how long, with what access control, and whether an internal console can export them cannot be determined from the APK.

---

## 9. Can traffic be intercepted?

### 9.1 Passive interception

Transport is plain HTTP, so an on-path observer (same Wi-Fi, ISP, transit provider, malicious hotspot) captures the full request.

| Element | Exposure |
|---|---|
| `model`, `manuf`, `ver=5.0` | **clear text** in the query string |
| source IP, destination, timestamp, request size | clear text |
| `sign` | ciphertext, but deterministic → stable per payload |
| body | AES-128-ECB ciphertext |

The body is not readable without the AES key, and the key is delivered RSA-encrypted to the server. Two caveats undermine that protection:

1. **Key derivation from plaintext.** `key = MD5(plaintext)`. If an attacker can enumerate the plaintext — many fields are constant for a given handset, and `Time` has one-second granularity — the key can be brute-forced offline and the body decrypted. For a targeted device whose attributes are largely known, the search space is small.
2. **1024-bit RSA.** The envelope's only secret is a 1024-bit modulus, below every current recommendation (NIST SP 800-57: ≥2048-bit until 2030). Factoring is not casual, but the margin is thin for a dataset that includes subscriber identifiers and coordinates.

### 9.2 Active interception

Because there is no TLS and no response authentication, an on-path attacker can:

- **suppress** a report by answering `HTTP/1.1 200 OK` (the client treats 200 — and 416 — as success and discards the queued record);
- **replay** any captured request verbatim (no nonce, no expiry);
- **redirect** the destination by setting `serverAddress` through the exported service (F-02), or by DNS spoofing;
- **correlate** repeated uploads through the deterministic `sign`, without decrypting anything.

### 9.3 Practical answer

Metadata and the fact of reporting are exposed; the body is protected by an envelope that is weak by design but not trivially broken in transit. The more serious exposure is not confidentiality but **control**: an adversary who can sit on the path can silently prevent a device-management report from ever arriving, and the client will report success.

---

## 10. Findings

Severity uses a five-band scale; CVSS v3.1 vectors are indicative and assume a handset with the app installed as a system application.

### F-01 — No client authentication on the telemetry endpoint · **High** · CVSS 7.5 `AV:N/AC:L/PR:N/UI:N/S:U/C:N/I:H/A:N`

`sign` is RSA encryption of `MD5(body)` under a public key embedded in the APK; no shared secret, token or certificate exists. Anyone with the APK can produce a valid submission.
**CWE-306** Missing Authentication for Critical Function · **MASVS-NETWORK-2**
**Impact** forged device records, database poisoning, unauthenticated load on the DM platform.
**Fix** mutual TLS with per-device credentials provisioned at manufacture, or an HMAC over the canonical payload with a per-device key stored in the TEE/Keystore; add a server-issued nonce.

### F-02 — Exported Binder service allows upload redirection and state tampering · **High** · CVSS 7.8 `AV:L/AC:L/PR:N/UI:N/S:U/C:H/I:H/A:L`

`RegisterHelperService` is `android:exported="true"` with no `android:permission`. Its AIDL surface (`service/a.java`) exposes:

| Transaction | Method | Effect |
|---|---|---|
| 1 | `a(String)` | writes the `serverAddress` preference → **every subsequent upload goes to an arbitrary host** |
| 2 | `a(int)` | rewrites all collection/upload intervals |
| 3 | `b()` | deletes the shared-preference file |
| 4 | `a(boolean)` | enables/disables verbose logging |
| 5 | `a()` | reads the logging flag |

`c/d/a/b/l.java:241-243` is the only consumer of `serverAddress`, and `l.a(Context,String)` is called from nowhere else. Combined with F-03 (any app can invoke `OnBootReceiver` explicitly to start a collection cycle), this is an **on-demand exfiltration primitive**: an installed app with no permissions can bind, point the agent at its own server, trigger a report, and receive the enveloped payload plus all metadata.
**CWE-926** Improper Export of Android Application Components · **MASVS-PLATFORM-1**
**Fix** `android:exported="false"`; if a vendor component genuinely needs it, guard with a signature-level permission.

### F-03 — Plaintext HTTP transport for PII, cleartext permitted app-wide · **High** · CVSS 6.5 `AV:N/AC:L/PR:N/UI:N/S:U/C:L/I:L/A:N`

`http://dm.wo.com.cn:18080/register`, `android:usesCleartextTraffic="true"`, no `networkSecurityConfig`, no pinning. Metadata in clear; body enveloped but unauth­enticated; responses unauthenticated.
**CWE-319** Cleartext Transmission of Sensitive Information · **MASVS-NETWORK-1**
**Fix** HTTPS with a modern cipher suite and certificate pinning; set `cleartextTrafficPermitted="false"`; retire port 18080.

### F-04 — Collection proceeds without meaningful consent · **High**

`c/d/a/a/a.java:546-551` returns `device_provisioned == 1`; only vivo additionally reads `Settings.Secure.operator_service_improve_plan` (`c/d/a/b/l.java:97-106`). On Xiaomi, OPPO, Samsung, realme, ZTE, nubia, OnePlus, WIKO, PTAC and TDTECH builds, **the user's "operator service improvement plan" choice is never consulted**. The app has no launcher entry, no consent screen, no privacy notice and no policy URL; the only privacy strings in the DEX belong to the Baidu SDK.
Data includes sensitive personal information under PIPL art. 28 (subscriber identity, whereabouts, app usage) and is disclosed to a third party (Baidu) for positioning.
**CWE-359** Exposure of Private Personal Information · **MASVS-PRIVACY**
**Fix** gate collection on an explicit, revocable, per-purpose consent recorded by the system; expose a user-visible control; provide a privacy notice; minimise identifiers (drop IMEI, hash ICCID/IMSI).

### F-05 — Weak cryptographic envelope · **Medium** · CVSS 5.9

MD5 used as KDF, AES-128 in ECB mode, RSA-1024 PKCS#1 v1.5, no nonce/IV, deterministic output.
**CWE-327 / CWE-330 / CWE-326**
**Impact** offline key recovery when the plaintext is guessable; block-level pattern leakage; identical payloads are linkable; the 1024-bit key is the single point of failure.
**Fix** TLS 1.3 in place of the envelope; if an envelope is still required, AES-256-GCM with a random IV and a KDF such as HKDF-SHA-256, RSA-3072 or ECDH P-256.

### F-06 — Sensitive identifiers written to logcat · **Medium**

`c/d/a/b/h.java` writes to `Log.d/Log.e` when `d.f1331a` is set; call sites log IMEI (`control/d.java:103`), ICCID/IMSI (`control/d.java:55-63`), coordinates (`GetSignalInfoReceiver.java:88`) and the complete JSON payload (`control/g.java:37`, `c/d/a/a/n.java:376`). The flag can be switched on by any app through F-02.
**CWE-532** Insertion of Sensitive Information into Log File
**Fix** remove identifier logging in release builds; never expose the log switch over IPC.

### F-07 — Exported diagnostic activity displays full device identity · **Medium**

`Activity.TestActivity` is exported with a custom action and no permission. If the runtime permissions are granted, it renders the JSON including IMEI, IMSI, ICCID, cell and coordinates. Any app can launch it; on a locked device it can be used for information disclosure or overlay/tapjacking setups.
**CWE-926**
**Fix** remove from release builds, or require a signature-level permission and `android:exported="false"`.

### F-08 — Bundled third-party location SDK with hard-coded API key · **Medium**

Baidu LBS 9.5.2.1 (`liblocSDK8b.so`, `libindoor.so`, 18 Java shims) runs in a separate `:remote` process with background location and contacts `*.map.baidu.com`. The LBS key `IGY6EFGRsixbYqk7GeZdAOD2Od3Rj7iF` is hard-coded in the manifest. The SDK's own data flows are outside the operator's privacy notice and outside this audit (native code is packed).
**CWE-798** Use of Hard-coded Credentials · **CWE-359**
**Fix** confirm a data-processing agreement with the SDK vendor, restrict the key by package/signature, document the third-party flow, and evaluate an on-device geolocation path that does not require a third party.

### F-09 — Excess privilege and hidden operation · **Medium**

System signature + `sharedUserId` + `READ_PRIVILEGED_PHONE_STATE` + `PACKAGE_USAGE_STATS` + `QUERY_ALL_PACKAGES`, with no launcher icon and no user-visible surface. The app can read identifiers and app-usage data that ordinary applications cannot, and the user has no way to discover it.
**CWE-250 / CWE-269**
**Fix** least privilege per data purpose; drop `QUERY_ALL_PACKAGES` if only a whitelist is needed; surface the agent in system settings with a data-collection toggle.

### F-10 — HTTP 416 treated as delivery success · **Low**

`control/g.java:48-50`. A server-side rejection is recorded as a successful report, corrupting the operator's view of device compliance.
**CWE-393** Return of Wrong Status Code
**Fix** treat only 2xx as success and surface non-2xx to the queue with backoff.

### F-11 — Cross-OEM code reuse and residual artefacts · **Informational**

`c/c/a/a.java` still declares `com.gree.telephonymanager.IGreeTelephonyManager`; eleven vendor adapters share one package name. Code provenance is therefore unclear, and a defect fixed for one OEM build may persist in another.
**Recommendation** establish a single upstream with per-vendor configuration rather than per-vendor forks.

---

## 11. Privacy and compliance observations

This section describes observable behaviour; it is not a legal opinion.

1. **Purpose limitation.** An RF-survey record keyed to IMEI and precise coordinates is difficult to justify as "network optimisation" without a documented purpose and retention limit.
2. **Consent.** PIPL requires separate consent for sensitive personal information and for disclosure to third parties. The implementation substitutes a device-provisioning flag for consent on all non-vivo builds.
3. **Transparency.** No privacy notice, no policy link, no collection indicator, no opt-out. The application is invisible to the user.
4. **Third-party disclosure.** Location is resolved through Baidu LBS; the SDK's own telemetry is a separate processing activity that the notice must cover.
5. **Data minimisation.** IMEI is transmitted in full and never masked; ICCID/IMSI retain ten digits. Both exceed what a device-management platform needs for accounting.
6. **Retention.** Ten device records and one hundred signal records are kept on the device indefinitely until uploaded; server-side retention is unknown.

---

## 12. Detection and mitigation

**For an enterprise or a user who finds this app on a device**

- Enumerate: `adb shell pm list packages -s | grep mobiletools`, `adb shell dumpsys package com.mobiletools.systemhelper`.
- Verify the signer digest against §2.2; a mismatch means a repackaged build.
- Check the consent flag: `adb shell settings get secure operator_service_improve_plan`.
- Block egress to `dm.wo.com.cn:18080` and to `*.map.baidu.com` at the network edge if the device is not managed by the carrier.
- Removing a system app requires a privileged uninstall; on managed fleets, prefer a policy that disables the package (`pm disable-user --user 0`).

**For the vendor**

1. Replace the envelope with TLS 1.3 + certificate pinning; retire HTTP/18080.
2. Introduce per-device credentials in the TEE and mutual authentication (F-01).
3. Set `exported="false"` on every component that does not require external access (F-02, F-07).
4. Gate collection on explicit consent for every vendor, not only vivo (F-04).
5. Remove identifier logging and the IPC log switch (F-06).
6. Reduce identifiers: drop IMEI, hash ICCID/IMSI with a server-side salt (F-04).
7. Publish a privacy notice and a data-retention schedule.

---

## 13. Limitations

- **No dynamic analysis.** No device or emulator was available, so runtime behaviour (actual permission grants, actual upload timing, server responses) was not observed. All behavioural claims derive from code paths.
- **Server side not tested.** No POST was sent, no credentials were used, no path enumeration was performed. Statements about the endpoint are limited to what the edge returned for unauthenticated requests.
- **Native SDK not reversed.** `liblocSDK8b.so` and `libindoor.so` are packed; the Baidu data flow is inferred from Java shims, manifest metadata and string evidence.
- **Obfuscation.** First-party code is name-obfuscated (`c.d.a.*`); the mapping in this report was reconstructed manually and cross-checked against call sites.
- **Single artefact.** Findings describe build `3.0.1-20250311` signed with the MIUI platform key; other vendor builds of the same package may differ.

---

## Appendix A — Indicators of compromise

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
prefs        com_mobiletools_systemhelper_preference  (serverAddress, intervals, firstPowerOn)
db           /data/data/com.mobiletools.systemhelper/databases/systemhelper
db           /data/data/com.mobiletools.systemhelper/databases/signalhelper
process      com.mobiletools.systemhelper:remote   (Baidu SDK)
```

## Appendix B — Evidence index

```
evidence/AndroidManifest.xml            decoded manifest
evidence/manifest-summary.txt           package, permissions, sharedUserId
evidence/components.csv                 component export matrix
evidence/data-fields.csv                field-by-field collection map
evidence/hashes.txt                     hashes and signing certificate
evidence/unicom-pub.cer                 public key extracted from assets/
evidence/strings/dex-strings.txt        full printable string table (11,175 entries)
evidence/strings/urls.txt               all URIs found in the DEX
evidence/decompiled/                    34 first-party and support classes, as decompiled
evidence/network/probe-log.txt          endpoint probe transcript
evidence/network/sample-request.txt     reconstructed request (never transmitted)
tools/repro_crypto.py                   offline protocol reconstruction
docs/WIRE-FORMAT.md                     protocol specification
docs/DATA-FLOWS.md                      collection-to-upload data flow
docs/COMPONENTS.md                      attack surface of exported components
docs/METHOD.md                          tooling and reproduction steps
```

---

*Prepared from a single APK supplied for analysis. No production system was modified, no credential was used, and no data was submitted to any endpoint described in this report.*
