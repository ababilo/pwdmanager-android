package com.ababilo.pwd.pwdmanager;

import android.content.Context;

import com.ababilo.pwd.pwdmanager.api.ClientFactory;
import com.ababilo.pwd.pwdmanager.api.TrustedServiceClient;
import com.ababilo.pwd.pwdmanager.service.DatabaseManager;
import com.ababilo.pwd.pwdmanager.service.DatabaseManagerImpl;
import com.ababilo.pwd.pwdmanager.service.bluetooth.BluetoothManager;
import com.ababilo.pwd.pwdmanager.service.protocol.OnResponseReceived;
import com.ababilo.pwd.pwdmanager.service.protocol.Protocol;
import com.ababilo.pwd.pwdmanager.service.protocol.ProtocolKeysProvider;
import com.ababilo.pwd.pwdmanager.service.protocol.ProtocolObserver;
import com.ababilo.pwd.pwdmanager.service.protocol.ProtocolService;
import com.ababilo.pwd.pwdmanager.service.protocol.ProtocolServiceImpl;
import com.ababilo.pwd.pwdmanager.util.ByteArrayBase64Adapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ababilo on 12.11.16.
 */

@Module
public class PresenterModule {

    private Context context;

    public PresenterModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    Context context() {
        return context;
    }

    @Provides
    @Singleton
    DatabaseManager databaseManager(Gson gson, ProtocolKeysProvider keysProvider) {
        return new DatabaseManagerImpl(gson, keysProvider);
    }

    @Provides
    @Singleton
    ProtocolKeysProvider keysProvider() {
        return new ProtocolKeysProvider();
    }

    @Provides
    @Singleton
    BluetoothManager bluetoothManager() {
        return new BluetoothManager();
    }

    @Provides
    @Singleton
    Protocol protocol(ProtocolKeysProvider keysProvider) {
        return new Protocol(keysProvider);
    }

    @Provides
    @Singleton
    ProtocolService protocolService(BluetoothManager bluetoothManager, Protocol protocol) {
        return new ProtocolServiceImpl(bluetoothManager, protocol);
    }

    @Provides
    @Singleton
    ClientFactory clientFactory() {
        return new ClientFactory();
    }

    @Provides
    @Singleton
    TrustedServiceClient trustedServiceClient(ClientFactory factory, Context context) {
        return new TrustedServiceClient(factory, context);
    }

    @Provides
    @Singleton
    Gson gson() {
        return new GsonBuilder()
                .registerTypeHierarchyAdapter(byte[].class, new ByteArrayBase64Adapter())
                .create();
    }

    @Provides
    @Singleton
    OnResponseReceived observer(DatabaseManager databaseManager, ProtocolKeysProvider keysProvider, Gson gson, TrustedServiceClient client) {
        return new ProtocolObserver(databaseManager, keysProvider, gson, client);
    }
}
