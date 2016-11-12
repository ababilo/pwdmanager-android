package com.ababilo.pwd.pwdmanager;

import com.ababilo.pwd.pwdmanager.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by ababilo on 12.11.16.
 */
@Component(modules = {ViewModule.class})
@Singleton
public interface ViewComponent {

    void inject(MainActivity activity);
}