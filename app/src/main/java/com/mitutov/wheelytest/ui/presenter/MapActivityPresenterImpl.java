package com.mitutov.wheelytest.ui.presenter;

import android.content.SharedPreferences;
import android.util.Log;

import com.mitutov.wheelytest.common.WheelyConfig;
import com.mitutov.wheelytest.common.bus.EventBus;
import com.mitutov.wheelytest.common.bus.OnMessageEvent;
import com.mitutov.wheelytest.common.bus.StopServiceEvent;
import com.mitutov.wheelytest.ui.model.WheelyLocation;
import com.mitutov.wheelytest.ui.view.MapView;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Alexey Mitutov on 18.07.16.
 *
 */
public class MapActivityPresenterImpl implements MapActivityPresenter{

    private MapView view;

    @Inject
    public MapActivityPresenterImpl(MapView view) {
        this.view = view;
    }

    @Override
    public Subscription onCreate(SharedPreferences preferences, EventBus eventBus) {

        preferences.edit().putBoolean(WheelyConfig.PREF_IS_LOGIN, true).apply();

        return eventBus.observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {
                    if (event instanceof OnMessageEvent) {
                        OnMessageEvent onMessageEvent = (OnMessageEvent) event;
                        ArrayList<WheelyLocation> locations = onMessageEvent.getWheelyLocations();

                        for (WheelyLocation location : locations) {
                            Log.i("Location: ", location.toString());
                        }

                        view.updateMap(locations);
                    }
                });
    }

    @Override
    public void onDisconnectBtnClick(SharedPreferences preferences, EventBus eventBus) {
        eventBus.send(new StopServiceEvent());
        preferences.edit().putBoolean(WheelyConfig.PREF_IS_LOGIN, false).apply();
        view.startLoginActivity();
    }
}
