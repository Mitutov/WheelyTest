package com.mitutov.wheelytest.di.modules;

import com.mitutov.wheelytest.ui.presenter.MapActivityPresenter;
import com.mitutov.wheelytest.ui.presenter.MapActivityPresenterImpl;
import com.mitutov.wheelytest.ui.view.MapActivity;
import com.mitutov.wheelytest.ui.view.MapView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alexey Mitutov on 17.07.16.
 *
 */

@Module
public class MapModule {

    private MapActivity activity;

    public MapModule(MapActivity activity) {
        this.activity = activity;
    }

    /** Provide MapView */
    @Provides
    public MapView provideMapView() {
        return activity;
    }

    /** Provide MapActivityPresenterImpl */
    @Provides
    public MapActivityPresenter provideMapActivityPresenterImpl (MapView view) {
        return new MapActivityPresenterImpl(view);
    }

}
