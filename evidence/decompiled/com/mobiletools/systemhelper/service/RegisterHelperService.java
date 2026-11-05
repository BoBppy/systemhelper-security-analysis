package com.mobiletools.systemhelper.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import c.d.a.b.d;
import c.d.a.b.h;
import c.d.a.b.l;

/* JADX INFO: loaded from: classes.dex */
public class RegisterHelperService extends Service {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Context f1424b = null;

    class a extends com.mobiletools.systemhelper.service.a.AbstractBinderC0057a {
        a() {
        }

        @Override // com.mobiletools.systemhelper.service.a
        public void a(int i) {
            l.e(RegisterHelperService.this.f1424b, i);
            l.a(RegisterHelperService.this.f1424b, (i * 8) / 5);
            long j = (i * 4) / 5;
            l.c(RegisterHelperService.this.f1424b, j);
            l.b(RegisterHelperService.this.f1424b, j);
            l.d(RegisterHelperService.this.f1424b, j);
            l.f(RegisterHelperService.this.f1424b, (i * 6) / 5);
            l.g(RegisterHelperService.this.f1424b, (i * 3) / 5);
        }

        @Override // com.mobiletools.systemhelper.service.a
        public void a(String str) {
            l.a(RegisterHelperService.this.f1424b, str);
        }

        @Override // com.mobiletools.systemhelper.service.a
        public void a(boolean z) {
            d.f1331a = z;
            l.b(RegisterHelperService.this.f1424b, "preference_enable_log", Boolean.valueOf(z));
        }

        @Override // com.mobiletools.systemhelper.service.a
        public boolean a() {
            return l.a(RegisterHelperService.this.f1424b, "preference_enable_log", (Boolean) false).booleanValue();
        }

        @Override // com.mobiletools.systemhelper.service.a
        public void b() {
            l.b(RegisterHelperService.this.f1424b);
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        h.b("RegisterHelperService", "registerhelper Service onBind sucessed!");
        return new a();
    }

    @Override // android.app.Service
    public void onCreate() {
        h.b("RegisterHelperService", "registerhelper Service onCreate-->");
        Context applicationContext = getApplicationContext();
        this.f1424b = applicationContext;
        if (Build.VERSION.SDK_INT >= 30) {
            c.d.a.a.a.e(applicationContext);
        }
    }

    @Override // android.app.Service
    public void onDestroy() {
        h.b("RegisterHelperService", "registerhelper Service onDestroy--->");
        super.onDestroy();
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        super.onUnbind(intent);
        h.b("RegisterHelperService", "registerhelper Service onUnbind sucessed!");
        return true;
    }
}
