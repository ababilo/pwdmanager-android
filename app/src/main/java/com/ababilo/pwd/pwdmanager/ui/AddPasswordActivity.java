package com.ababilo.pwd.pwdmanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.ababilo.pwd.pwdmanager.App;
import com.ababilo.pwd.pwdmanager.R;
import com.ababilo.pwd.pwdmanager.core.presenter.AddPasswordPresenter;
import com.ababilo.pwd.pwdmanager.core.presenter.BasePresenter;
import com.ababilo.pwd.pwdmanager.core.view.AddPasswordView;
import com.ababilo.pwd.pwdmanager.model.Password;
import com.arellomobile.mvp.presenter.InjectPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddPasswordActivity extends MoxyAppCompatActivity implements AddPasswordView {

    public static final int REQUEST_CODE = 1403;

    @BindView(R.id.AddPasswordActivity__toolbar)
    Toolbar toolbar;
    @BindView(R.id.AddPasswordActivity__title)
    TextView titleView;
    @BindView(R.id.AddPasswordActivity__password)
    TextView passwordView;
    @BindView(R.id.AddPasswordActivity__repeat_password)
    TextView repeatPasswordView;

    @InjectPresenter
    AddPasswordPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_password);
        attachInjector();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_password_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onApplyClick(MenuItem item) {
        String title = titleView.getText().toString();
        String password = passwordView.getText().toString();
        String repeated = repeatPasswordView.getText().toString();
        if (!password.equals(repeated)) {
            repeatPasswordView.setError(getString(R.string.error__password_not_match));
        }

        presenter.addPassword(title, password);
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
    public void onSuccessful(Password password) {
        Intent intent = this.getIntent();
        intent.putExtra("PASSWORD", password);
        setResult(RESULT_OK, intent);
        presenter.savePassword(password);
    }

    @Override
    public void onError() {
        Toast.makeText(this, "Error adding", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPasswordSaved() {
        finish();
    }
}
