package com.ababilo.pwd.pwdmanager.api;

import android.content.Context;

import com.ababilo.pwd.pwdmanager.R;

import retrofit2.Retrofit;

/**
 * Created by ababilo on 16.11.16.
 */

public class TrustedServiceClient {

    private TrustedServiceApi api;

    public TrustedServiceClient(ClientFactory factory, Context context) {
        Retrofit client = factory.getClient(context.getString(R.string.service_endpoint), context);
        this.api = client.create(TrustedServiceApi.class);
    }

    public TrustedServiceApi api() {
        return api;
    }
}
