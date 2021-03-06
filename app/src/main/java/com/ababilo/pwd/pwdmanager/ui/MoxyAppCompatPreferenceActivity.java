package com.ababilo.pwd.pwdmanager.ui;

import android.os.Bundle;

import com.ababilo.pwd.pwdmanager.core.presenter.BasePresenter;
import com.arellomobile.mvp.MvpDelegate;

/**
 * Created by ababilo on 14.11.16.
 */

public abstract class MoxyAppCompatPreferenceActivity extends AppCompatPreferenceActivity {

    private MvpDelegate<? extends MoxyAppCompatPreferenceActivity> mMvpDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMvpDelegate().onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getMvpDelegate().onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getMvpDelegate().onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getMvpDelegate().onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        getMvpDelegate().onStop();
    }

    public MvpDelegate getMvpDelegate() {
        if (mMvpDelegate == null) {
            mMvpDelegate = new MvpDelegate<>(this);
        }
        return mMvpDelegate;
    }

    protected abstract void attachInjector();
    protected abstract BasePresenter getPresenter();
}
