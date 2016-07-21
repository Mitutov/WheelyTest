package com.mitutov.wheelytest.di.modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.mitutov.wheelytest.app.WheelyApp;
import com.mitutov.wheelytest.common.bus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alexey Mitutov on 17.07.16.
 *
 */

@Module
public class WheelyAppModule {

    private final WheelyApp app;
    private static final String PREF_NAME = "preferences";

    public WheelyAppModule(WheelyApp app) {
        this.app = app;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return app;
    }

    @Provides
    @Singleton
    public SharedPreferences provideSharedPreferences() {
        return app.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    public EventBus provideEventBus() {
        return new EventBus();
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new Gson();
    }

}
