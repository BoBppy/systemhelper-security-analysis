# Reconstructed upload protocol

Everything below was derived from `control/g.java`, `control/e.java`, `c/d/a/b/{c,f,j,d}.java` and `c/d/a/a/n.java`, then verified offline with `tools/repro_crypto.py`. No request was transmitted.

## 1. Transport

```
POST http://dm.wo.com.cn:18080/register?ver=5.0&model=<Build.MODEL>&manuf=<manufacturer>&sign=<urlencoded base64>
Content-Type: application/encrypted-json
Content-Length: <len(body)>
Connect-Timeout: 10 s
Read-Timeout: 15 s
```

- Scheme is `http`; no TLS, no redirect handling, no proxy awareness beyond the platform default.
- `model` and `manuf` are URL-encoded `Build.MODEL` / manufacturer — **clear text**.
- `ver=5.0` is the DM specification version asserted by the client.
- No `Authorization`, no cookie, no client certificate.

## 2. Envelope

```
key   = MD5(UTF-8(plaintext_json))          // 16 bytes, AES-128
body  = AES/ECB/PKCS5Padding(key, plaintext_json)
sign  = Base64(RSA/ECB/PKCS1Padding(pub.cer, key))
```

Code path:

| Step | Java | File |
|---|---|---|
| MD5 | `c.a(String) -> MessageDigest("MD5")` | `c/d/a/b/c.java:26-34` |
| AES | `c.a(String, byte[]) -> Cipher("AES/ECB/PKCS5Padding")` | `c/d/a/b/c.java:36-71` |
| RSA | `c.a(Context, byte[]) -> j.a(pub.cer, bArr)` | `c/d/a/b/c.java:21-24`, `c/d/a/b/j.java` |
| Base64 | `c.a(byte[]) -> android.util.Base64` flags 0 (NO_WRAP) | `c/d/a/b/c.java:17-19` |

`pub.cer` is a 1024-bit RSA certificate, `CN=UNICOM`, valid 2019-05-24 → 2119-04-30, SHA-256 fingerprint `81:0C:01:22:BA:6A:F7:A4:B6:A5:92:1F:DB:BB:F9:19:7F:B2:E0:B2:E8:2D:E0:C5:AA:40:D2:8F:1B:7C:65:01`.

Properties:

- **Deterministic.** ECB, fixed key derivation, PKCS#1 v1.5 — identical plaintext yields identical body and identical `sign`.
- **No nonce, no IV, no timestamp validation by the client.**
- **Not a signature.** RSA is applied with the *public* key, so the operation is reversible only by the server; it proves nothing about the sender.

## 3. Plaintext JSON

Top-level object produced by `com.mobiletools.systemhelper.control.b#a()` (`control/b.java:85-346`).

### 3.1 Device block (always present)

| Field | Type | Source | Notes |
|---|---|---|---|
| `Manuf` | string | `Build.MANUFACTURER` / OEM map | also in query string |
| `Model` | string | `Build.MODEL` | also in query string |
| `HWVersion` | string | `ro.hardware.version` family | |
| `SWVersion` | string | `Build.DISPLAY`, MIUI version on Xiaomi | |
| `OS` / `OSVersion` | string | `"Android"` / `Build.VERSION.RELEASE` | |
| `IMEI1` / `IMEI2` | string | `getImei(0/1)` or MIUI reflection | **unmasked** |
| `ICCID1` / `ICCID2` | string | `getSimSerialNumber()` | first 10 chars + `**********` |
| `IMSI1` / `IMSI2` | string | `getSubscriberId()` | first 10 chars + `*****` |
| `MNC1` / `MNC2` | string | `getSimOperator()` | |
| `NetworkType1/2` | string | `LTE`, `NSA`, `SA`, `WCDMA`, `GSM`, … | derived from `CellIdentity` subtype |
| `TAC1/2`, `CID1/2`, `PCI1/2` | string | `CellIdentityLte/Nr` | |
| `VolteEnabled1/2`, `5GEnabled1/2`, `VoNREnable1/2` | string | `ImsManager`, vendor AIDL, system props | `"0"`/`"1"` |
| `DataSlot` | string | `0`/`1`/`2` | active data subscription |
| `AccType` | string | `WiFi` / `Mobile` / `Disconnected` | |
| `IPv6` | string | `NetworkInterface` enumeration | IPv4 is always empty |
| `SubType` | object | `{RAM, ROM, FREE}` | capacity classes, e.g. `"8G"` |
| `DeviceType` | string | constant `"000005"` | |
| `RegType` | string | `F`/`A`/`B`/`S`/`T`/`R` | see §3.4 |
| `SpecVersion` | string | `"5.0"` | |
| `ClientVersion` | string | `"3.0.1"` | |
| `Time` | string | `yyyy-MM-dd HH:mm:ss` | local time, second precision |
| `SigInfo` | array | RF/location records | always present, may be empty |
| `AppInfo` | array | installed-application records | always present, may be empty |

### 3.2 `SigInfo[]` element

`c/d/a/b/n.java:52-102`:

```
Time, IMEI, ICCID, ClientVersion, MNC, IMSI, TAC, CID, PCI, NetworkType,
ARFCN, Bands, Bandwidth, CQITableIndex, CQIIndex, RSRP, RSRQ, SINR,
CSIRSRP, CSIRSRQ, CSISINR, Uspeed, Dspeed, CallDisconnectCause,
DataFailCause, EPSFallback, LONGITUDE, LATITUDE, ALTITUDE, SPEED, BEARING
```

Records are produced per SIM slot, every hour (`GetSignalInfoReceiver`), stored in the `signal` table (cap 100) and drained on the next `RegType=R` upload.

### 3.3 `AppInfo[]` element

`c/d/a/b/b.java`:

```
appLabel, pkgName, Time, runTime ("<n>min"), tx1, rx1, tx2, rx2,
totalData1, totalData2, totalDataWiFi
```

`pkgName` values starting with `com.android` are filtered before upload (`c/d/a/a/n.java:37`).

### 3.4 `RegType`

| Value | Trigger | Extra sections |
|---|---|---|
| `F` | first boot after install | device (+ app inventory when permitted) |
| `B` | ordinary boot (after 240 s) | device |
| `S` | 24 h schedule | device |
| `T` | SIM change (after 180 s) | device, only when ICCID changed |
| `R` | 12 h signal schedule | device + `SigInfo[]` |
| `A` | 24 h app schedule | device + `AppInfo[]` |

## 4. Response handling

```java
this.f1338c = conn.getResponseCode();
if (this.f1338c == 200) { /* success */ }
```

and in the caller:

```java
boolean ok = http.a(url, body, digest);
if (!ok && http.a() == 416) ok = true;   // 416 treated as success
```

No body parsing, no status validation beyond those two codes, no retry/backoff, no server authentication.

## 5. Forgeability

Given `pub.cer` (public) and the algorithm above, any party can produce a request that is structurally indistinguishable from a device report. `tools/repro_crypto.py` does exactly that offline and verifies the round-trip. Consequences are analysed in `REPORT.md` §7 and §8.

## 6. Determinism and linkability

Both ciphertext and `sign` are deterministic. A passive observer who sees two requests can tell, without any key, whether the payloads are byte-identical — for example an unchanged device re-reporting the same `Time`-stamped record — and can use `sign` as a stable correlation handle.
