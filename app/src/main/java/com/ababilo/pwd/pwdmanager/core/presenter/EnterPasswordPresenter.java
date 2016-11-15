package com.ababilo.pwd.pwdmanager.core.presenter;

import android.text.TextUtils;

import com.ababilo.pwd.pwdmanager.App;
import com.ababilo.pwd.pwdmanager.core.view.EnterPasswordView;
import com.ababilo.pwd.pwdmanager.model.Database;
import com.ababilo.pwd.pwdmanager.service.DatabaseManager;
import com.arellomobile.mvp.InjectViewState;

import java.util.Collections;
import java.util.UUID;

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

        getViewState().onDatabaseLoaded(new Database(UUID.randomUUID().toString(), Collections.emptyList()));

//        databaseManager.loadDatabase(path, password)
//                .subscribe(
//                        database -> getViewState().onDatabaseLoaded(database),
//                        th -> getViewState().onDatabaseError()
//                );
    }
}
