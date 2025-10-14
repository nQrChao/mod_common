package com.chaoji.im.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class ViewPagerNoScroll extends ViewPager {

    private boolean isCanScroll = false;

    public ViewPagerNoScroll(Context context) {
        super(context);
    }

    public ViewPagerNoScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isCanScroll && super.onInterceptTouchEvent(ev);
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isCanScroll && super.onTouchEvent(ev);
    }
    public void setCanScroll(boolean canScroll) {
        isCanScroll = canScroll;
    }
}