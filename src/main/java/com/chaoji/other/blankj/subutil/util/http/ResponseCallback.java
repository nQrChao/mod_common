package com.chaoji.other.blankj.subutil.util.http;

public abstract class ResponseCallback {
    public abstract void onResponse(Response response);

    public abstract void onFailed(Exception e);
}
