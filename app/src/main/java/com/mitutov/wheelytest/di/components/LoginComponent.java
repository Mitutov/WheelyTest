package com.mitutov.wheelytest.di.components;

import com.mitutov.wheelytest.ui.view.LoginActivity;
import com.mitutov.wheelytest.di.ActivityScope;
import com.mitutov.wheelytest.di.modules.LoginModule;

import dagger.Component;

/**
 * Created by Alexey Mitutov on 17.07.16.
 *
 */

@ActivityScope
@Component(
        dependencies = WheelyAppComponent.class,
        modules = LoginModule.class
)
public interface LoginComponent {
    void inject(LoginActivity loginActivity);
}
