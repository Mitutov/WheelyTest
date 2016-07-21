package com.mitutov.wheelytest.network;

import android.util.Log;

import okhttp3.Response;
import okhttp3.ws.WebSocket;

/**
 * Created by Alexey Mitutov on 17.07.16.
 *
 */
public class OnOpen extends AbstractWSEvent {

    private WebSocket webSocket;
    private Response response;

    public OnOpen(WebSocket webSocket, Response response) {
        this.webSocket = webSocket;
        this.response = response;
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }

    public Response getResponse() {
        return response;
    }

    @Override
    public void log() {
        Log.i("TAG === ", "WS OnOpen.");
    }
}
