package com.mitutov.wheelytest.network;

import com.mitutov.wheelytest.common.WheelyConfig;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.ws.WebSocket;
import okhttp3.ws.WebSocketCall;
import okhttp3.ws.WebSocketListener;
import okio.Buffer;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;

/**
 * Created by Alexey Mitutov on 17.07.16.
 *
 */
public class WheelyWebSocket extends SerializedSubject<AbstractWSEvent, AbstractWSEvent>{

    private OkHttpClient okHttpClient;

    public WheelyWebSocket()  {
        super(PublishSubject.create());
        okHttpClient = new OkHttpClient();
    }

    public void connect(String user, String passwd) {

        String serviceURL = WheelyConfig.SERVER_URL + "username=" + user + "&password=" + passwd;

        Request request = new Request.Builder()
                .url(serviceURL)
                .build();

        WebSocketCall call = WebSocketCall.create(okHttpClient, request);

        call.enqueue(new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                WheelyWebSocket.this.onNext(new OnOpen(webSocket, response));
            }

            @Override
            public void onFailure(IOException e, Response response) {
                WheelyWebSocket.this.onNext(new OnFailure(e, response));
            }

            @Override
            public void onMessage(ResponseBody message) throws IOException {
                WheelyWebSocket.this.onNext(new OnMessage(message));
            }

            @Override
            public void onPong(Buffer payload) {
                WheelyWebSocket.this.onNext(new OnPong(payload));
            }

            @Override
            public void onClose(int code, String reason) {
                WheelyWebSocket.this.onNext(new OnClose(code, reason));
            }
        });
    }
}
