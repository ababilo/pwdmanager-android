package com.ababilo.pwd.pwdmanager.core.presenter;

import com.ababilo.pwd.pwdmanager.App;
import com.ababilo.pwd.pwdmanager.core.view.AddPasswordView;
import com.ababilo.pwd.pwdmanager.service.DatabaseManager;
import com.ababilo.pwd.pwdmanager.service.protocol.ProtocolService;
import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

/**
 * Created by ababilo on 15.11.16.
 */

@InjectViewState
public class AddPasswordPresenter extends BasePresenter<AddPasswordView> {

    @Inject
    DatabaseManager databaseManager;
    @Inject
    ProtocolService protocolService;

    public AddPasswordPresenter() {
        App.getPresenterComponent().inject(this);
    }

    public void addPassword(String title, String password) {
        databaseManager.getDatabase()
                .flatMap(database -> protocolService.addPassword((short) database.getPasswords().size(), title, password))
                .subscribe(
                        pwd -> getViewState().onSuccessful(pwd),
                        th -> getViewState().onError()
                );
    }
}
