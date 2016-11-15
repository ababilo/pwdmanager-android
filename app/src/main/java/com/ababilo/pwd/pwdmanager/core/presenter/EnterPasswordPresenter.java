package com.ababilo.pwd.pwdmanager.core.presenter;

import android.text.TextUtils;

import com.ababilo.pwd.pwdmanager.App;
import com.ababilo.pwd.pwdmanager.core.view.EnterPasswordView;
import com.ababilo.pwd.pwdmanager.service.DatabaseManager;
import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

/**
 * Created by ababilo on 14.11.16.
 */

@InjectViewState
public class EnterPasswordPresenter extends BasePresenter<EnterPasswordView> {

    @Inject
    DatabaseManager databaseManager;

    public EnterPasswordPresenter() {
        App.getPresenterComponent().inject(this);
    }

    public void onOkClick(String path, String password) {
        if (TextUtils.isEmpty(password)) {
            getViewState().onPasswordEmpty();
        }

        databaseManager.loadDatabase(path, password)
                .subscribe(
                        database -> getViewState().onDatabaseLoaded(database),
                        th -> getViewState().onDatabaseError()
                );
    }
}
