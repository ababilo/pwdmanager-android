package com.ababilo.pwd.pwdmanager.util;

/**
 * Created by ababilo on 14.11.16.
 */

public class ArrayUtils {

    public static byte[] concatArrays(byte[]... arrays) {
        int resLength = countArraysLength(arrays);

        byte[] result = new byte[resLength];

        int position = 0;
        for (byte[] arr : arrays) {
            if (null != arr) {
                System.arraycopy(arr, 0, result, position, arr.length);
                position += arr.length;
            }
        }
        return result;
    }

    private static int countArraysLength(byte[]... arrays) {
        int resLength = 0;
        for (byte[] arr : arrays) {
            if (null != arr) {
                resLength += arr.length;
            }
        }
        return resLength;
    }

    public static byte[] truncateZeros(byte[] data) {
        int length = countDataLength(data);
        byte[] dataOnly = new byte[length];
        System.arraycopy(data, 0, dataOnly, 0, length);
        return dataOnly;
    }

    private static int countDataLength(byte[] data) {
        int resLength = 0;
        for (byte b : data) {
            if (b != 0) {
                resLength++;
            } else {
                break;
            }
        }
        return resLength;
    }
}
