package com.ababilo.pwd.pwdmanager;

import com.ababilo.pwd.pwdmanager.ui.EnterPasswordActivity;
import com.ababilo.pwd.pwdmanager.ui.MainActivity;
import com.ababilo.pwd.pwdmanager.ui.SettingsActivity;
import com.ababilo.pwd.pwdmanager.ui.SplashActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by ababilo on 12.11.16.
 */
@Component(modules = {ViewModule.class})
@Singleton
public interface ViewComponent {

    void inject(MainActivity activity);
    void inject(EnterPasswordActivity activity);
    void inject(SettingsActivity activity);
    void inject(SplashActivity activity);
}