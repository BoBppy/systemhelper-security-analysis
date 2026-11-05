package com.mobiletools.systemhelper.service;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import c.d.a.a.n;
import c.d.a.b.d;
import c.d.a.b.h;

/* JADX INFO: loaded from: classes.dex */
public class RegisterIntentService extends IntentService {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Context f1426b;

    class a implements ServiceConnection {
        a() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            h.a("RegisterIntentService", "onServiceConnected-->");
            d.w = c.c.a.a.AbstractBinderC0054a.a(iBinder);
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            h.a("RegisterIntentService", "onServiceDisconnected-->");
            d.w = null;
        }
    }

    static {
        new a();
    }

    public RegisterIntentService() {
        super("RegisterIntentService");
        this.f1426b = null;
    }

    @Override // android.app.IntentService, android.app.Service
    public void onCreate() {
        super.onCreate();
        h.a("RegisterIntentService", "RegisterIntentService onCreate-->");
        this.f1426b = getApplicationContext();
        n.c();
        n.a(this.f1426b);
    }

    @Override // android.app.IntentService, android.app.Service
    public void onDestroy() {
        super.onDestroy();
    }

    @Override // android.app.IntentService
    protected void onHandleIntent(Intent intent) {
        intent.getStringExtra("RegType");
        n.a(this.f1426b);
    }
}
