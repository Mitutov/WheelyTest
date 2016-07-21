package com.mitutov.wheelytest.di.modules;

import com.mitutov.wheelytest.ui.presenter.LoginActivityPresenter;
import com.mitutov.wheelytest.ui.presenter.LoginActivityPresenterImpl;
import com.mitutov.wheelytest.ui.view.LoginActivity;
import com.mitutov.wheelytest.ui.view.LoginView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alexey Mitutov on 17.07.16.
 *
 */

@Module
public class LoginModule {

    private LoginActivity activity;

    public LoginModule(LoginActivity activity) {
        this.activity = activity;
    }

    /** Provide LoginView */
    @Provides
    public LoginView provideLoginView() {
        return activity;
    }

    /** Provide LoginView LoginActivityPresenterImpl */
    @Provides
    public LoginActivityPresenter provideLoginActivityPresenterImpl(LoginView view) {
        return new LoginActivityPresenterImpl(view);
    }
}
