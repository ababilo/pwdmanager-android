package com.ababilo.pwd.pwdmanager.util;

import android.util.Base64;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by ababilo on 14.11.16.
 */

public class HashUtils {

    public static String base64Sha256String(String input) {
        return Base64.encodeToString(sha256Bytes(input.getBytes()), Base64.DEFAULT);
    }

    public static byte[] PBKDF2(char[] input, byte[] salt) {
        final int iterations = 10000;

        // Generate a 256-bit key
        final int outputKeyLength = 256;

        try {
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec keySpec = new PBEKeySpec(input, salt, iterations, outputKeyLength);
            SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
            return secretKey.getEncoded();
        } catch (Exception e) {
            return null;
        }
    }


    public static byte[] sha256Bytes(byte[] input) {
        MessageDigest mda;
        try {
            mda = MessageDigest.getInstance("SHA-256", "BC");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return mda.digest(input);
    }

    public static byte[] hmacSha256(byte[] input, byte[] key) {
        Mac sha256Hmac;
        try {
            sha256Hmac = Mac.getInstance("HmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

        SecretKeySpec secretKey = new SecretKeySpec(key, "HmacSHA256");
        try {
            sha256Hmac.init(secretKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return sha256Hmac.doFinal(input);
    }
}
