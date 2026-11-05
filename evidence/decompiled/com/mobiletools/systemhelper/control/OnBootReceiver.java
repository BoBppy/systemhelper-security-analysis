package com.mobiletools.systemhelper.control;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import c.d.a.a.n;
import c.d.a.b.h;
import c.d.a.b.l;

/* JADX INFO: loaded from: classes.dex */
public class OnBootReceiver extends BroadcastReceiver {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static Context f1405a;

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        f1405a = context;
        if (!c.d.a.b.d.f1332b) {
            c.d.a.b.d.f1331a = l.a(context, "preference_enable_log", (Boolean) false).booleanValue();
        }
        h.a("OnBootReceiver", "onReceive-->intent action: " + intent.getAction());
        c.d.a.a.a.e(context);
        String action = intent.getAction();
        if (action != null && (action.equals("android.intent.action.BOOT_COMPLETED") || action.equals("android.intent.action.QUICKBOOT_POWERON"))) {
            h.a("OnBootReceiver", " boot delayed for 4 minutes-->");
            c.d.a.b.a.a(f1405a, 240000L);
            c.d.a.b.a.b(f1405a, l.j(context));
            c.d.a.b.a.c(f1405a, l.f(context), l.i(context));
            c.d.a.b.a.b(f1405a, l.e(context), l.g(context));
            c.d.a.b.a.a(f1405a, l.d(context), l.c(context));
            return;
        }
        h.a("OnBootReceiver", "SIM_STATE_CHANGED, state:" + ((TelephonyManager) context.getSystemService("phone")).getSimState());
        new SIMStateReceiver().a(f1405a.getApplicationContext());
        new ConnectStateReceiver().a(f1405a.getApplicationContext());
        n.c();
        if (n.a(context) != null) {
            String strA = l.a(context, "firstPowerOn", "");
            if (strA != null && !strA.isEmpty()) {
                b.a(f1405a, "B");
            } else {
                b.a(f1405a, "F");
                l.b(context, "firstPowerOn", "firstPowerOn");
            }
        }
    }
}
