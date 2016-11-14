package com.ababilo.pwd.pwdmanager.service;

import android.text.TextUtils;

import com.ababilo.pwd.pwdmanager.model.Database;
import com.ababilo.pwd.pwdmanager.util.AESUtil;
import com.ababilo.pwd.pwdmanager.util.HashUtils;
import com.ababilo.pwd.pwdmanager.util.ObservableWrapper;
import com.google.gson.Gson;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import dagger.internal.Preconditions;
import rx.Observable;

/**
 * Created by ababilo on 14.11.16.
 */
public class DatabaseManagerImpl implements DatabaseManager {

    private static final String SALT = "sbs-101-o";
    private static final String DEFAULT_PATH = "database";
    private static final Gson GSON = new Gson();

    ProtocolKeysProvider keysProvider;

    public DatabaseManagerImpl(ProtocolKeysProvider keysProvider) {
        this.keysProvider = keysProvider;
    }

    @Override
    public Observable<Database> loadDatabase(String path, String password) {
        return new ObservableWrapper<Database>(Observable.create(subscriber -> {
            try {
                byte[] data = IOUtils.toByteArray(new FileInputStream(TextUtils.isEmpty(path) ? DEFAULT_PATH : path));
                byte[] decrypted = AESUtil.decrypt(data, getHashedPassword(password));

                Preconditions.checkNotNull(decrypted);
                Database database = GSON.fromJson(new String(decrypted, Charsets.UTF_8), Database.class);
                subscriber.onNext(database);
            } catch (Exception e) {
                subscriber.onError(e);
            }
        })).wrap();
    }

    private byte[] getHashedPassword(String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return HashUtils.PBKDF2(password.toCharArray(), SALT.getBytes(Charsets.UTF_8));
    }
}
