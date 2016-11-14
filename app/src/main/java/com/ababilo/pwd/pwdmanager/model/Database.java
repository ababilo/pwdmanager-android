package com.ababilo.pwd.pwdmanager.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ababilo on 14.11.16.
 */

public class Database implements Serializable {

    private String clientId;
    private List<Password> passwords;

    public Database(String clientId, List<Password> passwords) {
        this.clientId = clientId;
        this.passwords = passwords;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public List<Password> getPasswords() {
        return passwords;
    }

    public void setPasswords(List<Password> passwords) {
        this.passwords = passwords;
    }
}
