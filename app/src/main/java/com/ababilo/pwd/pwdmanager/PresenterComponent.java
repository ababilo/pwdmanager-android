package com.ababilo.pwd.pwdmanager;

import com.ababilo.pwd.pwdmanager.core.presenter.AddPasswordPresenter;
import com.ababilo.pwd.pwdmanager.core.presenter.EnterPasswordPresenter;
import com.ababilo.pwd.pwdmanager.core.presenter.MainPresenter;
import com.ababilo.pwd.pwdmanager.core.presenter.SettingsPresenter;
import com.ababilo.pwd.pwdmanager.core.presenter.SplashPresenter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by ababilo on 12.11.16.
 */
@Component(modules = {PresenterModule.class})
@Singleton
public interface PresenterComponent {

    void inject(MainPresenter presenter);
    void inject(SplashPresenter presenter);
    void inject(EnterPasswordPresenter presenter);
    void inject(AddPasswordPresenter presenter);
    void inject(SettingsPresenter presenter);
}
