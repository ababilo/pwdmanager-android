package com.ababilo.pwd.pwdmanager.core.view;

import com.ababilo.pwd.pwdmanager.model.Database;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

/**
 * Created by ababilo on 14.11.16.
 */
@StateStrategyType(AddToEndSingleStrategy.class)
public interface SplashView extends BaseView {

    void onSavedPassword(Database database);
    void onPasswordNotFound();
    void onPathEmpty();
}
