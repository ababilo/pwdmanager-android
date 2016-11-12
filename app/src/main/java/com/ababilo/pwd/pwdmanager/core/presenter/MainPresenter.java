package com.ababilo.pwd.pwdmanager.core.presenter;

import com.ababilo.pwd.pwdmanager.App;
import com.ababilo.pwd.pwdmanager.core.view.MainView;
import com.arellomobile.mvp.InjectViewState;

/**
 * Created by ababilo on 12.11.16.
 */

@InjectViewState
public class MainPresenter extends BasePresenter<MainView> {

    public MainPresenter() {
        App.getPresenterComponent().inject(this);
    }
}
