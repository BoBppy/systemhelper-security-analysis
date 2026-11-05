# Method, tooling and reproduction

## 1. Environment

| Item | Value |
|---|---|
| Host | Windows 11, Git Bash |
| Python | 3.13 (`C:\Users\hi\AppData\Local\Programs\Python\Python313`) |
| jadx | 1.5.6, `C:\Lab\software\jadx` (GUI jar used through the CLI entry point, bundled JRE 25) |
| androguard | 4.1.4 (manifest parsing only) |
| openssl | Git Bash build (certificate inspection) |
| adb | platform-tools present, **no device attached** |

The jadx distribution in `C:\Lab\software\jadx` ships only `jadx-gui-1.5.6-all.jar`. The CLI is reachable through `jadx.cli.JadxCLI`. A wrapper was added so that `jadx` works from both shells:

```bash
# C:\Lab\software\jadx\bin\jadx
#!/bin/sh
JADX_HOME="$(cd "$(dirname "$0")/.." && pwd)"
exec "$JADX_HOME/jre/bin/java.exe" -cp "$JADX_HOME/lib/jadx-gui-1.5.6-all.jar" jadx.cli.JadxCLI "$@"
```

```bat
:: C:\Lab\software\jadx\bin\jadx.cmd
@echo off
"%~dp0..\jre\bin\java.exe" -cp "%~dp0..\lib\jadx-gui-1.5.6-all.jar" jadx.cli.JadxCLI %*
```

`C:\Lab\software\jadx\bin` and `C:\Lab\software\jadx\jre\bin` were appended to the user `PATH`.

## 2. Acquisition and triage

```bash
sha256sum SystemHelper_3.0.1-20250311.apk
unzip -o SystemHelper_3.0.1-20250311.apk -d work/unzip
find work/unzip -type f | sed 's/.*\.//' | sort | uniq -c | sort -rn
```

Result: one DEX (1,513,428 B), two ARM64 shared objects (Baidu LBS), one 596-byte certificate in `assets/`, 254 PNG, 121 XML. No other DEX, no split APK, no obfuscator-specific packer.

## 3. Manifest and certificate

```bash
# manifest, permissions, components
python - <<'PY'
import loguru; loguru.logger.remove()
from androguard.core.apk import APK
a = APK("SystemHelper_3.0.1-20250311.apk")
print(a.get_package(), a.get_androidversion_name(), a.get_min_sdk_version(), a.get_target_sdk_version())
for p in sorted(a.get_permissions()): print(p)
open("work/evidence/AndroidManifest.xml","w").write(a.get_android_manifest_axml().get_xml().decode())
PY

# signing certificate
python - <<'PY'
from cryptography.hazmat.primitives.serialization import pkcs7
from cryptography.hazmat.primitives import hashes
for c in pkcs7.load_der_pkcs7_certificates(open("work/unzip/META-INF/CERT.RSA","rb").read()):
    print(c.subject.rfc4514_string(), c.fingerprint(hashes.SHA1()).hex(":"))
PY

# embedded public key
openssl x509 -in work/unzip/assets/pub.cer -inform DER -noout -text
```

## 4. Decompilation

```bash
export PATH="/c/Lab/software/jadx/bin:$PATH"
jadx -d work/jadx --no-res --show-bad-code SystemHelper_3.0.1-20250311.apk
```

`--no-res` was used because only code behaviour was needed; resources were inspected from the unzipped APK. Six decompilation errors were reported (library internals); none affected the classes analysed here.

Navigation:

```bash
grep -rn "getImei\|getSubscriberId\|getSimSerialNumber" --include=*.java .
grep -rhoE "https?://[A-Za-z0-9._~:/?#@!$&'()*+,;=%-]+" --include=*.java . | sort -u
grep -rn "exported" ../../evidence/AndroidManifest.xml
```

String recovery from the DEX and from the native libraries used a small Python extractor (`[\x20-\x7e]{5,}`) because `strings(1)` is not present in this Git Bash installation.

## 5. Protocol reconstruction

`tools/repro_crypto.py` reads `assets/pub.cer` from an extracted APK, builds a payload with the field set of `control/b.java`, derives the AES key with MD5, encrypts with AES-128-ECB, encrypts the digest with RSA PKCS#1 v1.5, and assembles the URL. It then decrypts its own output to prove the round-trip.

```bash
python tools/repro_crypto.py --apk-dir <extracted-apk> --out evidence/network
```

The script **does not open a socket**. Output: `evidence/network/sample-request.txt`.

## 6. Endpoint probing

Deliberately minimal and non-destructive:

```bash
nslookup dm.wo.com.cn
python - <<'PY'   # TCP connect only, three A records, port 18080
import socket, time
for ip in ["180.97.168.69","124.232.185.44","61.174.41.35"]:
    s=socket.socket(); s.settimeout(6); t=time.time()
    try: s.connect((ip,18080)); print(ip,"OPEN",int((time.time()-t)*1000),"ms")
    except Exception as e: print(ip,type(e).__name__,e)
    finally: s.close()
PY

curl -sS -i --max-time 12 -A "security-research/1.0" "http://dm.wo.com.cn:18080/"
curl -sS -i -X OPTIONS --max-time 12 "http://dm.wo.com.cn:18080/register"
curl -sS -I --max-time 12 "http://dm.wo.com.cn:18080/register"
```

No `POST`, no body, no credential, no path enumeration, three requests in total. Transcript: `evidence/network/probe-log.txt`.

## 7. What was not done

| Not performed | Reason |
|---|---|
| Dynamic instrumentation (Frida, runtime permission tracing) | no device or emulator available; creating an AVD would have required a multi-gigabyte system image and would not have reproduced the privileged system-app context that matters here |
| POST to the production endpoint | would have written a record into a third party's production database |
| Path/parameter fuzzing | out of scope for a passive assessment of a third-party service |
| Native reverse engineering of `liblocSDK8b.so` / `libindoor.so` | packed; the Baidu data flow is documented from the Java shims and manifest |
| Server-side audit | no authorisation and no visibility |

## 8. Reproducibility caveats

- Jadx output is not byte-stable across versions; line numbers in the report refer to jadx 1.5.6 output as archived in `evidence/decompiled/`.
- The payload JSON in `sample-request.txt` is synthetic. Its `Time` field and identifiers are fabricated; the cryptographic envelope is real.
- Endpoint behaviour was measured from one residential network on 2026-11-05 and may differ by source network, method and time.
