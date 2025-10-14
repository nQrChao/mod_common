package com.box.other.kongzue.baseokhttp.listener;

import okhttp3.Response;
import okio.ByteString;

public interface WebSocketStatusListener {
    
    void connected(Response response);
    
    void onMessage(String message);
    
    void onMessage(ByteString message);
    
    void onReconnect();
    
    void onDisconnected(int breakStatus);
    
    void onConnectionFailed(Throwable t);
    
}
