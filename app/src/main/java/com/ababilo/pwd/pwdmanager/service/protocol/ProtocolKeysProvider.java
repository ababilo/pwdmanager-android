package com.ababilo.pwd.pwdmanager.service.protocol;

import com.ababilo.pwd.pwdmanager.util.ArrayUtils;

import java.security.SecureRandom;

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

    public byte[] getCurrentHBTKEy() {
        return currentHBTKEy;
    }

    public synchronized void rollKeys() {
        currentBTKey = nextBTKey;
        currentHBTKEy = nextHBTKey;
    }

    public synchronized byte[] appendNextKeys(byte[] data) {
        nextBTKey = generateKey();
        nextHBTKey = generateKey();

        return ArrayUtils.concatArrays(data, nextBTKey, nextHBTKey);
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
