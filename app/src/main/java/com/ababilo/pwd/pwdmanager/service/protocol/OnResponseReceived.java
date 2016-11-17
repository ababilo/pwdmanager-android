package com.ababilo.pwd.pwdmanager.service.protocol;

/**
 * Created by ababilo on 14.11.16.
 */

public interface OnResponseReceived {

    interface OnCompete {
        void call() throws Throwable;
    }
    interface OnError {
        void handle(Throwable th);
    }

    void onPongReceived();
    void onResponseReceived(byte[] data);
    void onUnknownReceived();
    void onBackupReceived(byte[] data);
    void onBackupSent();
    void putCompetitionCallback(OnCompete callback, OnError handler);
}
