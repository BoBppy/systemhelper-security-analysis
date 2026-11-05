package c.d.a.b;

import android.content.Context;
import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public class h {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static int f1339a = 3072;

    public static void a(Context context, String str) {
    }

    public static void a(String str, String str2) {
        if (!d.f1331a || str == null || str2 == null) {
            return;
        }
        if (str2.length() > f1339a) {
            int length = str2.length() / f1339a;
            int i = 0;
            while (i < length) {
                int i2 = f1339a;
                int i3 = i * i2;
                i++;
                Log.d(str, str2.substring(i3, i2 * i));
            }
            int length2 = str2.length() % f1339a;
            if (length2 <= 0) {
                return;
            } else {
                str2 = str2.substring(str2.length() - length2);
            }
        }
        Log.d(str, str2);
    }

    public static void b(String str, String str2) {
        if (!d.f1331a || str == null || str2 == null) {
            return;
        }
        Log.e(str, str2);
    }
}
