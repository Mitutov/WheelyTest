package com.mitutov.wheelytest.ui.view;

import com.mitutov.wheelytest.ui.model.WheelyLocation;

import java.util.ArrayList;

/**
 * Created by Alexey Mitutov on 18.07.16.
 *
 */
public interface MapView {
    void updateMap(ArrayList<WheelyLocation> locations);
    void startLoginActivity();
}
