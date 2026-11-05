package com.mobiletools.systemhelper.control;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import c.d.a.b.h;
import c.d.a.b.l;

/* JADX INFO: loaded from: classes.dex */
public class ConnectStateReceiver extends BroadcastReceiver {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static Context f1398a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static ConnectStateReceiver f1399b;

    public static void a() {
        ConnectStateReceiver connectStateReceiver = f1399b;
        if (connectStateReceiver != null) {
            f1398a.unregisterReceiver(connectStateReceiver);
            h.a("ConnectStateReceiver", "ConnectStateReceiver cancel watch-->");
            f1399b = null;
            c.d.a.b.d.k = false;
        }
    }

    public void a(Context context) {
        f1398a = context;
        a();
        c.d.a.b.d.k = true;
        f1399b = this;
        h.a("ConnectStateReceiver", "ConnectStateReceiver startWatch-->");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        f1398a.registerReceiver(this, intentFilter);
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        f1398a = context;
        h.a("ConnectStateReceiver", "onReceive-->intent action: " + intent.getAction());
        if (c.d.a.b.d.k) {
            c.d.a.b.d.k = false;
            return;
        }
        String action = intent.getAction();
        if (action == null || !action.equals("android.net.conn.CONNECTIVITY_CHANGE")) {
            return;
        }
        h.a("ConnectStateReceiver", !l.k(f1398a).booleanValue() ? "NetWork disconnected" : "NetWork connected");
    }
}
