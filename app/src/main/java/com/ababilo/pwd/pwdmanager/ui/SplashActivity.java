package com.ababilo.pwd.pwdmanager.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ababilo.pwd.pwdmanager.R;
import com.ababilo.pwd.pwdmanager.util.ActivityUtil;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ActivityUtil.loadRootActivity(this, MainActivity.class);
    }
}
