package com.mobiletools.systemhelper.control;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import c.d.a.a.n;
import c.d.a.b.h;
import c.d.a.b.l;

/* JADX INFO: loaded from: classes.dex */
public class AppInfoRegisterReceiver extends BroadcastReceiver {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static Context f1397a;

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        f1397a = context;
        h.a("AppInfoRegisterReceiver", "onReceive-->intent action: " + intent.getAction());
        if (Build.VERSION.SDK_INT >= 31) {
            c.d.a.a.a.e(context);
        }
        c.d.a.b.a.a(f1397a, l.d(context), l.c(context));
        n.c();
        if (n.a(context) != null) {
            b.a(f1397a, "A");
        }
    }
}
