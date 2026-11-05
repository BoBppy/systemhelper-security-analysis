package com.mobiletools.systemhelper.control;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.TelephonyManager;
import c.d.a.a.n;
import c.d.a.b.h;

/* JADX INFO: loaded from: classes.dex */
public class SIMStateReceiver extends BroadcastReceiver {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static Context f1406a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static SIMStateReceiver f1407b;

    public static void a() {
        SIMStateReceiver sIMStateReceiver = f1407b;
        if (sIMStateReceiver != null) {
            f1406a.unregisterReceiver(sIMStateReceiver);
            h.a("SIMStateReceiver", "SimStateReceiver cancel watch-->");
            f1407b = null;
            c.d.a.b.d.j = false;
        }
    }

    public void a(Context context) {
        f1406a = context;
        a();
        c.d.a.b.d.j = true;
        f1407b = this;
        h.a("SIMStateReceiver", "SimStateReceiver startWatch-->");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        f1406a.registerReceiver(this, intentFilter);
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x005b, code lost:
    
        if (r2 != 6) goto L24;
     */
    @Override // android.content.BroadcastReceiver
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onReceive(Context context, Intent intent) {
        f1406a = context;
        h.a("SIMStateReceiver", "onReceive-->intent action: " + intent.getAction());
        if (c.d.a.b.d.j) {
            c.d.a.b.d.j = false;
            return;
        }
        String action = intent.getAction();
        if (action != null && action.equals("android.intent.action.SIM_STATE_CHANGED")) {
            int simState = ((TelephonyManager) context.getSystemService("phone")).getSimState();
            h.a("SIMStateReceiver", "SIM_STATE_CHANGED, state:" + simState);
            if (simState != 0) {
                if (simState != 1) {
                    if (simState != 5) {
                    }
                    c.d.a.a.a.e(context);
                    h.a("SIMStateReceiver", "wait for sim ready for 6 minutes-->");
                    c.d.a.b.a.c(f1406a, 180000L);
                    return;
                }
                c.d.a.a.a.f(context);
                c.d.a.a.a.e(context);
                h.a("SIMStateReceiver", "wait for sim ready for 6 minutes-->");
                c.d.a.b.a.c(f1406a, 180000L);
                return;
            }
            c.d.a.a.a.f(context);
            c.d.a.a.a.f(context);
            c.d.a.a.a.f(context);
            c.d.a.a.a.e(context);
            h.a("SIMStateReceiver", "wait for sim ready for 6 minutes-->");
            c.d.a.b.a.c(f1406a, 180000L);
            return;
        }
        n.c();
        if (n.a(context) != null) {
            b.a(f1406a, "T");
            c.d.a.a.a.f(context);
        }
    }
}
