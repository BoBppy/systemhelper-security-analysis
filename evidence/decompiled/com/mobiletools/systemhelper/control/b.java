package com.mobiletools.systemhelper.control;

import android.content.Context;
import c.d.a.a.n;
import c.d.a.b.h;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public String f1413a = null;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public String f1414b = null;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public String f1415c = null;
    public String d = null;
    public String e = null;
    public String f = null;
    public String g = null;
    public String h = null;
    public String i = null;
    public String j = null;
    public String k = null;
    public String l = null;
    public String m = null;
    public String n = null;
    public String o = null;
    public String p = null;
    public String q = null;
    public String r = null;
    public String s = null;
    public String t = null;
    public String u = null;
    public String v = null;
    public String w = null;
    public String x = null;
    public String y = null;
    public String z = null;
    public String A = null;
    public String B = null;
    public String C = null;
    public String D = null;
    public String E = "0";
    public String F = null;
    public String G = null;
    public String H = null;
    public JSONObject I = null;
    public String J = null;
    public String K = null;
    public String L = null;
    public String M = null;
    public String N = null;
    public JSONArray O = null;
    public JSONArray P = null;
    private String Q = null;

    public static synchronized b a(Context context, String str) {
        c.d.a.b.d.d = context;
        return n.a(context, str);
    }

    /* JADX WARN: Code duplicated, block: B:106:0x01ed A[Catch: all -> 0x02d2, JSONException -> 0x02d4, TryCatch #1 {JSONException -> 0x02d4, blocks: (B:3:0x0001, B:5:0x000a, B:6:0x000e, B:8:0x0019, B:9:0x001d, B:11:0x0028, B:12:0x002c, B:14:0x0037, B:15:0x003b, B:17:0x0046, B:18:0x004a, B:20:0x0055, B:21:0x0059, B:23:0x0064, B:24:0x0068, B:26:0x0073, B:27:0x0077, B:29:0x0082, B:30:0x0086, B:32:0x0091, B:33:0x0095, B:35:0x00a0, B:36:0x00a4, B:38:0x00a8, B:39:0x00ac, B:41:0x00b7, B:42:0x00bb, B:44:0x00c6, B:45:0x00ca, B:47:0x00d5, B:48:0x00d9, B:50:0x00e4, B:51:0x00e8, B:53:0x00f3, B:54:0x00f7, B:56:0x0102, B:57:0x0106, B:59:0x0111, B:60:0x0115, B:62:0x0120, B:63:0x0124, B:65:0x012f, B:66:0x0133, B:68:0x013e, B:69:0x0142, B:71:0x014d, B:72:0x0151, B:74:0x0155, B:75:0x0159, B:77:0x0164, B:78:0x0168, B:80:0x0173, B:81:0x0177, B:83:0x0182, B:84:0x0186, B:86:0x0191, B:87:0x0195, B:89:0x01a0, B:90:0x01a4, B:92:0x01af, B:93:0x01b3, B:96:0x01c0, B:104:0x01e2, B:106:0x01ed, B:107:0x01f1, B:109:0x01fc, B:110:0x0200, B:112:0x0204, B:113:0x0208, B:116:0x0217, B:118:0x0220, B:120:0x0224, B:123:0x0230, B:125:0x0234, B:126:0x023b, B:128:0x023f, B:129:0x0243, B:131:0x0250, B:132:0x0255, B:134:0x0259, B:135:0x025d, B:137:0x0261, B:138:0x0268, B:140:0x026c, B:141:0x0270, B:143:0x0281, B:149:0x02a1, B:151:0x02ab, B:157:0x02cb, B:153:0x02b5, B:155:0x02b9, B:156:0x02c4, B:145:0x028b, B:147:0x028f, B:148:0x029a, B:122:0x022c, B:117:0x021b, B:97:0x01c3, B:99:0x01cb, B:101:0x01d5), top: B:168:0x0001, outer: #0 }] */
    /* JADX WARN: Code duplicated, block: B:109:0x01fc A[Catch: all -> 0x02d2, JSONException -> 0x02d4, TryCatch #1 {JSONException -> 0x02d4, blocks: (B:3:0x0001, B:5:0x000a, B:6:0x000e, B:8:0x0019, B:9:0x001d, B:11:0x0028, B:12:0x002c, B:14:0x0037, B:15:0x003b, B:17:0x0046, B:18:0x004a, B:20:0x0055, B:21:0x0059, B:23:0x0064, B:24:0x0068, B:26:0x0073, B:27:0x0077, B:29:0x0082, B:30:0x0086, B:32:0x0091, B:33:0x0095, B:35:0x00a0, B:36:0x00a4, B:38:0x00a8, B:39:0x00ac, B:41:0x00b7, B:42:0x00bb, B:44:0x00c6, B:45:0x00ca, B:47:0x00d5, B:48:0x00d9, B:50:0x00e4, B:51:0x00e8, B:53:0x00f3, B:54:0x00f7, B:56:0x0102, B:57:0x0106, B:59:0x0111, B:60:0x0115, B:62:0x0120, B:63:0x0124, B:65:0x012f, B:66:0x0133, B:68:0x013e, B:69:0x0142, B:71:0x014d, B:72:0x0151, B:74:0x0155, B:75:0x0159, B:77:0x0164, B:78:0x0168, B:80:0x0173, B:81:0x0177, B:83:0x0182, B:84:0x0186, B:86:0x0191, B:87:0x0195, B:89:0x01a0, B:90:0x01a4, B:92:0x01af, B:93:0x01b3, B:96:0x01c0, B:104:0x01e2, B:106:0x01ed, B:107:0x01f1, B:109:0x01fc, B:110:0x0200, B:112:0x0204, B:113:0x0208, B:116:0x0217, B:118:0x0220, B:120:0x0224, B:123:0x0230, B:125:0x0234, B:126:0x023b, B:128:0x023f, B:129:0x0243, B:131:0x0250, B:132:0x0255, B:134:0x0259, B:135:0x025d, B:137:0x0261, B:138:0x0268, B:140:0x026c, B:141:0x0270, B:143:0x0281, B:149:0x02a1, B:151:0x02ab, B:157:0x02cb, B:153:0x02b5, B:155:0x02b9, B:156:0x02c4, B:145:0x028b, B:147:0x028f, B:148:0x029a, B:122:0x022c, B:117:0x021b, B:97:0x01c3, B:99:0x01cb, B:101:0x01d5), top: B:168:0x0001, outer: #0 }] */
    /* JADX WARN: Code duplicated, block: B:112:0x0204 A[Catch: all -> 0x02d2, JSONException -> 0x02d4, TryCatch #1 {JSONException -> 0x02d4, blocks: (B:3:0x0001, B:5:0x000a, B:6:0x000e, B:8:0x0019, B:9:0x001d, B:11:0x0028, B:12:0x002c, B:14:0x0037, B:15:0x003b, B:17:0x0046, B:18:0x004a, B:20:0x0055, B:21:0x0059, B:23:0x0064, B:24:0x0068, B:26:0x0073, B:27:0x0077, B:29:0x0082, B:30:0x0086, B:32:0x0091, B:33:0x0095, B:35:0x00a0, B:36:0x00a4, B:38:0x00a8, B:39:0x00ac, B:41:0x00b7, B:42:0x00bb, B:44:0x00c6, B:45:0x00ca, B:47:0x00d5, B:48:0x00d9, B:50:0x00e4, B:51:0x00e8, B:53:0x00f3, B:54:0x00f7, B:56:0x0102, B:57:0x0106, B:59:0x0111, B:60:0x0115, B:62:0x0120, B:63:0x0124, B:65:0x012f, B:66:0x0133, B:68:0x013e, B:69:0x0142, B:71:0x014d, B:72:0x0151, B:74:0x0155, B:75:0x0159, B:77:0x0164, B:78:0x0168, B:80:0x0173, B:81:0x0177, B:83:0x0182, B:84:0x0186, B:86:0x0191, B:87:0x0195, B:89:0x01a0, B:90:0x01a4, B:92:0x01af, B:93:0x01b3, B:96:0x01c0, B:104:0x01e2, B:106:0x01ed, B:107:0x01f1, B:109:0x01fc, B:110:0x0200, B:112:0x0204, B:113:0x0208, B:116:0x0217, B:118:0x0220, B:120:0x0224, B:123:0x0230, B:125:0x0234, B:126:0x023b, B:128:0x023f, B:129:0x0243, B:131:0x0250, B:132:0x0255, B:134:0x0259, B:135:0x025d, B:137:0x0261, B:138:0x0268, B:140:0x026c, B:141:0x0270, B:143:0x0281, B:149:0x02a1, B:151:0x02ab, B:157:0x02cb, B:153:0x02b5, B:155:0x02b9, B:156:0x02c4, B:145:0x028b, B:147:0x028f, B:148:0x029a, B:122:0x022c, B:117:0x021b, B:97:0x01c3, B:99:0x01cb, B:101:0x01d5), top: B:168:0x0001, outer: #0 }] */
    /* JADX WARN: Code duplicated, block: B:115:0x0213  */
    /* JADX WARN: Code duplicated, block: B:117:0x021b A[Catch: all -> 0x02d2, JSONException -> 0x02d4, TryCatch #1 {JSONException -> 0x02d4, blocks: (B:3:0x0001, B:5:0x000a, B:6:0x000e, B:8:0x0019, B:9:0x001d, B:11:0x0028, B:12:0x002c, B:14:0x0037, B:15:0x003b, B:17:0x0046, B:18:0x004a, B:20:0x0055, B:21:0x0059, B:23:0x0064, B:24:0x0068, B:26:0x0073, B:27:0x0077, B:29:0x0082, B:30:0x0086, B:32:0x0091, B:33:0x0095, B:35:0x00a0, B:36:0x00a4, B:38:0x00a8, B:39:0x00ac, B:41:0x00b7, B:42:0x00bb, B:44:0x00c6, B:45:0x00ca, B:47:0x00d5, B:48:0x00d9, B:50:0x00e4, B:51:0x00e8, B:53:0x00f3, B:54:0x00f7, B:56:0x0102, B:57:0x0106, B:59:0x0111, B:60:0x0115, B:62:0x0120, B:63:0x0124, B:65:0x012f, B:66:0x0133, B:68:0x013e, B:69:0x0142, B:71:0x014d, B:72:0x0151, B:74:0x0155, B:75:0x0159, B:77:0x0164, B:78:0x0168, B:80:0x0173, B:81:0x0177, B:83:0x0182, B:84:0x0186, B:86:0x0191, B:87:0x0195, B:89:0x01a0, B:90:0x01a4, B:92:0x01af, B:93:0x01b3, B:96:0x01c0, B:104:0x01e2, B:106:0x01ed, B:107:0x01f1, B:109:0x01fc, B:110:0x0200, B:112:0x0204, B:113:0x0208, B:116:0x0217, B:118:0x0220, B:120:0x0224, B:123:0x0230, B:125:0x0234, B:126:0x023b, B:128:0x023f, B:129:0x0243, B:131:0x0250, B:132:0x0255, B:134:0x0259, B:135:0x025d, B:137:0x0261, B:138:0x0268, B:140:0x026c, B:141:0x0270, B:143:0x0281, B:149:0x02a1, B:151:0x02ab, B:157:0x02cb, B:153:0x02b5, B:155:0x02b9, B:156:0x02c4, B:145:0x028b, B:147:0x028f, B:148:0x029a, B:122:0x022c, B:117:0x021b, B:97:0x01c3, B:99:0x01cb, B:101:0x01d5), top: B:168:0x0001, outer: #0 }] */
    /* JADX WARN: Code duplicated, block: B:122:0x022c A[Catch: all -> 0x02d2, JSONException -> 0x02d4, TryCatch #1 {JSONException -> 0x02d4, blocks: (B:3:0x0001, B:5:0x000a, B:6:0x000e, B:8:0x0019, B:9:0x001d, B:11:0x0028, B:12:0x002c, B:14:0x0037, B:15:0x003b, B:17:0x0046, B:18:0x004a, B:20:0x0055, B:21:0x0059, B:23:0x0064, B:24:0x0068, B:26:0x0073, B:27:0x0077, B:29:0x0082, B:30:0x0086, B:32:0x0091, B:33:0x0095, B:35:0x00a0, B:36:0x00a4, B:38:0x00a8, B:39:0x00ac, B:41:0x00b7, B:42:0x00bb, B:44:0x00c6, B:45:0x00ca, B:47:0x00d5, B:48:0x00d9, B:50:0x00e4, B:51:0x00e8, B:53:0x00f3, B:54:0x00f7, B:56:0x0102, B:57:0x0106, B:59:0x0111, B:60:0x0115, B:62:0x0120, B:63:0x0124, B:65:0x012f, B:66:0x0133, B:68:0x013e, B:69:0x0142, B:71:0x014d, B:72:0x0151, B:74:0x0155, B:75:0x0159, B:77:0x0164, B:78:0x0168, B:80:0x0173, B:81:0x0177, B:83:0x0182, B:84:0x0186, B:86:0x0191, B:87:0x0195, B:89:0x01a0, B:90:0x01a4, B:92:0x01af, B:93:0x01b3, B:96:0x01c0, B:104:0x01e2, B:106:0x01ed, B:107:0x01f1, B:109:0x01fc, B:110:0x0200, B:112:0x0204, B:113:0x0208, B:116:0x0217, B:118:0x0220, B:120:0x0224, B:123:0x0230, B:125:0x0234, B:126:0x023b, B:128:0x023f, B:129:0x0243, B:131:0x0250, B:132:0x0255, B:134:0x0259, B:135:0x025d, B:137:0x0261, B:138:0x0268, B:140:0x026c, B:141:0x0270, B:143:0x0281, B:149:0x02a1, B:151:0x02ab, B:157:0x02cb, B:153:0x02b5, B:155:0x02b9, B:156:0x02c4, B:145:0x028b, B:147:0x028f, B:148:0x029a, B:122:0x022c, B:117:0x021b, B:97:0x01c3, B:99:0x01cb, B:101:0x01d5), top: B:168:0x0001, outer: #0 }] */
    /* JADX WARN: Code duplicated, block: B:125:0x0234 A[Catch: all -> 0x02d2, JSONException -> 0x02d4, TryCatch #1 {JSONException -> 0x02d4, blocks: (B:3:0x0001, B:5:0x000a, B:6:0x000e, B:8:0x0019, B:9:0x001d, B:11:0x0028, B:12:0x002c, B:14:0x0037, B:15:0x003b, B:17:0x0046, B:18:0x004a, B:20:0x0055, B:21:0x0059, B:23:0x0064, B:24:0x0068, B:26:0x0073, B:27:0x0077, B:29:0x0082, B:30:0x0086, B:32:0x0091, B:33:0x0095, B:35:0x00a0, B:36:0x00a4, B:38:0x00a8, B:39:0x00ac, B:41:0x00b7, B:42:0x00bb, B:44:0x00c6, B:45:0x00ca, B:47:0x00d5, B:48:0x00d9, B:50:0x00e4, B:51:0x00e8, B:53:0x00f3, B:54:0x00f7, B:56:0x0102, B:57:0x0106, B:59:0x0111, B:60:0x0115, B:62:0x0120, B:63:0x0124, B:65:0x012f, B:66:0x0133, B:68:0x013e, B:69:0x0142, B:71:0x014d, B:72:0x0151, B:74:0x0155, B:75:0x0159, B:77:0x0164, B:78:0x0168, B:80:0x0173, B:81:0x0177, B:83:0x0182, B:84:0x0186, B:86:0x0191, B:87:0x0195, B:89:0x01a0, B:90:0x01a4, B:92:0x01af, B:93:0x01b3, B:96:0x01c0, B:104:0x01e2, B:106:0x01ed, B:107:0x01f1, B:109:0x01fc, B:110:0x0200, B:112:0x0204, B:113:0x0208, B:116:0x0217, B:118:0x0220, B:120:0x0224, B:123:0x0230, B:125:0x0234, B:126:0x023b, B:128:0x023f, B:129:0x0243, B:131:0x0250, B:132:0x0255, B:134:0x0259, B:135:0x025d, B:137:0x0261, B:138:0x0268, B:140:0x026c, B:141:0x0270, B:143:0x0281, B:149:0x02a1, B:151:0x02ab, B:157:0x02cb, B:153:0x02b5, B:155:0x02b9, B:156:0x02c4, B:145:0x028b, B:147:0x028f, B:148:0x029a, B:122:0x022c, B:117:0x021b, B:97:0x01c3, B:99:0x01cb, B:101:0x01d5), top: B:168:0x0001, outer: #0 }] */
    /* JADX WARN: Code duplicated, block: B:128:0x023f A[Catch: all -> 0x02d2, JSONException -> 0x02d4, TryCatch #1 {JSONException -> 0x02d4, blocks: (B:3:0x0001, B:5:0x000a, B:6:0x000e, B:8:0x0019, B:9:0x001d, B:11:0x0028, B:12:0x002c, B:14:0x0037, B:15:0x003b, B:17:0x0046, B:18:0x004a, B:20:0x0055, B:21:0x0059, B:23:0x0064, B:24:0x0068, B:26:0x0073, B:27:0x0077, B:29:0x0082, B:30:0x0086, B:32:0x0091, B:33:0x0095, B:35:0x00a0, B:36:0x00a4, B:38:0x00a8, B:39:0x00ac, B:41:0x00b7, B:42:0x00bb, B:44:0x00c6, B:45:0x00ca, B:47:0x00d5, B:48:0x00d9, B:50:0x00e4, B:51:0x00e8, B:53:0x00f3, B:54:0x00f7, B:56:0x0102, B:57:0x0106, B:59:0x0111, B:60:0x0115, B:62:0x0120, B:63:0x0124, B:65:0x012f, B:66:0x0133, B:68:0x013e, B:69:0x0142, B:71:0x014d, B:72:0x0151, B:74:0x0155, B:75:0x0159, B:77:0x0164, B:78:0x0168, B:80:0x0173, B:81:0x0177, B:83:0x0182, B:84:0x0186, B:86:0x0191, B:87:0x0195, B:89:0x01a0, B:90:0x01a4, B:92:0x01af, B:93:0x01b3, B:96:0x01c0, B:104:0x01e2, B:106:0x01ed, B:107:0x01f1, B:109:0x01fc, B:110:0x0200, B:112:0x0204, B:113:0x0208, B:116:0x0217, B:118:0x0220, B:120:0x0224, B:123:0x0230, B:125:0x0234, B:126:0x023b, B:128:0x023f, B:129:0x0243, B:131:0x0250, B:132:0x0255, B:134:0x0259, B:135:0x025d, B:137:0x0261, B:138:0x0268, B:140:0x026c, B:141:0x0270, B:143:0x0281, B:149:0x02a1, B:151:0x02ab, B:157:0x02cb, B:153:0x02b5, B:155:0x02b9, B:156:0x02c4, B:145:0x028b, B:147:0x028f, B:148:0x029a, B:122:0x022c, B:117:0x021b, B:97:0x01c3, B:99:0x01cb, B:101:0x01d5), top: B:168:0x0001, outer: #0 }] */
    /* JADX WARN: Code duplicated, block: B:131:0x0250 A[Catch: all -> 0x02d2, JSONException -> 0x02d4, TryCatch #1 {JSONException -> 0x02d4, blocks: (B:3:0x0001, B:5:0x000a, B:6:0x000e, B:8:0x0019, B:9:0x001d, B:11:0x0028, B:12:0x002c, B:14:0x0037, B:15:0x003b, B:17:0x0046, B:18:0x004a, B:20:0x0055, B:21:0x0059, B:23:0x0064, B:24:0x0068, B:26:0x0073, B:27:0x0077, B:29:0x0082, B:30:0x0086, B:32:0x0091, B:33:0x0095, B:35:0x00a0, B:36:0x00a4, B:38:0x00a8, B:39:0x00ac, B:41:0x00b7, B:42:0x00bb, B:44:0x00c6, B:45:0x00ca, B:47:0x00d5, B:48:0x00d9, B:50:0x00e4, B:51:0x00e8, B:53:0x00f3, B:54:0x00f7, B:56:0x0102, B:57:0x0106, B:59:0x0111, B:60:0x0115, B:62:0x0120, B:63:0x0124, B:65:0x012f, B:66:0x0133, B:68:0x013e, B:69:0x0142, B:71:0x014d, B:72:0x0151, B:74:0x0155, B:75:0x0159, B:77:0x0164, B:78:0x0168, B:80:0x0173, B:81:0x0177, B:83:0x0182, B:84:0x0186, B:86:0x0191, B:87:0x0195, B:89:0x01a0, B:90:0x01a4, B:92:0x01af, B:93:0x01b3, B:96:0x01c0, B:104:0x01e2, B:106:0x01ed, B:107:0x01f1, B:109:0x01fc, B:110:0x0200, B:112:0x0204, B:113:0x0208, B:116:0x0217, B:118:0x0220, B:120:0x0224, B:123:0x0230, B:125:0x0234, B:126:0x023b, B:128:0x023f, B:129:0x0243, B:131:0x0250, B:132:0x0255, B:134:0x0259, B:135:0x025d, B:137:0x0261, B:138:0x0268, B:140:0x026c, B:141:0x0270, B:143:0x0281, B:149:0x02a1, B:151:0x02ab, B:157:0x02cb, B:153:0x02b5, B:155:0x02b9, B:156:0x02c4, B:145:0x028b, B:147:0x028f, B:148:0x029a, B:122:0x022c, B:117:0x021b, B:97:0x01c3, B:99:0x01cb, B:101:0x01d5), top: B:168:0x0001, outer: #0 }] */
    /* JADX WARN: Code duplicated, block: B:134:0x0259 A[Catch: all -> 0x02d2, JSONException -> 0x02d4, TryCatch #1 {JSONException -> 0x02d4, blocks: (B:3:0x0001, B:5:0x000a, B:6:0x000e, B:8:0x0019, B:9:0x001d, B:11:0x0028, B:12:0x002c, B:14:0x0037, B:15:0x003b, B:17:0x0046, B:18:0x004a, B:20:0x0055, B:21:0x0059, B:23:0x0064, B:24:0x0068, B:26:0x0073, B:27:0x0077, B:29:0x0082, B:30:0x0086, B:32:0x0091, B:33:0x0095, B:35:0x00a0, B:36:0x00a4, B:38:0x00a8, B:39:0x00ac, B:41:0x00b7, B:42:0x00bb, B:44:0x00c6, B:45:0x00ca, B:47:0x00d5, B:48:0x00d9, B:50:0x00e4, B:51:0x00e8, B:53:0x00f3, B:54:0x00f7, B:56:0x0102, B:57:0x0106, B:59:0x0111, B:60:0x0115, B:62:0x0120, B:63:0x0124, B:65:0x012f, B:66:0x0133, B:68:0x013e, B:69:0x0142, B:71:0x014d, B:72:0x0151, B:74:0x0155, B:75:0x0159, B:77:0x0164, B:78:0x0168, B:80:0x0173, B:81:0x0177, B:83:0x0182, B:84:0x0186, B:86:0x0191, B:87:0x0195, B:89:0x01a0, B:90:0x01a4, B:92:0x01af, B:93:0x01b3, B:96:0x01c0, B:104:0x01e2, B:106:0x01ed, B:107:0x01f1, B:109:0x01fc, B:110:0x0200, B:112:0x0204, B:113:0x0208, B:116:0x0217, B:118:0x0220, B:120:0x0224, B:123:0x0230, B:125:0x0234, B:126:0x023b, B:128:0x023f, B:129:0x0243, B:131:0x0250, B:132:0x0255, B:134:0x0259, B:135:0x025d, B:137:0x0261, B:138:0x0268, B:140:0x026c, B:141:0x0270, B:143:0x0281, B:149:0x02a1, B:151:0x02ab, B:157:0x02cb, B:153:0x02b5, B:155:0x02b9, B:156:0x02c4, B:145:0x028b, B:147:0x028f, B:148:0x029a, B:122:0x022c, B:117:0x021b, B:97:0x01c3, B:99:0x01cb, B:101:0x01d5), top: B:168:0x0001, outer: #0 }] */
    /* JADX WARN: Code duplicated, block: B:137:0x0261 A[Catch: all -> 0x02d2, JSONException -> 0x02d4, TryCatch #1 {JSONException -> 0x02d4, blocks: (B:3:0x0001, B:5:0x000a, B:6:0x000e, B:8:0x0019, B:9:0x001d, B:11:0x0028, B:12:0x002c, B:14:0x0037, B:15:0x003b, B:17:0x0046, B:18:0x004a, B:20:0x0055, B:21:0x0059, B:23:0x0064, B:24:0x0068, B:26:0x0073, B:27:0x0077, B:29:0x0082, B:30:0x0086, B:32:0x0091, B:33:0x0095, B:35:0x00a0, B:36:0x00a4, B:38:0x00a8, B:39:0x00ac, B:41:0x00b7, B:42:0x00bb, B:44:0x00c6, B:45:0x00ca, B:47:0x00d5, B:48:0x00d9, B:50:0x00e4, B:51:0x00e8, B:53:0x00f3, B:54:0x00f7, B:56:0x0102, B:57:0x0106, B:59:0x0111, B:60:0x0115, B:62:0x0120, B:63:0x0124, B:65:0x012f, B:66:0x0133, B:68:0x013e, B:69:0x0142, B:71:0x014d, B:72:0x0151, B:74:0x0155, B:75:0x0159, B:77:0x0164, B:78:0x0168, B:80:0x0173, B:81:0x0177, B:83:0x0182, B:84:0x0186, B:86:0x0191, B:87:0x0195, B:89:0x01a0, B:90:0x01a4, B:92:0x01af, B:93:0x01b3, B:96:0x01c0, B:104:0x01e2, B:106:0x01ed, B:107:0x01f1, B:109:0x01fc, B:110:0x0200, B:112:0x0204, B:113:0x0208, B:116:0x0217, B:118:0x0220, B:120:0x0224, B:123:0x0230, B:125:0x0234, B:126:0x023b, B:128:0x023f, B:129:0x0243, B:131:0x0250, B:132:0x0255, B:134:0x0259, B:135:0x025d, B:137:0x0261, B:138:0x0268, B:140:0x026c, B:141:0x0270, B:143:0x0281, B:149:0x02a1, B:151:0x02ab, B:157:0x02cb, B:153:0x02b5, B:155:0x02b9, B:156:0x02c4, B:145:0x028b, B:147:0x028f, B:148:0x029a, B:122:0x022c, B:117:0x021b, B:97:0x01c3, B:99:0x01cb, B:101:0x01d5), top: B:168:0x0001, outer: #0 }] */
    /* JADX WARN: Code duplicated, block: B:140:0x026c A[Catch: all -> 0x02d2, JSONException -> 0x02d4, TryCatch #1 {JSONException -> 0x02d4, blocks: (B:3:0x0001, B:5:0x000a, B:6:0x000e, B:8:0x0019, B:9:0x001d, B:11:0x0028, B:12:0x002c, B:14:0x0037, B:15:0x003b, B:17:0x0046, B:18:0x004a, B:20:0x0055, B:21:0x0059, B:23:0x0064, B:24:0x0068, B:26:0x0073, B:27:0x0077, B:29:0x0082, B:30:0x0086, B:32:0x0091, B:33:0x0095, B:35:0x00a0, B:36:0x00a4, B:38:0x00a8, B:39:0x00ac, B:41:0x00b7, B:42:0x00bb, B:44:0x00c6, B:45:0x00ca, B:47:0x00d5, B:48:0x00d9, B:50:0x00e4, B:51:0x00e8, B:53:0x00f3, B:54:0x00f7, B:56:0x0102, B:57:0x0106, B:59:0x0111, B:60:0x0115, B:62:0x0120, B:63:0x0124, B:65:0x012f, B:66:0x0133, B:68:0x013e, B:69:0x0142, B:71:0x014d, B:72:0x0151, B:74:0x0155, B:75:0x0159, B:77:0x0164, B:78:0x0168, B:80:0x0173, B:81:0x0177, B:83:0x0182, B:84:0x0186, B:86:0x0191, B:87:0x0195, B:89:0x01a0, B:90:0x01a4, B:92:0x01af, B:93:0x01b3, B:96:0x01c0, B:104:0x01e2, B:106:0x01ed, B:107:0x01f1, B:109:0x01fc, B:110:0x0200, B:112:0x0204, B:113:0x0208, B:116:0x0217, B:118:0x0220, B:120:0x0224, B:123:0x0230, B:125:0x0234, B:126:0x023b, B:128:0x023f, B:129:0x0243, B:131:0x0250, B:132:0x0255, B:134:0x0259, B:135:0x025d, B:137:0x0261, B:138:0x0268, B:140:0x026c, B:141:0x0270, B:143:0x0281, B:149:0x02a1, B:151:0x02ab, B:157:0x02cb, B:153:0x02b5, B:155:0x02b9, B:156:0x02c4, B:145:0x028b, B:147:0x028f, B:148:0x029a, B:122:0x022c, B:117:0x021b, B:97:0x01c3, B:99:0x01cb, B:101:0x01d5), top: B:168:0x0001, outer: #0 }] */
    /* JADX WARN: Code duplicated, block: B:145:0x028b A[Catch: all -> 0x02d2, JSONException -> 0x02d4, TryCatch #1 {JSONException -> 0x02d4, blocks: (B:3:0x0001, B:5:0x000a, B:6:0x000e, B:8:0x0019, B:9:0x001d, B:11:0x0028, B:12:0x002c, B:14:0x0037, B:15:0x003b, B:17:0x0046, B:18:0x004a, B:20:0x0055, B:21:0x0059, B:23:0x0064, B:24:0x0068, B:26:0x0073, B:27:0x0077, B:29:0x0082, B:30:0x0086, B:32:0x0091, B:33:0x0095, B:35:0x00a0, B:36:0x00a4, B:38:0x00a8, B:39:0x00ac, B:41:0x00b7, B:42:0x00bb, B:44:0x00c6, B:45:0x00ca, B:47:0x00d5, B:48:0x00d9, B:50:0x00e4, B:51:0x00e8, B:53:0x00f3, B:54:0x00f7, B:56:0x0102, B:57:0x0106, B:59:0x0111, B:60:0x0115, B:62:0x0120, B:63:0x0124, B:65:0x012f, B:66:0x0133, B:68:0x013e, B:69:0x0142, B:71:0x014d, B:72:0x0151, B:74:0x0155, B:75:0x0159, B:77:0x0164, B:78:0x0168, B:80:0x0173, B:81:0x0177, B:83:0x0182, B:84:0x0186, B:86:0x0191, B:87:0x0195, B:89:0x01a0, B:90:0x01a4, B:92:0x01af, B:93:0x01b3, B:96:0x01c0, B:104:0x01e2, B:106:0x01ed, B:107:0x01f1, B:109:0x01fc, B:110:0x0200, B:112:0x0204, B:113:0x0208, B:116:0x0217, B:118:0x0220, B:120:0x0224, B:123:0x0230, B:125:0x0234, B:126:0x023b, B:128:0x023f, B:129:0x0243, B:131:0x0250, B:132:0x0255, B:134:0x0259, B:135:0x025d, B:137:0x0261, B:138:0x0268, B:140:0x026c, B:141:0x0270, B:143:0x0281, B:149:0x02a1, B:151:0x02ab, B:157:0x02cb, B:153:0x02b5, B:155:0x02b9, B:156:0x02c4, B:145:0x028b, B:147:0x028f, B:148:0x029a, B:122:0x022c, B:117:0x021b, B:97:0x01c3, B:99:0x01cb, B:101:0x01d5), top: B:168:0x0001, outer: #0 }] */
    /* JADX WARN: Code duplicated, block: B:147:0x028f A[Catch: all -> 0x02d2, JSONException -> 0x02d4, TryCatch #1 {JSONException -> 0x02d4, blocks: (B:3:0x0001, B:5:0x000a, B:6:0x000e, B:8:0x0019, B:9:0x001d, B:11:0x0028, B:12:0x002c, B:14:0x0037, B:15:0x003b, B:17:0x0046, B:18:0x004a, B:20:0x0055, B:21:0x0059, B:23:0x0064, B:24:0x0068, B:26:0x0073, B:27:0x0077, B:29:0x0082, B:30:0x0086, B:32:0x0091, B:33:0x0095, B:35:0x00a0, B:36:0x00a4, B:38:0x00a8, B:39:0x00ac, B:41:0x00b7, B:42:0x00bb, B:44:0x00c6, B:45:0x00ca, B:47:0x00d5, B:48:0x00d9, B:50:0x00e4, B:51:0x00e8, B:53:0x00f3, B:54:0x00f7, B:56:0x0102, B:57:0x0106, B:59:0x0111, B:60:0x0115, B:62:0x0120, B:63:0x0124, B:65:0x012f, B:66:0x0133, B:68:0x013e, B:69:0x0142, B:71:0x014d, B:72:0x0151, B:74:0x0155, B:75:0x0159, B:77:0x0164, B:78:0x0168, B:80:0x0173, B:81:0x0177, B:83:0x0182, B:84:0x0186, B:86:0x0191, B:87:0x0195, B:89:0x01a0, B:90:0x01a4, B:92:0x01af, B:93:0x01b3, B:96:0x01c0, B:104:0x01e2, B:106:0x01ed, B:107:0x01f1, B:109:0x01fc, B:110:0x0200, B:112:0x0204, B:113:0x0208, B:116:0x0217, B:118:0x0220, B:120:0x0224, B:123:0x0230, B:125:0x0234, B:126:0x023b, B:128:0x023f, B:129:0x0243, B:131:0x0250, B:132:0x0255, B:134:0x0259, B:135:0x025d, B:137:0x0261, B:138:0x0268, B:140:0x026c, B:141:0x0270, B:143:0x0281, B:149:0x02a1, B:151:0x02ab, B:157:0x02cb, B:153:0x02b5, B:155:0x02b9, B:156:0x02c4, B:145:0x028b, B:147:0x028f, B:148:0x029a, B:122:0x022c, B:117:0x021b, B:97:0x01c3, B:99:0x01cb, B:101:0x01d5), top: B:168:0x0001, outer: #0 }] */
    /* JADX WARN: Code duplicated, block: B:148:0x029a A[Catch: all -> 0x02d2, JSONException -> 0x02d4, TryCatch #1 {JSONException -> 0x02d4, blocks: (B:3:0x0001, B:5:0x000a, B:6:0x000e, B:8:0x0019, B:9:0x001d, B:11:0x0028, B:12:0x002c, B:14:0x0037, B:15:0x003b, B:17:0x0046, B:18:0x004a, B:20:0x0055, B:21:0x0059, B:23:0x0064, B:24:0x0068, B:26:0x0073, B:27:0x0077, B:29:0x0082, B:30:0x0086, B:32:0x0091, B:33:0x0095, B:35:0x00a0, B:36:0x00a4, B:38:0x00a8, B:39:0x00ac, B:41:0x00b7, B:42:0x00bb, B:44:0x00c6, B:45:0x00ca, B:47:0x00d5, B:48:0x00d9, B:50:0x00e4, B:51:0x00e8, B:53:0x00f3, B:54:0x00f7, B:56:0x0102, B:57:0x0106, B:59:0x0111, B:60:0x0115, B:62:0x0120, B:63:0x0124, B:65:0x012f, B:66:0x0133, B:68:0x013e, B:69:0x0142, B:71:0x014d, B:72:0x0151, B:74:0x0155, B:75:0x0159, B:77:0x0164, B:78:0x0168, B:80:0x0173, B:81:0x0177, B:83:0x0182, B:84:0x0186, B:86:0x0191, B:87:0x0195, B:89:0x01a0, B:90:0x01a4, B:92:0x01af, B:93:0x01b3, B:96:0x01c0, B:104:0x01e2, B:106:0x01ed, B:107:0x01f1, B:109:0x01fc, B:110:0x0200, B:112:0x0204, B:113:0x0208, B:116:0x0217, B:118:0x0220, B:120:0x0224, B:123:0x0230, B:125:0x0234, B:126:0x023b, B:128:0x023f, B:129:0x0243, B:131:0x0250, B:132:0x0255, B:134:0x0259, B:135:0x025d, B:137:0x0261, B:138:0x0268, B:140:0x026c, B:141:0x0270, B:143:0x0281, B:149:0x02a1, B:151:0x02ab, B:157:0x02cb, B:153:0x02b5, B:155:0x02b9, B:156:0x02c4, B:145:0x028b, B:147:0x028f, B:148:0x029a, B:122:0x022c, B:117:0x021b, B:97:0x01c3, B:99:0x01cb, B:101:0x01d5), top: B:168:0x0001, outer: #0 }] */
    /* JADX WARN: Code duplicated, block: B:153:0x02b5 A[Catch: all -> 0x02d2, JSONException -> 0x02d4, TryCatch #1 {JSONException -> 0x02d4, blocks: (B:3:0x0001, B:5:0x000a, B:6:0x000e, B:8:0x0019, B:9:0x001d, B:11:0x0028, B:12:0x002c, B:14:0x0037, B:15:0x003b, B:17:0x0046, B:18:0x004a, B:20:0x0055, B:21:0x0059, B:23:0x0064, B:24:0x0068, B:26:0x0073, B:27:0x0077, B:29:0x0082, B:30:0x0086, B:32:0x0091, B:33:0x0095, B:35:0x00a0, B:36:0x00a4, B:38:0x00a8, B:39:0x00ac, B:41:0x00b7, B:42:0x00bb, B:44:0x00c6, B:45:0x00ca, B:47:0x00d5, B:48:0x00d9, B:50:0x00e4, B:51:0x00e8, B:53:0x00f3, B:54:0x00f7, B:56:0x0102, B:57:0x0106, B:59:0x0111, B:60:0x0115, B:62:0x0120, B:63:0x0124, B:65:0x012f, B:66:0x0133, B:68:0x013e, B:69:0x0142, B:71:0x014d, B:72:0x0151, B:74:0x0155, B:75:0x0159, B:77:0x0164, B:78:0x0168, B:80:0x0173, B:81:0x0177, B:83:0x0182, B:84:0x0186, B:86:0x0191, B:87:0x0195, B:89:0x01a0, B:90:0x01a4, B:92:0x01af, B:93:0x01b3, B:96:0x01c0, B:104:0x01e2, B:106:0x01ed, B:107:0x01f1, B:109:0x01fc, B:110:0x0200, B:112:0x0204, B:113:0x0208, B:116:0x0217, B:118:0x0220, B:120:0x0224, B:123:0x0230, B:125:0x0234, B:126:0x023b, B:128:0x023f, B:129:0x0243, B:131:0x0250, B:132:0x0255, B:134:0x0259, B:135:0x025d, B:137:0x0261, B:138:0x0268, B:140:0x026c, B:141:0x0270, B:143:0x0281, B:149:0x02a1, B:151:0x02ab, B:157:0x02cb, B:153:0x02b5, B:155:0x02b9, B:156:0x02c4, B:145:0x028b, B:147:0x028f, B:148:0x029a, B:122:0x022c, B:117:0x021b, B:97:0x01c3, B:99:0x01cb, B:101:0x01d5), top: B:168:0x0001, outer: #0 }] */
    /* JADX WARN: Code duplicated, block: B:155:0x02b9 A[Catch: all -> 0x02d2, JSONException -> 0x02d4, TryCatch #1 {JSONException -> 0x02d4, blocks: (B:3:0x0001, B:5:0x000a, B:6:0x000e, B:8:0x0019, B:9:0x001d, B:11:0x0028, B:12:0x002c, B:14:0x0037, B:15:0x003b, B:17:0x0046, B:18:0x004a, B:20:0x0055, B:21:0x0059, B:23:0x0064, B:24:0x0068, B:26:0x0073, B:27:0x0077, B:29:0x0082, B:30:0x0086, B:32:0x0091, B:33:0x0095, B:35:0x00a0, B:36:0x00a4, B:38:0x00a8, B:39:0x00ac, B:41:0x00b7, B:42:0x00bb, B:44:0x00c6, B:45:0x00ca, B:47:0x00d5, B:48:0x00d9, B:50:0x00e4, B:51:0x00e8, B:53:0x00f3, B:54:0x00f7, B:56:0x0102, B:57:0x0106, B:59:0x0111, B:60:0x0115, B:62:0x0120, B:63:0x0124, B:65:0x012f, B:66:0x0133, B:68:0x013e, B:69:0x0142, B:71:0x014d, B:72:0x0151, B:74:0x0155, B:75:0x0159, B:77:0x0164, B:78:0x0168, B:80:0x0173, B:81:0x0177, B:83:0x0182, B:84:0x0186, B:86:0x0191, B:87:0x0195, B:89:0x01a0, B:90:0x01a4, B:92:0x01af, B:93:0x01b3, B:96:0x01c0, B:104:0x01e2, B:106:0x01ed, B:107:0x01f1, B:109:0x01fc, B:110:0x0200, B:112:0x0204, B:113:0x0208, B:116:0x0217, B:118:0x0220, B:120:0x0224, B:123:0x0230, B:125:0x0234, B:126:0x023b, B:128:0x023f, B:129:0x0243, B:131:0x0250, B:132:0x0255, B:134:0x0259, B:135:0x025d, B:137:0x0261, B:138:0x0268, B:140:0x026c, B:141:0x0270, B:143:0x0281, B:149:0x02a1, B:151:0x02ab, B:157:0x02cb, B:153:0x02b5, B:155:0x02b9, B:156:0x02c4, B:145:0x028b, B:147:0x028f, B:148:0x029a, B:122:0x022c, B:117:0x021b, B:97:0x01c3, B:99:0x01cb, B:101:0x01d5), top: B:168:0x0001, outer: #0 }] */
    /* JADX WARN: Code duplicated, block: B:156:0x02c4 A[Catch: all -> 0x02d2, JSONException -> 0x02d4, TryCatch #1 {JSONException -> 0x02d4, blocks: (B:3:0x0001, B:5:0x000a, B:6:0x000e, B:8:0x0019, B:9:0x001d, B:11:0x0028, B:12:0x002c, B:14:0x0037, B:15:0x003b, B:17:0x0046, B:18:0x004a, B:20:0x0055, B:21:0x0059, B:23:0x0064, B:24:0x0068, B:26:0x0073, B:27:0x0077, B:29:0x0082, B:30:0x0086, B:32:0x0091, B:33:0x0095, B:35:0x00a0, B:36:0x00a4, B:38:0x00a8, B:39:0x00ac, B:41:0x00b7, B:42:0x00bb, B:44:0x00c6, B:45:0x00ca, B:47:0x00d5, B:48:0x00d9, B:50:0x00e4, B:51:0x00e8, B:53:0x00f3, B:54:0x00f7, B:56:0x0102, B:57:0x0106, B:59:0x0111, B:60:0x0115, B:62:0x0120, B:63:0x0124, B:65:0x012f, B:66:0x0133, B:68:0x013e, B:69:0x0142, B:71:0x014d, B:72:0x0151, B:74:0x0155, B:75:0x0159, B:77:0x0164, B:78:0x0168, B:80:0x0173, B:81:0x0177, B:83:0x0182, B:84:0x0186, B:86:0x0191, B:87:0x0195, B:89:0x01a0, B:90:0x01a4, B:92:0x01af, B:93:0x01b3, B:96:0x01c0, B:104:0x01e2, B:106:0x01ed, B:107:0x01f1, B:109:0x01fc, B:110:0x0200, B:112:0x0204, B:113:0x0208, B:116:0x0217, B:118:0x0220, B:120:0x0224, B:123:0x0230, B:125:0x0234, B:126:0x023b, B:128:0x023f, B:129:0x0243, B:131:0x0250, B:132:0x0255, B:134:0x0259, B:135:0x025d, B:137:0x0261, B:138:0x0268, B:140:0x026c, B:141:0x0270, B:143:0x0281, B:149:0x02a1, B:151:0x02ab, B:157:0x02cb, B:153:0x02b5, B:155:0x02b9, B:156:0x02c4, B:145:0x028b, B:147:0x028f, B:148:0x029a, B:122:0x022c, B:117:0x021b, B:97:0x01c3, B:99:0x01cb, B:101:0x01d5), top: B:168:0x0001, outer: #0 }] */
    public synchronized String a() {
        String str;
        Object obj;
        try {
            JSONObject jSONObject = new JSONObject();
            if (this.f1413a == null) {
                this.f1413a = "Unknown";
            }
            jSONObject.put("Manuf", this.f1413a);
            if (this.f1414b == null) {
                this.f1414b = "Unknown";
            }
            jSONObject.put("Model", this.f1414b);
            if (this.f1415c == null) {
                this.f1415c = "Unknown";
            }
            jSONObject.put("HWVersion", this.f1415c);
            if (this.d == null) {
                this.d = "Unknown";
            }
            jSONObject.put("SWVersion", this.d);
            if (this.e == null) {
                this.e = "Unknown";
            }
            jSONObject.put("OS", this.e);
            if (this.f == null) {
                this.f = "Unknown";
            }
            jSONObject.put("OSVersion", this.f);
            if (this.g == null) {
                this.g = "Unknown";
            }
            jSONObject.put("IMEI1", this.g);
            if (this.h == null) {
                this.h = "";
            }
            jSONObject.put("ICCID1", this.h);
            if (this.i == null) {
                this.i = "";
            }
            jSONObject.put("IMSI1", this.i);
            if (this.j == null) {
                this.j = "";
            }
            jSONObject.put("MNC1", this.j);
            if (this.k == null) {
                this.k = "";
            }
            if (this.l == null) {
                this.l = "";
            }
            jSONObject.put("NetworkType1", this.l);
            if (this.m == null) {
                this.m = "";
            }
            jSONObject.put("TAC1", this.m);
            if (this.n == null) {
                this.n = "";
            }
            jSONObject.put("CID1", this.n);
            if (this.o == null) {
                this.o = "";
            }
            jSONObject.put("PCI1", this.o);
            if (this.p == null) {
                this.p = "";
            }
            jSONObject.put("VolteEnabled1", this.p);
            if (this.q == null) {
                this.q = "";
            }
            jSONObject.put("5GEnabled1", this.q);
            if (this.r == null) {
                this.r = "";
            }
            jSONObject.put("VoNREnable1", this.r);
            if (this.s == null) {
                this.s = "";
            }
            jSONObject.put("IMEI2", this.s);
            if (this.t == null) {
                this.t = "";
            }
            jSONObject.put("ICCID2", this.t);
            if (this.u == null) {
                this.u = "";
            }
            jSONObject.put("IMSI2", this.u);
            if (this.v == null) {
                this.v = "";
            }
            jSONObject.put("MNC2", this.v);
            if (this.w == null) {
                this.w = "";
            }
            if (this.x == null) {
                this.x = "";
            }
            jSONObject.put("NetworkType2", this.x);
            if (this.y == null) {
                this.y = "";
            }
            jSONObject.put("TAC2", this.y);
            if (this.z == null) {
                this.z = "";
            }
            jSONObject.put("CID2", this.z);
            if (this.A == null) {
                this.A = "";
            }
            jSONObject.put("PCI2", this.A);
            if (this.B == null) {
                this.B = "";
            }
            jSONObject.put("VolteEnabled2", this.B);
            if (this.C == null) {
                this.C = "";
            }
            jSONObject.put("5GEnabled2", this.C);
            if (this.D == null) {
                this.D = "";
            }
            jSONObject.put("VoNREnable2", this.D);
            if (this.E == null || this.E.isEmpty() || (!this.E.equals("1") && !this.E.equals("2"))) {
                String str2 = "0";
                this.E = str2;
                jSONObject.put("DataSlot", this.E);
                if (this.F == null) {
                    this.F = "Unknown";
                }
                jSONObject.put("AccType", this.F);
                if (this.G == null) {
                    this.G = "";
                }
                if (this.H == null) {
                    this.H = "";
                }
                jSONObject.put("IPv6", this.H);
                if (this.I == null) {
                    str = "SubType";
                    obj = "";
                } else {
                    str = "SubType";
                    obj = this.I;
                }
                jSONObject.put(str, obj);
                if (this.J != null) {
                    this.J = "000";
                } else {
                    this.J = "000";
                }
                if (this.J != null) {
                    jSONObject.put("DeviceType", this.J);
                }
                if (this.K == null) {
                    this.K = "";
                }
                jSONObject.put("RegType", this.K);
                this.L = "5.0";
                if ("5.0" != 0) {
                    jSONObject.put("SpecVersion", "5.0");
                }
                if (this.M == null) {
                    this.M = "3.0.1";
                }
                if (this.M != null) {
                    jSONObject.put("ClientVersion", this.M);
                }
                if (this.N == null) {
                    this.N = "";
                }
                jSONObject.put("Time", this.N);
                if (this.K.equals("R")) {
                    if (this.P == null) {
                        jSONObject.put("SigInfo", new JSONArray());
                    } else {
                        jSONObject.put("SigInfo", this.P);
                    }
                } else if (this.P == null) {
                    jSONObject.put("SigInfo", new JSONArray());
                } else {
                    jSONObject.put("SigInfo", this.P);
                }
                if (this.K.equals("A")) {
                    if (this.O == null) {
                        jSONObject.put("AppInfo", new JSONArray());
                    } else {
                        jSONObject.put("AppInfo", this.O);
                    }
                } else if (this.O == null) {
                    jSONObject.put("AppInfo", new JSONArray());
                } else {
                    jSONObject.put("AppInfo", this.O);
                }
                this.Q = jSONObject.toString();
            } else {
                jSONObject.put("DataSlot", this.E);
                if (this.F == null) {
                    this.F = "Unknown";
                }
                jSONObject.put("AccType", this.F);
                if (this.G == null) {
                    this.G = "";
                }
                if (this.H == null) {
                    this.H = "";
                }
                jSONObject.put("IPv6", this.H);
                if (this.I == null) {
                    str = "SubType";
                    obj = "";
                } else {
                    str = "SubType";
                    obj = this.I;
                }
                jSONObject.put(str, obj);
                if (this.J != null || this.J.isEmpty()) {
                    this.J = "000";
                }
                if (this.J != null) {
                    jSONObject.put("DeviceType", this.J);
                }
                if (this.K == null) {
                    this.K = "";
                }
                jSONObject.put("RegType", this.K);
                this.L = "5.0";
                if ("5.0" != 0) {
                    jSONObject.put("SpecVersion", "5.0");
                }
                if (this.M == null) {
                    this.M = "3.0.1";
                }
                if (this.M != null) {
                    jSONObject.put("ClientVersion", this.M);
                }
                if (this.N == null) {
                    this.N = "";
                }
                jSONObject.put("Time", this.N);
                if (this.K.equals("R") || this.K.equals("")) {
                    if (this.P == null) {
                        jSONObject.put("SigInfo", new JSONArray());
                    } else {
                        jSONObject.put("SigInfo", this.P);
                    }
                }
                if (this.K.equals("A") || this.K.equals("")) {
                    if (this.O == null) {
                        jSONObject.put("AppInfo", new JSONArray());
                    } else {
                        jSONObject.put("AppInfo", this.O);
                    }
                }
                this.Q = jSONObject.toString();
            }
        } catch (JSONException e) {
            h.b("DeviceInfo", "JSONException->" + e.getMessage());
        }
        throw th;
        return this.Q;
    }
}
