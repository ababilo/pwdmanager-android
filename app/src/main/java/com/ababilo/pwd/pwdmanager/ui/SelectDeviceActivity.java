package com.ababilo.pwd.pwdmanager.ui;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
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
import com.ababilo.pwd.pwdmanager.util.ListAdapterHolder;
import com.ababilo.pwd.pwdmanager.util.OnItemClickListener;
import com.ababilo.pwd.pwdmanager.util.PreferencesManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectDeviceActivity extends MoxyAppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 3433;

    @BindView(R.id.SelectDeviceActivity__toolbar)
    Toolbar toolbar;
    @BindView(R.id.SelectDeviceActivity__list)
    RecyclerView list;

    @Inject
    PreferencesManager preferencesManager;

    List<BluetoothDevice> devices = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_device);
        attachInjector();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);

        RecyclerView.Adapter adapter = new ListAdapterHolder<BluetoothDevice, DeviceViewHolder>(devices, R.layout.device_list_item) {
            @Override
            public void onItemClick(View view, int position) {
                preferencesManager.setString(PreferencesManager.Preference.MAC, devices.get(position).getAddress());
                finish();
            }

            @Override
            public void onBindViewHolder(DeviceViewHolder holder, int position) {
                holder.title.setText(list.get(position).getName());
                holder.mac.setText(list.get(position).getAddress());
            }

            @Override
            protected DeviceViewHolder createViewHolder(View view, OnItemClickListener listener) {
                return new DeviceViewHolder(view, listener);
            }
        };
        list.setAdapter(adapter);
    }

    private static class DeviceViewHolder extends ListAdapterHolder.ViewHolder {

        TextView title;
        TextView mac;

        DeviceViewHolder(View view, OnItemClickListener listener) {
            super(view, listener);
            this.title = (TextView) view.findViewById(R.id.device_title);
            this.mac = (TextView) view.findViewById(R.id.device_mac);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.select_device_menu, menu);
        return true;
    }

    public void onBtSettingsClick(MenuItem menuItem) {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        ComponentName cn = new ComponentName("com.android.settings",
                "com.android.settings.bluetooth.BluetoothSettings");
        intent.setComponent(cn);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillPairedDevices();
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

    private void fillPairedDevices() {
        if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            fillDevices();
        }
    }

    private void fillDevices() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        devices.clear();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                devices.add(device);
            }
        }
    }

    @Override
    protected void attachInjector() {
        App.getViewComponent().inject(this);
        ButterKnife.bind(this);
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }
}
