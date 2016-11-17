package com.ababilo.pwd.pwdmanager.service.protocol;

import android.util.Log;

import com.ababilo.pwd.pwdmanager.model.Commands;
import com.ababilo.pwd.pwdmanager.model.Message;
import com.ababilo.pwd.pwdmanager.model.Password;
import com.ababilo.pwd.pwdmanager.model.Responses;
import com.ababilo.pwd.pwdmanager.service.bluetooth.BluetoothManager;
import com.ababilo.pwd.pwdmanager.service.bluetooth.BluetoothObserver;
import com.ababilo.pwd.pwdmanager.util.ObservableWrapper;

import java.util.Arrays;
import java.util.List;

import rx.Observable;

/**
 * Created by ababilo on 14.11.16.
 */

public class ProtocolServiceImpl implements ProtocolService {

    private BluetoothManager bluetoothManager;
    private Protocol protocol;

    public ProtocolServiceImpl(BluetoothManager bluetoothManager,
                               Protocol protocol) {
        this.bluetoothManager = bluetoothManager;
        this.protocol = protocol;
    }

    @Override
    public Observable<Void> sendPing() {
        return new ObservableWrapper<Void>(Observable.create(subscriber -> {
            try {
                Log.i("PROTOCOL", "Started ping");
                ensureConnected();
                bluetoothManager.sendData(Message.getBytes(new Message(Commands.PING)));
                subscriber.onNext(null);
            } catch (Throwable th) {
                Log.e("PROTOCOL", "Ping error", th);
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
                byte[] pack = protocol.sendPassword(password);
                bluetoothManager.sendData(Message.getBytes(new Message(Commands.SEND_PASS, pack)));
                subscriber.onNext(null);
            } catch (Throwable th) {
                subscriber.onError(th);
            }
        })).wrap();
    }

    @Override
    public Observable<Void> connect(String mac, OnResponseReceived observer) {
        return new ObservableWrapper<Void>(Observable.create(subscriber -> {
            try {
                Log.i("PROTOCOL", "Started connect");
                if (!bluetoothManager.isConnected()) {
                    connectInternal(mac, observer);
                }
                ensureConnected();
                subscriber.onNext(null);
            } catch (Throwable th) {
                Log.e("PROTOCOL", "Connect error", th);
                subscriber.onError(th);
            }
        })).wrap();
    }

    @Override
    public Observable<Password> addPassword(short id, String title, String password) {
        return new ObservableWrapper<Password>(Observable.create(subscriber -> {
            try {
                ensureConnected();
                byte[] part1 = ProtocolKeysProvider.generateSecureBytes(128);
                byte[] pack = protocol.addPassword(id, title, password, part1);
                bluetoothManager.sendData(Message.getBytes(new Message(Commands.ADD_PASS, pack)));
                subscriber.onNext(new Password(id, title, part1));
            } catch (Throwable th) {
                subscriber.onError(th);
            }
        })).wrap();
    }

    @Override
    public Observable<Void> requestBackup(String clientId) {
        return new ObservableWrapper<Void>(Observable.create(subscriber -> {
            try {
                Log.d("PROTOCOL", "Backup request started");
                ensureConnected();
                byte[] pack = protocol.requestBackup(clientId);
                bluetoothManager.sendData(Message.getBytes(new Message(Commands.REQUEST_BACKUP, pack)));
                Log.i("PROTOCOL", "REQUESTED BACKUP " + Arrays.toString(pack));
                subscriber.onNext(null);
            } catch (Throwable th) {
                Log.e("PROTOCOL", "Request backup error", th);
                subscriber.onError(th);
            }
        })).wrap();
    }

    @Override
    public Observable<Void> sendBackup(List<Password> passwords) {
        return new ObservableWrapper<Void>(Observable.create(subscriber -> {
            try {
                Log.d("PROTOCOL", "Backup send started");
                ensureConnected();
                byte[] pack = protocol.sendBackup(passwords);
                bluetoothManager.sendData(Message.getBytes(new Message(Commands.SEND_BACKUP, pack)));
                Log.i("PROTOCOL", "SENT BACKUP " + Arrays.toString(pack));
                subscriber.onNext(null);
            } catch (Throwable th) {
                Log.e("PROTOCOL", "Send backup error", th);
                subscriber.onError(th);
            }
        })).wrap();
    }

    private void connectInternal(String mac, OnResponseReceived observer) {
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
                Log.i("BT", "RECEIVED " + Arrays.toString(data));
                if (null != data && data.length > 0) {
                    switch (Responses.fromByte(data[0])) {
                        case PONG: observer.onPongReceived(); break;
                        case RESPONSE: observer.onResponseReceived(data); break;
                        case BACKUP_RECEIVED: observer.onBackupReceived(data); break;
                        case BACKUP_SENT: observer.onBackupSent(); break;
                        case UNKNOWN: observer.onUnknownReceived(); break;
                    }
                }
            }
        });
    }
}
