package com.ababilo.pwd.pwdmanager.core.presenter;

import com.ababilo.pwd.pwdmanager.App;
import com.ababilo.pwd.pwdmanager.core.view.MainView;
import com.ababilo.pwd.pwdmanager.model.Password;
import com.ababilo.pwd.pwdmanager.service.DatabaseManager;
import com.ababilo.pwd.pwdmanager.service.protocol.OnResponseReceived;
import com.ababilo.pwd.pwdmanager.service.protocol.ProtocolService;
import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

/**
 * Created by ababilo on 12.11.16.
 */

@InjectViewState
public class MainPresenter extends BasePresenter<MainView> {

    @Inject
    ProtocolService protocolService;
    @Inject
    OnResponseReceived observer;
    @Inject
    DatabaseManager databaseManager;

    public MainPresenter() {
        App.getPresenterComponent().inject(this);
    }

    public void sendPassword(Password password) {
        if (null == password) {
            // error
        }

        getViewState().startSending();
        protocolService.sendPassword(password)
                .subscribe(
                        none -> {},
                        th -> {}
                );
    }

    public void connectDevice(String mac) {
        if (null == mac) {
            getViewState().onDeviceNotConnected();
            return;
        }
        protocolService.connect(mac, observer)
                .flatMap(none -> protocolService.sendPing())
                .subscribe(
                        none -> getViewState().onDeviceConnected(),
                        throwable -> getViewState().onDeviceError()
                );
    }

    public void loadDatabase() {
        databaseManager.getDatabase().subscribe(database -> getViewState().onDatabaseLoaded(database));
    }
}
