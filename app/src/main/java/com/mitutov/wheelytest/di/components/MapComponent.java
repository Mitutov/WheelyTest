package com.mitutov.wheelytest.di.components;

import com.mitutov.wheelytest.ui.view.MapActivity;
import com.mitutov.wheelytest.di.ActivityScope;
import com.mitutov.wheelytest.di.modules.MapModule;

import dagger.Component;

/**
 * Created by Alexey Mitutov on 17.07.16.
 *
 */

@ActivityScope
@Component(
        dependencies = WheelyAppComponent.class,
        modules = MapModule.class
)
public interface MapComponent {
    void inject(MapActivity mapActivity);
}
