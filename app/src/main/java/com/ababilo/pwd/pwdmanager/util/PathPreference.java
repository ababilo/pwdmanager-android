package com.ababilo.pwd.pwdmanager.util;

import android.content.Context;
import android.os.Environment;
import android.preference.Preference;
import android.util.AttributeSet;

import java.io.File;

/**
 * Created by ababilo on 14.11.16.
 */

public class PathPreference extends Preference {

    private Context context;

    public PathPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            // Restore existing state
            setValue(new File(this.getPersistedString((String) defaultValue)));
        } else {
            // Set default state from the XML attribute
            setValue((File) defaultValue);
        }
    }

    public void setValue(File value) {
        persistString(value.getAbsolutePath());
        setSummary(value.getAbsolutePath());
    }

    @Override
    public void onClick() {
        FileDialog fileDialog = new FileDialog(context, Environment.getExternalStorageDirectory());
        fileDialog.addFileListener(this::setValue);
        fileDialog.showDialog();
    }
}
