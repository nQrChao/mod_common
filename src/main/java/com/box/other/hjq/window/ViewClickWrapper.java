package com.box.other.hjq.window;

import android.view.View;

@SuppressWarnings("rawtypes")
final class ViewClickWrapper implements View.OnClickListener {

    private final EasyWindow<?> mEasyWindow;
    private final EasyWindow.OnClickListener mListener;

    ViewClickWrapper(EasyWindow<?> easyWindow, EasyWindow.OnClickListener listener) {
        mEasyWindow = easyWindow;
        mListener = listener;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onClick(View view) {
        if (mListener == null) {
            return;
        }
        mListener.onClick(mEasyWindow, view);
    }
}