package com.ababilo.pwd.pwdmanager.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by ababilo on 14.11.16.
 */

public class PreferencesManager {

    public enum Preference {
        DB_PASSWORD,
        DB_PATH,
        MAC, FORGET_POLICY,
        LAST_BACKUP_CREATED,
        LAST_BACKUP_RESTORED
    }

    private SharedPreferences preferences;

    public PreferencesManager(Context context) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getString(Preference preference) {
        return preferences.getString(preference.name(), null);
    }

    public Integer getInt(Preference preference) {
        try {
            return Integer.valueOf(preferences.getString(preference.name(), null));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public void setString(Preference preference, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(preference.name(), value);
        editor.apply();
    }
}
