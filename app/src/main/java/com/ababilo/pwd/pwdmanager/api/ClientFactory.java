package com.ababilo.pwd.pwdmanager.api;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

/**
 * Created by ababilo on 16.11.16.
 */

public class ClientFactory {

    private Retrofit retrofit;

    public Retrofit getClient(String baseUrl, Gson gson) {
        if (null == retrofit) {
//            Interceptor interceptor = chain -> {
//                Request request = chain.request();
//                AccountManager am = AccountManager.get(context);
//                Account account = AccountUtils.getAccount(am);
//                if (null == account) {
//                    return chain.proceed(request);
//                }
//                String ticket = am.peekAuthToken(account, CjuAccount.ACCOUNT_TYPE);
//                if (!TextUtils.isEmpty(ticket)) {
//                    request = request.newBuilder().addHeader(CustomHeaders.TICKET, ticket).build();
//                }
//                return chain.proceed(request);
//            };

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            //builder.interceptors().add(interceptor);
            OkHttpClient client = builder.build();
            RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(rxAdapter)
                    .build();
        }
        return retrofit;
    }
}
