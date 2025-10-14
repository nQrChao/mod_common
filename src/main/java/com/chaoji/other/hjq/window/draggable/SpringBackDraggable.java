package com.chaoji.other.hjq.window.draggable;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.chaoji.other.hjq.window.EasyWindow;


public class SpringBackDraggable extends BaseDraggable {
    public static final int ORIENTATION_HORIZONTAL = LinearLayout.HORIZONTAL;
    public static final int ORIENTATION_VERTICAL = LinearLayout.VERTICAL;

    private float mViewDownX;
    private float mViewDownY;

    private final int mSpringBackOrientation;

    private boolean mTouchMoving;

    private SpringBackAnimCallback mSpringBackAnimCallback;

    public SpringBackDraggable() {
        this(ORIENTATION_HORIZONTAL);
    }

    public SpringBackDraggable(int springBackOrientation) {
        mSpringBackOrientation = springBackOrientation;
        switch (mSpringBackOrientation) {
            case ORIENTATION_HORIZONTAL:
            case ORIENTATION_VERTICAL:
                break;
            default:
                throw new IllegalArgumentException("You cannot pass in directions other than horizontal or vertical");
        }
    }

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
                    dispatchSpringBackViewToScreenEdge(event.getRawX(), event.getRawY());
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

    public void dispatchSpringBackViewToScreenEdge() {
        dispatchSpringBackViewToScreenEdge(getViewOnScreenX(), getViewOnScreenY());
    }

    public void dispatchSpringBackViewToScreenEdge(float rawX, float rawY) {
        float rawMoveX = rawX - getWindowInvisibleWidth();
        float rawMoveY = rawY - getWindowInvisibleHeight();

        switch (mSpringBackOrientation) {
            case ORIENTATION_HORIZONTAL:
                float startX = Math.max(rawMoveX - mViewDownX, 0);
                float endX;
                int screenWidth = getWindowWidth();
                if (rawMoveX < screenWidth / 2f) {
                    endX = 0f;
                } else {
                    endX = Math.max((float) screenWidth - getViewWidth(), 0);
                }
                float y = rawMoveY - mViewDownY;
                if (!equalsWithRelativeTolerance(startX, endX)) {
                    startHorizontalAnimation(startX, endX, y);
                }
                break;
            case ORIENTATION_VERTICAL:
                float x = rawMoveX - mViewDownX;
                float startY = Math.max(rawMoveY - mViewDownY, 0);
                float endY;
                int screenHeight = getWindowHeight();
                if (rawMoveY < screenHeight / 2f) {
                    endY = 0f;
                } else {
                    endY = Math.max((float) screenHeight - getViewHeight(), 0);
                }
                if (!equalsWithRelativeTolerance(startY, endY)) {
                    startVerticalAnimation(x, startY, endY);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onScreenRotateInfluenceCoordinateChangeFinish() {
        super.onScreenRotateInfluenceCoordinateChangeFinish();
        dispatchSpringBackViewToScreenEdge();
    }

    /**
     * 判断两个浮点数是否相等（Java 浮点数不能直接用 != 或者 == 来判断）
     */
    public boolean equalsWithRelativeTolerance(float number1, float number2) {
        // 定义一个允许的误差范围，值为 0.00001
        float epsilon = 1e-5f;
        // 两个浮点数近似相等
        return Math.abs(number1 - number2) < epsilon;
    }

    public void startHorizontalAnimation(float startX, float endX, final float y) {
        startHorizontalAnimation(startX, endX, y, calculateAnimationDuration(startX, endX));
    }

    /**
     * 执行水平回弹动画
     *
     * @param startX        X 轴起点坐标
     * @param endX          X 轴终点坐标
     * @param y             Y 轴坐标
     * @param duration      动画时长
     */
    public void startHorizontalAnimation(float startX, float endX, float y, long duration) {
        startAnimation(startX, endX, duration, animation -> updateLocation((float) animation.getAnimatedValue(), y));
    }

    public void startVerticalAnimation(float x, float startY, float endY) {
        startVerticalAnimation(x, startY, endY, calculateAnimationDuration(startY, endY));
    }

    /**
     * 执行垂直回弹动画
     *
     * @param x             X 轴坐标
     * @param startY        Y 轴起点坐标
     * @param endY          Y 轴终点坐标
     * @param duration      动画时长
     */
    public void startVerticalAnimation(float x, float startY, float endY, long duration) {
        startAnimation(startY, endY, duration, animation -> updateLocation(x, (float) animation.getAnimatedValue()));
    }

    /**
     * 开启动画
     */
    public void startAnimation(float start, float end, long duration, AnimatorUpdateListener listener) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(start, end);
        valueAnimator.setDuration(duration);
        if (listener != null) {
            valueAnimator.addUpdateListener(listener);
        }
        valueAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animator) {
                dispatchSpringBackAnimationStartCallback(animator);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                dispatchSpringBackAnimationEndCallback(animator);
            }
        });
        valueAnimator.start();
    }

    /**
     * 根据距离算出动画的时间
     *
     * @param startCoordinate               起始坐标
     * @param endCoordinate                 结束坐标
     */
    public long calculateAnimationDuration(float startCoordinate, float endCoordinate) {
        // 为什么要根据距离来算出动画的时间？
        // issue 地址：https://github.com/getActivity/EasyWindow/issues/36
        // 因为不那么做，如果悬浮球回弹的距离比较短的情况，加上 ValueAnimator 动画更新回调次数比较多的情况下
        // 会导致自动回弹的时候出现轻微卡顿，但这其实不是卡顿，而是一次滑动的距离太短的导致的
        // 所以这里需要限定最短的动画时间必须要 200 毫秒及以上，这样就能避免这个问题
        // 同时为了避免回弹动画时间过长，这里还限定了最长只能 800 毫秒，动画时间太长用户体验也不好
        long coordinateDistance = (long) ((Math.abs(endCoordinate - startCoordinate)) / 2f);
        return Math.min(Math.max(coordinateDistance, 200), 800);
    }

    /**
     * 设置拖拽回弹回调
     */
    public void setSpringBackAnimCallback(SpringBackAnimCallback callback) {
        mSpringBackAnimCallback = callback;
    }

    /**
     * 派发拖拽回弹动画开始回调
     */
    protected void dispatchSpringBackAnimationStartCallback(Animator animator) {
        // Log.i(getClass().getSimpleName(), "开始拖拽回弹动画");
        if (mSpringBackAnimCallback == null) {
            return;
        }
        mSpringBackAnimCallback.onSpringBackAnimationStart(getEasyWindow(), animator);
    }

    /**
     * 派发拖拽回弹动画结束回调
     */
    protected void dispatchSpringBackAnimationEndCallback(Animator animator) {
        // Log.i(getClass().getSimpleName(), "结束拖拽回弹动画");
        if (mSpringBackAnimCallback == null) {
            return;
        }
        mSpringBackAnimCallback.onSpringBackAnimationEnd(getEasyWindow(), animator);
    }

    /**
     * 当前是否处于触摸移动状态
     */
    public boolean isTouchMoving() {
        return mTouchMoving;
    }

    public interface SpringBackAnimCallback {

        /**
         * 回弹动画开始执行
         */
        void onSpringBackAnimationStart(EasyWindow<?> easyWindow, Animator animator);

        /**
         * 回弹动画结束执行
         */
        void onSpringBackAnimationEnd(EasyWindow<?> easyWindow, Animator animator);
    }
}