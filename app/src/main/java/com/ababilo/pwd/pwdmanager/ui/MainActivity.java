package com.ababilo.pwd.pwdmanager.ui;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ababilo.pwd.pwdmanager.App;
import com.ababilo.pwd.pwdmanager.R;
import com.ababilo.pwd.pwdmanager.core.presenter.BasePresenter;
import com.ababilo.pwd.pwdmanager.core.presenter.MainPresenter;
import com.ababilo.pwd.pwdmanager.core.view.MainView;
import com.ababilo.pwd.pwdmanager.model.Database;
import com.ababilo.pwd.pwdmanager.model.Password;
import com.ababilo.pwd.pwdmanager.util.ActivityUtil;
import com.ababilo.pwd.pwdmanager.util.LedController;
import com.ababilo.pwd.pwdmanager.util.ListAdapterHolder;
import com.ababilo.pwd.pwdmanager.util.OnItemClickListener;
import com.ababilo.pwd.pwdmanager.util.PreferencesManager;
import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.Collections;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends MoxyAppCompatActivity implements MainView {

    public static final int REQUEST_ENABLE_BT = 3433;

    @BindView(R.id.MainActivity__toolbar)
    Toolbar toolbar;
    @BindView(R.id.MainActivity__fab)
    FloatingActionButton fab;
    @BindView(R.id.MainActivity__list)
    RecyclerView recyclerView;

    private Database database;
    private LedController ledController;

    @Inject
    PreferencesManager preferencesManager;

    @InjectPresenter
    MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
            return;
        }
        setContentView(R.layout.activity_main);
        attachInjector();
        loadData();

        setSupportActionBar(toolbar);
        fab.setOnClickListener(v -> ActivityUtil.loadActivityForResult(self(), AddPasswordActivity.class, AddPasswordActivity.REQUEST_CODE));

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter
        RecyclerView.Adapter adapter = new ListAdapterHolder<Password, PasswordViewHolder>(
                null != database ? database.getPasswords() : Collections.emptyList(), R.layout.password_list_item) {
            @Override
            public void onItemClick(View view, int position) {
                presenter.sendPassword(database.getPasswords().get(position));
            }

            @Override
            public void onBindViewHolder(PasswordViewHolder holder, int position) {
                holder.title.setText(list.get(position).getTitle());
                holder.lastUsed.setText(new Date().toString());
            }

            @Override
            protected PasswordViewHolder createViewHolder(View view, OnItemClickListener listener) {
                return new PasswordViewHolder(view, listener);
            }
        };
        recyclerView.setAdapter(adapter);
    }

    private static class PasswordViewHolder extends ListAdapterHolder.ViewHolder {

        TextView title;
        TextView lastUsed;

        PasswordViewHolder(View view, OnItemClickListener listener) {
            super(view, listener);
            this.title = (TextView) view.findViewById(R.id.password_title);
            this.lastUsed = (TextView) view.findViewById(R.id.password_last_used);
        }
    }

    private void loadData() {
        database = (Database) getIntent().getSerializableExtra("DATABASE");
        if (null == database) {
            presenter.loadDatabase();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem led = menu.findItem(R.id.MainActivity__menu_led);
        ledController = new LedController(this, led);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AddPasswordActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            Password password = (Password) data.getExtras().getSerializable("PASSWORD");
            database.getPasswords().add(password);
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    public void onLedClick(MenuItem item) {
        ledController.setState(LedController.State.WAITING);
        if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        String mac = preferencesManager.getString(PreferencesManager.Preference.MAC);
        presenter.connectDevice(mac);
    }

    public void onSettingsClick(MenuItem item) {
        ActivityUtil.loadActivity(this, SettingsActivity.class);
    }

    public void onExitClick(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
    }

    @Override
    public void startSending() {
        ledController.setState(LedController.State.WAITING);
    }

    @Override
    public void onDeviceConnected() {
        ledController.setState(LedController.State.CONNECTED);
    }

    @Override
    public void onDeviceError() {
        ledController.setState(LedController.State.ERROR);
    }

    @Override
    public void onDeviceNotConnected() {
        ActivityUtil.loadActivity(this, SelectDeviceActivity.class);
    }

    @Override
    public void onDatabaseLoaded(Database database) {
        this.database.getPasswords().clear();
        this.database.getPasswords().addAll(database.getPasswords());
        recyclerView.getAdapter().notifyDataSetChanged();
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

    private Activity self() {
        return this;
    }
}
