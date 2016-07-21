package com.mitutov.wheelytest.ui.model;

import java.io.Serializable;

/**
 * Created by Alexey Mitutov on 17.07.16.
 *
 */
public class WheelyLocation implements Serializable {

    private int id;
    private double lat;
    private double lon;

    public WheelyLocation() {
    }

    @Override
    public String toString() {
        return "WheelyLocation{" +
                "id=" + id +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
