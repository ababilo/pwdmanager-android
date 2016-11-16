package com.ababilo.pwd.pwdmanager.core.presenter;

import com.ababilo.pwd.pwdmanager.App;
import com.ababilo.pwd.pwdmanager.api.TrustedServiceClient;
import com.ababilo.pwd.pwdmanager.api.model.EncryptedPackage;
import com.ababilo.pwd.pwdmanager.core.view.SettingsView;
import com.ababilo.pwd.pwdmanager.model.Database;
import com.ababilo.pwd.pwdmanager.model.Password;
import com.ababilo.pwd.pwdmanager.service.DatabaseManager;
import com.ababilo.pwd.pwdmanager.service.protocol.ProtocolKeysProvider;
import com.ababilo.pwd.pwdmanager.service.protocol.ProtocolService;
import com.ababilo.pwd.pwdmanager.util.AESUtil;
import com.ababilo.pwd.pwdmanager.util.ObservableWrapper;
import com.arellomobile.mvp.InjectViewState;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.Charsets;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

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
    TrustedServiceClient client;
    @Inject
    Gson gson;

    public SettingsPresenter() {
        App.getPresenterComponent().inject(this);
    }

    public void createBackup() {
        getViewState().onRequestStarted();
        new ObservableWrapper<Void>(databaseManager.getDatabase()
                .flatMap(database -> protocolService.requestBackup(database.getClientId()))).wrap()
                .subscribe(none -> getViewState().onRequestAccepted(), th -> getViewState().onRequestError());
    }

    public void restoreBackup() {
        getViewState().onRequestStarted();
        databaseManager.getDatabase()
                .flatMap(
                        database -> client.api().getBackup(createRestoreRequest(database.getClientId(), database.getClientSecret())),
                        (database, encryptedPackage) -> saveBackup(encryptedPackage, database)
                );
    }

    private EncryptedPackage createRestoreRequest(String clientId, byte[] secret) {
        return new EncryptedPackage(AESUtil.encrypt(clientId.getBytes(Charsets.US_ASCII), secret));
    }

    private Observable<Void> saveBackup(EncryptedPackage data, Database database) {
        return new ObservableWrapper<Void>(Observable.create(subscriber -> {
            try {
                byte[] decrypted = AESUtil.decrypt(data.getData(), database.getClientSecret());
                Map<Short, Password> backup = gson.fromJson(new String(decrypted, Charsets.US_ASCII), new TypeToken<Map<Short, byte[]>>() {
                }.getType());

                for (Map.Entry<Short, Password> entry : backup.entrySet()) {
                    byte[] part1 = ProtocolKeysProvider.generateSecureBytes(128);

                }

                subscriber.onNext(null);
            } catch (Exception e) {
                subscriber.onError(e);
            }
        })).wrap();
    }
}
