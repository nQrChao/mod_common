package com.chaoji.other.kongzue.baseokhttp.listener;

import android.content.Context;

public interface ParameterInterceptListener<Parameter> {
    Parameter onIntercept(Context context, String url, Parameter parameter);
}
