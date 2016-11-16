package com.ababilo.pwd.pwdmanager.model;

/**
 * Created by ababilo on 14.11.16.
 */

public enum Commands {

    PING(0xFF),
    SEND_PASS(0xAD),
    ADD_PASS(0xDE),
    REQUEST_BACKUP(0xAB);

    private byte type;

    Commands(int type) {
        this.type = (byte) type;
    }

    public byte getByte() {
        return type;
    }
}
