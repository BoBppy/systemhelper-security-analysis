package com.mobiletools.systemhelper.control;

import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthNr;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import c.d.a.b.h;
import c.d.a.b.k;
import c.d.a.b.l;
import c.d.a.b.n;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private Context f1418a = null;

    void a(Context context, int i, n nVar) {
        ServiceState serviceState;
        String strValueOf;
        String str;
        StringBuilder sb;
        try {
            nVar.d = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        new a(context, i);
        nVar.y = a.f1410b;
        nVar.z = a.f1411c;
        h.a("GetSignalStrengthInfo", "signalInfo.callDisconnectCause: " + nVar.y + ", " + nVar.z);
        int iA = c.d.a.a.a.a(i);
        h.a("GetSignalStrengthInfo", "getSignal====> slot: " + i + " subId: " + iA);
        TelephonyManager telephonyManagerCreateForSubscriptionId = ((TelephonyManager) context.getSystemService("phone")).createForSubscriptionId(iA);
        String strA = l.a("ro.product.manufacturer");
        if (strA != null && !strA.isEmpty() && strA.equalsIgnoreCase("vivo")) {
            c.d.a.b.d.e = l.a(this.f1418a);
        }
        if (c.d.a.b.d.e) {
            String simSerialNumber = telephonyManagerCreateForSubscriptionId.getSimSerialNumber();
            nVar.f1345c = simSerialNumber;
            if (simSerialNumber == null || simSerialNumber.isEmpty()) {
                nVar.f1345c = "ex01";
            }
            h.a("GetSignalStrengthInfo", "ICCID:" + nVar.f1345c);
            String subscriberId = telephonyManagerCreateForSubscriptionId.getSubscriberId();
            nVar.g = subscriberId;
            if (subscriberId == null || subscriberId.isEmpty()) {
                nVar.g = "ex01";
            }
            nVar.e = telephonyManagerCreateForSubscriptionId.getSimOperator();
            nVar.f = telephonyManagerCreateForSubscriptionId.getNetworkOperator();
            h.a("GetSignalStrengthInfo", "mncch :  " + nVar.f);
            String str2 = nVar.e;
            if (str2 == null || str2.isEmpty()) {
                nVar.e = "ex01";
            }
            String str3 = nVar.f;
            if (str3 == null || str3.isEmpty()) {
                h.a("GetSignalStrengthInfo", "mncch==> null " + nVar.f);
                nVar.f = "ex01";
            }
            String str4 = nVar.g;
            if (str4 != null && nVar.f1345c != null && !str4.equals("") && nVar.g.length() >= 5 && nVar.f1345c.length() >= 6 && !l.b(nVar.g).booleanValue()) {
                if (nVar.g.length() >= 11 && nVar.f1345c.length() >= 11) {
                    nVar.g = nVar.g.substring(0, 10) + "*****";
                    sb = new StringBuilder();
                } else if (nVar.g.length() >= 11) {
                    nVar.g = nVar.g.substring(0, 10) + "*****";
                } else if (nVar.f1345c.length() >= 11) {
                    sb = new StringBuilder();
                }
                sb.append(nVar.f1345c.substring(0, 10));
                sb.append("**********");
                nVar.f1345c = sb.toString();
            }
            if (!c.d.a.b.d.e) {
                h.b("GetSignalStrengthInfo", "Insufficient permission!!");
                return;
            }
            if (strA != null && !strA.isEmpty()) {
                if (strA.equalsIgnoreCase("Xiaomi")) {
                    Class<?> clsA = k.a("miui.telephony.TelephonyManager");
                    try {
                        nVar.f1344b = (String) k.a(k.a(clsA, "getImeiForSlot", (Class<?>[]) new Class[]{Integer.TYPE}), k.a(k.a(clsA, "getDefault", (Class<?>[]) new Class[0]), (Object) null, new Object[0]), Integer.valueOf(i));
                        h.a("GetSignalStrengthInfo", "siginfo.IMEI: " + nVar.f1344b);
                    } catch (Exception unused) {
                        h.b("GetSignalStrengthInfo", "Exception:getImeiForSlot");
                    }
                } else {
                    nVar.f1344b = telephonyManagerCreateForSubscriptionId.getImei();
                }
                h.a("GetSignalStrengthInfo", "getImei====> imei: " + nVar.f1344b);
            }
            if (b.b.c.a.a.a(context, "android.permission.ACCESS_COARSE_LOCATION") == 0 && b.b.c.a.a.a(context, "android.permission.READ_PHONE_STATE") == 0 && (serviceState = telephonyManagerCreateForSubscriptionId.getServiceState()) != null) {
                for (NetworkRegistrationInfo networkRegistrationInfo : serviceState.getNetworkRegistrationInfoList()) {
                    if (networkRegistrationInfo.getDomain() == 2 && networkRegistrationInfo.getTransportType() == 1) {
                        String string = networkRegistrationInfo.toString();
                        CellIdentity cellIdentity = networkRegistrationInfo.getCellIdentity();
                        if (cellIdentity != null) {
                            if (cellIdentity instanceof CellIdentityLte) {
                                CellIdentityLte cellIdentityLte = (CellIdentityLte) cellIdentity;
                                nVar.i = String.valueOf(cellIdentityLte.getCi());
                                nVar.h = String.valueOf(cellIdentityLte.getTac());
                                nVar.j = String.valueOf(cellIdentityLte.getPci());
                                nVar.m = Arrays.toString(cellIdentityLte.getBands()).replace("[", "").replace("]", "");
                                nVar.n = String.valueOf(cellIdentityLte.getBandwidth());
                                nVar.k = "LTE";
                                if (!string.contains("isEnDcAvailable = true")) {
                                    break;
                                } else {
                                    str = "NSA";
                                }
                            } else if (cellIdentity instanceof CellIdentityNr) {
                                CellIdentityNr cellIdentityNr = (CellIdentityNr) cellIdentity;
                                nVar.i = String.valueOf(cellIdentityNr.getNci());
                                nVar.h = String.valueOf(cellIdentityNr.getTac());
                                nVar.j = String.valueOf(cellIdentityNr.getPci());
                                nVar.m = Arrays.toString(cellIdentityNr.getBands()).replace("[", "").replace("]", "");
                                nVar.l = String.valueOf(cellIdentityNr.getNrarfcn());
                                str = "SA";
                            }
                            nVar.k = str;
                            break;
                        }
                        return;
                    }
                }
                if (b.b.c.a.a.a(context, "android.permission.ACCESS_NETWORK_STATE") == 0) {
                    nVar.F = l.k(context).booleanValue() ? !l.l(context).booleanValue() ? "Mobile" : "WiFi" : "Disconnected";
                }
                SignalStrength signalStrength = telephonyManagerCreateForSubscriptionId.getSignalStrength();
                h.a("GetSignalStrengthInfo", "slot: " + i + "signalStrength" + signalStrength);
                List<CellSignalStrength> cellSignalStrengths = signalStrength.getCellSignalStrengths();
                StringBuilder sb2 = new StringBuilder();
                sb2.append("cellSignalStrengthList ===> ");
                sb2.append(cellSignalStrengths);
                h.a("GetSignalStrengthInfo", sb2.toString());
                if (cellSignalStrengths != null) {
                    for (CellSignalStrength cellSignalStrength : cellSignalStrengths) {
                        if (cellSignalStrength instanceof CellSignalStrengthNr) {
                            CellSignalStrengthNr cellSignalStrengthNr = (CellSignalStrengthNr) cellSignalStrength;
                            nVar.q = String.valueOf(cellSignalStrengthNr.getSsRsrp());
                            h.a("GetSignalStrengthInfo", "rsrp : " + nVar.q);
                            nVar.s = String.valueOf(cellSignalStrengthNr.getSsRsrq());
                            nVar.r = String.valueOf(cellSignalStrengthNr.getSsSinr());
                            nVar.o = String.valueOf(cellSignalStrengthNr.getCsiCqiTableIndex());
                            strValueOf = String.valueOf(cellSignalStrengthNr.getCsiCqiReport());
                        } else if (cellSignalStrength instanceof CellSignalStrengthLte) {
                            CellSignalStrengthLte cellSignalStrengthLte = (CellSignalStrengthLte) cellSignalStrength;
                            nVar.q = String.valueOf(cellSignalStrengthLte.getRsrp());
                            nVar.s = String.valueOf(cellSignalStrengthLte.getRsrq());
                            nVar.r = String.valueOf(cellSignalStrengthLte.getRssnr());
                            nVar.o = String.valueOf(cellSignalStrengthLte.getCqiTableIndex());
                            strValueOf = String.valueOf(cellSignalStrengthLte.getCqi());
                        }
                        nVar.p = strValueOf;
                        return;
                    }
                }
            }
        }
    }

    public void b(Context context, int i, n nVar) {
        this.f1418a = context;
        h.a("GetSignalStrengthInfo", "GetSignalStrengthInfo====>");
        if (c.d.a.a.a.a(i) > 0) {
            a(context, i, nVar);
        }
    }
}
