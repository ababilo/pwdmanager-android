package com.ababilo.pwd.pwdmanager.model;

/**
 * Created by ababilo on 14.11.16.
 */

public enum Responses {

    PONG(0xFF),
    RESPONSE(0xEE),
    BACKUP_RECEIVED(0xAA),
    BACKUP_SENT(0xBB),
    UNKNOWN(0x00);

    private byte type;

    Responses(int type) {
        this.type = (byte) type;
    }

    public static Responses fromByte(byte typeByte) {
        for (Responses t : values()) {
            if (t.type == typeByte) {
                return t;
            }
        }
        return UNKNOWN;
    }
}
