package com.ababilo.pwd.pwdmanager.api.model;

/**
 * Created by ababilo on 16.11.16.
 */

public class Confirmation {

    private byte[] signature;

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }
}
