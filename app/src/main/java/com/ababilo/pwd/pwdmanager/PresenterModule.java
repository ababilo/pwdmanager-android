package com.ababilo.pwd.pwdmanager;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ababilo on 12.11.16.
 */

@Module
public class PresenterModule {

    private Context context;

    public PresenterModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    Context context() {
        return context;
    }
}