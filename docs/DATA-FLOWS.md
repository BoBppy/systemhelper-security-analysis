# Data flows: collection → queue → transport

All references are to `evidence/decompiled/`. The application has no user interface in normal operation; the only activity is a diagnostic screen.

## 1. Entry points

| Entry | Component | Code | Effect |
|---|---|---|---|
| Boot completed | `control.OnBootReceiver` | `OnBootReceiver.java:26-33` | schedules a second self-invocation after 240 s, plus four exact alarms |
| Second invocation (no action) | `control.OnBootReceiver` | `OnBootReceiver.java:35-47` | registers dynamic receivers, reports `B` (first time `F`) |
| 24 h alarm | `control.ScheduleRegisterReceiver` | `ScheduleRegisterReceiver.java:17-26` | reports `S` |
| 24 h alarm | `control.AppInfoRegisterReceiver` | `AppInfoRegisterReceiver.java:18-29` | reports `A` |
| 12 h alarm | `control.SignalInfoRegisterReceiver` | `SignalInfoRegisterReceiver.java:17-26` | reports `R` |
| 12 h alarm | `control.GetSignalInfoReceiver` | `GetSignalInfoReceiver.java:132-155` | Baidu locate → RF sample → `signal` DB |
| SIM state change | `control.SIMStateReceiver` | `SIMStateReceiver.java:49-88` | after 180 s, reports `T` if ICCID changed |
| Explicit intent (any action) | `control.OnBootReceiver` | `OnBootReceiver.java:35-47` | **reports `B`/`F` without any schedule constraint** |
| Explicit intent (any action) | `control.SIMStateReceiver` | `SIMStateReceiver.java:83-87` | attempts `T` |
| AIDL call | `service.RegisterHelperService` | `RegisterHelperService.java:22-53` | writes `serverAddress`, intervals, log flag; wipes prefs |

## 2. Collection

```
control.b.a(context, regType)                     control/b.java:62-65
  └─ c.d.a.a.n.a(context, regType)                c/d/a/a/n.java:118-170
       ├─ c(context, bVar)                        c/d/a/a/n.java:353-377
       │    ├─ versionName
       │    ├─ vendor reader (Xiaomi/OPPO/vivo/…)  c/d/a/a/a.java and siblings
       │    │    ├─ IMEI1/IMEI2      → bVar.g / bVar.s
       │    │    ├─ ICCID1/2, IMSI1/2, MNC, TAC/CID/PCI
       │    │    └─ VoLTE / 5G / VoNR flags
       │    └─ AccType, timestamp
       ├─ a(context,bVar), b(context,bVar)        SIM-change detection, persists ICCID
       ├─ a(bVar)                                 masking + slot ordering, n.java:172-285
       └─ per RegType:
            R → load signal DB → bVar.P          c/d/a/b/m.java (cap 100)
            A → thread f → app inventory         control/f.java
            B/F/S/T → post device block
```

### 2.1 Masking (`c/d/a/a/n.java:247-280`)

```java
if (imsi != null && iccid != null && !l.b(imsi)) {      // l.b(): true for 46001/46006/46009/46010
    if (imsi.length() < 11 || iccid.length() < 11) { ... }
    else {
        iccid = iccid.substring(0,10) + "**********";
        imsi  = imsi.substring(0,10)  + "*****";
    }
    tac = sha256(tac); cid = sha256(cid);
}
```

`IMEI1`/`IMEI2` are never passed through this function.

### 2.2 Application inventory (`control/f.java`)

- `PackageManager.getInstalledApplications(0)` for labels and UIDs.
- `UsageStatsManager.queryAndAggregateUsageStats(now - 24 h, now)` for foreground minutes.
- `NetworkStatsManager.querySummary()` per UID for mobile, second-subscription and Wi-Fi byte counters.
- Filtered: `pkgName.startsWith("com.android")`.

### 2.3 Location and RF sampling (`control/GetSignalInfoReceiver.java`)

- Baidu `LocationClient`, `bd09ll`, `Hight_Accuracy`, `setIsNeedAddress(false)`, `start()`.
- On callback: `longitude = cVar.t()`, `latitude = cVar.q()`, `altitude = cVar.d()`.
- Then `control/d.java` fills radio fields (`RSRP`, `RSRQ`, `SINR`, `CQI`, `TAC`, `CID`, `PCI`, `NetworkType`, `ARFCN`, `Bands`, `Bandwidth`, `CallDisconnectCause`, `DataFailCause`, `EPSFallback`).
- Sample persisted to `signal` table only when MNC starts with `460` (`GetSignalInfoReceiver.java:92-96`).

## 3. Consent gate

| Vendor | Gate | Code |
|---|---|---|
| vivo | `device_provisioned == 1 && Settings.Secure.operator_service_improve_plan == 1` | `c/d/a/b/l.java:97-106` |
| all others | `device_provisioned == 1` only | `c/d/a/a/a.java:546-551` |

`device_provisioned` becomes 1 when the setup wizard finishes, i.e. on every normal handset. There is no per-purpose consent, no revocation path, and no user-visible indicator.

## 4. Queue and retry

```
post success  → 200 (or 416) → record dropped
post failure  → c.d.a.b.e.a(json)  → systemhelper.dmregister (cap 10, FIFO trim)
next success  → control/e.java drains one row and posts it (RegType unchanged, original Time)
```

Signal samples are only cleared after a successful `R` upload (`c/d/a/a/n.java:142`).

## 5. Transport

```
control.g / control.e  →  c.d.a.b.c  →  c.d.a.b.f  →  HttpURLConnection  →  dm.wo.com.cn:18080
```

Details in `WIRE-FORMAT.md`.

## 6. Where the data ends up

| Destination | Data | Protocol |
|---|---|---|
| `dm.wo.com.cn:18080` | device block, RF/location, app inventory | HTTP POST, AES/RSA envelope |
| `loc.map.baidu.com` etc. | positioning request/response handled by the Baidu SDK (Wi-Fi/cell scan data, coordinates) | HTTPS (SDK internal) |
| on-device `systemhelper` / `signalhelper` | plaintext JSON, up to 10 + 100 rows | SQLite |

The Baidu SDK runs in `:remote` and its internal behaviour was not reversed; the destination hosts are taken from the SDK's string table and from the manifest API key declaration.
