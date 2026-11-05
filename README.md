# SystemHelper 3.0.1 — Security and Privacy Assessment of a Pre-installed Carrier DM Client

A reverse-engineering study of `com.mobiletools.systemhelper`, a silent device-management (DM) agent embedded in an OEM Android build. The application has no launcher icon, no user-facing consent flow and no update channel of its own; it collects hardware identifiers, subscriber identifiers, radio and location telemetry and a full inventory of installed applications, then uploads them to a carrier endpoint over plaintext HTTP.

This repository contains the full report, the decompiled evidence, the reconstructed wire protocol and the tooling needed to reproduce every claim.

## Provenance

- **Source**: the APK was supplied as an image extracted from a Xiaomi 17 running HyperOS.
- **Corroboration**: the signing certificate is Xiaomi's MIUI platform certificate (see [Signing](#signing)), and the code contains Xiaomi-specific branches — reflective calls to `miui.telephony.TelephonyManager.getImeiForSlot`, `Settings.Global.miui_version_name`, and a vendor dispatcher keyed on `ro.product.manufacturer`. There is no `LAUNCHER` entry, which places it in the pre-installed system image rather than in user space.
- **What it is**: a China Unicom "terminal information reporting" client implementing the DM 5.0 specification (`SpecVersion=5.0`, `DeviceType=000005`), shipped as a privileged system app so that it can read identifiers and usage statistics that ordinary applications cannot.

A companion analysis of the OEM's own security package (`com.miui.guardprovider`, signed with the same certificate) is in `../guardprovider-security-analysis`.

The research value is a documented case of **carrier-side telemetry inside an OEM ROM supply chain**: who installs it, what it can reach with system privileges, how the data is protected in transit, and where the design breaks. It is relevant to mobile privacy auditing, pre-installation review and DM/MDM protocol analysis.

## Sample identity

| Field | Value |
|---|---|
| Application label | `SystemHelper` |
| Package name | `com.mobiletools.systemhelper` |
| Version name | `3.0.1-20250311` |
| Version code | `31` |
| Build date (from version string) | 2025-03-11 |
| minSdkVersion | 31 (Android 12) |
| targetSdkVersion | 33 (Android 13) |
| compileSdkVersion | 33 |
| Platform build | `platformBuildVersionCode=33`, codename `13` |
| `sharedUserId` | `com.mobiletools.systemhelper.uid` |
| APK size | 3,261,815 bytes |
| APK SHA-256 | `5267008ede808c467988df588915791d21a6f1f3fbe219c57b125a235000df4f` |
| APK MD5 | `a3d23cc81ac1954609dd07d124c85d73` |
| ZIP entries | 409 |
| DEX | 1 × `classes.dex` (1,513,428 B, SHA-256 `f6bee7fb…9c4fba`) |
| ABI | `arm64-v8a` only |
| Native libraries | `liblocSDK8b.so` (368,568 B), `libindoor.so` (1,596,896 B) — Baidu LBS |
| Assets | `pub.cer` only (X.509, `CN=UNICOM`, 1024-bit RSA) |
| Signature schemes | v1 + v2 + v3, all valid |
| Expected install class | privileged system app (`/system/priv-app`), not user-installable in normal operation |
| Launcher activity | **none** |
| Components | 1 activity, 4 services, 7 receivers, 0 providers |
| Permissions | 17 (`READ_PRIVILEGED_PHONE_STATE`, `PACKAGE_USAGE_STATS`, `QUERY_ALL_PACKAGES`, background location, …) |
| Declared permission | `com.mobiletools.systemhelper.permission` (signature) — declared, unused |
| Protocol | DM 5.0, `POST /register`, `Content-Type: application/encrypted-json` |
| Primary endpoint | `http://dm.wo.com.cn:18080/register` |
| Secondary data flows | Baidu LBS `*.map.baidu.com` over HTTPS |

### Signing

| Field | Value |
|---|---|
| Subject / Issuer | `EMAILADDRESS=miui@xiaomi.com, CN=MIUI, OU=MIUI, O=Xiaomi, L=Beijing, ST=Beijing, C=CN` |
| Serial | `0xe552a8ecb9011b7c` |
| Validity | 2011-12-06 → 2039-04-23 |
| Key | RSA 2048, `sha1WithRSAEncryption` |
| SHA-1 | `7B:6D:C7:07:9C:34:73:9C:E8:11:59:71:9F:B5:EB:61:D2:A0:32:25` |
| SHA-256 | `C9:00:9D:01:EB:F9:F5:D0:30:2B:C7:1B:2F:E9:AA:9A:47:A4:32:BB:A1:73:08:A3:11:1B:75:D7:B2:14:90:25` |

The digest matches public signature records for Xiaomi system applications (Mi Share, CIT). That confirms the build was signed with the MIUI platform key; it does not by itself establish the distribution channel of this particular image.

## Bundled SDKs and dependencies

| Component | Version | Footprint | Purpose |
|---|---|---|---|
| Baidu LBS (location + indoor) | 9.5.2.x | `liblocSDK8b.so`, `libindoor.so`, 91 obfuscated classes (`a.a.a.*`) + 18 shims (`com.baidu.location.*`) | network/indoor positioning, BD09LL |
| AndroidX | 1.0.0 (`appcompat`, `core`, `fragment`, `coordinatorlayout`, …, 24 modules) | packaged | UI/backport |
| Android Architecture Components | 1.1.1 (`lifecycle`, `livedata`, `viewmodel`, `runtime`) | packaged | lifecycle |
| `android.support.*` (repackaged) | 28.0.0-era | 208 classes | legacy support library |
| HTTP client | platform `HttpURLConnection` | — | no OkHttp/Retrofit |
| JSON | `org.json` (platform) | — | no Gson/Moshi |
| Analytics / crash reporting / ads | **none found** | — | no Firebase, Umeng, Bugly, AppsFlyer, Tencent, ByteDance |

