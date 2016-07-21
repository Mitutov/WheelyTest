package com.mitutov.wheelytest.network;

import android.util.Log;

/**
 * Created by Alexey Mitutov on 17.07.16.
 *
 */
public class OnClose extends AbstractWSEvent {

    private int code;
    private String reason;

    public OnClose(int code, String reason) {
        this.code = code;
        this.reason = reason;
    }

    public int getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public void log() {
        Log.i("TAG === ", "WS closed.");
    }
}
