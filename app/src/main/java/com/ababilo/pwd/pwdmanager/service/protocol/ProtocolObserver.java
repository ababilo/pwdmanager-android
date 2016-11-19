package com.ababilo.pwd.pwdmanager.service.protocol;

import android.util.Log;

import com.ababilo.pwd.pwdmanager.service.BackupService;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by ababilo on 16.11.16.
 */

public class ProtocolObserver implements OnResponseReceived {

    private final ProtocolKeysProvider keysProvider;
    private final BackupService backupService;

    private BlockingQueue<Observable<Void>> queue = new ArrayBlockingQueue<>(10, true);

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
        notifyCallerActivity();
    }

    @Override
    public void onResponseReceived(byte[] data) {
        // todo process
        rollKeys();
        Log.i("BT Response", Arrays.toString(data));
        notifyCallerActivity();
    }

    @Override
    public void onUnknownReceived() {
        Log.w("DEVICE", "Unknown response");
        notifyCallerActivity();
    }

    @Override
    public void onBackupReceived(byte[] data) {
        backupService.createBackup(data).subscribe(none -> {
            rollKeys();
            notifyCallerActivity();
        }, throwable -> {
            Log.e("OBSERVER", "Error creating backup", throwable);
            notifyCallerActivity();
        });
    }

    @Override
    public void onBackupSent() {
        Log.i("OBSERVER", "Backup sent");
        //rollKeys();
        notifyCallerActivity();
    }

    @Override
    public void putCompetitionCallback(OnCompete callback, OnError handler) {
        Log.i("OBSERVER", "Added completion callback " + callback);
        queue.add(Observable.<Void>create(subscriber -> {
            try {
                callback.call();
                Log.i("OBSERVER", "Ran completion callback " + callback);
                subscriber.onNext(null);
            } catch (Throwable th) {
                handler.handle(th);
                subscriber.onError(th);
            }
        }).subscribeOn(AndroidSchedulers.mainThread()) // operate in ui thread only
                .observeOn(AndroidSchedulers.mainThread()));
    }

    private void notifyCallerActivity() {
        Observable<Void> observable;
        while (null != (observable = queue.poll())) {
            observable.subscribe(none -> {}, th -> Log.e("OBSERVER", "Error in completion callback", th));
        }
    }
}
