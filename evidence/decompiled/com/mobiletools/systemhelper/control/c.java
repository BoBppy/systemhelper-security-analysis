package com.mobiletools.systemhelper.control;

import android.content.Context;
import c.d.a.b.h;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/* JADX INFO: loaded from: classes.dex */
public class c extends Thread {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Context f1416b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private b f1417c;
    private a d;
    private String e = "";
    private String f = "";

    public interface a {
        void a(Context context, b bVar, String str, String str2);
    }

    public c(Context context, b bVar, a aVar) {
        this.f1416b = null;
        this.f1417c = null;
        this.f1416b = context;
        this.f1417c = bVar;
        this.d = aVar;
    }

    private String a() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                Enumeration<InetAddress> inetAddresses = networkInterfaces.nextElement().getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddressNextElement = inetAddresses.nextElement();
                    String hostAddress = inetAddressNextElement.getHostAddress();
                    if (!inetAddressNextElement.isLoopbackAddress() && !inetAddressNextElement.isLinkLocalAddress() && (inetAddressNextElement instanceof Inet6Address)) {
                        String str = "address" + hostAddress;
                        return hostAddress;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        h.a("GetIPAddressThread", "getIPv6Address:");
        return "";
    }

    private String b() {
        return "";
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        this.e = b();
        this.f = a();
        h.a("GetIPAddressThread", "IPv4:" + this.e + ", IPv6:" + this.f);
        this.d.a(this.f1416b, this.f1417c, this.e, this.f);
    }
}
