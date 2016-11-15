package com.ababilo.pwd.pwdmanager.ui;

import android.os.Bundle;

import com.ababilo.pwd.pwdmanager.App;
import com.ababilo.pwd.pwdmanager.R;
import com.ababilo.pwd.pwdmanager.core.presenter.BasePresenter;
import com.ababilo.pwd.pwdmanager.core.presenter.SplashPresenter;
import com.ababilo.pwd.pwdmanager.core.view.SplashView;
import com.ababilo.pwd.pwdmanager.model.Database;
import com.ababilo.pwd.pwdmanager.util.ActivityUtil;
import com.ababilo.pwd.pwdmanager.util.PreferencesManager;
import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.Collections;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class SplashActivity extends MoxyAppCompatActivity implements SplashView {

    @Inject
    PreferencesManager preferencesManager;

    @InjectPresenter
    SplashPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        attachInjector();

        String password = preferencesManager.getString(PreferencesManager.Preference.DB_PASSWORD);
        String path = preferencesManager.getString(PreferencesManager.Preference.DB_PATH);
        presenter.checkDatabase(password, path);
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
    public void onSavedPassword(Database database) {
        ActivityUtil.loadRootActivity(this, MainActivity.class, Collections.singletonMap("DATABASE", database));
    }

    @Override
    public void onPasswordNotFound() {
        ActivityUtil.loadActivity(this, EnterPasswordActivity.class,
                Collections.singletonMap("FORGET_POLICY", preferencesManager.getInt(PreferencesManager.Preference.FORGET_POLICY)));
    }

    @Override
    public void onPathEmpty() {

    }
}
