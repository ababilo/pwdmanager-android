package com.ababilo.pwd.pwdmanager;

import android.content.Context;

import com.ababilo.pwd.pwdmanager.service.DatabaseManager;
import com.ababilo.pwd.pwdmanager.service.DatabaseManagerImpl;
import com.ababilo.pwd.pwdmanager.service.bluetooth.BluetoothManager;
import com.ababilo.pwd.pwdmanager.service.protocol.Protocol;
import com.ababilo.pwd.pwdmanager.service.protocol.ProtocolKeysProvider;
import com.ababilo.pwd.pwdmanager.service.protocol.ProtocolService;
import com.ababilo.pwd.pwdmanager.service.protocol.ProtocolServiceImpl;

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
    DatabaseManager databaseManager(ProtocolKeysProvider keysProvider) {
        return new DatabaseManagerImpl(keysProvider);
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
}
