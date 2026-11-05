#!/usr/bin/env python3
"""
repro_crypto.py -- offline reconstruction of the SystemHelper registration request format.

Purpose
-------
Demonstrate, without contacting any server, that the "sign" parameter is not a
client signature but an RSA encryption (with a PUBLIC key shipped inside the APK)
of the AES key, where the AES key is MD5(plaintext JSON).

Consequences proved by this script:
  * any party holding the APK can produce a syntactically valid request body;
  * the request carries no device credential, token, or shared secret;
  * therefore the endpoint cannot authenticate the client by this scheme.

Inputs : assets/pub.cer extracted from the APK
Outputs: evidence/network/sample-request.txt  (NOT transmitted)

Usage  : python repro_crypto.py --apk-dir ../apk_extracted --out ../evidence/network
"""

import argparse
import base64
import hashlib
import json
import os
import sys

from cryptography import x509
from cryptography.hazmat.primitives import padding
from cryptography.hazmat.primitives.asymmetric import padding as apadding
from cryptography.hazmat.primitives.ciphers import Cipher, algorithms, modes

ENDPOINT = "http://dm.wo.com.cn:18080/register"


def build_payload() -> dict:
    """Field set mirrors com.mobiletools.systemhelper.control.b#a()."""
    return {
        "Manuf": "Xiaomi",
        "Model": "M2102K1AC",
        "HWVersion": "Unknown",
        "SWVersion": "V13.0.5.0.SKBCNXM",
        "OS": "Android",
        "OSVersion": "13",
        "IMEI1": "860000000000001",
        "ICCID1": "8986010000000000000",
        "IMSI1": "460010000000000",
        "MNC1": "46001",
        "NetworkType1": "LTE",
        "TAC1": "12345",
        "CID1": "123456789",
        "PCI1": "123",
        "VolteEnabled1": "1",
        "5GEnabled1": "0",
        "VoNREnable1": "0",
        "IMEI2": "",
        "ICCID2": "",
        "IMSI2": "",
        "MNC2": "",
        "NetworkType2": "",
        "TAC2": "",
        "CID2": "",
        "PCI2": "",
        "VolteEnabled2": "",
        "5GEnabled2": "",
        "VoNREnable2": "",
        "DataSlot": "1",
        "AccType": "WiFi",
        "IPv6": "",
        "SubType": "",
        "DeviceType": "000005",
        "RegType": "R",
        "SpecVersion": "5.0",
        "ClientVersion": "3.0.1",
        "Time": "2026-11-05 12:00:00",
        "SigInfo": [],
        "AppInfo": [],
    }


def main() -> int:
    ap = argparse.ArgumentParser()
    ap.add_argument("--apk-dir", default="apk_extracted")
    ap.add_argument("--out", default="evidence/network")
    args = ap.parse_args()

    cert_path = os.path.join(args.apk_dir, "assets", "pub.cer")
    with open(cert_path, "rb") as fh:
        cert = x509.load_der_x509_certificate(fh.read())
    pubkey = cert.public_key()

    payload = build_payload()
    # Java: org.json.JSONObject.toString() -> compact JSON, insertion order preserved.
    body_json = json.dumps(payload, separators=(",", ":"), ensure_ascii=False)
    key = hashlib.md5(body_json.encode("utf-8")).digest()  # c.a(String) -> MD5

    padder = padding.PKCS7(128).padder()
    padded = padder.update(body_json.encode("utf-8")) + padder.finalize()
    encryptor = Cipher(algorithms.AES(key), modes.ECB()).encryptor()
    body = encryptor.update(padded) + encryptor.finalize()

    # RSA/ECB/PKCS1Padding over the 16-byte MD5 digest, Base64 (NO_WRAP), URL-encoded.
    sign_raw = pubkey.encrypt(key, apadding.PKCS1v15())
    sign = base64.b64encode(sign_raw).decode()
    from urllib.parse import quote
    url = (
        f"{ENDPOINT}?ver=5.0&model={quote(payload['Model'])}"
        f"&manuf={quote(payload['Manuf'])}&sign={quote(sign)}"
    )

    # Self-check: the receiver side recovers MD5 -> AES key -> plaintext.
    dec = Cipher(algorithms.AES(key), modes.ECB()).decryptor()
    unpadder = padding.PKCS7(128).unpadder()
    recovered = unpadder.update(dec.update(body) + dec.finalize()) + unpadder.finalize()
    assert recovered.decode("utf-8") == body_json, "AES round-trip failed"
    assert hashlib.md5(recovered).digest() == key

    lines = [
        "Offline reconstruction of the SystemHelper registration request.",
        "NOT SENT. Generated solely to document the wire format.",
        "",
        f"endpoint   : {ENDPOINT}",
        f"public key : {cert.subject.rfc4514_string()} / {pubkey.key_size} bit RSA",
        f"md5(json)  : {key.hex()}  (16 bytes -> AES-128 key)",
        f"aes mode   : AES/ECB/PKCS5Padding",
        f"body bytes : {len(body)}",
        f"body b64   : {base64.b64encode(body).decode()}",
        f"sign bytes : {len(sign_raw)}",
        "",
        "request URL:",
        url,
        "",
        "plaintext JSON:",
        body_json,
    ]
    os.makedirs(args.out, exist_ok=True)
    out_path = os.path.join(args.out, "sample-request.txt")
    with open(out_path, "w", encoding="utf-8") as fh:
        fh.write("\n".join(lines) + "\n")

    print(f"[+] wrote {out_path}")
    print(f"[+] AES key = MD5(plaintext) = {key.hex()}")
    print(f"[+] sign    = RSA-1024-PKCS1v15(MD5) base64, {len(sign)} chars")
    print("[+] round-trip verified; request was NOT transmitted")
    return 0


if __name__ == "__main__":
    sys.exit(main())
