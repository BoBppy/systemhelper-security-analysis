# Changelog

All dates are the analysis date of the artefact, not the build date of the application.

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
