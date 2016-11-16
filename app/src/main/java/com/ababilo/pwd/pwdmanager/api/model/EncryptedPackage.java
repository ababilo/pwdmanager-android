package com.ababilo.pwd.pwdmanager.api.model;

/**
 * Created by ababilo on 16.11.16.
 */

public class EncryptedPackage {

    private byte[] data;

    public EncryptedPackage(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
