package com.chaoji.other.hjq.window.draggable;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;

public class MovingDraggable extends BaseDraggable {

    private float mViewDownX;
    private float mViewDownY;

    /** 触摸移动标记 */
    private boolean mTouchMoving;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mViewDownX = event.getX();
                mViewDownY = event.getY();
                mTouchMoving = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float rawMoveX = event.getRawX() - getWindowInvisibleWidth();
                float rawMoveY = event.getRawY() - getWindowInvisibleHeight();

                float newX = Math.max(rawMoveX - mViewDownX, 0);
                float newY = Math.max(rawMoveY - mViewDownY, 0);

                updateLocation(newX, newY);

                if (mTouchMoving) {
                    dispatchExecuteDraggingCallback();
                } else if (isFingerMove(mViewDownX, event.getX(), mViewDownY, event.getY())) {
                    mTouchMoving = true;
                    dispatchStartDraggingCallback();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mTouchMoving) {
                    dispatchStopDraggingCallback();
                }
                try {
                    return mTouchMoving;
                } finally {
                    mTouchMoving = false;
                }
            default:
                break;
        }
        return false;
    }

    public boolean isTouchMoving() {
        return mTouchMoving;
    }
}