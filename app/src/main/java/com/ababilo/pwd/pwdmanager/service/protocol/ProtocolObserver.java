package com.ababilo.pwd.pwdmanager.service.protocol;

import android.util.Log;

import com.ababilo.pwd.pwdmanager.service.BackupService;

import java.util.Arrays;

/**
 * Created by ababilo on 16.11.16.
 */

public class ProtocolObserver implements OnResponseReceived {

    private final ProtocolKeysProvider keysProvider;
    private final BackupService backupService;

    public ProtocolObserver(ProtocolKeysProvider keysProvider,
                            BackupService backupService) {
        this.keysProvider = keysProvider;
        this.backupService = backupService;
    }

    private void rollKeys() {
        keysProvider.rollKeys();
    }

    @Override
    public void onPongReceived() {
    }

    @Override
    public void onResponseReceived(byte[] data) {
        // todo process
        rollKeys();
        Log.i("BT Response", Arrays.toString(data));
    }

    @Override
    public void onUnknownReceived() {
        Log.w("DEVICE", "Unknown response");
    }

    @Override
    public void onBackupReceived(byte[] data) {
        backupService.createBackup(data);
    }
}
