package com.ababilo.pwd.pwdmanager.service;

import com.ababilo.pwd.pwdmanager.model.Database;
import com.ababilo.pwd.pwdmanager.model.Password;

import java.util.List;

import rx.Observable;

/**
 * Created by ababilo on 16.11.16.
 */

public interface BackupService {

    interface OnBackupRestored {
        void call(List<Password> passwords);
    }
    interface OnBackupReceived {
        void onCompleted();
        void onError(Throwable th);
    }

    Observable<Void> createBackup(byte[] data, OnBackupReceived callback);
    Observable<Void> restoreBackup(Database database, OnBackupRestored onBackupRestored);
}
