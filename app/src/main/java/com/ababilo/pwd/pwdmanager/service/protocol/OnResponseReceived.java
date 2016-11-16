package com.ababilo.pwd.pwdmanager.service.protocol;

/**
 * Created by ababilo on 14.11.16.
 */

public interface OnResponseReceived {

    void onPongReceived();
    void onResponseReceived(byte[] data);
    void onUnknownReceived();
    void onBackupReceived(byte[] data);
}
