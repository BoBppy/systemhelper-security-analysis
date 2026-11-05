package c.d.a.b;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class l {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static SimpleDateFormat f1340a = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public static long a(Context context, String str, long j) {
        return context.getSharedPreferences("com_mobiletools_systemhelper_preference", 4).getLong(str, j);
    }

    public static Boolean a(Context context, String str, Boolean bool) {
        return Boolean.valueOf(context.getSharedPreferences("com_mobiletools_systemhelper_preference", 4).getBoolean(str, bool.booleanValue()));
    }

    public static Boolean a(com.mobiletools.systemhelper.control.b bVar) {
        try {
            h.a("RegisterUtils", "deviceinfo.mnc1: " + bVar.j + "mnc2:" + bVar.v);
            if (bVar.j != null && !bVar.j.isEmpty() && !bVar.j.equals("ex00") && !bVar.j.equals("ex01") && !bVar.j.startsWith("460")) {
                return false;
            }
            if (bVar.v != null && !bVar.v.isEmpty() && !bVar.v.equals("ex00") && !bVar.v.equals("ex01") && !bVar.v.startsWith("460")) {
                return false;
            }
            if (bVar.E != null && !bVar.E.isEmpty()) {
                if (bVar.E.equals("1")) {
                    if (bVar.k != null && !bVar.k.isEmpty() && !bVar.k.startsWith("460")) {
                        return false;
                    }
                } else {
                    if (!bVar.E.equals("2")) {
                        h.b("RegisterUtils", "isUnicomNetwork->DataSlot：" + bVar.E);
                        return false;
                    }
                    if (bVar.w != null && !bVar.w.isEmpty() && !bVar.w.startsWith("460")) {
                        return false;
                    }
                }
            }
            return true;
        } catch (Exception e) {
            h.b("RegisterUtils", "Exception===isChinaNetwork");
            e.printStackTrace();
            return true;
        }
    }

    public static String a() {
        return f1340a.format(new Date());
    }

    public static String a(Context context, String str, String str2) {
        return context.getSharedPreferences("com_mobiletools_systemhelper_preference", 4).getString(str, str2);
    }

    public static String a(String str) {
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            return (String) cls.getMethod("get", String.class).invoke(cls, str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String a(String str, String str2) {
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            return (String) cls.getMethod("get", String.class, String.class).invoke(cls.newInstance(), str, str2);
        } catch (Exception e) {
            h.b("RegisterUtils", "getSystemProperties->" + e.getMessage());
            return str2;
        }
    }

    public static void a(Context context, long j) {
        h.a("RegisterUtils", "setAppInfoInterval--->");
        b(context, "appInfoInterval", j);
    }

    public static void a(Context context, String str) {
        b(context, "serverAddress", str);
    }

    public static boolean a(Context context) {
        boolean z = false;
        Boolean boolValueOf = Boolean.valueOf(Settings.Global.getInt(context.getContentResolver(), "device_provisioned", 0) == 1);
        h.a("RegisterUtils", "isBootWizardCompleted: " + boolValueOf);
        if (boolValueOf.booleanValue()) {
            z = Settings.Secure.getInt(context.getContentResolver(), "operator_service_improve_plan", 0) == 1;
            h.a("RegisterUtils", "checkUserPermission:" + z);
        }
        return z;
    }

    public static Boolean b(com.mobiletools.systemhelper.control.b bVar) {
        try {
            if (bVar.E != null && !bVar.E.isEmpty()) {
                if (bVar.E.equals("1")) {
                    if (bVar.j != null && !bVar.j.isEmpty() && (bVar.j.equals("46001") || bVar.j.equals("46006") || bVar.j.equals("46009") || bVar.j.equals("46010"))) {
                        return true;
                    }
                } else {
                    if (!bVar.E.equals("2")) {
                        h.b("RegisterUtils", "isUnicomNetwork->DataSlot：" + bVar.E);
                        return false;
                    }
                    if (bVar.v != null && !bVar.v.isEmpty() && (bVar.v.equals("46001") || bVar.v.equals("46006") || bVar.v.equals("46009") || bVar.v.equals("46010"))) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            h.b("RegisterUtils", "Exception====isUnicomNetwork");
            e.printStackTrace();
        }
        return false;
    }

    public static Boolean b(String str) {
        if (str != null) {
            try {
                if (!str.isEmpty() && (str.indexOf("46001") == 0 || str.indexOf("46006") == 0 || str.indexOf("46009") == 0 || str.indexOf("46010") == 0)) {
                    return true;
                }
            } catch (Exception e) {
                h.b("RegisterUtils", "Exception===");
                e.printStackTrace();
            }
        }
        return false;
    }

    public static String b(String str, String str2) {
        try {
            try {
                Class<?> cls = Class.forName("android.os.SystemProperties");
                return (String) cls.getMethod("get", String.class, String.class).invoke(cls, str, "unknown");
            } catch (Exception e) {
                e.printStackTrace();
                return str2;
            }
        } catch (Throwable unused) {
            return str2;
        }
    }

    @SuppressLint({"SdCardPath"})
    public static void b(Context context) {
        File file = new File("/data/data/com.mobiletools.systemhelper/shared_prefs/com_mobiletools_systemhelper_preference.xml");
        if (file.exists()) {
            file.delete();
        }
    }

    public static void b(Context context, long j) {
        h.a("RegisterUtils", "setInitAppInfoInterval--->");
        b(context, "appInfoInterval2", j);
    }

    public static void b(Context context, String str, long j) {
        SharedPreferences.Editor editorEdit = context.getSharedPreferences("com_mobiletools_systemhelper_preference", 4).edit();
        editorEdit.putLong(str, j);
        editorEdit.commit();
    }

    public static void b(Context context, String str, Boolean bool) {
        SharedPreferences.Editor editorEdit = context.getSharedPreferences("com_mobiletools_systemhelper_preference", 4).edit();
        editorEdit.putBoolean(str, bool.booleanValue());
        editorEdit.commit();
    }

    public static void b(Context context, String str, String str2) {
        SharedPreferences.Editor editorEdit = context.getSharedPreferences("com_mobiletools_systemhelper_preference", 4).edit();
        editorEdit.putString(str, str2);
        editorEdit.commit();
    }

    public static long c(Context context) {
        h.a("RegisterUtils", "getAppInfoInterval--->");
        return a(context, "appInfoInterval", d.t);
    }

    public static void c(Context context, long j) {
        h.a("RegisterUtils", "setInitScheduleInterval--->");
        b(context, "scheduleInterval2", j);
    }

    public static long d(Context context) {
        h.a("RegisterUtils", "getInitAppInfoInterval--->");
        return a(context, "appInfoInterval2", d.m);
    }

    public static void d(Context context, long j) {
        h.a("RegisterUtils", "setInitScheduleInterval--->");
        b(context, "signalInfoInterval2", j);
    }

    public static long e(Context context) {
        h.a("RegisterUtils", "getInitScheduleInterval--->");
        return a(context, "scheduleInterval2", d.n);
    }

    public static void e(Context context, long j) {
        h.a("RegisterUtils", "setScheduleInterval--->");
        b(context, "scheduleInterval", j);
    }

    public static long f(Context context) {
        h.a("RegisterUtils", "getInitScheduleInterval--->");
        h.a("RegisterUtils", "GlobalSetting.DEFAULT_RANDOM_SIG : " + d.o);
        return a(context, "signalInfoInterval2", d.o);
    }

    public static void f(Context context, long j) {
        h.a("RegisterUtils", "setSignalInfoInterval--->");
        b(context, "signalInfoInterval", j);
    }

    public static long g(Context context) {
        h.a("RegisterUtils", "getScheduleInterval--->");
        return a(context, "scheduleInterval", d.s);
    }

    public static void g(Context context, long j) {
        b(context, "GetsignalInfoInterval", j);
    }

    public static String h(Context context) {
        return d.f1332b ? d.l : a(context, "serverAddress", d.l);
    }

    public static long i(Context context) {
        h.a("RegisterUtils", "getSignalInfoInterval--->");
        return a(context, "signalInfoInterval", d.u);
    }

    public static long j(Context context) {
        return a(context, "GetsignalInfoInterval", d.v);
    }

    public static Boolean k(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        h.a("RegisterUtils", "network connected------------->:" + activeNetworkInfo);
        return Boolean.valueOf(activeNetworkInfo != null && activeNetworkInfo.isConnected());
    }

    public static Boolean l(Context context) {
        return Boolean.valueOf(((ConnectivityManager) context.getSystemService("connectivity")).getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED);
    }
}
