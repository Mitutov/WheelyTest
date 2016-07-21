package com.mitutov.wheelytest.ui.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mitutov.wheelytest.R;
import com.mitutov.wheelytest.app.BaseActivity;
import com.mitutov.wheelytest.common.WheelyConfig;
import com.mitutov.wheelytest.common.bus.EventBus;
import com.mitutov.wheelytest.di.components.DaggerMapComponent;
import com.mitutov.wheelytest.di.components.HasComponent;
import com.mitutov.wheelytest.di.components.MapComponent;
import com.mitutov.wheelytest.di.modules.MapModule;
import com.mitutov.wheelytest.di.components.WheelyAppComponent;
import com.mitutov.wheelytest.ui.model.WheelyLocation;
import com.mitutov.wheelytest.ui.presenter.MapActivityPresenterImpl;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscription;

public class MapActivity extends BaseActivity implements OnMapReadyCallback, HasComponent<MapComponent>, MapView {

    @Inject
    public MapActivityPresenterImpl presenter;
    @Inject
    EventBus eventBus;
    @Inject
    Gson gson;
    @Inject
    SharedPreferences preferences;

    private GoogleMap map;
    private MapComponent component;
    private Subscription eventSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_activity_map);
        setSupportActionBar(toolbar);

        Button btnDisconnect = (Button) findViewById(R.id.btnDisconnect);
        btnDisconnect.setOnClickListener(view -> presenter.onDisconnectBtnClick(preferences, eventBus));

        eventSubscription = presenter.onCreate(preferences, eventBus);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        String savedMarkersString = preferences.getString(WheelyConfig.PREF_MAP_MARKERS, null);
        if (savedMarkersString != null) {
            ArrayList<WheelyLocation> savedLocations = gson.fromJson(savedMarkersString, new TypeToken<ArrayList<WheelyLocation>>(){}.getType());
            updateMap(savedLocations);
        }
    }

    @Override
    protected void onStop() {
        eventSubscription.unsubscribe();
        super.onStop();
    }

    // MapView implement methods Start ==========

    @Override
    public void updateMap(ArrayList<WheelyLocation> locations) {
        if (map != null) {
            LatLng firstLocation = new LatLng(locations.get(0).getLat(), locations.get(0).getLon());
            for(WheelyLocation location: locations){
                LatLng latLng = new LatLng(location.getLat(), location.getLon());
                map.addMarker(new MarkerOptions().position(latLng)).setTitle(String.valueOf(location.getId()));
            }
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 10.5f));
        }
    }

    @Override
    public void startLoginActivity() {
        Intent intent = new Intent(MapActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    // MapinView implement methods End ==========


    // BaseActivity extended method =========
    @Override
    protected void setupComponent(WheelyAppComponent appComponent) {
        component = DaggerMapComponent.builder()
                .wheelyAppComponent(appComponent)
                .mapModule(new MapModule(this))
                .build();
        component.inject(this);

    }

    // HasComponent implement method =========
    @Override
    public MapComponent getComponent() {
        return component;
    }
}