Absence of an analytics SDK matters for the privacy assessment: the only third-party data flow is Baidu LBS, everything else is first-party.

## Key findings

| ID | Severity | Finding |
|---|---|---|
| F-01 | High | No client authentication. `sign` is RSA encryption of `MD5(payload)` under a public key shipped in the APK — any holder of the APK can forge a submission. |
| F-02 | Medium | Exported, permission-less Binder service: any installed app can redirect the upload destination, change intervals, enable identifier logging, or wipe the queue. The payload stays encrypted, so this is redirection/tampering, not disclosure. |
| F-03 | High | Plaintext HTTP (`dm.wo.com.cn:18080`) for PII; `usesCleartextTraffic="true"` app-wide; no pinning. |
| F-04 | High | Consent is inferred from `device_provisioned`, not obtained. The carrier's own improvement-plan flag is honoured only on vivo. |
| F-05 | Medium | MD5-as-KDF + AES-128-ECB + RSA-1024 + no nonce; deterministic ciphertext and deterministic `sign`. |
| F-06 | Medium | IMEI, IMSI, ICCID and coordinates written to logcat when a flag is set — and the flag is settable over IPC. |
| F-07 | Medium | Exported diagnostic activity renders the full device-identity JSON. |
| F-08 | Medium | Bundled Baidu LBS with background location and a hard-coded API key; independent third-party data flow. |
| F-09 | Medium | System signature + `sharedUserId` + privileged phone state + usage stats, with no launcher icon and no user-visible surface. |
| F-10 | Low | HTTP 416 treated as delivery success. |
| F-11 | Info | Cross-OEM code reuse; residual Gree AIDL descriptor. |
| F-12 | Medium | `sharedUserId` puts the plaintext telemetry queue (IMEI, IMSI, ICCID, coordinates) inside a shared UID reachable by any same-signed sibling package. |

Direct answers to the four questions that motivated the assessment:

1. **What is collected** — IMEI (full, both slots), ICCID/IMSI (first 10 digits), cell identity, precise Baidu BD09LL coordinates, RF measurements, installed apps with usage time and traffic. → [§3](REPORT.md#3-what-is-collected)
2. **How and when it is reported** — boot + 4 min, then every 12/24 h with random offsets, plus SIM-change triggers; AES/RSA envelope, `Content-Type: application/encrypted-json`. → [§4](REPORT.md#4-when-it-collects-and-reports), [§5](REPORT.md#5-how-it-reports)
3. **Endpoints and authentication** — one first-party endpoint over HTTP; no token, no certificate, no shared secret; the `sign` parameter provides no authentication. → [§6](REPORT.md#6-endpoints), [§7](REPORT.md#7-authentication)
4. **Can data be read back or intercepted** — no read path exists in the client and the edge WAF rejects unauthenticated requests (`reason:UrlACL`); traffic is interceptable, suppressible and replayable, but the body is enveloped and not trivially decryptable. → [§8](REPORT.md#8-can-data-be-obtained-from-the-endpoint), [§9](REPORT.md#9-can-traffic-be-intercepted)

## Repository layout

```
README.md                  this file
REPORT.md                  full report (English)
REPORT.zh-CN.md            full report (Chinese)
CHANGELOG.md               revision history of this repository
metadata/
  app-profile.json         machine-readable sample profile (identity, components, SDKs, IOCs)
docs/
  METHOD.md                tooling, commands, reproduction
  WIRE-FORMAT.md           reconstructed upload protocol
  DATA-FLOWS.md            collection → queue → transport, with code references
  COMPONENTS.md            exported-component attack surface
evidence/
  AndroidManifest.xml      decoded manifest
  manifest-summary.txt     package / permissions / sharedUserId
  components.csv           export matrix
  data-fields.csv          field-level collection map
  hashes.txt               hashes and signing certificate
  unicom-pub.cer           public key extracted from assets/
  strings/                 DEX string table and URI list
  decompiled/              34 decompiled classes referenced by the report
  network/                 endpoint probe transcript, reconstructed request
tools/
  repro_crypto.py          offline reconstruction of the request envelope
```

## Reproduce

```bash
# static analysis (jadx 1.5.6 + bundled JRE)
jadx -d work/jadx --no-res --show-bad-code SystemHelper_3.0.1-20250311.apk

# protocol reconstruction — writes evidence/network/sample-request.txt, sends nothing
python tools/repro_crypto.py --apk-dir <extracted-apk> --out evidence/network
```

Toolchain details, the jadx wrapper used on this host, and the exact probe commands are in [docs/METHOD.md](docs/METHOD.md).

## Research use

- **Scope of the claim.** Findings describe one artefact (`3.0.1-20250311`, MIUI platform signature). The same package name contains adapters for eleven OEMs, so other builds can differ in signing key, enabled vendor branches and consent logic.
- **What the repository supports.** Field-level data inventory, protocol reconstruction, component attack surface, and a reproducible crypto reconstruction. It does not support claims about server-side storage, retention or access control.
- **Ethics.** No production system was modified, no credential was used, no payload was submitted. The only requests to the carrier endpoint were one `GET /`, one `OPTIONS /register` and one `HEAD /register`; no path enumeration or brute force was performed.
- **Limitations.** No device was available, so no dynamic instrumentation was performed; the Baidu native libraries are packed and were not reversed.

## How to cite

```
SystemHelper 3.0.1 — Security and Privacy Assessment of a Pre-installed Carrier DM Client.
Repository: systemhelper-security-analysis, 2026-11-05.
Sample SHA-256: 5267008ede808c467988df588915791d21a6f1f3fbe219c57b125a235000df4f
```
