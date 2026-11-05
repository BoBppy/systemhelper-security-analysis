package com.mobiletools.systemhelper.service;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

/* JADX INFO: loaded from: classes.dex */
public interface a extends IInterface {

    /* JADX INFO: renamed from: com.mobiletools.systemhelper.service.a$a, reason: collision with other inner class name */
    public static abstract class AbstractBinderC0057a extends Binder implements a {
        public AbstractBinderC0057a() {
            attachInterface(this, "com.mobiletools.systemhelper.service.IRegisterHelperService");
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            if (i == 1) {
                parcel.enforceInterface("com.mobiletools.systemhelper.service.IRegisterHelperService");
                a(parcel.readString());
            } else if (i == 2) {
                parcel.enforceInterface("com.mobiletools.systemhelper.service.IRegisterHelperService");
                a(parcel.readInt());
            } else if (i == 3) {
                parcel.enforceInterface("com.mobiletools.systemhelper.service.IRegisterHelperService");
                b();
            } else {
                if (i != 4) {
                    if (i != 5) {
                        if (i != 1598968902) {
                            return super.onTransact(i, parcel, parcel2, i2);
                        }
                        parcel2.writeString("com.mobiletools.systemhelper.service.IRegisterHelperService");
                        return true;
                    }
                    parcel.enforceInterface("com.mobiletools.systemhelper.service.IRegisterHelperService");
                    boolean zA = a();
                    parcel2.writeNoException();
                    parcel2.writeInt(zA ? 1 : 0);
                    return true;
                }
                parcel.enforceInterface("com.mobiletools.systemhelper.service.IRegisterHelperService");
                a(parcel.readInt() != 0);
            }
            parcel2.writeNoException();
            return true;
        }
    }

    void a(int i);

    void a(String str);

    void a(boolean z);

    boolean a();

    void b();
}
