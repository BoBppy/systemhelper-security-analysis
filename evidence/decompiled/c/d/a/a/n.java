package c.d.a.a;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;

/* JADX INFO: loaded from: classes.dex */
public class n {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static Map<String, String> f1325a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static c f1326b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static List<c.d.a.b.b> f1327c;
    private static com.mobiletools.systemhelper.control.f.a d = new a();
    private static com.mobiletools.systemhelper.control.c.a e = new b();

    class a implements com.mobiletools.systemhelper.control.f.a {
        a() {
        }

        @Override // com.mobiletools.systemhelper.control.f.a
        public void a(Context context, com.mobiletools.systemhelper.control.b bVar, List<c.d.a.b.b> list) {
            List unused = n.f1327c = list;
            if (n.f1327c != null) {
                JSONArray jSONArray = new JSONArray();
                for (c.d.a.b.b bVar2 : n.f1327c) {
                    try {
                        if (!bVar2.a().getString("pkgName").startsWith("com.android")) {
                            jSONArray.put(bVar2.a());
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (bVar != null) {
                    bVar.O = jSONArray;
                    c.d.a.b.h.a("DeviceManager", "appInfolength:" + jSONArray.length());
                }
            }
            ((c.d.a.b.d.f1333c && c.d.a.b.d.e) ? new com.mobiletools.systemhelper.control.c(context, bVar, n.e) : new com.mobiletools.systemhelper.control.g(context, bVar)).start();
        }
    }

    class b implements com.mobiletools.systemhelper.control.c.a {
        b() {
        }

        @Override // com.mobiletools.systemhelper.control.c.a
        public void a(Context context, com.mobiletools.systemhelper.control.b bVar, String str, String str2) {
            if (bVar != null) {
                bVar.G = str;
                bVar.H = str2;
            }
            String str3 = bVar.H;
            if (str3 == null || str3.equals("")) {
                bVar.H = "ex01";
            }
            new com.mobiletools.systemhelper.control.g(context, bVar).start();
        }
    }

    public interface c {
        void a(Context context, com.mobiletools.systemhelper.control.b bVar);

        boolean a(Context context);
    }

    public static c a(Context context) {
        c aVar;
        c.d.a.a.a.e(context);
        c cVar = f1326b;
        if (cVar != null) {
            return cVar;
        }
        c.d.a.b.h.a(c.d.a.b.d.d, "model is " + Build.MODEL);
        String str = f1325a.get(Build.MODEL);
        if (str == null) {
            c.d.a.b.h.b("DeviceManager", "getReader error!! manufacture==null! model=" + Build.MODEL);
            aVar = new c.d.a.a.a();
        } else if (str.equals("Xiaomi")) {
            aVar = new l();
        } else if (str.equals("OPPO")) {
            aVar = new d();
        } else if (str.equalsIgnoreCase("vivo")) {
            aVar = new j();
        } else if (str.equalsIgnoreCase("samsung")) {
            aVar = new h();
        } else if (str.equalsIgnoreCase("realme")) {
            aVar = new g();
        } else if (str.equalsIgnoreCase("ZTE")) {
            aVar = new m();
        } else if (str.equalsIgnoreCase("nubia")) {
            aVar = new c.d.a.a.c();
        } else if (str.equalsIgnoreCase("OnePlus")) {
            aVar = new e();
        } else if (str.equalsIgnoreCase("Liantong")) {
            aVar = new c.d.a.a.b();
        } else if (str.equalsIgnoreCase("TDTECH")) {
            aVar = new i();
        } else if (str.equalsIgnoreCase("PTAC")) {
            aVar = new f();
        } else {
            aVar = str.equalsIgnoreCase("WIKO") ? new k() : new c.d.a.a.a();
        }
        f1326b = aVar;
        return f1326b;
    }

    public static synchronized com.mobiletools.systemhelper.control.b a(Context context, String str) {
        Thread gVar;
        c.d.a.b.h.a("DeviceManager", "enter getCurDeviceInfo--RegType:" + str);
        com.mobiletools.systemhelper.control.b bVar = new com.mobiletools.systemhelper.control.b();
        bVar.K = str;
        c(context, bVar);
        if (!str.trim().equals("T")) {
            a(context, bVar);
            b(context, bVar);
        } else if (!a(context, bVar) && !b(context, bVar)) {
            c.d.a.b.h.a("DeviceManager", "SIM not changed，not post！");
            return bVar;
        }
        a(bVar);
        if (!str.trim().equals("A")) {
            if (str.trim().equals("R")) {
                c.d.a.b.h.a("DeviceManager", "regtype R");
                c.d.a.b.m mVarA = c.d.a.b.m.a(context, c.d.a.b.m.f1342c, c.d.a.b.m.f1341b.intValue());
                JSONArray jSONArrayB = mVarA.b();
                c.d.a.b.h.a("DeviceManager", "signalJSONArray.length() : " + jSONArrayB.length());
                if (jSONArrayB == null || jSONArrayB.length() <= 0) {
                    c.d.a.b.h.a("DeviceManager", "deviceinfo.siginfo 为空！ ");
                } else {
                    bVar.P = jSONArrayB;
                    mVarA.a();
                    mVarA.close();
                    c.d.a.b.h.a("DeviceManager", "signalJSONArray>>>>>:" + jSONArrayB.toString());
                    if (c.d.a.b.d.f1333c && c.d.a.b.d.e) {
                        gVar = new com.mobiletools.systemhelper.control.c(context, bVar, e);
                    } else {
                        c.d.a.b.h.a("DeviceManager", "GlobalSetting.isUserPermissionOpen is false,type R-appinfo is not post");
                        gVar = new com.mobiletools.systemhelper.control.g(context, bVar);
                    }
                }
            } else if (str.trim().equals("B") || str.trim().equals("F") || str.trim().equals("S") || str.trim().equals("T")) {
                if (c.d.a.b.d.f1333c && c.d.a.b.d.e) {
                    gVar = new com.mobiletools.systemhelper.control.c(context, bVar, e);
                } else {
                    c.d.a.b.h.a("DeviceManager", "GlobalSetting.isUserPermissionOpen is false,B/F/S/Tdeviceinfo is not post");
                    gVar = new com.mobiletools.systemhelper.control.g(context, bVar);
                }
            }
            return bVar;
        }
        if (c.d.a.b.d.e) {
            gVar = new com.mobiletools.systemhelper.control.f(context, bVar, d);
        } else {
            c.d.a.b.h.a("DeviceManager", "GlobalSetting.isUserPermissionOpen is false,type A-appinfo is not post");
            gVar = new com.mobiletools.systemhelper.control.g(context, bVar);
        }
        gVar.start();
        return bVar;
    }

    private static synchronized void a(com.mobiletools.systemhelper.control.b bVar) {
        String str;
        String str2;
        String str3;
        if (bVar.g != null && bVar.s != null && !bVar.g.isEmpty() && !bVar.s.isEmpty() && bVar.g.compareTo(bVar.s) > 0) {
            com.mobiletools.systemhelper.control.b bVar2 = new com.mobiletools.systemhelper.control.b();
            bVar2.g = bVar.g;
            bVar2.h = bVar.h;
            bVar2.i = bVar.i;
            bVar2.j = bVar.j;
            bVar2.k = bVar.k;
            bVar2.l = bVar.l;
            bVar2.n = bVar.n;
            bVar2.o = bVar.o;
            bVar2.m = bVar.m;
            bVar2.p = bVar.p;
            bVar2.r = bVar.r;
            bVar2.q = bVar.q;
            String str4 = bVar.s;
            bVar2.s = str4;
            String str5 = bVar.t;
            bVar2.t = str5;
            String str6 = bVar.u;
            bVar2.u = str6;
            String str7 = bVar.v;
            bVar2.v = str7;
            String str8 = bVar.w;
            bVar2.w = str8;
            String str9 = bVar.x;
            bVar2.x = str9;
            String str10 = bVar.z;
            bVar2.z = str10;
            String str11 = bVar.A;
            bVar2.A = str11;
            String str12 = bVar.y;
            bVar2.y = str12;
            String str13 = bVar.B;
            bVar2.B = str13;
            String str14 = bVar.D;
            bVar2.D = str14;
            String str15 = bVar.C;
            bVar2.C = str15;
            bVar.g = str4;
            bVar.h = str5;
            bVar.i = str6;
            bVar.j = str7;
            bVar.k = str8;
            bVar.l = str9;
            bVar.n = str10;
            bVar.o = str11;
            bVar.m = str12;
            bVar.p = str13;
            bVar.r = str14;
            bVar.q = str15;
            bVar.s = bVar2.g;
            bVar.t = bVar2.h;
            bVar.u = bVar2.i;
            bVar.v = bVar2.j;
            bVar.w = bVar2.k;
            bVar.x = bVar2.l;
            bVar.z = bVar2.n;
            bVar.A = bVar2.o;
            bVar.y = bVar2.m;
            bVar.B = bVar2.p;
            bVar.D = bVar2.r;
            bVar.C = bVar2.q;
            if (bVar.E != null) {
                if (!bVar.E.equals("1")) {
                    str3 = bVar.E.equals("2") ? "1" : "2";
                    throw th;
                }
                bVar.E = str3;
            }
        }
        try {
            if (bVar.i != null && bVar.h != null && !bVar.i.equals("") && bVar.i.length() >= 5 && bVar.h.length() >= 6 && !c.d.a.b.l.b(bVar.i).booleanValue()) {
                if (bVar.i.length() < 11 || bVar.h.length() < 11) {
                    if (bVar.h.length() >= 11) {
                        bVar.h = bVar.h.substring(0, 10) + "**********";
                    } else if (bVar.i.length() >= 11) {
                        str2 = bVar.i.substring(0, 10) + "*****";
                    }
                    bVar.n = c.d.a.b.i.a(bVar.n);
                    bVar.m = c.d.a.b.i.a(bVar.m);
                } else {
                    bVar.h = bVar.h.substring(0, 10) + "**********";
                    str2 = bVar.i.substring(0, 10) + "*****";
                }
                bVar.i = str2;
                bVar.n = c.d.a.b.i.a(bVar.n);
                bVar.m = c.d.a.b.i.a(bVar.m);
            }
            if (bVar.u != null && bVar.t != null && !bVar.u.equals("") && bVar.u.length() >= 5 && bVar.t.length() >= 6 && !c.d.a.b.l.b(bVar.u).booleanValue()) {
                if (bVar.u.length() < 11 || bVar.t.length() < 11) {
                    if (bVar.t.length() >= 11) {
                        bVar.t = bVar.t.substring(0, 10) + "**********";
                    } else if (bVar.u.length() >= 11) {
                        str = bVar.u.substring(0, 10) + "*****";
                    }
                    bVar.z = c.d.a.b.i.a(bVar.z);
                    bVar.y = c.d.a.b.i.a(bVar.y);
                } else {
                    bVar.t = bVar.t.substring(0, 10) + "**********";
                    str = bVar.u.substring(0, 10) + "*****";
                }
                bVar.u = str;
                bVar.z = c.d.a.b.i.a(bVar.z);
                bVar.y = c.d.a.b.i.a(bVar.y);
            }
        } catch (Exception e2) {
            c.d.a.b.h.b("DeviceManager", "exception === sortAndAnonymousSubscriber");
            e2.printStackTrace();
        }
    }

    private static synchronized boolean a(Context context, com.mobiletools.systemhelper.control.b bVar) {
        String strA = c.d.a.b.l.a(context, "iccid1", (String) null);
        if (strA == null || strA.isEmpty()) {
            if (bVar.h != null && !bVar.h.isEmpty()) {
                c.d.a.b.h.a("DeviceManager", "SIM1 Changed,old iccid:" + strA + ", new iccid:" + bVar.h);
                c.d.a.b.l.b(context, "iccid1", bVar.h);
                return true;
            }
        } else if (!strA.equals(bVar.h)) {
            c.d.a.b.h.a("DeviceManager", "SIM1 Changed,old iccid:" + strA + ", new iccid:" + bVar.h);
            c.d.a.b.l.b(context, "iccid1", bVar.h);
            return true;
        }
        return false;
    }

    public static boolean b(Context context) {
        c cVarA;
        if (context == null || (cVarA = a(context)) == null) {
            return false;
        }
        return cVarA.a(context);
    }

    private static synchronized boolean b(Context context, com.mobiletools.systemhelper.control.b bVar) {
        String strA = c.d.a.b.l.a(context, "iccid2", (String) null);
        if (strA == null || strA.isEmpty()) {
            if (bVar.t != null && !bVar.t.isEmpty()) {
                c.d.a.b.h.a("DeviceManager", "SIM2 Changed,old iccid:" + strA + ", new iccid:" + bVar.t);
                c.d.a.b.l.b(context, "iccid2", bVar.t);
                return true;
            }
        } else if (!strA.equals(bVar.t)) {
            c.d.a.b.h.a("DeviceManager", "SIM2 Changed,old iccid:" + strA + ", new iccid:" + bVar.t);
            c.d.a.b.l.b(context, "iccid2", bVar.t);
            return true;
        }
        return false;
    }

    public static Boolean c(Context context) {
        boolean zB;
        if (context == null) {
            zB = false;
        } else {
            c();
            zB = b(context);
        }
        return Boolean.valueOf(zB);
    }

    public static void c() {
        if (f1325a == null) {
            f1325a = new HashMap();
            String strA = c.d.a.b.l.a("ro.product.manufacturer");
            if (strA == null || strA.isEmpty()) {
                return;
            }
            c.d.a.b.h.a("DeviceManager", "manufacturer=" + strA);
            if (strA.trim().equalsIgnoreCase("vivo") || strA.trim().equalsIgnoreCase("samsung") || strA.trim().equalsIgnoreCase("realme") || strA.trim().equalsIgnoreCase("Xiaomi") || strA.trim().equalsIgnoreCase("OPPO") || strA.trim().equalsIgnoreCase("Liantong") || strA.trim().equalsIgnoreCase("OnePlus") || strA.trim().equalsIgnoreCase("TDTECH") || strA.trim().equalsIgnoreCase("nubia") || strA.trim().equalsIgnoreCase("PTAC") || strA.trim().equalsIgnoreCase("ZTE") || strA.trim().equalsIgnoreCase("WIKO")) {
                c.d.a.b.h.a("DeviceManager", "model: " + Build.MODEL);
                f1325a.put(Build.MODEL, strA);
            }
        }
    }

    private static synchronized void c(Context context, com.mobiletools.systemhelper.control.b bVar) {
        String str;
        try {
            bVar.M = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e2) {
            e2.printStackTrace();
        }
        c();
        c cVarA = a(context);
        if (cVarA == null) {
            c.d.a.b.h.b("DeviceManager", "manuf==null,model=" + Build.MODEL);
            return;
        }
        cVarA.a(context, bVar);
        if (b.b.c.a.a.a(context, "android.permission.ACCESS_NETWORK_STATE") == 0) {
            if (c.d.a.b.l.k(context).booleanValue()) {
                str = !c.d.a.b.l.l(context).booleanValue() ? "Mobile" : "WiFi";
            } else {
                str = "Disconnected";
            }
            bVar.F = str;
        }
        bVar.N = c.d.a.b.l.a();
        c.d.a.b.h.a("DeviceManager", "getDeviceInfo:" + bVar.a());
    }
}
