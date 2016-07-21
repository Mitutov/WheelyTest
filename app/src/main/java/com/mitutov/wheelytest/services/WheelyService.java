package com.mitutov.wheelytest.services;

import android.Manifest;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mitutov.wheelytest.R;
import com.mitutov.wheelytest.app.WheelyApp;
import com.mitutov.wheelytest.common.bus.EventBus;
import com.mitutov.wheelytest.common.bus.OnAuthorizedEvent;
import com.mitutov.wheelytest.common.bus.OnForbiddenEvent;
import com.mitutov.wheelytest.common.bus.OnMessageEvent;
import com.mitutov.wheelytest.common.bus.StopServiceEvent;
import com.mitutov.wheelytest.common.WheelyConfig;
import com.mitutov.wheelytest.ui.model.WheelyLocation;
import com.mitutov.wheelytest.network.AbstractWSEvent;
import com.mitutov.wheelytest.network.OnFailure;
import com.mitutov.wheelytest.network.WheelyWebSocket;
import com.mitutov.wheelytest.network.OnMessage;
import com.mitutov.wheelytest.network.OnOpen;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ws.WebSocket;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Alexey Mitutov on 17.07.16.
 *
 */
public class WheelyService extends Service implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @Inject
    EventBus eventBus;
    @Inject
    Gson gson;
    @Inject
    SharedPreferences preferences;

    private WheelyWebSocket wheelyWebSocket;
    private Intent hideIntent;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private WebSocket webSocket;
    private Action1<AbstractWSEvent> subscriber;
    private MediaType JSON = MediaType.parse("application/vnd.okhttp.websocket+text; charset=utf-8");
    private Subscription eventSubscription;
    private String user;
    private String passwd;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(WheelyConfig.LOCATION_UPDATE_INTERVAL);
        locationRequest.setFastestInterval(WheelyConfig.LOCATION_FAST_UPDATE_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // инжектим сервис
        WheelyApp app = (WheelyApp) getApplication();
        app.getAppComponent().inject(this);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this).setSmallIcon(R.mipmap.ic_launcher);
        Notification notification = builder.build();
        startForeground(777, notification);
        hideIntent = new Intent(this, HideNotificationService.class);
        startService(hideIntent);

        subscriber = event -> {
            event.log();
            try {
                if (event instanceof OnOpen) {
                    OnOpen onOpen = (OnOpen) event;
                    webSocket = onOpen.getWebSocket();
                    eventBus.send(new OnAuthorizedEvent());
                } else if (event instanceof OnMessage) {

                    TypeToken typeToken = new TypeToken<ArrayList<WheelyLocation>>(){};

                    OnMessage onMessage = (OnMessage) event;
                    String message = onMessage.getMessage().string();

                    ArrayList<WheelyLocation> wheelyLocations = gson.fromJson(message, typeToken.getType());

                    String savedLocationsString = preferences.getString(WheelyConfig.PREF_MAP_MARKERS, null);
                    if (savedLocationsString != null) {
                        ArrayList<WheelyLocation> savedLocations = gson.fromJson(savedLocationsString, typeToken.getType());
                        savedLocations.addAll(wheelyLocations);
                        String newSavedLocationsString = gson.toJson(savedLocations, typeToken.getType());
                        preferences.edit().putString(WheelyConfig.PREF_MAP_MARKERS, newSavedLocationsString).apply();
                    }else {
                        preferences.edit().putString(WheelyConfig.PREF_MAP_MARKERS, message).apply();
                    }

                    eventBus.send(new OnMessageEvent(wheelyLocations));
                } else if (event instanceof OnFailure) {

                    OnFailure onFailure = (OnFailure) event;
                    Response response = onFailure.getResponse();
                    if (response != null) {
                        int responseCode = response.code();
                        if(responseCode == HttpURLConnection.HTTP_FORBIDDEN) {
                            eventBus.send(new OnForbiddenEvent());
                            this.stopSelf();
                        }
                    }else {
                        webSocket = null;
                        wheelyWebSocket.connect(user, passwd);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Bundle extras = intent.getExtras();
        if(extras != null) {
            user = extras.getString(WheelyConfig.EXTRAS_USER_KEY);
            passwd = extras.getString(WheelyConfig.EXTRAS_PASSWD_KEY);
        }

        Log.e("TAG=========", "Service start...");

        wheelyWebSocket = new WheelyWebSocket();
        wheelyWebSocket.connect(user, passwd);
        wheelyWebSocket.subscribe(subscriber);

        eventSubscription = eventBus.subscribe(event -> {
            if (event instanceof StopServiceEvent) {
                preferences.edit().remove(WheelyConfig.PREF_MAP_MARKERS).apply();
                this.stopSelf();
            }
        });
        googleApiClient.connect();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        eventSubscription.unsubscribe();
        stopService(hideIntent);
        googleApiClient.disconnect();
        try {
            if (webSocket != null) webSocket.close(1000, "Service stop.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("TAG=========", "Service stop...");
        super.onDestroy();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        if (webSocket != null) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("lat", location.getLatitude());
            jsonObject.addProperty("lon", location.getLongitude());
            String requestContent = gson.toJson(jsonObject);
            RequestBody requestBody = RequestBody.create(JSON, requestContent);
            try {
                webSocket.sendMessage(requestBody);
                Log.e("TAG=========", "Sending changed location... \n" + requestContent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
