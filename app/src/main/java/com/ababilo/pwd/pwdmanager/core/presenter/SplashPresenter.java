package com.ababilo.pwd.pwdmanager.core.presenter;

import android.text.TextUtils;

import com.ababilo.pwd.pwdmanager.App;
import com.ababilo.pwd.pwdmanager.core.view.SplashView;
import com.ababilo.pwd.pwdmanager.service.DatabaseManager;
import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

/**
 * Created by ababilo on 14.11.16.
 */

@InjectViewState
public class SplashPresenter extends BasePresenter<SplashView> {

    @Inject
    DatabaseManager databaseManager;

    public SplashPresenter() {
        App.getPresenterComponent().inject(this);
    }

    public void checkPassword(String password, String path) {
        if (TextUtils.isEmpty(password)) {
            getViewState().onPasswordNotFound();
        }

        databaseManager.loadDatabase(path, password)
                .subscribe(
                        database -> getViewState().onSavedPassword(database),
                        th -> getViewState().onPasswordNotFound()
                );
    }
}
