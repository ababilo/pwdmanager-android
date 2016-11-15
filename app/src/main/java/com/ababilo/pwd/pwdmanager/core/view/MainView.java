package com.ababilo.pwd.pwdmanager.core.view;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

/**
 * Created by ababilo on 12.11.16.
 */

@StateStrategyType(AddToEndSingleStrategy.class)
public interface MainView extends BaseView {

    void startSending();
    void onDeviceConnected();
    void onDeviceError();
    void onDeviceNotConnected();
}
