package com.ababilo.pwd.pwdmanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ababilo.pwd.pwdmanager.App;
import com.ababilo.pwd.pwdmanager.R;
import com.ababilo.pwd.pwdmanager.core.presenter.BasePresenter;
import com.ababilo.pwd.pwdmanager.core.presenter.EnterPasswordPresenter;
import com.ababilo.pwd.pwdmanager.core.view.EnterPasswordView;
import com.ababilo.pwd.pwdmanager.model.Database;
import com.ababilo.pwd.pwdmanager.util.ActivityUtil;
import com.ababilo.pwd.pwdmanager.util.PasswordForgetPolicyType;
import com.ababilo.pwd.pwdmanager.util.PreferencesManager;
import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.Collections;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EnterPasswordActivity extends MoxyAppCompatActivity implements EnterPasswordView {

    @BindView(R.id.EnterPasswordActivity__password_input)
    TextView passwordView;

    @InjectPresenter
    EnterPasswordPresenter presenter;
    @Inject
    PreferencesManager preferencesManager;

    private PasswordForgetPolicyType passwordForgetPolicyType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_password);
        attachInjector();
        passwordForgetPolicyType = PasswordForgetPolicyType.fromInt(getIntent().getIntExtra("FORGET_POLICY", -1));
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

    public void onExitClick(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
    }

    public void onOkClick(View view) {
        String path = preferencesManager.getString(PreferencesManager.Preference.DB_PATH);
        String password = passwordView.getText().toString();
        presenter.onOkClick(path, password);
    }

    public void onSettingsClick(View view) {
        ActivityUtil.loadActivity(this, SettingsActivity.class);
    }

    @Override
    public void onPasswordEmpty() {
        Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDatabaseLoaded(Database database) {
        ActivityUtil.loadRootActivity(this, MainActivity.class, Collections.singletonMap("DATABASE", database));
    }

    @Override
    public void onDatabaseError() {
        Toast.makeText(this, "Wrong password", Toast.LENGTH_SHORT).show();
    }
}
