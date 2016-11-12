package com.ababilo.pwd.pwdmanager;

import com.ababilo.pwd.pwdmanager.core.presenter.MainPresenter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by ababilo on 12.11.16.
 */
@Component(modules = {PresenterModule.class})
@Singleton
public interface PresenterComponent {

    void inject(MainPresenter presenter);
}
