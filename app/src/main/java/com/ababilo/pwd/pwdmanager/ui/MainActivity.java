package com.ababilo.pwd.pwdmanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.ababilo.pwd.pwdmanager.util.ListAdapterHolder;
import com.ababilo.pwd.pwdmanager.util.OnItemClickListener;
import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.Collections;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends MoxyAppCompatActivity implements MainView {

    @BindView(R.id.MainActivity__toolbar)
    Toolbar toolbar;
    @BindView(R.id.MainActivity__fab)
    FloatingActionButton fab;
    @BindView(R.id.MainActivity__list)
    RecyclerView recyclerView;

    private Database database;
    private LedController ledController;

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
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem led = menu.findItem(R.id.MainActivity__menu_led);
        ledController = new LedController(this, led);
        return true;
    }

    public void onLedClick(MenuItem item) {
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
    public void attachInjector() {
        App.getViewComponent().inject(this);
        ButterKnife.bind(this);
    }

    @Override
    protected BasePresenter getPresenter() {
        return presenter;
    }
}
