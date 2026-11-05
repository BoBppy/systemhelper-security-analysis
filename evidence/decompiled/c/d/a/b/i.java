package c.d.a.b;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/* JADX INFO: loaded from: classes.dex */
public class i {
    public static String a(String str) {
        byte b2;
        if (str == null || str.equals("")) {
            return "";
        }
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e2) {
            System.out.println("NoSuchAlgorithmException = " + e2.toString());
        }
        byte[] bArrDigest = messageDigest.digest();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < bArrDigest.length; i++) {
            if (Integer.toHexString(bArrDigest[i] & 255).length() == 1) {
                stringBuffer.append("0");
                b2 = bArrDigest[i];
            } else {
                b2 = bArrDigest[i];
            }
            stringBuffer.append(Integer.toHexString(b2 & 255));
        }
        return stringBuffer.toString().toLowerCase();
    }
}
