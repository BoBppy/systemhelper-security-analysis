package com.mobiletools.systemhelper.control;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import c.d.a.a.n;
import c.d.a.b.h;
import c.d.a.b.l;

/* JADX INFO: loaded from: classes.dex */
public class SignalInfoRegisterReceiver extends BroadcastReceiver {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static Context f1409a;

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        f1409a = context;
        h.a("SignalInfoRegisterReceiver", "onReceive-->intent action: " + intent.getAction());
        c.d.a.a.a.e(context);
        c.d.a.b.a.c(f1409a, l.f(context), l.i(context));
        n.c();
        if (n.a(context) != null) {
            b.a(f1409a, "R");
        }
    }
}
