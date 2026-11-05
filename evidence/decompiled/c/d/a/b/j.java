package c.d.a.b;

import java.io.InputStream;
import java.security.Key;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.crypto.Cipher;

/* JADX INFO: loaded from: classes.dex */
public class j {
    public Key a(InputStream inputStream) {
        try {
            return ((X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(inputStream)).getPublicKey();
        } catch (CertificateException e) {
            e.printStackTrace();
            h.b("RSAHelper", e.getMessage());
            return null;
        }
    }

    public byte[] a(Key key, byte[] bArr) {
        if (key == null) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(1, key);
            return cipher.doFinal(bArr);
        } catch (Exception e) {
            h.b("RSAHelper", e.getMessage());
            return null;
        }
    }
}
