package c.d.a.a;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellIdentityTdscdma;
import android.telephony.CellIdentityWcdma;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.ims.ImsManager;
import android.text.TextUtils;
import android.util.Log;
import java.lang.reflect.Method;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public class a implements n.c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final String f1323a = a("persist.vendor.radio.5g_mode_pref", "0");

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final String f1324b = a("persist.radio.vivo.fiveg", "false");

    public static int a(int i) {
        String strA = c.d.a.b.l.a("ro.product.manufacturer");
        if (strA == null || strA.isEmpty()) {
            return -1;
        }
        if (!strA.equalsIgnoreCase("Xiaomi") && !strA.equalsIgnoreCase("SamSung") && !strA.equalsIgnoreCase("WIKO")) {
            int[] iArr = (int[]) c.d.a.b.k.a(c.d.a.b.k.a(c.d.a.b.k.a("android.telephony.SubscriptionManager"), "getSubId", (Class<?>[]) new Class[]{Integer.TYPE}), (Object) null, Integer.valueOf(i));
            if (iArr != null) {
                return iArr[0];
            }
            return -1;
        }
        Class<?> clsA = c.d.a.b.k.a("android.telephony.SubscriptionManager");
        if (Build.VERSION.SDK_INT > 33) {
            return ((Integer) c.d.a.b.k.a(c.d.a.b.k.a(clsA, "getSubscriptionId", (Class<?>[]) new Class[]{Integer.TYPE}), (Object) null, Integer.valueOf(i))).intValue();
        }
        int[] iArr2 = (int[]) c.d.a.b.k.a(c.d.a.b.k.a(clsA, "getSubId", (Class<?>[]) new Class[]{Integer.TYPE}), (Object) null, Integer.valueOf(i));
        if (iArr2 != null) {
            return iArr2[0];
        }
        return -1;
    }

    public static String a(String str, String str2) {
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            Object objInvoke = cls.getMethod("get", String.class, String.class).invoke(cls, str, str2);
            return (objInvoke == null || !(objInvoke instanceof String)) ? str2 : (String) objInvoke;
        } catch (Exception e) {
            Log.e("DeviceInfoReaderBase", "getString failed", e);
            return str2;
        }
    }

    private String c(Context context, int i) {
        String str;
        int defaultDataSubscriptionId = SubscriptionManager.getDefaultDataSubscriptionId();
        if (Build.VERSION.SDK_INT >= 29) {
            str = SubscriptionManager.getSlotIndex(defaultDataSubscriptionId) + "";
        } else {
            str = null;
        }
        Log.d("DeviceInfoReaderBase", "DataSlot:" + str);
        String strB = b(context, i);
        int i2 = Integer.parseInt(str);
        Log.d("DeviceInfoReaderBase", "get5GState: ----switchState:" + strB + "-----slotBySubId：" + i2);
        StringBuilder sb = new StringBuilder();
        sb.append("phoneId:");
        sb.append(i);
        Log.d("DeviceInfoReaderBase", sb.toString());
        return (c(context) || i2 == i) ? strB : "0";
    }

    private String d(Context context) {
        String string = Settings.Global.getString(context.getContentResolver(), "miui_version_name");
        String strA = c.d.a.b.l.a("ro.build.version.incremental");
        if (!TextUtils.isEmpty(string) && string.length() > 1) {
            return string.replaceAll("\r|\n", "");
        }
        if (strA == null || strA.length() == 0) {
            return strA;
        }
        int iLastIndexOf = strA.lastIndexOf(".");
        String strSubstring = strA.substring(1, iLastIndexOf);
        return "MIUI " + strSubstring.substring(0, strSubstring.indexOf(".", strSubstring.indexOf(".", strSubstring.indexOf(".") + 1) + 1)) + strSubstring + "(" + strA.substring(iLastIndexOf + 1) + ")";
    }

    public static synchronized void e(Context context) {
        c.d.a.b.h.a("DeviceInfoReaderBase", "listenNetworkState---->");
        if (c.d.a.b.d.e) {
            int iA = a(0);
            c.d.a.b.h.a("DeviceInfoReaderBase", "subIdSlot1:" + iA);
            if (iA > 0 && c.d.a.b.d.f == null) {
                c.d.a.b.d.f = ((TelephonyManager) context.getSystemService("phone")).createForSubscriptionId(iA);
                c.d.a.b.h.a("DeviceInfoReaderBase", "telephonyManagerSlot1" + c.d.a.b.d.f);
                if (c.d.a.b.d.h == null) {
                    c.d.a.b.h.a("DeviceInfoReaderBase", "registerTelephonyCallback--mDMTelephonyCallback1");
                    c.d.a.b.d.h = new com.mobiletools.systemhelper.control.a(context, 0);
                    c.d.a.b.d.f.registerTelephonyCallback(context.getMainExecutor(), c.d.a.b.d.h);
                }
            }
            int iA2 = a(1);
            c.d.a.b.h.a("DeviceInfoReaderBase", "subIdSlot2:" + iA2);
            if (iA2 > 0 && c.d.a.b.d.g == null) {
                c.d.a.b.d.g = ((TelephonyManager) context.getSystemService("phone")).createForSubscriptionId(iA2);
                c.d.a.b.h.a("DeviceInfoReaderBase", "telephonyManagerSlot2" + c.d.a.b.d.g);
                if (c.d.a.b.d.i == null) {
                    c.d.a.b.h.a("DeviceInfoReaderBase", "registerTelephonyCallback--mDMTelephonyCallback2");
                    c.d.a.b.d.i = new com.mobiletools.systemhelper.control.a(context, 1);
                    c.d.a.b.d.g.registerTelephonyCallback(context.getMainExecutor(), c.d.a.b.d.i);
                }
            }
        }
    }

    public static synchronized void f(Context context) {
        c.d.a.b.h.a("DeviceInfoReaderBase", "unlistenNetworkState-->");
        if (c.d.a.b.d.h != null && Build.VERSION.SDK_INT >= 31) {
            c.d.a.b.d.f.unregisterTelephonyCallback(c.d.a.b.d.h);
        }
        c.d.a.b.d.f = null;
        c.d.a.b.d.h = null;
        if (c.d.a.b.d.i != null && Build.VERSION.SDK_INT >= 31) {
            c.d.a.b.d.g.unregisterTelephonyCallback(c.d.a.b.d.i);
        }
        c.d.a.b.d.g = null;
        c.d.a.b.d.i = null;
    }

    public Object a(Context context, String str, String str2, int i) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("slotId", Integer.valueOf(i));
        HashMap<String, Object> mapA = a(context, str, map);
        if (mapA != null) {
            return mapA.get(str2);
        }
        return null;
    }

    protected synchronized String a(Context context, int i) {
        String str;
        str = "-1";
        ImsManager imsManager = (ImsManager) context.getSystemService("telephony_ims");
        int iA = a(i);
        if (iA >= 0) {
            str = imsManager.getImsMmTelManager(iA).isAdvancedCallingSettingEnabled() ? "1" : "0";
        }
        return str;
    }

    public HashMap<String, Object> a(Context context, String str, HashMap<String, Object> map) {
        if (str == null) {
            return null;
        }
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            Method declaredMethod = telephonyManager.getClass().getDeclaredMethod("getITelephony", null);
            declaredMethod.setAccessible(true);
            Object objInvoke = declaredMethod.invoke(telephonyManager, null);
            if (objInvoke == null) {
                Log.e("DeviceInfoReaderBase", "invoke method: " + str + " error!");
                return null;
            }
            Class<?> cls = Class.forName("com.android.internal.telephony.VivoTelephonyApiParams");
            Object objNewInstance = cls.getConstructor(String.class).newInstance(str);
            if (map != null) {
                Method method = cls.getMethod("put", String.class, Object.class);
                for (String str2 : map.keySet()) {
                    if ((!"slot".equals(str2) && !"phoneId".equals(str2)) || ((Integer) map.get(str2)).intValue() >= 0) {
                        method.invoke(objNewInstance, str2, map.get(str2));
                    }
                }
            }
            Object objInvoke2 = objInvoke.getClass().getMethod("vivoTelephonyApi", cls).invoke(objInvoke, objNewInstance);
            if (objInvoke2 != null) {
                return (HashMap) objInvoke2.getClass().getMethod("getParamValues", new Class[0]).invoke(objInvoke2, new Object[0]);
            }
            return null;
        } catch (Exception e) {
            Log.e("DeviceInfoReaderBase", Log.getStackTraceString(e));
            return null;
        }
    }

    protected synchronized void a(Context context, int i, com.mobiletools.systemhelper.control.b bVar) {
        if (b.b.c.a.a.a(context, "android.permission.ACCESS_COARSE_LOCATION") == 0 && b.b.c.a.a.a(context, "android.permission.READ_PHONE_STATE") == 0) {
            String strValueOf = "";
            String strValueOf2 = "";
            String strValueOf3 = "";
            String str = "";
            String str2 = "1";
            TelephonyManager telephonyManagerCreateForSubscriptionId = ((TelephonyManager) context.getSystemService("phone")).createForSubscriptionId(a(i));
            String simSerialNumber = telephonyManagerCreateForSubscriptionId.getSimSerialNumber();
            c.d.a.b.h.a("DeviceInfoReaderBase", "iccid:" + simSerialNumber);
            if (simSerialNumber == null || simSerialNumber.isEmpty()) {
                simSerialNumber = "ex01";
            }
            String subscriberId = telephonyManagerCreateForSubscriptionId.getSubscriberId();
            if (subscriberId == null || subscriberId.isEmpty()) {
                subscriberId = "ex01";
            }
            String networkOperator = telephonyManagerCreateForSubscriptionId.getNetworkOperator();
            String simOperator = telephonyManagerCreateForSubscriptionId.getSimOperator();
            c.d.a.b.h.a("DeviceInfoReaderBase", "tm.getSimOperator mnc :" + simOperator);
            if (simOperator == null || simOperator.isEmpty()) {
                simOperator = "ex01";
            }
            ServiceState serviceState = telephonyManagerCreateForSubscriptionId.getServiceState();
            if (serviceState == null) {
                c.d.a.b.h.a("DeviceInfoReaderBase", "setCellLoc failed!");
                return;
            }
            for (NetworkRegistrationInfo networkRegistrationInfo : serviceState.getNetworkRegistrationInfoList()) {
                if (networkRegistrationInfo.getDomain() == 2 && networkRegistrationInfo.getTransportType() == 1) {
                    String string = networkRegistrationInfo.toString();
                    CellIdentity cellIdentity = networkRegistrationInfo.getCellIdentity();
                    if (cellIdentity != null) {
                        if (!(cellIdentity instanceof CellIdentityGsm)) {
                            if (!(cellIdentity instanceof CellIdentityWcdma)) {
                                if (!(cellIdentity instanceof CellIdentityTdscdma)) {
                                    if (!(cellIdentity instanceof CellIdentityCdma)) {
                                        if (cellIdentity instanceof CellIdentityLte) {
                                            CellIdentityLte cellIdentityLte = (CellIdentityLte) cellIdentity;
                                            strValueOf = String.valueOf(cellIdentityLte.getCi());
                                            strValueOf2 = String.valueOf(cellIdentityLte.getTac());
                                            strValueOf3 = String.valueOf(cellIdentityLte.getPci());
                                            str = string.contains("isEnDcAvailable = true") ? "NSA" : "LTE";
                                        } else if (cellIdentity instanceof CellIdentityNr) {
                                            CellIdentityNr cellIdentityNr = (CellIdentityNr) cellIdentity;
                                            strValueOf = String.valueOf(cellIdentityNr.getNci());
                                            strValueOf2 = String.valueOf(cellIdentityNr.getTac());
                                            strValueOf3 = String.valueOf(cellIdentityNr.getPci());
                                            str = "SA";
                                            if (!string.contains("mVopsSupport = 1")) {
                                                str2 = "0";
                                                break;
                                            }
                                        }
                                        str2 = "1";
                                        break;
                                    }
                                    CellIdentityCdma cellIdentityCdma = (CellIdentityCdma) cellIdentity;
                                    strValueOf = String.valueOf(cellIdentityCdma.getBasestationId());
                                    int longitude = cellIdentityCdma.getLongitude();
                                    int latitude = cellIdentityCdma.getLatitude();
                                    if (longitude != Integer.MAX_VALUE && latitude != Integer.MAX_VALUE) {
                                        strValueOf2 = String.format("%d:%d", Integer.valueOf(longitude), Integer.valueOf(latitude));
                                    }
                                    str = "CDMA";
                                    break;
                                }
                                CellIdentityTdscdma cellIdentityTdscdma = (CellIdentityTdscdma) cellIdentity;
                                strValueOf = String.valueOf(cellIdentityTdscdma.getCid());
                                strValueOf2 = String.valueOf(cellIdentityTdscdma.getLac());
                                str = "TD-SCDMA";
                                break;
                            }
                            CellIdentityWcdma cellIdentityWcdma = (CellIdentityWcdma) cellIdentity;
                            strValueOf = String.valueOf(cellIdentityWcdma.getCid());
                            strValueOf2 = String.valueOf(cellIdentityWcdma.getLac());
                            str = "WCDMA";
                            break;
                        }
                        CellIdentityGsm cellIdentityGsm = (CellIdentityGsm) cellIdentity;
                        strValueOf = String.valueOf(cellIdentityGsm.getCid());
                        strValueOf2 = String.valueOf(cellIdentityGsm.getLac());
                        str = "GSM";
                        break;
                    }
                    c.d.a.b.h.b("DeviceInfoReaderBase", "cellIdentity==null!");
                    break;
                }
            }
            if (strValueOf == null || strValueOf.isEmpty()) {
                strValueOf = "ex01";
            }
            if (strValueOf2 == null || strValueOf2.isEmpty()) {
                strValueOf2 = "ex01";
            }
            if (strValueOf3 == null || strValueOf3.isEmpty()) {
                strValueOf3 = "ex01";
            }
            if (str.isEmpty()) {
                str = "ex01";
            }
            c.d.a.b.h.a("DeviceInfoReaderBase", "getNetworkRegistrationInfo->slot:" + i + ", tac:" + strValueOf2 + ", cid:" + strValueOf + "，pci:" + strValueOf3 + "，networkType:" + str + ", voNREnable:" + str2);
            if (i == 0) {
                bVar.h = simSerialNumber;
                bVar.i = subscriberId;
                bVar.j = simOperator;
                bVar.k = networkOperator;
                bVar.n = strValueOf;
                bVar.m = strValueOf2;
                bVar.o = strValueOf3;
                bVar.l = str;
                bVar.r = str2;
            } else if (i == 1) {
                bVar.t = simSerialNumber;
                bVar.u = subscriberId;
                bVar.v = simOperator;
                bVar.w = networkOperator;
                bVar.z = strValueOf;
                bVar.y = strValueOf2;
                bVar.A = strValueOf3;
                bVar.x = str;
                bVar.D = str2;
            }
            return;
        }
        c.d.a.b.h.b("DeviceInfoReaderBase", "getNetworkRegistrationInfo no enough permission!");
    }

    @Override // c.d.a.a.n.c
    public void a(Context context, com.mobiletools.systemhelper.control.b bVar) {
        StringBuilder sb;
        StringBuilder sb2;
        String str = Build.MODEL;
        bVar.f1414b = str;
        String str2 = n.f1325a.get(str);
        bVar.f1413a = str2;
        if (str2 == null || str2.isEmpty()) {
            bVar.f1413a = Build.MANUFACTURER;
        }
        if (bVar.f1413a.equals("vivo")) {
            boolean zA = c.d.a.b.l.a(context);
            c.d.a.b.d.e = zA;
            if (!zA) {
                sb2 = new StringBuilder();
            }
            sb2.append("vivo DM isUserPermissionOpen : ");
            sb2.append(c.d.a.b.d.e);
            c.d.a.b.h.a("DeviceInfoReaderBase", sb2.toString());
            return;
        }
        c.d.a.b.d.e = b(context);
        c.d.a.b.h.a("DeviceInfoReaderBase", "queryAppUsageStatsDescend  isUserPermissionOpen :" + c.d.a.b.d.e);
        c.d.a.b.h.a("DeviceInfoReaderBase", "GlobalSetting.isUserPermissionOpen" + c.d.a.b.d.e);
        if (!c.d.a.b.d.e) {
            String str3 = Build.MODEL;
            bVar.f1414b = str3;
            String str4 = n.f1325a.get(str3);
            bVar.f1413a = str4;
            if (str4 == null || str4.isEmpty()) {
                bVar.f1413a = Build.MANUFACTURER;
            }
            bVar.e = "Android";
            bVar.f = Build.VERSION.RELEASE;
            bVar.d = Build.DISPLAY;
            bVar.f1415c = c.d.a.b.l.a("ro.hardware.version", "Unknown");
            bVar.I = c.d.a.b.g.b(context);
            return;
        }
        bVar.e = "Android";
        bVar.f = Build.VERSION.RELEASE;
        bVar.d = bVar.f1413a.equals("Xiaomi") ? d(context) : Build.DISPLAY;
        bVar.f1415c = c.d.a.b.l.a("ro.hardware.version", "Unknown");
        bVar.I = c.d.a.b.g.b(context);
        if (bVar.f1413a.equals("vivo")) {
            boolean zA2 = c.d.a.b.l.a(c.d.a.b.d.d);
            c.d.a.b.d.e = zA2;
            if (!zA2) {
                sb2 = new StringBuilder();
                sb2.append("vivo DM isUserPermissionOpen : ");
                sb2.append(c.d.a.b.d.e);
                c.d.a.b.h.a("DeviceInfoReaderBase", sb2.toString());
                return;
            }
            if (Build.VERSION.SDK_INT >= 33) {
                c.d.a.b.h.a("DeviceInfoReaderBase", "enter DeviceInfoReaderBase super.read read vivo");
                c(context, bVar);
                return;
            }
            sb = new StringBuilder();
        } else {
            if (Build.VERSION.SDK_INT >= 31) {
                b(context, bVar);
                return;
            }
            sb = new StringBuilder();
        }
        sb.append("Unsupported API:");
        sb.append(Build.VERSION.SDK_INT);
        c.d.a.b.h.b("DeviceInfoReaderBase", sb.toString());
    }

    public boolean a() {
        Log.d("DeviceInfoReaderBase", "the 5G attribute:" + f1323a + ", " + f1324b);
        return "1".equals(f1323a) || "true".equals(f1324b);
    }

    @Override // c.d.a.a.n.c
    public boolean a(Context context) {
        return new ServiceState().getRoaming();
    }

    public String b(Context context, int i) {
        Object objA = a(context, "API_TAG_getNRSwitchState", "response", i);
        Log.d("DeviceInfoReaderBase", "ret:" + objA);
        int iIntValue = (objA == null || !(objA instanceof Integer)) ? -1 : ((Integer) objA).intValue();
        Log.d("DeviceInfoReaderBase", "get5GSwitchState: " + iIntValue);
        return String.valueOf(iIntValue);
    }

    /* JADX WARN: Code duplicated, block: B:36:0x015e  */
    /* JADX WARN: Code duplicated, block: B:38:0x0162  */
    /* JADX WARN: Code duplicated, block: B:39:0x016a  */
    /* JADX WARN: Code duplicated, block: B:41:0x0180  */
    /* JADX WARN: Code duplicated, block: B:43:0x0184  */
    /* JADX WARN: Code duplicated, block: B:44:0x018c  */
    /* JADX WARN: Code duplicated, block: B:50:? A[RETURN, SYNTHETIC] */
    void b(Context context, com.mobiletools.systemhelper.control.b bVar) {
        String strValueOf;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        bVar.J = "000005";
        if (bVar.f1413a.equals("Xiaomi")) {
            bVar.J = "000005";
            Class<?> clsA = c.d.a.b.k.a("miui.telephony.TelephonyManager");
            Object objA = c.d.a.b.k.a(c.d.a.b.k.a(clsA, "getDefault", (Class<?>[]) new Class[0]), (Object) null, new Object[0]);
            try {
                bVar.g = (String) c.d.a.b.k.a(c.d.a.b.k.a(clsA, "getImeiForSlot", (Class<?>[]) new Class[]{Integer.TYPE}), objA, 0);
                c.d.a.b.h.a("DeviceInfoReaderBase", "IMEI1: " + bVar.g);
                bVar.s = (String) c.d.a.b.k.a(c.d.a.b.k.a(clsA, "getImeiForSlot", (Class<?>[]) new Class[]{Integer.TYPE}), objA, 1);
                c.d.a.b.h.a("DeviceInfoReaderBase", "IMEI2: " + bVar.s);
            } catch (Exception unused) {
                c.d.a.b.h.b("DeviceInfoReaderBase", "Exception:getImeiForSlot");
            }
        } else {
            bVar.g = telephonyManager.getImei(0);
            bVar.s = telephonyManager.getImei(1);
        }
        String str = bVar.g;
        if (str == null || str.isEmpty()) {
            bVar.g = "ex00";
        }
        String str2 = bVar.s;
        if (str2 == null || str2.isEmpty()) {
            bVar.s = "ex00";
        }
        SubscriptionManager subscriptionManager = (SubscriptionManager) context.getSystemService("telephony_subscription_service");
        int iA = a(0);
        int iA2 = a(1);
        int activeDataSubscriptionId = SubscriptionManager.getActiveDataSubscriptionId();
        c.d.a.b.h.a("DeviceInfoReaderBase", "subId0:" + iA + "，subId1:" + iA2 + ", activeDataSubId：" + activeDataSubscriptionId);
        try {
            if (iA < 0 && iA2 < 0) {
                strValueOf = "0";
            } else {
                if (activeDataSubscriptionId != iA) {
                    if (activeDataSubscriptionId == iA2) {
                        strValueOf = String.valueOf(2);
                    }
                    c.d.a.b.h.a("DeviceInfoReaderBase", "20240524dataslot: " + bVar.E);
                    c.d.a.b.h.a("DeviceInfoReaderBase", "20240524defaultDataSubId: " + ((Integer) subscriptionManager.getClass().getMethod("getDefaultDataSubscriptionId", new Class[0]).invoke(subscriptionManager, new Object[0])).intValue());
                    if (iA >= 0) {
                        bVar.h = "ex00";
                        bVar.i = "ex00";
                        bVar.j = "ex00";
                        bVar.l = "ex00";
                        bVar.m = "ex00";
                        bVar.n = "ex00";
                        bVar.o = "ex00";
                        bVar.r = "ex00";
                        bVar.p = "ex00";
                        bVar.q = "ex00";
                    } else if (Build.VERSION.SDK_INT >= 30) {
                        bVar.q = "1";
                        a(context, 0, bVar);
                        bVar.p = "1";
                    }
                    if (iA2 >= 0) {
                        if (Build.VERSION.SDK_INT >= 30) {
                            bVar.C = "1";
                            a(context, 1, bVar);
                            bVar.B = "1";
                            return;
                        }
                        return;
                    }
                    bVar.t = "ex00";
                    bVar.u = "ex00";
                    bVar.v = "ex00";
                    bVar.x = "ex00";
                    bVar.y = "ex00";
                    bVar.z = "ex00";
                    bVar.A = "ex00";
                    bVar.D = "ex00";
                    bVar.B = "ex00";
                    bVar.C = "ex00";
                }
                strValueOf = String.valueOf(1);
            }
            c.d.a.b.h.a("DeviceInfoReaderBase", "20240524defaultDataSubId: " + ((Integer) subscriptionManager.getClass().getMethod("getDefaultDataSubscriptionId", new Class[0]).invoke(subscriptionManager, new Object[0])).intValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
        bVar.E = strValueOf;
        c.d.a.b.h.a("DeviceInfoReaderBase", "20240524dataslot: " + bVar.E);
        if (iA >= 0) {
            bVar.h = "ex00";
            bVar.i = "ex00";
            bVar.j = "ex00";
            bVar.l = "ex00";
            bVar.m = "ex00";
            bVar.n = "ex00";
            bVar.o = "ex00";
            bVar.r = "ex00";
            bVar.p = "ex00";
            bVar.q = "ex00";
        } else if (Build.VERSION.SDK_INT >= 30) {
            bVar.q = "1";
            a(context, 0, bVar);
            bVar.p = "1";
        }
        if (iA2 >= 0) {
            if (Build.VERSION.SDK_INT >= 30) {
                bVar.C = "1";
                a(context, 1, bVar);
                bVar.B = "1";
                return;
            }
            return;
        }
        bVar.t = "ex00";
        bVar.u = "ex00";
        bVar.v = "ex00";
        bVar.x = "ex00";
        bVar.y = "ex00";
        bVar.z = "ex00";
        bVar.A = "ex00";
        bVar.D = "ex00";
        bVar.B = "ex00";
        bVar.C = "ex00";
    }

    protected synchronized boolean b(Context context) {
        Boolean boolValueOf;
        boolValueOf = Boolean.valueOf(Settings.Global.getInt(context.getContentResolver(), "device_provisioned", 0) == 1);
        c.d.a.b.h.a("DeviceInfoReaderBase", "isBootWizardCompleted: " + boolValueOf);
        return boolValueOf.booleanValue();
    }

    /* JADX WARN: Code duplicated, block: B:24:0x0087  */
    /* JADX WARN: Code duplicated, block: B:26:0x008b  */
    /* JADX WARN: Code duplicated, block: B:28:0x0091  */
    /* JADX WARN: Code duplicated, block: B:30:0x00a1  */
    /* JADX WARN: Code duplicated, block: B:32:0x00b7  */
    /* JADX WARN: Code duplicated, block: B:34:0x00bb  */
    /* JADX WARN: Code duplicated, block: B:36:0x00c1  */
    /* JADX WARN: Code duplicated, block: B:38:0x00d1  */
    /* JADX WARN: Code duplicated, block: B:40:? A[RETURN, SYNTHETIC] */
    void c(Context context, com.mobiletools.systemhelper.control.b bVar) {
        String strValueOf;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        bVar.J = "000005";
        String imei = telephonyManager.getImei(0);
        bVar.g = imei;
        if (imei == null || imei.isEmpty()) {
            bVar.g = "ex00";
        }
        String imei2 = telephonyManager.getImei(1);
        bVar.s = imei2;
        if (imei2 == null || imei2.isEmpty()) {
            bVar.s = "ex00";
        }
        int iA = a(0);
        int iA2 = a(1);
        int activeDataSubscriptionId = SubscriptionManager.getActiveDataSubscriptionId();
        c.d.a.b.h.a("DeviceInfoReaderBase", "subId0:" + iA + "，subId1:" + iA2 + ", activeDataSubId：" + activeDataSubscriptionId);
        if (iA < 0 && iA2 < 0) {
            strValueOf = "0";
        } else {
            if (activeDataSubscriptionId != iA) {
                if (activeDataSubscriptionId == iA2) {
                    strValueOf = String.valueOf(2);
                }
                if (iA >= 0) {
                    bVar.h = "ex00";
                    bVar.i = "ex00";
                    bVar.j = "ex00";
                    bVar.l = "ex00";
                    bVar.m = "ex00";
                    bVar.n = "ex00";
                    bVar.o = "ex00";
                    bVar.r = "ex00";
                    bVar.p = "ex00";
                    bVar.q = "ex00";
                } else if (Build.VERSION.SDK_INT >= 30) {
                    if (a()) {
                        bVar.q = c(context, 0);
                    }
                    a(context, 0, bVar);
                    bVar.p = a(context, 0);
                }
                if (iA2 >= 0) {
                    if (Build.VERSION.SDK_INT >= 30) {
                        if (a()) {
                            bVar.C = c(context, 1);
                        }
                        a(context, 1, bVar);
                        bVar.B = a(context, 1);
                        return;
                    }
                    return;
                }
                bVar.t = "ex00";
                bVar.u = "ex00";
                bVar.v = "ex00";
                bVar.x = "ex00";
                bVar.y = "ex00";
                bVar.z = "ex00";
                bVar.A = "ex00";
                bVar.D = "ex00";
                bVar.B = "ex00";
                bVar.C = "ex00";
            }
            strValueOf = String.valueOf(1);
        }
        bVar.E = strValueOf;
        if (iA >= 0) {
            bVar.h = "ex00";
            bVar.i = "ex00";
            bVar.j = "ex00";
            bVar.l = "ex00";
            bVar.m = "ex00";
            bVar.n = "ex00";
            bVar.o = "ex00";
            bVar.r = "ex00";
            bVar.p = "ex00";
            bVar.q = "ex00";
        } else if (Build.VERSION.SDK_INT >= 30) {
            if (a()) {
                bVar.q = c(context, 0);
            }
            a(context, 0, bVar);
            bVar.p = a(context, 0);
        }
        if (iA2 >= 0) {
            if (Build.VERSION.SDK_INT >= 30) {
                if (a()) {
                    bVar.C = c(context, 1);
                }
                a(context, 1, bVar);
                bVar.B = a(context, 1);
                return;
            }
            return;
        }
        bVar.t = "ex00";
        bVar.u = "ex00";
        bVar.v = "ex00";
        bVar.x = "ex00";
        bVar.y = "ex00";
        bVar.z = "ex00";
        bVar.A = "ex00";
        bVar.D = "ex00";
        bVar.B = "ex00";
        bVar.C = "ex00";
    }

    public boolean c(Context context) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("dual_nr_support", false);
        HashMap<String, Object> mapA = a(context, "API_TAG_getNrCapability", map);
        Log.d("DeviceInfoReaderBase", "resultMap:" + map);
        boolean zBooleanValue = mapA != null ? ((Boolean) mapA.get("dual_nr_support")).booleanValue() : false;
        Log.d("DeviceInfoReaderBase", "isDualMode5G: " + zBooleanValue);
        return zBooleanValue;
    }
}
