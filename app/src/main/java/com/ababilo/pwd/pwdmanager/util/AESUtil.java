package com.ababilo.pwd.pwdmanager.util;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by ababilo on 14.11.16.
 */

public class AESUtil {

    private static final SecureRandom sr = new SecureRandom();

    private static byte[] generateSecureBytes(int length) {
        byte[] newBytes = new byte[length];
        sr.nextBytes(newBytes);
        return newBytes;
    }

    private static byte[] generateIV() {
        return generateSecureBytes(16);
    }

    public static byte[] encrypt(byte[] data, byte[] key) {
        byte[] iv = generateIV();

        byte[] encryptedData;
        try {
            encryptedData = encrypt(key, iv, data);
        } catch (Exception ex) {
            return null;
        }
        return ArrayUtils.concatArrays(iv, encryptedData);
    }

    public static byte[] decrypt(byte[] data, byte[] key) {
        byte[] iv = extractIV(data);
        byte[] encryptedData = extractEncryptedData(data);
        if (iv == null) {
            return null;
        }
        try {
            return decrypt(key, iv, encryptedData);
        } catch (Exception ex) {
            return null;
        }
    }

    private static byte[] extractEncryptedData(byte[] data) {
        int encryptedDataLength = data.length - 16;
        byte[] encryptedData = new byte[encryptedDataLength];
        System.arraycopy(data, 16, encryptedData, 0, encryptedDataLength);
        return encryptedData;
    }


    private static byte[] extractIV(byte[] data) {
        if (data.length < 16) {
            return null;
        }
        byte[] iv = new byte[16];
        System.arraycopy(data, 0, iv, 0, 16);
        return iv;
    }

    private static byte[] encrypt(byte[] key, byte[] IV, byte[] data) throws InvalidAlgorithmParameterException, InvalidKeyException, IOException {
        SecretKey aesKey = new SecretKeySpec(key, 0, key.length, "AES");
        Cipher encryptCipher;
        try {
            encryptCipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        } catch (Exception e) {
            return null;
        }

        encryptCipher.init(Cipher.ENCRYPT_MODE, aesKey, new IvParameterSpec(IV));
        return cipher(data, encryptCipher);
    }

    private static byte[] decrypt(byte[] key, byte[] IV, byte[] encryptedBytes) throws InvalidAlgorithmParameterException, InvalidKeyException, IOException {
        SecretKey aesKey = new SecretKeySpec(key, 0, key.length, "AES");

        Cipher decryptCipher;
        try {
            decryptCipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        } catch (Exception e) {
            return null;
        }

        decryptCipher.init(Cipher.DECRYPT_MODE, aesKey, new IvParameterSpec(IV));
        return cipher(encryptedBytes, decryptCipher);
    }

    private static byte[] cipher(byte[] data, Cipher cipher) {
        try (ByteArrayInputStream in = new ByteArrayInputStream(data);
             CipherInputStream cin = new CipherInputStream(in, cipher);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            IOUtils.copyLarge(cin, out);
            return out.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }
}
