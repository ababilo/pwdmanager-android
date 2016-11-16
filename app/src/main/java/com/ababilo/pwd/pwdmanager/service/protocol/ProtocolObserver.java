package com.ababilo.pwd.pwdmanager.service.protocol;

import android.util.Log;

import com.ababilo.pwd.pwdmanager.api.TrustedServiceClient;
import com.ababilo.pwd.pwdmanager.api.model.Confirmation;
import com.ababilo.pwd.pwdmanager.api.model.EncryptedPackage;
import com.ababilo.pwd.pwdmanager.model.Database;
import com.ababilo.pwd.pwdmanager.model.Password;
import com.ababilo.pwd.pwdmanager.service.DatabaseManager;
import com.ababilo.pwd.pwdmanager.util.AESUtil;
import com.ababilo.pwd.pwdmanager.util.ArrayUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.Charsets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by ababilo on 16.11.16.
 */

public class ProtocolObserver implements OnResponseReceived {

    private final DatabaseManager databaseManager;
    private final ProtocolKeysProvider keysProvider;
    private final Gson gson;
    private final TrustedServiceClient client;

    public ProtocolObserver(DatabaseManager databaseManager,
                            ProtocolKeysProvider keysProvider,
                            Gson gson,
                            TrustedServiceClient client) {
        this.databaseManager = databaseManager;
        this.keysProvider = keysProvider;
        this.gson = gson;
        this.client = client;
    }

    @Override
    public void onPongReceived() {
    }

    @Override
    public void onResponseReceived(byte[] data) {
        // todo process
        Log.i("BT Response", Arrays.toString(data));
    }

    @Override
    public void onUnknownReceived() {
        Log.w("DEVICE", "Unknown response");
    }

    @Override
    public void onBackupReceived(byte[] data) {
        try {
            byte[] decrypted = AESUtil.decrypt(data, keysProvider.getCurrentBTKey());
            Map<Short, byte[]> remoteDb = gson.fromJson(new String(decrypted, Charsets.US_ASCII), new TypeToken<Map<Short, byte[]>>(){}.getType());

            databaseManager.getDatabase()
                    .flatMap(database -> createBackup(database, remoteDb))
                    .subscribe(
                            confirmation -> Log.i("PROTOCOL", "Confirmation received: " + Arrays.toString(confirmation.getSignature())),
                            throwable -> Log.e("PROTOCOL", "Error sending backup", throwable)
                    );
        } catch (Exception e) {
            Log.e("PROTOCOL", "Error while backup", e);
        }
    }

    private Observable<Confirmation> createBackup(Database localDb, Map<Short, byte[]> remoteDb) {
        List<Password> toBackup = new ArrayList<>(localDb.getPasswords().size());
        for (Map.Entry<Short, byte[]> entry : remoteDb.entrySet()) {
            Password password = getPassword(localDb, entry.getKey());
            if (null != password) {
                toBackup.add(new Password(entry.getKey(), password.getTitle(), buildPassword(entry.getValue(), password.getPart())));
            }
        }
        String json = gson.toJson(toBackup);
        EncryptedPackage request = new EncryptedPackage(AESUtil.encrypt(json.getBytes(Charsets.UTF_8), localDb.getClientSecret()));
        return client.api().createBackup(request);
    }

    private Password getPassword(Database database, short id) {
        for (Password password : database.getPasswords()) {
            if (id == password.getId()) {
                return password;
            }
        }

        return null;
    }

    private byte[] buildPassword(byte[] part1, byte[] part2) {
        if (part1.length != part2.length) {
            throw new IllegalArgumentException("Different lengths!");
        }

        byte[] password = new byte[part1.length];
        for (int i = 0; i < password.length; i++) {
            password[i] = (byte) (part1[i] ^ part2[i]);
        }

        return ArrayUtils.truncateZeros(password);
    }
}
