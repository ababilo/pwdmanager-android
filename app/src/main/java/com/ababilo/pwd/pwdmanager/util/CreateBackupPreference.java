package com.ababilo.pwd.pwdmanager.util;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;

import com.ababilo.pwd.pwdmanager.ui.SettingsActivity;

/**
 * Created by ababilo on 16.11.16.
 */

public class CreateBackupPreference extends Preference {

    private Context context;

    public CreateBackupPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            // Restore existing state
            setValue(this.getPersistedString((String) defaultValue));
        } else {
            // Set default state from the XML attribute
            setValue((String) defaultValue);
        }
    }

    public void setValue(String value) {
        persistString(value);
        setSummary(value);
    }

    @Override
    public void onClick() {
        ((SettingsActivity) context).onCreateBackupClick();
    }
}
