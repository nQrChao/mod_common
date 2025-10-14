package com.chaoji.im.utils.floattoast.draggable;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.chaoji.im.utils.floattoast.XToast;


/**
 * desc   : 拖拽后回弹处理实现类
 */
public class SpringHideTimeDraggable extends BaseDraggable {
    /**
     * 水平方向回弹
     */
    public static final int ORIENTATION_HORIZONTAL = LinearLayout.HORIZONTAL;
    /**
     * 垂直方向回弹
     */
    public static final int ORIENTATION_VERTICAL = LinearLayout.VERTICAL;

    /**
     * 手指按下的坐标
     */
    private float mViewDownX;
    private float mViewDownY;

    /**
     * 回弹的方向
     */
    private final int mOrientation;

    /**
     * 触摸移动标记
     */
    private boolean mMoveTouch;
    private boolean mTimer;

    /**
     * 用于 定时 隐藏 logo的定时器
     */
    public CountDownTimer mHideTimer;

    /**
     * 坐落 左 右 标记
     */
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    /**
     * 悬浮窗 坐落 位置
     */
    private int mHintLocation;

    /**
     * 悬浮窗  隐藏后显示的透明度/默认0.7
     */
    private float mHintAlpha = 0.8f;
    private long mMillisInFuture = 3000L;


    /**
     * 设置可拖拽，自动隐藏
     *
     * @param hintAlpha      隐藏后的透明值0-1
     * @param millisInFuture 隐藏延迟毫秒
     */
    public SpringHideTimeDraggable(float hintAlpha, long millisInFuture) {
        this(ORIENTATION_HORIZONTAL, hintAlpha, millisInFuture, LEFT, true);
    }

    public SpringHideTimeDraggable(float hintAlpha, long millisInFuture, int mHintLocation) {
        this(ORIENTATION_HORIZONTAL, hintAlpha, millisInFuture, mHintLocation, true);
    }

    public SpringHideTimeDraggable(float hintAlpha, long millisInFuture, int mHintLocation, boolean timer) {
        this(ORIENTATION_HORIZONTAL, hintAlpha, millisInFuture, mHintLocation, timer);
    }

    public SpringHideTimeDraggable(int orientation, float hintAlpha, long millisInFuture, int hintLocation, boolean timer) {
        //Logs.e("SpringHideDraggable");
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
        switch (mOrientation) {
            case ORIENTATION_HORIZONTAL:
            case ORIENTATION_VERTICAL:
                break;
            default:
                throw new IllegalArgumentException("You cannot pass in directions other than horizontal or vertical");
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
        //Logs.e("onTouch");
        float rawMoveX;
        float rawMoveY;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //重置位置
                resetViewSize(getDecorView());
                // 记录按下的位置（相对 View 的坐标）
                mViewDownX = event.getX();
                mViewDownY = event.getY();
                //mMoveTouch = false;
                break;
            case MotionEvent.ACTION_MOVE:
                // 记录移动的位置（相对屏幕的坐标）
                rawMoveX = event.getRawX() - getWindowInvisibleWidth();
                rawMoveY = event.getRawY() - getWindowInvisibleHeight();
                // 更新移动的位置
                updateLocation(rawMoveX - mViewDownX, rawMoveY - mViewDownY);
                if (!mMoveTouch && isTouchMove(mViewDownX, event.getX(), mViewDownY, event.getY())) {
                    // 如果用户移动了手指，那么就拦截本次触摸事件，从而不让点击事件生效
                    mMoveTouch = true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // 记录移动的位置（相对屏幕的坐标）
                rawMoveX = event.getRawX() - getWindowInvisibleWidth();
                rawMoveY = event.getRawY() - getWindowInvisibleHeight();
                // 自动回弹吸附
                switch (mOrientation) {
                    case ORIENTATION_HORIZONTAL:
                        final float rawFinalX;
                        // 获取当前屏幕的宽度
                        int screenWidth = getWindowWidth();
                        if (rawMoveX < screenWidth / 2f) {
                            // 回弹到屏幕左边
                            mHintLocation = LEFT;
                            rawFinalX = 0f;
                        } else {
                            // 回弹到屏幕右边
                            mHintLocation = RIGHT;
                            rawFinalX = screenWidth;
                        }
                        // 从移动的点回弹到边界上
                        startHorizontalAnimation(rawMoveX - mViewDownX, rawFinalX - mViewDownX, rawMoveY - mViewDownY);
                        break;
                    case ORIENTATION_VERTICAL:
                        final float rawFinalY;
                        // 获取当前屏幕的高度
                        int screenHeight = getWindowHeight();
                        if (rawMoveY < screenHeight / 2f) {
                            // 回弹到屏幕顶部
                            rawFinalY = 0f;
                        } else {
                            // 回弹到屏幕底部
                            rawFinalY = screenHeight;
                        }
                        startVerticalAnimation(rawMoveX - mViewDownX, rawMoveY - mViewDownY, rawFinalY);
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

    /**
     * 执行水平回弹动画
     *
     * @param startX X 轴起点坐标
     * @param endX   X 轴终点坐标
     * @param y      Y 轴坐标
     */
    private void startHorizontalAnimation(float startX, float endX, final float y) {
        ValueAnimator animator = ValueAnimator.ofFloat(startX, endX);
        animator.setDuration(calculateAnimationDuration(startX, endX));
        animator.addUpdateListener(animation -> updateLocation((float) animation.getAnimatedValue(), y));
        animator.start();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mMoveTouch = false;
                if (mTimer) {
                    mHideTimer.start();
                }
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
    }

    /**
     * 执行垂直回弹动画
     *
     * @param x      X 轴坐标
     * @param startY Y 轴起点坐标
     * @param endY   Y 轴终点坐标
     */
    private void startVerticalAnimation(final float x, float startY, final float endY) {
        ValueAnimator animator = ValueAnimator.ofFloat(startY, endY);
        animator.setDuration(calculateAnimationDuration(startY, endY));
        animator.addUpdateListener(animation -> updateLocation(x, (float) animation.getAnimatedValue()));
        animator.start();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (mTimer) {
                    mHideTimer.start();
                }
                mMoveTouch = false;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
    }

    /**
     * 根据距离算出动画的时间
     *
     * @param startCoordinate 起始坐标
     * @param endCoordinate   结束坐标
     */
    private long calculateAnimationDuration(float startCoordinate, float endCoordinate) {
        //Logs.e( "calculateAnimationDuration");
        // 为什么要根据距离来算出动画的时间？
        // issue 地址：https://github.com/getActivity/XToast/issues/36
        // 因为不那么做，如果悬浮球回弹的距离比较短的情况，加上 ValueAnimator 动画更新回调次数比较多的情况下
        // 会导致自动回弹的时候出现轻微卡顿，但这其实不是卡顿，而是一次滑动的距离太短的导致的
        long animationDuration = (long) ((Math.abs(endCoordinate - startCoordinate)) / 2f);
        if (animationDuration > 800) {
            animationDuration = 800;
        }
        return animationDuration;
    }

    /**
     * 初始化 隐藏悬浮窗的定时器
     */
    private void initTimer() {
        mHideTimer = new CountDownTimer(mMillisInFuture, 10) {        //悬浮窗超过3秒没有操作的话会自动隐藏
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
                    //Logs.e("initTimer：!mMoveTouch");
                    if (mHintLocation == LEFT) {
                        shrinkLeftLogoView(getDecorView());
                    } else {
                        shrinkRightLogoView(getDecorView());
                    }
                }
            }
        };
    }

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