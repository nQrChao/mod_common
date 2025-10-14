package com.chaoji.other.hjq.window;

import android.view.View;

@SuppressWarnings("rawtypes")
final class ViewLongClickWrapper implements View.OnLongClickListener {

    private final EasyWindow<?> mEasyWindow;
    private final EasyWindow.OnLongClickListener mListener;

    ViewLongClickWrapper(EasyWindow<?> easyWindow, EasyWindow.OnLongClickListener listener) {
        mEasyWindow = easyWindow;
        mListener = listener;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean onLongClick(View view) {
        if (mListener == null) {
            return false;
        }
        return mListener.onLongClick(mEasyWindow, view);
    }
}