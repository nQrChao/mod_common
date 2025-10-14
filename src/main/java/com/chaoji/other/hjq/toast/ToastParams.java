package com.chaoji.other.hjq.toast;

import com.chaoji.other.hjq.toast.config.IToastInterceptor;
import com.chaoji.other.hjq.toast.config.IToastStrategy;
import com.chaoji.other.hjq.toast.config.IToastStyle;

public class ToastParams {

    public CharSequence text;

    public int duration = -1;

    public long delayMillis = 0;

    public boolean crossPageShow;

    public IToastStyle<?> style;

    public IToastStrategy strategy;

    public IToastInterceptor interceptor;
}