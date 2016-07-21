package com.mitutov.wheelytest.di.components;

import android.app.Application;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.mitutov.wheelytest.app.WheelyApp;
import com.mitutov.wheelytest.common.bus.EventBus;
import com.mitutov.wheelytest.di.modules.WheelyAppModule;
import com.mitutov.wheelytest.services.WheelyService;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Alexey Mitutov on 17.07.16.
 *
 */

@Singleton
@Component(modules = {WheelyAppModule.class})
public interface WheelyAppComponent {

    void inject(WheelyApp wheelyApp);
    void inject(WheelyService wheelyService);

    Application app();

    SharedPreferences sharedPreferences();
    EventBus eventBus();
    Gson gson();

}
