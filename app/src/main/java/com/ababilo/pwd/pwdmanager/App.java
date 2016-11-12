package com.ababilo.pwd.pwdmanager;

import android.util.Log;

import com.arellomobile.mvp.MvpApplication;

/**
 * Created by ababilo on 12.11.16.
 */
public class App extends MvpApplication {

    private static ViewComponent viewComponent;
    private static PresenterComponent presenterComponent;

    public static ViewComponent getViewComponent() {
        return viewComponent;
    }
    public static PresenterComponent getPresenterComponent() {
        return presenterComponent;
    }

    public App() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("APP", "onCreateApp");
        viewComponent = buildViewComponent();
        presenterComponent = buildPresenterComponent();
    }

    protected ViewComponent buildViewComponent() {
        return DaggerViewComponent.builder()
                .viewModule(new ViewModule(getApplicationContext()))
                .build();
    }

    protected PresenterComponent buildPresenterComponent() {
        return DaggerPresenterComponent.builder()
                .presenterModule(new PresenterModule(getApplicationContext()))
                .build();
    }
}
