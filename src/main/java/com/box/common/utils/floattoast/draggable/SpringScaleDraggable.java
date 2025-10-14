package com.box.common.utils.floattoast.draggable;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import com.box.common.utils.floattoast.XToast;

/**
 * desc   : 拖拽后回弹处理实现类
 */
public class SpringScaleDraggable extends BaseDraggable {

    /**
     * 定义一个监听器接口，用于在悬浮窗停靠在左侧或右侧时发出通知。
     */
    public interface OnSideChangedListener {
        /**
         * 当悬浮窗停靠的侧边发生改变时回调。
         * @param side 新的停靠位置，值为 LEFT 或 RIGHT。
         */
        void onSideChanged(int side);
    }

    private OnSideChangedListener mSideChangedListener;

    /**
     * 设置侧边变化监听器。
     * @param listener 监听器实例
     */
    public void setOnSideChangedListener(OnSideChangedListener listener) {
        this.mSideChangedListener = listener;
    }

    public static final int ORIENTATION_HORIZONTAL = LinearLayout.HORIZONTAL;
    public static final int ORIENTATION_VERTICAL = LinearLayout.VERTICAL;

    private float mViewDownX;
    private float mViewDownY;

    private final int mOrientation;
    private boolean mMoveTouch;
    private boolean mTimer;
    public CountDownTimer mHideTimer;

    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    private int mHintLocation;

    private float mHintAlpha = 0.8f;
    private long mMillisInFuture = 3000L;

    public SpringScaleDraggable(float hintAlpha, long millisInFuture) {
        this(ORIENTATION_HORIZONTAL, hintAlpha, millisInFuture, LEFT, true);
    }

    public SpringScaleDraggable(float hintAlpha, long millisInFuture, int mHintLocation) {
        this(ORIENTATION_HORIZONTAL, hintAlpha, millisInFuture, mHintLocation, true);
    }

    public SpringScaleDraggable(float hintAlpha, long millisInFuture, int mHintLocation, boolean timer) {
        this(ORIENTATION_HORIZONTAL, hintAlpha, millisInFuture, mHintLocation, timer);
    }

    public SpringScaleDraggable(int orientation, float hintAlpha, long millisInFuture, int hintLocation, boolean timer) {
        mHintAlpha = hintAlpha;
        mOrientation = orientation;
        mHintLocation = hintLocation;
        mMillisInFuture = millisInFuture;
        mTimer = timer;
        if (timer) {
            initTimer();
            if (null != getDecorView())
                mHideTimer.start();
        }
    }

    @Override
    public void start(XToast<?> toast) {
        super.start(toast);
        if (!mMoveTouch && mTimer) {
            mHideTimer.start();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float rawMoveX;
        float rawMoveY;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                resetViewSize(getDecorView());
                mViewDownX = event.getX();
                mViewDownY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                rawMoveX = event.getRawX() - getWindowInvisibleWidth();
                rawMoveY = event.getRawY() - getWindowInvisibleHeight();
                updateLocation(rawMoveX - mViewDownX, rawMoveY - mViewDownY);
                if (!mMoveTouch && isTouchMove(mViewDownX, event.getX(), mViewDownY, event.getY())) {
                    mMoveTouch = true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                rawMoveX = event.getRawX() - getWindowInvisibleWidth();
                rawMoveY = event.getRawY() - getWindowInvisibleHeight();
                switch (mOrientation) {
                    case ORIENTATION_HORIZONTAL:
                        final float rawFinalX;
                        int screenWidth = getWindowWidth();
                        if (rawMoveX < screenWidth / 2f) {
                            mHintLocation = LEFT;
                            rawFinalX = 0f;
                        } else {
                            mHintLocation = RIGHT;
                            rawFinalX = screenWidth;
                        }
                        startHorizontalAnimation(rawMoveX - mViewDownX, rawFinalX - mViewDownX, rawMoveY - mViewDownY);
                        break;
                    default:
                        break;
                }
                return mMoveTouch;
            default:
                break;
        }
        return false;
    }

    private void startHorizontalAnimation(float startX, float endX, final float y) {
        ValueAnimator animator = ValueAnimator.ofFloat(startX, endX);
        animator.setDuration(calculateAnimationDuration(startX, endX));
        animator.addUpdateListener(animation -> updateLocation((float) animation.getAnimatedValue(), y));
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mMoveTouch = false;
                if (mTimer) {
                    mHideTimer.start();
                }
                // [已修改] 动画结束时，表示悬浮窗已经停靠，此时通知监听器。
                if (mSideChangedListener != null) {
                    mSideChangedListener.onSideChanged(mHintLocation);
                }
            }
        });
        animator.start();
    }

    // 其他方法保持不变...
    private void initTimer() {
        mHideTimer = new CountDownTimer(mMillisInFuture, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (mMoveTouch) {
                    if (mHideTimer != null)
                        mHideTimer.cancel();
                }
            }
            @Override
            public void onFinish() {
                if (!mMoveTouch) {
                    if (mHintLocation == LEFT) {
                        shrinkLeftLogoView(getDecorView());
                    } else {
                        shrinkRightLogoView(getDecorView());
                    }
                }
            }
        };
    }
    private long calculateAnimationDuration(float s, float e) {
        long animationDuration = (long) ((Math.abs(e - s)) / 2f);
        if (animationDuration > 800) {
            animationDuration = 800;
        }
        return animationDuration;
    }
    private void startVerticalAnimation(float x, float s, float e) {}
    private void shrinkLeftLogoView(View view) {
        view.setAlpha(mHintAlpha);
        view.setTranslationX(-view.getWidth() / 2f);
    }
    private void shrinkRightLogoView(View view) {
        view.setAlpha(mHintAlpha);
        view.setTranslationX(view.getWidth() / 2f);
    }
    private void resetViewSize(View view) {
        view.clearAnimation();
        view.setAlpha(1);
        view.setScaleX(1);
        view.setScaleY(1);
        view.setTranslationX(0);
    }
}
