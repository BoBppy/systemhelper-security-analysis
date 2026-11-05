package c.d.a.b;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class g {
    public static long a(Context context) {
        long jC = c();
        return jC < 0 ? c(context) : jC;
    }

    public static String a() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return ((int) Math.round(Double.valueOf((statFs.getFreeBlocksLong() * statFs.getBlockSizeLong()) / Math.pow(1024.0d, 3.0d)).doubleValue())) + "G";
    }

    public static String b() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        Double dValueOf = Double.valueOf((statFs.getBlockCountLong() * statFs.getBlockSizeLong()) / Math.pow(1024.0d, 3.0d));
        int i = 0;
        while (dValueOf.doubleValue() > 0.0d) {
            int i2 = i + 1;
            if (dValueOf.doubleValue() < Math.pow(2.0d, i2) && dValueOf.doubleValue() >= Math.pow(2.0d, i)) {
                break;
            }
            i = i2;
        }
        return ((int) Math.pow(2.0d, i + 1)) + "G";
    }

    public static JSONObject b(Context context) {
        String str = a(context) + "G";
        String strB = b();
        String strA = a();
        if (str == null) {
            str = "Unknown";
        }
        if (strB == null) {
            strB = "Unknown";
        }
        if (strA == null) {
            strA = "Unknown";
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("RAM", str);
            jSONObject.put("ROM", strB);
            jSONObject.put("FREE", strA);
            return jSONObject;
        } catch (JSONException e) {
            h.b("MemoryUtils", e.getMessage());
            return null;
        }
    }

    private static long c() {
        String strB = l.b("ro.boot.dram_info", "");
        if (strB == null || strB.isEmpty()) {
            h.b("MemoryUtils", "No dram_info property");
            return -1L;
        }
        h.a("MemoryUtils", "dramInfo:" + strB);
        String[] strArrSplit = strB.split(",");
        String strReplaceAll = strArrSplit[strArrSplit.length + (-1)].replaceAll("\\D+", "");
        h.a("MemoryUtils", "parsed value : " + strReplaceAll);
        if (strReplaceAll == null || strReplaceAll.isEmpty()) {
            return -1L;
        }
        return Long.valueOf(strReplaceAll).longValue() * 1;
    }

    private static long c(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        long j = memoryInfo.totalMem / 1048576;
        if (j > 30720) {
            return 32L;
        }
        if (j > 28672) {
            return 30L;
        }
        if (j > 26624) {
            return 28L;
        }
        if (j > 24576) {
            return 26L;
        }
        if (j > 22528) {
            return 24L;
        }
        if (j > 20480) {
            return 22L;
        }
        if (j > 18432) {
            return 20L;
        }
        if (j > 16384) {
            return 18L;
        }
        if (j > 14336) {
            return 16L;
        }
        if (j > 12288) {
            return 14L;
        }
        if (j > 10240) {
            return 12L;
        }
        if (j > 6144) {
            return 8L;
        }
        if (j > 4096) {
            return 6L;
        }
        if (j > 3072) {
            return 4L;
        }
        if (j > 2048) {
            return 3L;
        }
        if (j > 1536) {
            return 2L;
        }
        if (j > 1024 || j > 768) {
            return 1L;
        }
        int i = (j > 512L ? 1 : (j == 512L ? 0 : -1));
        return 0L;
    }
}
