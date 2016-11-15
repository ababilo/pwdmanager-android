package com.ababilo.pwd.pwdmanager.util;

/**
 * Created by ababilo on 15.11.16.
 */

public class ShortUtil {

    public static byte[] getShortBytes(short var) {
        byte[] res = new byte[2];
        int offset = 0;
        res[offset++] = (byte) (var >> 8);
        res[offset] = (byte) var;
        return res;
    }
}
