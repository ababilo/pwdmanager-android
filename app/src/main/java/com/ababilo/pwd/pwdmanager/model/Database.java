package com.ababilo.pwd.pwdmanager.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ababilo on 14.11.16.
 */

public class Database implements Serializable {

    private String clientId;
    private byte[] clientSecret;
    private byte[] btKey;
    private byte[] hbtKey;
    private List<Password> passwords;

    public Database(String clientId, byte[] clientSecret, byte[] btKey, byte[] hbtKey, List<Password> passwords) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.btKey = btKey;
        this.hbtKey = hbtKey;
        this.passwords = passwords;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public byte[] getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(byte[] clientSecret) {
        this.clientSecret = clientSecret;
    }

    public byte[] getBtKey() {
        return btKey;
    }

    public void setBtKey(byte[] btKey) {
        this.btKey = btKey;
    }

    public byte[] getHbtKey() {
        return hbtKey;
    }

    public void setHbtKey(byte[] hbtKey) {
        this.hbtKey = hbtKey;
    }

    public List<Password> getPasswords() {
        return passwords;
    }

    public void setPasswords(List<Password> passwords) {
        this.passwords = passwords;
    }
}
