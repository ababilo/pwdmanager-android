package com.ababilo.pwd.pwdmanager.service.protocol;

import android.util.Log;

import com.ababilo.pwd.pwdmanager.model.Commands;
import com.ababilo.pwd.pwdmanager.model.Message;
import com.ababilo.pwd.pwdmanager.model.Password;
import com.ababilo.pwd.pwdmanager.model.Responses;
import com.ababilo.pwd.pwdmanager.service.ProtocolKeysProvider;
import com.ababilo.pwd.pwdmanager.service.bluetooth.BluetoothManager;
import com.ababilo.pwd.pwdmanager.service.bluetooth.BluetoothObserver;
import com.ababilo.pwd.pwdmanager.util.ObservableWrapper;

import rx.Observable;

/**
 * Created by ababilo on 14.11.16.
 */

public class ProtocolServiceImpl implements ProtocolService {

    private BluetoothManager bluetoothManager;
    private ProtocolKeysProvider keysProvider;

    public ProtocolServiceImpl(BluetoothManager bluetoothManager,
                               ProtocolKeysProvider keysProvider) {
        this.bluetoothManager = bluetoothManager;
        this.keysProvider = keysProvider;
    }

    @Override
    public Observable<Void> sendPing() {
        return new ObservableWrapper<Void>(Observable.create(subscriber -> {
            try {
                ensureConnected();
                bluetoothManager.sendData(Message.getBytes(new Message(Commands.PING)));
                subscriber.onCompleted();
            } catch (Throwable th) {
                subscriber.onError(th);
            }
        })).wrap();
    }

    private void ensureConnected() {
        if (!bluetoothManager.isConnected()) {
            throw new IllegalStateException("BT must be connected");
        }
    }

    @Override
    public Observable<Void> sendPassword(Password password) {
        return new ObservableWrapper<Void>(Observable.create(subscriber -> {
            try {
                ensureConnected();
                //bluetoothManager.sendData(Message.getBytes(new Message(Commands.PING)));
                subscriber.onCompleted();
            } catch (Throwable th) {
                subscriber.onError(th);
            }
        })).wrap();
    }

    @Override
    public Observable<Void> connect(String mac, OnResponseReceived listener) {
        return new ObservableWrapper<Void>(Observable.create(subscriber -> {
            try {
                if (!bluetoothManager.isConnected()) {
                    connectInternal(mac, listener);
                }
                subscriber.onCompleted();
            } catch (Throwable th) {
                subscriber.onError(th);
            }
        })).wrap();
    }

    private void connectInternal(String mac, OnResponseReceived listener) {
        bluetoothManager.connect(mac, new BluetoothObserver() {
            @Override
            public void onConnectError(Throwable th) {
                Log.e("BT", "Can't connect", th);
            }

            @Override
            public void onReceiveError(Throwable th) {
                Log.e("BT", "Can't receive", th);
            }

            @Override
            public void onDataReceive(byte[] data) {
                if (null != data && data.length > 0) {
                    switch (Responses.fromByte(data[0])) {
                        case PONG: listener.onPongReceived(); break;
                        case RESPONSE: listener.onResponseReceived(); break;
                        case UNKNOWN: listener.onUnknownReceived(); break;
                    }
                }
            }
        });
    }
}
