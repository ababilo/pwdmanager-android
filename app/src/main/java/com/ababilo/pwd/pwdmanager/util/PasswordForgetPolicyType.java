package com.ababilo.pwd.pwdmanager.util;

/**
 * Created by ababilo on 14.11.16.
 */

public enum PasswordForgetPolicyType {

    ON_APP_CLOSE,
    AFTER_30_MINS,
    SAVE_IN_PREFS;

    public static PasswordForgetPolicyType fromInt(int x) {
        if (x < 0) {
            return null;
        }
        return values()[x];
    }
}
