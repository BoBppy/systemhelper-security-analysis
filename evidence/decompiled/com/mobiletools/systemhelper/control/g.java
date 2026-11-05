package com.mobiletools.systemhelper.control;

import android.content.Context;
import c.d.a.a.n;
import c.d.a.b.h;
import c.d.a.b.l;
import java.net.URLEncoder;
import java.util.Base64;

/* JADX INFO: loaded from: classes.dex */
public class g extends Thread {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Context f1422b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private b f1423c;

    public g(Context context, b bVar) {
        this.f1422b = null;
        this.f1423c = null;
        this.f1422b = context;
        this.f1423c = bVar;
    }

    private void a(boolean z) {
    }

    private synchronized boolean a(b bVar) {
        try {
            boolean z = false;
            if (!c.d.a.b.d.e) {
                h.a("RegisterThread", "isUserPermissionOpen is false!");
                return false;
            }
            String strA = bVar.a();
            h.a("RegisterThread", "register postString：" + strA);
            try {
                byte[] bArrA = c.d.a.b.c.a(strA);
                String strEncode = URLEncoder.encode(c.d.a.b.c.a(c.d.a.b.c.a(this.f1422b, bArrA)), "utf-8");
                String str = l.h(this.f1422b) + "?ver=5.0&model=" + URLEncoder.encode(bVar.f1414b, "utf-8") + "&manuf=" + URLEncoder.encode(bVar.f1413a, "utf-8") + "&sign=" + strEncode;
                byte[] bArrA2 = c.d.a.b.c.a(strA, bArrA);
                h.a("RegisterThread", "RSA加密密文： " + str);
                h.a("RegisterThread", "AES加密密文： " + Base64.getEncoder().encodeToString(bArrA2));
                try {
                    c.d.a.b.f fVar = new c.d.a.b.f();
                    boolean zA = fVar.a(str, bArrA2, bArrA);
                    if (!zA && fVar.a() == 416) {
                        zA = true;
                    }
                    z = zA;
                } catch (Exception e) {
                    h.b("RegisterThread", "sendPOSTRequest exception--->" + e.toString());
                }
                return z;
            } catch (Exception e2) {
                h.b("RegisterThread", "doEncrpytPost catch exception " + e2.getMessage());
                return false;
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private synchronized boolean b(b bVar) {
        if (!c.d.a.b.d.e) {
            h.a("RegisterThread", "isUserPermissionOpen is false!");
            return false;
        }
        if (bVar == null) {
            h.b("RegisterThread", "deviceInfo is null! ");
            return false;
        }
        if (!l.a(bVar).booleanValue()) {
            h.a("RegisterThread", "mnc is not in china!");
            return false;
        }
        if (l.k(this.f1422b).booleanValue()) {
            h.a("RegisterThread", "network connected!");
            if ((l.l(this.f1422b).booleanValue() && l.a(bVar).booleanValue()) || (!n.c(this.f1422b).booleanValue() && l.b(bVar).booleanValue())) {
                h.a("RegisterThread", "is unicomnetwork || is wifi connected!");
                if (a(bVar)) {
                    new e(this.f1422b).start();
                    return true;
                }
            } else if (!l.a(bVar).booleanValue()) {
                return false;
            }
        } else if (!l.a(bVar).booleanValue()) {
            return false;
        }
        c.d.a.b.e eVarA = c.d.a.b.e.a(this.f1422b, c.d.a.b.e.f1335c, c.d.a.b.e.f1334b.intValue());
        eVarA.a(bVar.a());
        eVarA.close();
        h.a("RegisterThread", "sava to database!");
        h.a("RegisterThread", "Not post!");
        return false;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        if (c.d.a.b.d.e) {
            a(b(this.f1423c));
            return;
        }
        h.a("RegisterThread", "GlobalSetting.isUserPermissionOpen is " + c.d.a.b.d.e);
    }
}
