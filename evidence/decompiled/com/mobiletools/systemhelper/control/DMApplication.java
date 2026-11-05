package com.mobiletools.systemhelper.control;

import android.app.Application;
import android.os.Build;
import c.d.a.b.h;

/* JADX INFO: loaded from: classes.dex */
public class DMApplication extends Application {
    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        h.a("DMApplication", "onCreate--->");
        if (Build.VERSION.SDK_INT >= 31) {
            c.d.a.a.a.e(this);
        }
    }

    @Override // android.app.Application
    public void onTerminate() {
        super.onTerminate();
        h.a("DMApplication", "onTerminate--->");
    }
}
