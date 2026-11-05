# Changelog

All dates are the analysis date of the artefact, not the build date of the application.

## 2026-11-05 — v1.1

- Added `docs/EXPOSURE.md`: what an app with no permissions can read, write and trigger; the `sharedUserId` trust boundary; why the exported service is a control plane rather than a data-read primitive.
- **Corrected F-02.** Severity High → Medium (CVSS 7.8 → 6.8). The body is `AES-128-ECB(json, key = MD5(json))` and `sign = RSA/PKCS#1(MD5(json))` with e=65537, so redirecting the endpoint yields ciphertext and traffic metadata, not IMEI or coordinates. The impact is integrity and availability, not confidentiality.
- **Added F-12.** `android:sharedUserId="com.mobiletools.systemhelper.uid"` places the plaintext queues (`systemhelper.db`, `signalhelper.db`) inside a UID shared with any same-signed package.
- Corrected the timing diagram: `GetSignalInfo` samples every ~1 h (`d.v = 3600000`), not ~12 h; the 12 h figure applies to the `RegType=R` batch upload.
- Added a per-dataset cadence table (sampling rate, trigger, upload type) to §4.
- Added §14 disclosure path: which findings can support a CVE or CNVD entry.

## 2026-11-05 — v1.0 (initial publication)

- Sample triage: identity, hashes, signature schemes (v1/v2/v3), ABI, DEX and native inventory.
- Manifest and component analysis; export matrix of 1 activity, 4 services, 7 receivers.
- Decompilation with jadx 1.5.6; 20 application classes and 28 DM support classes reviewed, 34 archived under `evidence/decompiled/`.
- Data-collection inventory: 37 device fields, 31 signal fields, 11 application fields (`evidence/data-fields.csv`).
- Wire protocol reconstructed offline and verified (`tools/repro_crypto.py`, `evidence/network/sample-request.txt`).
- Non-intrusive endpoint fingerprinting: DNS, TCP/18080, `GET /`, `OPTIONS /register`, `HEAD /register` (`evidence/network/probe-log.txt`).
- Findings F-01 … F-11, English and Chinese reports.
- README expanded with full sample metadata, SDK inventory and provenance.
- Machine-readable profile added: `metadata/app-profile.json`.

### Corrections applied during preparation

- Permission count corrected from 16 to 17 (the `uses-permission` set includes `READ_EXTERNAL_STORAGE`).
- Baidu SDK version statement softened to "9.5.2.x" and tied to the observed task tag `com.baidu.location.autonotifyloc_9.5.2.1`.
- Class-count statement corrected to 20 application classes + 28 support classes (585 decompiled files in total, including bundled libraries).
- Signer digest corroborated against public APK signature records for Mi Share and CIT.
