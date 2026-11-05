package c.d.a.b;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

/* JADX INFO: loaded from: classes.dex */
public class f {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private HttpURLConnection f1336a = null;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private HttpsURLConnection f1337b = null;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private int f1338c = 0;

    public synchronized int a() {
        return this.f1338c;
    }

    public synchronized boolean a(String str, byte[] bArr, byte[] bArr2) {
        boolean z;
        z = false;
        h.a("HttpHelper", "sendHttpRequest() path = " + str);
        if (str.startsWith("http://")) {
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
                this.f1336a = httpURLConnection;
                httpURLConnection.setConnectTimeout(10000);
                this.f1336a.setReadTimeout(15000);
                this.f1336a.setRequestMethod("POST");
                this.f1336a.setDoOutput(true);
                this.f1336a.setDoInput(true);
                this.f1336a.setRequestProperty("Content-Type", "application/encrypted-json");
                this.f1336a.setRequestProperty("Content-Length", String.valueOf(bArr.length));
                OutputStream outputStream = this.f1336a.getOutputStream();
                outputStream.write(bArr);
                outputStream.flush();
                outputStream.close();
                this.f1338c = this.f1336a.getResponseCode();
                h.a("HttpHelper", "server responseCode-->" + String.valueOf(this.f1338c));
                if (this.f1338c == 200) {
                    h.a("HttpHelper", "register success!");
                    z = true;
                } else {
                    h.b("HttpHelper", "register fail!");
                }
                if (this.f1336a != null) {
                    this.f1336a.disconnect();
                }
            } catch (Throwable th) {
                if (this.f1336a != null) {
                    this.f1336a.disconnect();
                }
                throw th;
            }
        } else if (str.startsWith("https://")) {
            try {
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) new URL(str).openConnection();
                this.f1337b = httpsURLConnection;
                httpsURLConnection.setConnectTimeout(10000);
                this.f1337b.setReadTimeout(15000);
                this.f1337b.setRequestMethod("POST");
                this.f1337b.setDoOutput(true);
                this.f1337b.setDoInput(true);
                this.f1337b.setRequestProperty("Content-Type", "application/encrypted-json");
                this.f1337b.setRequestProperty("Content-Length", String.valueOf(bArr.length));
                OutputStream outputStream2 = this.f1337b.getOutputStream();
                outputStream2.write(bArr);
                outputStream2.flush();
                outputStream2.close();
                this.f1338c = this.f1337b.getResponseCode();
                h.a("HttpHelper", "server responseCode-->" + String.valueOf(this.f1338c));
                if (this.f1338c == 200) {
                    h.a("HttpHelper", "register success!");
                    z = true;
                } else {
                    h.b("HttpHelper", "register fail!");
                }
                if (this.f1337b != null) {
                    this.f1337b.disconnect();
                }
            } catch (Throwable th2) {
                if (this.f1337b != null) {
                    this.f1337b.disconnect();
                }
                throw th2;
            }
        }
        return z;
    }
}
