package com.box.other.hjq.window;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public final class WindowLayout extends FrameLayout {

    /** 触摸事件监听 */
    private OnTouchListener mOnTouchListener;

    public WindowLayout(Context context) {
        super(context);
    }

    public WindowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WindowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mOnTouchListener != null && mOnTouchListener.onTouch(this, ev)) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        //super.setOnTouchListener(l);
        mOnTouchListener = l;
    }
}