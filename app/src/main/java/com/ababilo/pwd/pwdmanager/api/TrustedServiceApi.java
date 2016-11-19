package com.ababilo.pwd.pwdmanager.api;

import com.ababilo.pwd.pwdmanager.api.model.Confirmation;
import com.ababilo.pwd.pwdmanager.api.model.EncryptedPackage;
import com.ababilo.pwd.pwdmanager.api.model.Response;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by ababilo on 16.11.16.
 */

public interface TrustedServiceApi {

    @POST(value = "/backup")
    Observable<Confirmation> createBackup(@Body EncryptedPackage request, @Query("clientId") String clientId);

    @GET(value = "/backup")
    Observable<Response<EncryptedPackage>> getBackup(@Query("clientId") String clientId);
}
