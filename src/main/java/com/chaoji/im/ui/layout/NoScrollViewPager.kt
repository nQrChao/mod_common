package com.chaoji.im.ui.layout

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager
import kotlin.math.abs

class NoScrollViewPager @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null) :
    ViewPager(context, attrs) {

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }

    @Suppress("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }

    override fun executeKeyEvent(event: KeyEvent): Boolean {
        return false
    }

    override fun setCurrentItem(item: Int) {
        super.setCurrentItem(item, abs(currentItem - item) == 1)
    }
}