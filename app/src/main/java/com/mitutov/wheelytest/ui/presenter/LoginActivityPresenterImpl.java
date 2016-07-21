package com.mitutov.wheelytest.ui.presenter;

import android.content.SharedPreferences;

import com.mitutov.wheelytest.common.WheelyConfig;
import com.mitutov.wheelytest.common.bus.EventBus;
import com.mitutov.wheelytest.common.bus.OnAuthorizedEvent;
import com.mitutov.wheelytest.common.bus.OnForbiddenEvent;
import com.mitutov.wheelytest.ui.view.LoginView;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Alexey Mitutov on 18.07.16.
 *
 */
public class LoginActivityPresenterImpl implements LoginActivityPresenter {

    private LoginView view;

    @Inject
    public LoginActivityPresenterImpl(LoginView view) {
        this.view = view;
    }

    @Override
    public Subscription onCreate(SharedPreferences preferences, EventBus eventBus) {
        if (preferences.getBoolean(WheelyConfig.PREF_IS_LOGIN, false)) {
            view.startMapActivity();
        }
        return eventBus.observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {
                    if (event instanceof OnForbiddenEvent) {
                        view.showForbiddenMessage();
                    } else if (event instanceof OnAuthorizedEvent) {
                        view.startMapActivity();
                    }
                });
    }

    @Override
    public void onConnectBtnClick(String user, String passwd) {
        view.startWheelyService(user, passwd);
    }
}
