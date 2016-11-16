package com.ababilo.pwd.pwdmanager.api;

import com.ababilo.pwd.pwdmanager.api.model.Confirmation;
import com.ababilo.pwd.pwdmanager.api.model.EncryptedPackage;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by ababilo on 16.11.16.
 */

public interface TrustedServiceApi {

    @POST(value = "/backup/create")
    Observable<Confirmation> createBackup(@Body EncryptedPackage request);

    @POST(value = "/backup/restore")
    Observable<EncryptedPackage> getBackup(@Body EncryptedPackage request);
}
