package com.mitutov.wheelytest.ui.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mitutov.wheelytest.R;
import com.mitutov.wheelytest.app.BaseActivity;
import com.mitutov.wheelytest.common.WheelyConfig;
import com.mitutov.wheelytest.common.bus.EventBus;
import com.mitutov.wheelytest.di.components.DaggerLoginComponent;
import com.mitutov.wheelytest.di.components.HasComponent;
import com.mitutov.wheelytest.di.modules.LoginModule;
import com.mitutov.wheelytest.di.components.LoginComponent;
import com.mitutov.wheelytest.di.components.WheelyAppComponent;
import com.mitutov.wheelytest.services.WheelyService;
import com.mitutov.wheelytest.ui.presenter.LoginActivityPresenterImpl;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by Alexey Mitutov on 17.07.16.
 *
 */
public class LoginActivity extends BaseActivity implements HasComponent<LoginComponent>, LoginView {

    private static final String BUNDLE_PROGRESSBAR_STATE = "bundleProgressBarState";

    @Inject
    public LoginActivityPresenterImpl presenter;
    @Inject
    EventBus eventBus;
    @Inject
    SharedPreferences preferences;

    private LoginComponent component;
    private Subscription eventSubscription;

    private EditText userNameEdit;
    private EditText passwordEdit;
    private ProgressBar progressBar;
    private Button connectButton;

    @Override
    @SuppressWarnings("ResourceType")
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        eventSubscription = presenter.onCreate(preferences, eventBus);

        setContentView(R.layout.activity_login);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_activity_login);
        setSupportActionBar(toolbar);

        progressBar = (ProgressBar)findViewById(R.id.progress_bar_toolbar);
        // восстанавливаем статус прогрессбара
        if (savedInstanceState != null) {
            int progressBarState = savedInstanceState.getInt(BUNDLE_PROGRESSBAR_STATE);
            progressBar.setVisibility(progressBarState);
        }

        userNameEdit = (EditText)findViewById(R.id.userNameEditText);
        passwordEdit = (EditText)findViewById(R.id.passwordEditText);

        connectButton = (Button)findViewById(R.id.btnConnect);
        connectButton.setOnClickListener(view -> {
            if (checkPlayServices()) {
                if (isOnline() && checkLocationEnabled()) {
                    connectButton.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);
                    String user = userNameEdit.getText().toString();
                    String passwd = passwordEdit.getText().toString();
                    presenter.onConnectBtnClick(user, passwd);
                }else {
                    Toast.makeText(this, R.string.toast_is_no_online_or_gps_disabled, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // сохраняем статус прогрес бара для последующего восстановления
        int progressBarState = progressBar.getVisibility();
        outState.putInt(BUNDLE_PROGRESSBAR_STATE, progressBarState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        eventSubscription.unsubscribe();
        super.onStop();
    }

    // LoginView implement methods Start ==========

    @Override
    public void startMapActivity() {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void startWheelyService(String user, String passwd) {

        Bundle extras = new Bundle();
        extras.putString(WheelyConfig.EXTRAS_USER_KEY, user);
        extras.putString(WheelyConfig.EXTRAS_PASSWD_KEY, passwd);

        Intent service = new Intent(this, WheelyService.class);
        service.putExtras(extras);
        startService(service);
    }

    @Override
    public void showForbiddenMessage() {
        progressBar.setVisibility(View.INVISIBLE);
        connectButton.setEnabled(true);
        Toast.makeText(this, R.string.toast_forbidden, Toast.LENGTH_LONG).show();
    }

    // LoginView implement methods End ==========


    // BaseActivity extended method =========
    @Override
    protected void setupComponent(WheelyAppComponent appComponent) {

        component = DaggerLoginComponent.builder()
                .wheelyAppComponent(appComponent)
                .loginModule(new LoginModule(this))
                .build();
        component.inject(this);

    }

    // HasComponent implement method =========
    @Override
    public LoginComponent getComponent() {
        return component;
    }
}
