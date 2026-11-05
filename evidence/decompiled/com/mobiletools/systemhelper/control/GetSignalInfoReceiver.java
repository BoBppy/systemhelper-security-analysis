package com.mobiletools.systemhelper.control;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import c.d.a.b.l;
import c.d.a.b.m;
import c.d.a.b.n;
import com.baidu.location.h;
import com.baidu.location.i;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class GetSignalInfoReceiver extends BroadcastReceiver {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static Context f1400c;
    private static com.baidu.location.b d;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    n f1401a = new n();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public h f1402b;

    class a extends com.baidu.location.b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ Context f1403a;

        a(Context context) {
            this.f1403a = context;
        }

        @Override // com.baidu.location.b
        public void a(com.baidu.location.c cVar) {
            String str;
            String str2;
            if (cVar == null || cVar.r() == 167 || cVar.r() == 67 || cVar.r() == 62) {
                c.d.a.b.h.b("GetSignalInfoReceiver", "定位失败！");
                return;
            }
            c.d.a.b.h.a("GetSignalInfoReceiver", "定位成功！");
            GetSignalInfoReceiver.this.f1401a.B = String.valueOf(cVar.t());
            GetSignalInfoReceiver.this.f1401a.C = String.valueOf(cVar.q());
            GetSignalInfoReceiver.this.f1401a.D = String.valueOf(cVar.d());
            int iA = c.d.a.a.a.a(0);
            if (iA > 0 && (str2 = GetSignalInfoReceiver.this.f1401a.B) != null && str2 != "") {
                c.d.a.b.h.a("GetSignalInfoReceiver", "signalInfo.longitude: " + GetSignalInfoReceiver.this.f1401a.B);
                c.d.a.b.h.a("GetSignalInfoReceiver", "Receiver subIdSlot1: " + iA);
                new d().b(this.f1403a, 0, GetSignalInfoReceiver.this.f1401a);
                JSONObject jSONObjectA = GetSignalInfoReceiver.this.f1401a.a();
                c.d.a.b.h.a("GetSignalInfoReceiver", "siginfo: " + jSONObjectA);
                if (jSONObjectA != null) {
                    try {
                        if (GetSignalInfoReceiver.this.f1401a.f != null && GetSignalInfoReceiver.a(GetSignalInfoReceiver.this.f1401a).booleanValue()) {
                            m mVarA = m.a(GetSignalInfoReceiver.f1400c, m.f1342c, m.f1341b.intValue());
                            mVarA.a(jSONObjectA.toString());
                            mVarA.close();
                        }
                    } catch (Exception e) {
                        c.d.a.b.h.b("GetSignalInfoReceiver", "inSigChinaNetwork");
                        e.printStackTrace();
                    }
                }
            }
            int iA2 = c.d.a.a.a.a(1);
            if (iA2 > 0 && (str = GetSignalInfoReceiver.this.f1401a.B) != null && str != "") {
                c.d.a.b.h.a("GetSignalInfoReceiver", "signalInfo.longitude: " + GetSignalInfoReceiver.this.f1401a.B);
                c.d.a.b.h.a("GetSignalInfoReceiver", "Receiver subIdSlot2: " + iA2);
                new d().b(this.f1403a, 1, GetSignalInfoReceiver.this.f1401a);
                JSONObject jSONObjectA2 = GetSignalInfoReceiver.this.f1401a.a();
                c.d.a.b.h.a("GetSignalInfoReceiver", "siginfo: " + jSONObjectA2);
                if (jSONObjectA2 != null) {
                    try {
                        if (GetSignalInfoReceiver.this.f1401a.f != null && GetSignalInfoReceiver.a(GetSignalInfoReceiver.this.f1401a).booleanValue()) {
                            m mVarA2 = m.a(GetSignalInfoReceiver.f1400c, m.f1342c, m.f1341b.intValue());
                            mVarA2.a(jSONObjectA2.toString());
                            mVarA2.close();
                        }
                    } catch (Exception e2) {
                        c.d.a.b.h.b("GetSignalInfoReceiver", "inSigChinaNetwork");
                        e2.printStackTrace();
                    }
                }
            }
            c.d.a.b.h.a("GetSignalInfoReceiver", "经纬度信息signalInfo.longitude： " + GetSignalInfoReceiver.this.f1401a.B + ", " + GetSignalInfoReceiver.this.f1401a.C + ", " + GetSignalInfoReceiver.this.f1401a.D);
        }
    }

    public static Boolean a(n nVar) {
        c.d.a.b.h.a("GetSignalInfoReceiver", "siginfo.mncCH: " + nVar.f);
        String str = nVar.f;
        return (str == null || str.isEmpty() || !nVar.f.startsWith("460")) ? false : true;
    }

    private boolean a(Context context) {
        boolean z = false;
        Boolean boolValueOf = Boolean.valueOf(Settings.Global.getInt(context.getContentResolver(), "device_provisioned", 0) == 1);
        c.d.a.b.h.a("GetSignalInfoReceiver", "isBootWizardCompleted: " + boolValueOf);
        if (boolValueOf.booleanValue()) {
            z = Settings.Secure.getInt(context.getContentResolver(), "operator_service_improve_plan", 0) == 1;
            c.d.a.b.h.a("GetSignalInfoReceiver", "checkUserPermission:" + z);
        }
        return z;
    }

    private boolean b(Context context) {
        Boolean boolValueOf = Boolean.valueOf(Settings.Global.getInt(context.getContentResolver(), "device_provisioned", 0) == 1);
        c.d.a.b.h.a("GetSignalInfoReceiver", "isBootWizardCompleted: " + boolValueOf);
        return boolValueOf.booleanValue();
    }

    public void a(com.baidu.location.b bVar) {
        h.b(true);
        h hVar = this.f1402b;
        if (hVar != null) {
            hVar.a(bVar);
            i iVar = new i();
            iVar.a("bd09ll");
            iVar.a(i.b.Hight_Accuracy);
            iVar.c(true);
            iVar.a(0);
            iVar.a(true);
            this.f1402b.a(iVar);
            this.f1402b.j();
            c.d.a.b.h.a("GetSignalInfoReceiver", "mLocationClient start!");
        }
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        f1400c = context;
        String strA = l.a("ro.product.manufacturer");
        h.b(true);
        c.d.a.a.a.e(context);
        c.d.a.b.h.a("GetSignalInfoReceiver", "enter GetSignalInfoReceiver --->");
        try {
            this.f1402b = new h(f1400c);
            c.d.a.b.a.b(f1400c, l.j(context));
            if (strA != null && !strA.isEmpty()) {
                c.d.a.b.d.e = strA.equalsIgnoreCase("vivo") ? a(f1400c) : b(f1400c);
            }
            if (c.d.a.b.d.e) {
                a aVar = new a(context);
                d = aVar;
                a(aVar);
                c.d.a.a.n.c();
                c.d.a.a.n.a(context);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
