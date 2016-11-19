package com.ababilo.pwd.pwdmanager.service.protocol;

import android.util.Log;

import com.ababilo.pwd.pwdmanager.util.ArrayUtils;

import java.security.SecureRandom;
import java.util.Arrays;

/**
 * Created by ababilo on 14.11.16.
 */

public class ProtocolKeysProvider {

    private static final SecureRandom sr = new SecureRandom();

    private byte[] currentBTKey;
    private byte[] currentHBTKEy;

    private byte[] nextHBTKey;
    private byte[] nextBTKey;

    public byte[] getCurrentBTKey() {
        return currentBTKey;
    }

    public byte[] getCurrentHBTKey() {
        return currentHBTKEy;
    }

    public synchronized void loadKeys(byte[] btKey, byte[] htbKey) {
        Log.i("KEYS", "loadKeys");
        this.currentBTKey = btKey;
        this.currentHBTKEy = htbKey;
        Log.d("KEYS", "Set BTKEY: " + Arrays.toString(btKey));
    }

    public synchronized void rollKeys() {
        Log.i("KEYS", "rollKeys");
        currentBTKey = nextBTKey;
        currentHBTKEy = nextHBTKey;
    }

    public synchronized byte[] appendNextKeys(byte[] data) {
        nextBTKey = generateKey();
        nextHBTKey = generateKey();

        Log.d("KEYS", "Generated BTKEY: " + Arrays.toString(nextBTKey));

        return ArrayUtils.concatArrays(data, nextBTKey);//, nextHBTKey); // todo?
    }

    private byte[] generateKey() {
        return generateSecureBytes(32);
    }

    public static byte[] generateSecureBytes(int length) {
        byte[] newBytes = new byte[length];
        sr.nextBytes(newBytes);
        return newBytes;
    }
}
