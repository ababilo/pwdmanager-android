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
        Observable<Void> call(List<Password> passwords);
    }

    Observable<Void> createBackup(byte[] data);
    Observable<Void> restoreBackup(Database database, OnBackupRestored onBackupRestored);
}
