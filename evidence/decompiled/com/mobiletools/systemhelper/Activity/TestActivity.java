package com.mobiletools.systemhelper.Activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.c;
import android.widget.TextView;
import c.d.a.a.a;
import c.d.a.a.n;
import c.d.a.b.d;
import c.d.a.b.l;
import com.mobiletools.systemhelper.R;
import com.mobiletools.systemhelper.control.b;

/* JADX INFO: loaded from: classes.dex */
public class TestActivity extends c {
    private TextView q;

    private void l() {
        this.q = (TextView) findViewById(R.id.tv_device_info);
        n.c();
        this.q.setText(n.a(this) != null ? b.a(getApplicationContext(), "").a() : "");
    }

    @Override // android.support.v7.app.c, android.support.v4.app.f, android.support.v4.app.x, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_test);
        if (!d.f1332b) {
            d.f1331a = l.a((Context) this, "preference_enable_log", (Boolean) false).booleanValue();
        }
        if (Build.VERSION.SDK_INT >= 31) {
            a.e(this);
        }
        if (b.b.c.a.a.a(this, "android.permission.READ_PHONE_STATE") == 0 && b.b.c.a.a.a(this, "android.permission.ACCESS_COARSE_LOCATION") == 0 && b.b.c.a.a.a(this, "android.permission.ACCESS_FINE_LOCATION") == 0 && b.b.c.a.a.a(this, "android.permission.ACCESS_BACKGROUND_LOCATION") == 0 && b.b.c.a.a.a(this, "android.permission.ACCESS_NETWORK_STATE") == 0) {
            l();
        } else {
            android.support.v4.app.a.a(this, new String[]{"android.permission.READ_PHONE_STATE", "android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_BACKGROUND_LOCATION", "android.permission.ACCESS_NETWORK_STATE"}, 1);
        }
    }

    @Override // android.support.v4.app.f, android.app.Activity, android.support.v4.app.a.b
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (i != 1) {
            return;
        }
        if (iArr.length == 3 && iArr[0] == 0 && iArr[1] == 0 && iArr[2] == 0) {
            l();
        } else {
            finish();
        }
    }
}
