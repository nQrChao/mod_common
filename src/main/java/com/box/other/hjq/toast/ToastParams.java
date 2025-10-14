package com.box.other.hjq.toast;

import com.box.other.hjq.toast.config.IToastInterceptor;
import com.box.other.hjq.toast.config.IToastStrategy;
import com.box.other.hjq.toast.config.IToastStyle;

public class ToastParams {

    public CharSequence text;

    public int duration = -1;

    public long delayMillis = 0;

    public boolean crossPageShow;

    public IToastStyle<?> style;

    public IToastStrategy strategy;

    public IToastInterceptor interceptor;
}