package com.ababilo.pwd.pwdmanager.service.bluetooth;

/**
 * Created by ababilo on 14.11.16.
 */

public interface BluetoothObserver {

    void onConnectError(Throwable th);
    void onReceiveError(Throwable th);

    void onDataReceive(byte[] data);
}
