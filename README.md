# SystemHelper 3.0.1 — Security and Privacy Assessment

Reverse-engineering report for the Android package `com.mobiletools.systemhelper`, a pre-installed China Unicom device-management client that silently collects device identifiers, subscriber identifiers, radio/location telemetry and the installed-application inventory, and uploads them over plaintext HTTP.

| | |
|---|---|
| Sample | `SystemHelper_3.0.1-20250311.apk` |
| SHA-256 | `5267008ede808c467988df588915791d21a6f1f3fbe219c57b125a235000df4f` |
| Package | `com.mobiletools.systemhelper` · 3.0.1 (31) · minSdk 31 · targetSdk 33 |
| Signer | Xiaomi MIUI platform certificate, SHA-1 `7B:6D:C7:07:9C:34:73:9C:E8:11:59:71:9F:B5:EB:61:D2:A0:32:25` |
| Reports | [English](REPORT.md) · [中文](REPORT.zh-CN.md) |

## Key findings

| ID | Severity | Finding |
|---|---|---|
| F-01 | High | No client authentication. `sign` is RSA encryption of `MD5(payload)` under a public key shipped in the APK — any holder of the APK can forge a submission. |
| F-02 | High | Exported, permission-less Binder service: any installed app can redirect the upload destination, change intervals, enable identifier logging, or wipe the queue. |
| F-03 | High | Plaintext HTTP (`dm.wo.com.cn:18080`) for PII; `usesCleartextTraffic="true"` app-wide; no pinning. |
| F-04 | High | Consent is inferred from `device_provisioned`, not obtained. The operator's own improvement-plan flag is honoured only on vivo. |
| F-05 | Medium | MD5-as-KDF + AES-128-ECB + RSA-1024 + no nonce; deterministic ciphertext and deterministic `sign`. |
| F-06 | Medium | IMEI, IMSI, ICCID and coordinates written to logcat when a flag is set — and the flag is settable over IPC. |
| F-07 | Medium | Exported diagnostic activity renders the full device-identity JSON. |
| F-08 | Medium | Bundled Baidu LBS (9.5.2.x) with background location and a hard-coded API key; independent third-party data flow. |
| F-09 | Medium | System signature + `sharedUserId` + privileged phone state + usage stats, with no launcher icon and no user-visible surface. |
| F-10 | Low | HTTP 416 treated as delivery success. |
| F-11 | Info | Cross-OEM code reuse; residual Gree AIDL descriptor. |

Direct answers to the four questions that motivated the assessment:

1. **What is collected** — IMEI (full, both slots), ICCID/IMSI (first 10 digits), cell identity, precise Baidu BD09LL coordinates, RF measurements, installed apps with usage time and traffic. → §3
2. **How and when it is reported** — boot + 4 min, then every 12/24 h with random offsets, plus SIM-change triggers; AES/RSA envelope, `Content-Type: application/encrypted-json`. → §4, §5
3. **Endpoints and authentication** — one first-party endpoint over HTTP; no token, no certificate, no shared secret; the `sign` parameter provides no authentication. → §6, §7
4. **Can data be read back or intercepted** — no read path exists in the client and the edge WAF rejects unauthenticated requests (`reason:UrlACL`); traffic is interceptable, suppressible and replayable, but the body is enveloped and not trivially decryptable. → §8, §9

## Repository layout

```
README.md                  this file
REPORT.md                  full report (English)
REPORT.zh-CN.md            full report (Chinese)
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

## Scope and ethics

Analysis was performed on a single APK supplied by the requester. No production system was modified, no credential was used, and no payload was submitted to any endpoint. The only network activity against the operator endpoint consisted of one `GET /`, one `OPTIONS /register` and one `HEAD /register`; no path enumeration or brute force was performed. Findings about the server are limited to observable edge behaviour and are marked as such.

Native libraries are third-party (Baidu LBS) and were not reversed; the vendor's data flow is inferred from Java shims, manifest metadata and string evidence.
