package com.ababilo.pwd.pwdmanager.service;

import android.text.TextUtils;
import android.util.Log;

import com.ababilo.pwd.pwdmanager.model.Database;
import com.ababilo.pwd.pwdmanager.service.protocol.ProtocolKeysProvider;
import com.ababilo.pwd.pwdmanager.util.AESUtil;
import com.ababilo.pwd.pwdmanager.util.HashUtils;
import com.ababilo.pwd.pwdmanager.util.ObservableWrapper;
import com.google.gson.Gson;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.UUID;

import rx.Observable;

/**
 * Created by ababilo on 14.11.16.
 */
public class DatabaseManagerImpl implements DatabaseManager {

    private static final String DEFAULT_PATH = "database";
    private final Gson gson;
    private final ProtocolKeysProvider keysProvider;
    private byte[] password;
    private String path;
    private Database cached = new Database(UUID.randomUUID().toString(), new byte[128], new byte[32], new byte[32], Collections.emptyList()); // todo remove

    public DatabaseManagerImpl(Gson gson, ProtocolKeysProvider keysProvider) {
        this.gson = gson;
        this.keysProvider = keysProvider;
    }

    @Override
    public Observable<Database> loadDatabase(String path, String password) {
        return new ObservableWrapper<Database>(Observable.<byte[]>create(subscriber ->
                subscriber.onNext(HashUtils.PBKDF2(password.toCharArray(), SALT.getBytes(Charsets.US_ASCII))))
                .flatMap(pwd -> loadDatabase(path, pwd))).wrap();
    }

    private Observable<Database> loadDatabase(String path, byte[] password) {
        Observable<Database> memory = Observable.just(cached);
        Observable<Database> file = Observable.<Database>create(subscriber -> {
            try {
                Log.i("DB", "LOADED FROM FILE");
                this.password = null == this.password ? password : this.password;
                this.path = null == this.path ? (TextUtils.isEmpty(path) ? DEFAULT_PATH : path) : path;
                byte[] data = IOUtils.toByteArray(new FileInputStream(this.path));
                byte[] decrypted = AESUtil.decrypt(data, this.password);

                Database database = gson.fromJson(new String(decrypted, Charsets.US_ASCII), Database.class);
                subscriber.onNext(database);
            } catch (Exception e) {
                subscriber.onError(e);
            }
        }).doOnNext(database -> cached = database);

        return new ObservableWrapper<>(Observable.concat(memory, file).first(database -> null != database)
                .doOnNext(database -> keysProvider.loadKeys(database.getBtKey(), database.getHbtKey()))).wrap();
    }

    @Override
    public Observable<Database> getDatabase() {
        Log.i("DB", "LOADED FROM MEMORY");
        return loadDatabase(this.path, this.password);
    }

    @Override
    public Observable<Void> flushDatabase(Database database) {
        return new ObservableWrapper<Void>(Observable.create(subscriber -> {
            try {
                String json = gson.toJson(database);
                byte[] encrypted = AESUtil.encrypt(json.getBytes(Charsets.US_ASCII), password);
                try (OutputStream out = new FileOutputStream(this.path)) {
                    IOUtils.write(encrypted, out);
                    out.flush();
                }
            } catch (Exception e) {
                subscriber.onError(e);
            }
        })).wrap().doOnNext(none -> cached = null);
    }
}
