package com.chaoji.im.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class ViewLinearLayoutIntercept extends LinearLayout {

    public ViewLinearLayoutIntercept(Context context) {
        super(context);
    }

    public ViewLinearLayoutIntercept(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewLinearLayoutIntercept(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ViewLinearLayoutIntercept(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    boolean isIntercept = true;

    public void setIntercept(boolean intercept) {
        isIntercept = intercept;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isIntercept) return true;
        return super.onInterceptTouchEvent(ev);
    }
}
