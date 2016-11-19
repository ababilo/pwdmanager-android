package com.ababilo.pwd.pwdmanager.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ababilo.pwd.pwdmanager.App;
import com.ababilo.pwd.pwdmanager.R;
import com.ababilo.pwd.pwdmanager.core.presenter.BasePresenter;
import com.ababilo.pwd.pwdmanager.core.presenter.SettingsPresenter;
import com.ababilo.pwd.pwdmanager.core.view.SettingsView;
import com.ababilo.pwd.pwdmanager.util.PreferencesManager;
import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends MoxyAppCompatPreferenceActivity implements SettingsView {

    public static final int PATH_REQUEST_CODE = 1233;
    public static final int MAC_REQUEST_CODE = 1452;

    @BindView(R.id.SettingsActivity__toolbar)
    Toolbar toolbar;

    @Inject
    PreferencesManager preferencesManager;

    @InjectPresenter
    SettingsPresenter presenter;

    private ProgressDialog progressDialog;

    private static Preference.OnPreferenceChangeListener listener = (preference, value) -> {
        String stringValue = value.toString();

        if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list.
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(stringValue);

            // Set the summary to reflect the new value.
            preference.setSummary(index >= 0
                            ? listPreference.getEntries()[index]
                            : null
            );

        } else {
            // For all other preferences, set the summary to the value's
            // simple string representation.
            preference.setSummary(stringValue);
        }
        return true;
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(listener);

        // Trigger the listener immediately with the preference's
        // current value.
        listener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    public void attachInjector() {
        App.getViewComponent().inject(this);
        ButterKnife.bind(this);
    }

    @Override
    protected BasePresenter getPresenter() {
        return presenter;
    }

    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
        setContentView(R.layout.activity_settings);
        attachInjector();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getTitle());
    }

    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
                || DataSyncPreferenceFragment.class.getName().equals(fragmentName);
    }

    public void onCreateBackupClick() {
        presenter.createBackup();
    }

    public void onRestoreBackupClick() {
        presenter.restoreBackup();
    }

    @Override
    public void onRequestStarted() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void onRequestAccepted() {
        //preferencesManager.setString(PreferencesManager.Preference.LAST_BACKUP_CREATED, new Date().toString()); //todo
        if (null != progressDialog) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    public void onRequestError() {
        if (null != progressDialog) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        Toast.makeText(this, "Error while request", Toast.LENGTH_SHORT).show();
    }

    public static abstract class SettingsFragment extends PreferenceFragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View layout = inflater.inflate(R.layout.activity_settings, container, false);
            if (layout != null) {
                AppCompatPreferenceActivity activity = (AppCompatPreferenceActivity) getActivity();
                Toolbar toolbar = (Toolbar) layout.findViewById(R.id.SettingsActivity__toolbar);
                activity.setSupportActionBar(toolbar);

                ActionBar bar = activity.getSupportActionBar();
                bar.setHomeButtonEnabled(true);
                bar.setDisplayHomeAsUpEnabled(true);
                bar.setDisplayShowTitleEnabled(true);
            }
            return layout;
        }

        @Override
        public void onResume() {
            super.onResume();

            if (getView() != null) {
                View frame = (View) getView().getParent();
                if (frame != null)
                    frame.setPadding(0, 0, 0, 0);
            }
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    getActivity().onBackPressed();
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case PATH_REQUEST_CODE:
//                if (resultCode == RESULT_OK) {
//                    final Uri uri = data.getData();
//
//                    // Get the File path from the Uri
//                    String path = FileUtils.getPath(this, uri);
//
//                    // Alternatively, use FileUtils.getFile(Context, Uri)
//                    if (path != null && FileUtils.isLocal(path)) {
//                        preferencesManager.setString(PreferencesManager.Preference.DB_PATH, path);
//                    }
//                }
//                break;
//            case MAC_REQUEST_CODE:
//                if (resultCode == RESULT_OK) {
//                    String mac = data.getExtras().get("MAC").toString();
//                    preferencesManager.setString(PreferencesManager.Preference.MAC, mac);
//                }
//                break;
//        }
//        getPreferences(0);
//    }

    public static class GeneralPreferenceFragment extends SettingsFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);
        }

        @Override
        public void onResume() {
            super.onResume();
            bindPreferenceSummaryToValue(findPreference(PreferencesManager.Preference.DB_PATH.name()));
            bindPreferenceSummaryToValue(findPreference(PreferencesManager.Preference.MAC.name()));
        }
    }

    public static class DataSyncPreferenceFragment extends SettingsFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_data_sync);
            setHasOptionsMenu(true);
        }

        @Override
        public void onResume() {
            super.onResume();
            bindPreferenceSummaryToValue(findPreference(PreferencesManager.Preference.LAST_BACKUP_CREATED.name()));
            bindPreferenceSummaryToValue(findPreference(PreferencesManager.Preference.LAST_BACKUP_RESTORED.name()));
        }
    }
}
