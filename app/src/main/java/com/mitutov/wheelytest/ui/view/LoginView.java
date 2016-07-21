package com.mitutov.wheelytest.ui.view;

/**
 * Created by Alexey Mitutov on 18.07.16.
 *
 */
public interface LoginView {
    void startMapActivity();
    void startWheelyService(String user, String passwd);
    void showForbiddenMessage();
}
