package com.mitutov.wheelytest.network;

import android.util.Log;

import okhttp3.ResponseBody;

/**
 * Created by Alexey Mitutov on 17.07.16.
 *
 */
public class OnMessage extends AbstractWSEvent {

    private ResponseBody message;

    public OnMessage(ResponseBody message) {
        this.message = message;
    }

    public ResponseBody getMessage() {
        return message;
    }

    @Override
    public void log() {
        Log.i("TAG === ", "WS OnMessage.");
    }
}
