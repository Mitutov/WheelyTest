package com.mitutov.wheelytest.ui.presenter;

import android.content.SharedPreferences;

import com.mitutov.wheelytest.common.bus.EventBus;

import rx.Subscription;

/**
 * Created by Alexey Mitutov on 18.07.16.
 *
 */
public interface LoginActivityPresenter {
    Subscription onCreate(SharedPreferences preferences, EventBus eventBus);
    void onConnectBtnClick(String user, String passwd);
}
