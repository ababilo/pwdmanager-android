package com.ababilo.pwd.pwdmanager.core.view;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

/**
 * Created by ababilo on 16.11.16.
 */

@StateStrategyType(AddToEndSingleStrategy.class)
public interface SettingsView extends BaseView {
    void onRequestStarted();
    void onRequestAccepted();
    void onRequestError();
}
