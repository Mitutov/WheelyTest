package com.mitutov.wheelytest.network;

import android.util.Log;

import okio.Buffer;

/**
 * Created by Alexey Mitutov on 17.07.16.
 *
 */
public class OnPong extends AbstractWSEvent {

    private Buffer payload;

    public OnPong(Buffer payload) {
        this.payload = payload;
    }

    public Buffer getPayload() {
        return payload;
    }

    @Override
    public void log() {
        Log.i("TAG === ", "WS OnPong");
    }
}
