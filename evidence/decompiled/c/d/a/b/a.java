package c.d.a.b;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import com.mobiletools.systemhelper.control.AppInfoRegisterReceiver;
import com.mobiletools.systemhelper.control.GetSignalInfoReceiver;
import com.mobiletools.systemhelper.control.OnBootReceiver;
import com.mobiletools.systemhelper.control.SIMStateReceiver;
import com.mobiletools.systemhelper.control.ScheduleRegisterReceiver;
import com.mobiletools.systemhelper.control.SignalInfoRegisterReceiver;

/* JADX INFO: loaded from: classes.dex */
public class a {
    public static void a(Context context, long j) {
        ((AlarmManager) context.getSystemService("alarm")).setExactAndAllowWhileIdle(2, SystemClock.elapsedRealtime() + j, PendingIntent.getBroadcast(context, 0, new Intent(context, (Class<?>) OnBootReceiver.class), 201326592));
    }

    public static void a(Context context, long j, long j2) {
        h.a("AlarmHelper", "AppInfoAlarmAfter--->");
        h.a("AlarmHelper", "GlobalSetting.count_app = " + d.q);
        int i = d.q;
        h.a("AlarmHelper", "AppInfoAlarmAfter--->");
        if (i != 0) {
            ((AlarmManager) context.getSystemService("alarm")).setExactAndAllowWhileIdle(2, SystemClock.elapsedRealtime() + j2, PendingIntent.getBroadcast(context, 0, new Intent(context, (Class<?>) AppInfoRegisterReceiver.class), 201326592));
            h.a("AlarmHelper", "GlobalSetting.DEFAULT_RANDOM AppInfoAlarmAfter: " + d.m);
            h.a("AlarmHelper", "AppInfoAlarmAfter  SystemClock.elapsedRealtime() + ms: " + d.m);
            return;
        }
        ((AlarmManager) context.getSystemService("alarm")).setExactAndAllowWhileIdle(2, SystemClock.elapsedRealtime() + j + j2, PendingIntent.getBroadcast(context, 0, new Intent(context, (Class<?>) AppInfoRegisterReceiver.class), 201326592));
        h.a("AlarmHelper", "GlobalSetting.DEFAULT_RANDOM AppInfoAlarmAfter test: " + d.m);
        h.a("AlarmHelper", "测试助手修改后的app info rand ： " + j);
        d.q = d.q + 1;
    }

    public static void b(Context context, long j) {
        h.a("AlarmHelper", "GetSignalInfoAlarmAfter====>");
        Intent intent = new Intent(context, (Class<?>) GetSignalInfoReceiver.class);
        h.a("AlarmHelper", "intent: " + intent);
        ((AlarmManager) context.getSystemService("alarm")).setExactAndAllowWhileIdle(2, SystemClock.elapsedRealtime() + j, PendingIntent.getBroadcast(context, 0, intent, 201326592));
    }

    public static void b(Context context, long j, long j2) {
        h.a("AlarmHelper", "ScheduleAlarmAfter--->");
        h.a("AlarmHelper", "GlobalSetting.count = " + d.p);
        if (d.p != 0) {
            ((AlarmManager) context.getSystemService("alarm")).setExactAndAllowWhileIdle(2, SystemClock.elapsedRealtime() + j2, PendingIntent.getBroadcast(context, 0, new Intent(context, (Class<?>) ScheduleRegisterReceiver.class), 201326592));
            h.a("AlarmHelper", "GlobalSetting.DEFAULT_RANDOM_SCHEGULE ScheduleAlarmAfter: " + d.n);
            return;
        }
        ((AlarmManager) context.getSystemService("alarm")).setExactAndAllowWhileIdle(2, SystemClock.elapsedRealtime() + j + j2, PendingIntent.getBroadcast(context, 0, new Intent(context, (Class<?>) ScheduleRegisterReceiver.class), 201326592));
        h.a("AlarmHelper", "GlobalSetting.DEFAULT_RANDOM_SCHEGULE ScheduleAlarmAfter test: " + d.n);
        h.a("AlarmHelper", "测试助手修改后的rand ： " + j);
        d.p = d.p + 1;
    }

    public static void c(Context context, long j) {
        ((AlarmManager) context.getSystemService("alarm")).setExactAndAllowWhileIdle(2, SystemClock.elapsedRealtime() + j, PendingIntent.getBroadcast(context, 0, new Intent(context, (Class<?>) SIMStateReceiver.class), 201326592));
    }

    public static void c(Context context, long j, long j2) {
        h.a("AlarmHelper", "SignalInfoAlarmAfter--->");
        h.a("AlarmHelper", "GlobalSetting.count_sig = " + d.r);
        if (d.r != 0) {
            h.a("AlarmHelper", "GlobalSetting.count_sig > 0");
            ((AlarmManager) context.getSystemService("alarm")).setExactAndAllowWhileIdle(2, SystemClock.elapsedRealtime() + j2, PendingIntent.getBroadcast(context, 0, new Intent(context, (Class<?>) SignalInfoRegisterReceiver.class), 201326592));
            return;
        }
        ((AlarmManager) context.getSystemService("alarm")).setExactAndAllowWhileIdle(2, SystemClock.elapsedRealtime() + j + j2, PendingIntent.getBroadcast(context, 0, new Intent(context, (Class<?>) SignalInfoRegisterReceiver.class), 201326592));
        h.a("AlarmHelper", "测试助手修改后的rand ： " + j);
        d.r = d.r + 1;
    }
}
