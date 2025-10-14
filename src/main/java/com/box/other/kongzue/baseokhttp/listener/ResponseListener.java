package com.box.other.kongzue.baseokhttp.listener;

public abstract class ResponseListener implements BaseResponseListener {
    
    @Override
    public void response(Object response, Exception error) {
        onResponse(response == null ? "" : response.toString(), error);
    }
    
    public abstract void onResponse(String main, Exception error);
}
