package com.chaoji.other.hjq.window;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;

@SuppressWarnings("rawtypes")
final class ViewTouchWrapper implements View.OnTouchListener {

    private final EasyWindow<?> mEasyWindow;
    private final EasyWindow.OnTouchListener mListener;

    ViewTouchWrapper(EasyWindow<?> easyWindow, EasyWindow.OnTouchListener listener) {
        mEasyWindow = easyWindow;
        mListener = listener;
    }

    @SuppressLint("ClickableViewAccessibility")
    @SuppressWarnings("unchecked")
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (mListener == null) {
            return false;
        }
        return mListener.onTouch(mEasyWindow, view, event);
    }
}