package com.ababilo.pwd.pwdmanager.service.protocol;

import com.ababilo.pwd.pwdmanager.model.Password;

import java.util.List;

import rx.Observable;

/**
 * Created by ababilo on 14.11.16.
 */
public interface ProtocolService {

    Observable<Void> sendPing();
    Observable<Void> sendPassword(Password password);
    Observable<Void> connect(String mac, OnResponseReceived listener);
    Observable<Password> addPassword(short id, String title, String password);
    Observable<Void> requestBackup(String clientId);
    Observable<Void> sendBackup(List<Password> passwords);
}
