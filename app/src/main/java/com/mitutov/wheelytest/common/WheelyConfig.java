package com.mitutov.wheelytest.common;

/**
 * Created by Alexey Mitutov on 17.07.16.
 *
 */

public class WheelyConfig {
    public static final String SERVER_URL = "ws://mini-mdt.wheely.com?";
    public static final long LOCATION_UPDATE_INTERVAL = 5000; // 5 секунд
    public static final long LOCATION_FAST_UPDATE_INTERVAL = 1000; // 1 секунда
    public static final String PREF_IS_LOGIN = "prefIsLogin";
    public static final String PREF_MAP_MARKERS = "mapMarkers";
    public static final String EXTRAS_USER_KEY = "userKey";
    public static final String EXTRAS_PASSWD_KEY = "passwdKey";
}
