package com.mobiletools.systemhelper.control;

import android.content.Context;
import c.d.a.b.h;
import c.d.a.b.l;
import java.net.URLEncoder;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class e extends Thread {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Context f1419b;

    public e(Context context) {
        this.f1419b = null;
        this.f1419b = context;
    }

    private synchronized boolean a(JSONObject jSONObject) {
        String string;
        boolean z = false;
        if (!c.d.a.b.d.e) {
            return false;
        }
        String string2 = null;
        try {
            string = jSONObject.getString("Model");
            try {
                string2 = jSONObject.getString("Manuf");
            } catch (JSONException e) {
                e = e;
                e.printStackTrace();
            }
        } catch (JSONException e2) {
            e = e2;
            string = null;
        }
        String string3 = jSONObject.toString();
        h.a("HistoryPostThread", "model:" + string + " ,manuf:" + string2 + " ,postString：" + string3);
        try {
            byte[] bArrA = c.d.a.b.c.a(string3);
            String strEncode = URLEncoder.encode(c.d.a.b.c.a(c.d.a.b.c.a(this.f1419b, bArrA)), "utf-8");
            String str = l.h(this.f1419b) + "?ver=5.0&model=" + URLEncoder.encode(string, "utf-8") + "&manuf=" + URLEncoder.encode(string2, "utf-8") + "&sign=" + strEncode;
            byte[] bArrA2 = c.d.a.b.c.a(string3, bArrA);
            try {
                c.d.a.b.f fVar = new c.d.a.b.f();
                boolean zA = fVar.a(str, bArrA2, bArrA);
                if (!zA && fVar.a() == 416) {
                    zA = true;
                }
                z = zA;
            } catch (Exception e3) {
                h.b("HistoryPostThread", "sendPOSTRequest exception--->" + e3.toString());
            }
            return z;
        } catch (Exception e4) {
            h.b("HistoryPostThread", "doEncrpytPost catch exception " + e4.getMessage());
            return false;
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        if (c.d.a.b.d.e) {
            c.d.a.b.e eVarA = c.d.a.b.e.a(this.f1419b, c.d.a.b.e.f1335c, c.d.a.b.e.f1334b.intValue());
            try {
                JSONObject jSONObjectA = eVarA.a();
                if (jSONObjectA != null) {
                    h.a("HistoryPostThread", "post history result:" + a(jSONObjectA));
                }
                eVarA.close();
            } catch (Exception e) {
                e.printStackTrace();
                h.b("HistoryPostThread", "Exception : " + e);
            }
        }
    }
}
