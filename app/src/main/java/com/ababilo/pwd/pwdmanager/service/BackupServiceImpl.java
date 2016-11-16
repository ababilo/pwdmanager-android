package com.ababilo.pwd.pwdmanager.service;

import android.util.Log;

import com.ababilo.pwd.pwdmanager.api.TrustedServiceClient;
import com.ababilo.pwd.pwdmanager.api.model.Confirmation;
import com.ababilo.pwd.pwdmanager.api.model.EncryptedPackage;
import com.ababilo.pwd.pwdmanager.model.Database;
import com.ababilo.pwd.pwdmanager.model.Password;
import com.ababilo.pwd.pwdmanager.service.protocol.ProtocolKeysProvider;
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
public class BackupServiceImpl implements BackupService {

    private final DatabaseManager databaseManager;
    private final ProtocolKeysProvider keysProvider;
    private final Gson gson;
    private final TrustedServiceClient client;

    public BackupServiceImpl(DatabaseManager databaseManager,
                             ProtocolKeysProvider keysProvider,
                             Gson gson,
                             TrustedServiceClient client) {
        this.databaseManager = databaseManager;
        this.keysProvider = keysProvider;
        this.gson = gson;
        this.client = client;
    }

    private void rollKeys() {
        keysProvider.rollKeys();
    }

    @Override
    public void createBackup(byte[] data) {
        try {
            byte[] decrypted = AESUtil.decrypt(data, keysProvider.getCurrentBTKey());
            Map<Short, byte[]> remoteDb = gson.fromJson(new String(decrypted, Charsets.US_ASCII), new TypeToken<Map<Short, byte[]>>(){}.getType());

            databaseManager.getDatabase()
                    .flatMap(database -> createBackup(database, remoteDb))
                    .subscribe(
                            confirmation -> {
                                rollKeys();
                                Log.i("PROTOCOL", "Confirmation received: " + Arrays.toString(confirmation.getSignature()));
                            },
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

    @Override
    public Observable<Void> restoreBackup(Database database, OnBackupRestored onBackupRestored) {
        return client.api().getBackup(createRestoreRequest(database.getClientId(), database.getClientSecret()))
                .flatMap(encryptedPackage -> Observable.just(rebuildDatabase(database, encryptedPackage, onBackupRestored)))
                .flatMap(databaseManager::flushDatabase);
    }

    private Database rebuildDatabase(Database local, EncryptedPackage pack, OnBackupRestored onBackupRestored) {
        Log.i("BACKUP", "Started database rebuild");
        byte[] decrypted = AESUtil.decrypt(pack.getData(), local.getClientSecret());
        Database backup = gson.fromJson(new String(decrypted, Charsets.US_ASCII), Database.class);


        List<Password> toDevice = new ArrayList<>(backup.getPasswords().size());
        List<Password> toLocal = new ArrayList<>(backup.getPasswords().size());
        for (Password entry : backup.getPasswords()) {
            byte[] randomPart = ProtocolKeysProvider.generateSecureBytes(128);

            byte[] part2 = new byte[128];
            byte[] passwordBytes = entry.getPart();
            for (int i = 0; i < passwordBytes.length; i++) {
                part2[i] = (byte) (passwordBytes[i] ^ randomPart[i]);
            }

            toDevice.add(new Password(entry.getId(), entry.getTitle(), part2));
            toLocal.add(new Password(entry.getId(), entry.getTitle(), randomPart));
        }

        onBackupRestored.call(toDevice);
        return new Database(backup.getClientId(), backup.getClientSecret(), backup.getBtKey(), backup.getHbtKey(), toLocal);
    }

    private EncryptedPackage createRestoreRequest(String clientId, byte[] secret) {
        Log.i("BACKUP", "Started backup restore");
        return new EncryptedPackage(AESUtil.encrypt(clientId.getBytes(Charsets.US_ASCII), secret));
    }
}