package com.mitutov.wheelytest.network;

import android.util.Log;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by Alexey Mitutov on 17.07.16.
 *
 */
public class OnFailure extends AbstractWSEvent {

    private IOException exception;
    private Response response;

    public OnFailure(IOException exception, Response response) {
        this.exception = exception;
        this.response = response;
    }

    public IOException getException() {
        return exception;
    }

    public Response getResponse() {
        return response;
    }

    @Override
    public void log() {
        Log.i("TAG === ", "WS OnFailure.");
    }
}
