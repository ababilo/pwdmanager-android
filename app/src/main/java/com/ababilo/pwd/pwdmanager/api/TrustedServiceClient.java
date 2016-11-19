package com.ababilo.pwd.pwdmanager.api;

import android.content.Context;

import com.ababilo.pwd.pwdmanager.R;
import com.google.gson.Gson;

import retrofit2.Retrofit;

/**
 * Created by ababilo on 16.11.16.
 */

public class TrustedServiceClient {

    private TrustedServiceApi api;

    public TrustedServiceClient(ClientFactory factory, Gson gson, Context context) {
        Retrofit client = factory.getClient(context.getString(R.string.service_endpoint), gson);
        this.api = client.create(TrustedServiceApi.class);
    }

    public TrustedServiceApi api() {
        return api;
    }
}
