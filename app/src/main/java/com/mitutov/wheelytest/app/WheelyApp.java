package com.mitutov.wheelytest.app;

import android.app.Application;
import android.content.Context;

import com.mitutov.wheelytest.di.components.DaggerWheelyAppComponent;
import com.mitutov.wheelytest.di.components.WheelyAppComponent;
import com.mitutov.wheelytest.di.modules.WheelyAppModule;

/**
 * Created by Alexey Mitutov on 17.07.16.
 *
 */
public class WheelyApp extends Application {

    private WheelyAppComponent wheelyAppComponent;

    public static WheelyApp get(Context context) {
        return (WheelyApp) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        buildObjectGraphAndInject();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public void buildObjectGraphAndInject() {
        wheelyAppComponent = DaggerWheelyAppComponent.builder()
                .wheelyAppModule(new WheelyAppModule(this))
                .build();
    }

    public WheelyAppComponent getAppComponent() {
        return wheelyAppComponent;
    }

}
