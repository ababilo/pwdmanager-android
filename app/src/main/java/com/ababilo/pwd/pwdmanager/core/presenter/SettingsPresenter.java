package com.ababilo.pwd.pwdmanager.core.presenter;

import com.ababilo.pwd.pwdmanager.App;
import com.ababilo.pwd.pwdmanager.api.TrustedServiceClient;
import com.ababilo.pwd.pwdmanager.core.view.SettingsView;
import com.ababilo.pwd.pwdmanager.service.BackupService;
import com.ababilo.pwd.pwdmanager.service.DatabaseManager;
import com.ababilo.pwd.pwdmanager.service.protocol.OnResponseReceived;
import com.ababilo.pwd.pwdmanager.service.protocol.ProtocolService;
import com.ababilo.pwd.pwdmanager.util.ObservableWrapper;
import com.arellomobile.mvp.InjectViewState;
import com.google.gson.Gson;

import javax.inject.Inject;

/**
 * Created by ababilo on 16.11.16.
 */

@InjectViewState
public class SettingsPresenter extends BasePresenter<SettingsView> {

    @Inject
    ProtocolService protocolService;
    @Inject
    DatabaseManager databaseManager;
    @Inject
    BackupService backupService;
    @Inject
    TrustedServiceClient client;
    @Inject
    Gson gson;
    @Inject
    OnResponseReceived observer;

    public SettingsPresenter() {
        App.getPresenterComponent().inject(this);
    }

    public void createBackup() {
        getViewState().onRequestStarted();
        new ObservableWrapper<Void>(databaseManager.getDatabase()
                .flatMap(database -> protocolService.requestBackup(database.getClientId()))).wrap()
                .subscribe(
                        none -> observer.putCompetitionCallback(
                                () -> getViewState().onRequestAccepted(),
                                th -> getViewState().onRequestError()
                        ),
                        th -> getViewState().onRequestError()
                );
    }

    public void restoreBackup() {
        getViewState().onRequestStarted();
        new ObservableWrapper<Void>(databaseManager.getDatabase()
                .flatMap(database -> backupService.restoreBackup(database, passwords -> protocolService.sendBackup(passwords)))).wrap()
                .subscribe(
                        none -> observer.putCompetitionCallback(
                                () -> getViewState().onRequestAccepted(),
                                th -> getViewState().onRequestError()
                        ),
                        th -> getViewState().onRequestError()
                );
    }
}
