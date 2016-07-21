package com.mitutov.wheelytest.common.bus;

import com.mitutov.wheelytest.ui.model.WheelyLocation;

import java.util.ArrayList;

/**
 * Created by Alexey Mitutov on 17.07.16.
 *
 */
public class OnMessageEvent {

    private ArrayList<WheelyLocation> wheelyLocations;

    public OnMessageEvent(ArrayList<WheelyLocation> wheelyLocations) {
        this.wheelyLocations = wheelyLocations;
    }

    public ArrayList<WheelyLocation> getWheelyLocations() {
        return wheelyLocations;
    }
}
