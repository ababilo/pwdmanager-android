package com.ababilo.pwd.pwdmanager.core.view;

import com.ababilo.pwd.pwdmanager.model.Password;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

/**
 * Created by ababilo on 15.11.16.
 */

@StateStrategyType(AddToEndSingleStrategy.class)
public interface AddPasswordView extends BaseView {

    void onSuccessful(Password password);
    void onError();
}
