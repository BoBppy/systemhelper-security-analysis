package c.d.a.b;

import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class n {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public String f1343a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public String f1344b = "";

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public String f1345c = "";
    public String d = "";
    public String e = "";
    public String f = "";
    public String g = "";
    public String h = "";
    public String i = "";
    public String j = "";
    public String k = "";
    public String l = "";
    public String m = "";
    public String n = "";
    public String o = "";
    public String p = "";
    public String q = "";
    public String r = "";
    public String s = "";
    public String t = "";
    public String u = "";
    public String v = "";
    public String w = "";
    public String x = "";
    public String y = "";
    public String z = "";
    public String A = "";
    public String B = "";
    public String C = "";
    public String D = "";
    public String E = "";
    public String F = "";

    public n() {
        this.f1343a = "";
        this.f1343a = l.a();
    }

    public JSONObject a() {
        String str;
        String str2 = this.f1344b;
        if (str2 == null || this.e == null || this.g == null || this.h == null || this.i == null || this.j == null || this.k == null || this.f == null) {
            str = "imei/mnc/imsi/tac/cid/pci/networkType/mncch is null!";
        } else {
            if (!str2.isEmpty() && !this.e.isEmpty() && !this.g.isEmpty() && !this.h.isEmpty() && !this.i.isEmpty() && !this.j.isEmpty() && !this.k.isEmpty() && !this.f.isEmpty()) {
                JSONObject jSONObject = new JSONObject();
                try {
                    jSONObject.put("Time", this.f1343a);
                    jSONObject.put("IMEI", this.f1344b);
                    jSONObject.put("ICCID", this.f1345c);
                    jSONObject.put("ClientVersion", this.d);
                    jSONObject.put("MNC", this.e);
                    jSONObject.put("IMSI", this.g);
                    jSONObject.put("TAC", this.h);
                    jSONObject.put("CID", this.i);
                    jSONObject.put("PCI", this.j);
                    jSONObject.put("NetworkType", this.k);
                    jSONObject.put("ARFCN", this.l);
                    jSONObject.put("Bands", this.m);
                    jSONObject.put("Bandwidth", this.n);
                    jSONObject.put("CQITableIndex", this.o);
                    jSONObject.put("CQIIndex", this.p);
                    jSONObject.put("RSRP", this.q);
                    jSONObject.put("RSRQ", this.s);
                    jSONObject.put("SINR", this.r);
                    jSONObject.put("CSIRSRP", this.t);
                    jSONObject.put("CSIRSRQ", this.u);
                    jSONObject.put("CSISINR", this.v);
                    jSONObject.put("Uspeed", this.w);
                    jSONObject.put("Dspeed", this.x);
                    jSONObject.put("CallDisconnectCause", this.y);
                    jSONObject.put("DataFailCause", this.z);
                    jSONObject.put("EPSFallback", this.A);
                    jSONObject.put("LONGITUDE", this.B);
                    jSONObject.put("LATITUDE", this.C);
                    jSONObject.put("ALTITUDE", this.D);
                    jSONObject.put("SPEED", this.E);
                    jSONObject.put("BEARING", this.F);
                } catch (JSONException e) {
                    h.b("SignalInfo", "getJsonObject->" + e.getMessage());
                }
                return jSONObject;
            }
            h.a("SignalInfo", "imei:" + this.f1344b + "mnc:" + this.e + "imsi:" + this.g + "tac:" + this.h + "cid:" + this.i + "pci:" + this.j + "networkType:" + this.k + "mncch:" + this.f);
            str = "SignalInfo->getJsonObject==null";
        }
        h.b("SignalInfo", str);
        return null;
    }
}
