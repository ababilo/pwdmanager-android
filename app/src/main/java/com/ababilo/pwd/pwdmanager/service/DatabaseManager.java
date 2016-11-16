package com.ababilo.pwd.pwdmanager.service;

import com.ababilo.pwd.pwdmanager.model.Database;

import rx.Observable;

/**
 * Created by ababilo on 14.11.16.
 */

public interface DatabaseManager {

    String SALT = "sbs-101-o";

    Observable<Database> loadDatabase(String path, String password);
    Observable<Database> getDatabase();
    Observable<Void> flushDatabase(Database database);
}
