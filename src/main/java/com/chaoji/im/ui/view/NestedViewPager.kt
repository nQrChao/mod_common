package com.chaoji.im.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.chaoji.im.utils.ScrollableContainer

class NestedViewPager @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ViewPager(context, attrs), NestedScrollingParent, NestedScrollingChild {

    private val parentHelper = NestedScrollingParentHelper(this)
    private val childHelper = NestedScrollingChildHelper(this)

    init {
        isNestedScrollingEnabled = true
    }

    override fun setNestedScrollingEnabled(enabled: Boolean) {
        childHelper.isNestedScrollingEnabled = enabled
    }
    override fun isNestedScrollingEnabled(): Boolean {
        return childHelper.isNestedScrollingEnabled
    }

    override fun startNestedScroll(axes: Int): Boolean {
        return childHelper.startNestedScroll(axes, ViewCompat.TYPE_TOUCH)
    }

    override fun stopNestedScroll() {
        childHelper.stopNestedScroll(ViewCompat.TYPE_TOUCH)
    }

    override fun hasNestedScrollingParent(): Boolean {
        return childHelper.hasNestedScrollingParent(ViewCompat.TYPE_TOUCH)
    }

    override fun dispatchNestedScroll(
        dxConsumed: Int, dyConsumed: Int,
        dxUnconsumed: Int, dyUnconsumed: Int,
        offsetInWindow: IntArray?
    ): Boolean {
        return childHelper.dispatchNestedScroll(
            dxConsumed, dyConsumed,
            dxUnconsumed, dyUnconsumed,
            offsetInWindow,
            ViewCompat.TYPE_TOUCH
        )
    }

    override fun dispatchNestedPreScroll(
        dx: Int, dy: Int,
        consumed: IntArray?,
        offsetInWindow: IntArray?
    ): Boolean {
        return childHelper.dispatchNestedPreScroll(
            dx, dy,
            consumed, offsetInWindow,
            ViewCompat.TYPE_TOUCH
        )
    }

    override fun dispatchNestedFling(
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {
        return childHelper.dispatchNestedFling(velocityX, velocityY, consumed)
    }

    override fun dispatchNestedPreFling(
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        return childHelper.dispatchNestedPreFling(velocityX, velocityY)
    }

    override fun onStartNestedScroll(
        child: View,
        target: View,
        nestedScrollAxes: Int
    ): Boolean {
        return (nestedScrollAxes and ViewCompat.SCROLL_AXIS_VERTICAL) != 0
    }

    override fun onNestedScrollAccepted(
        child: View,
        target: View,
        axes: Int
    ) {
        parentHelper.onNestedScrollAccepted(child, target, axes)
        startNestedScroll(axes)
    }

    override fun onStopNestedScroll(target: View) {
        parentHelper.onStopNestedScroll(target)
        stopNestedScroll()
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int
    ) {
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, null)
    }

    override fun onNestedPreScroll(
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray
    ) {
        dispatchNestedPreScroll(dx, dy, consumed, null)
    }

    override fun onNestedFling(
        target: View,
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {
        return dispatchNestedFling(velocityX, velocityY, consumed)
    }

    override fun onNestedPreFling(
        target: View,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        return dispatchNestedPreFling(velocityX, velocityY)
    }

    override fun getNestedScrollAxes(): Int {
        return parentHelper.nestedScrollAxes
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL)
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> stopNestedScroll()
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return try {
            super.onInterceptTouchEvent(ev)
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return try {
            super.onTouchEvent(ev)
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    override fun canScrollVertically(direction: Int): Boolean {
        val currentView = findCurrentScollingView()
        return currentView?.canScrollVertically(direction) ?: super.canScrollVertically(direction)
    }

    private fun findCurrentScollingView(): View? {
        val currentAdapter: PagerAdapter = adapter ?: return null

        // 针对 FragmentStatePagerAdapter / FragmentPagerAdapter
        if (currentAdapter is FragmentStatePagerAdapter) {
            val currentFragment: Fragment? = currentAdapter.getItem(currentItem)
            if (currentFragment is ScrollableContainer) {
                return currentFragment.getScrollableView()
            }
        }

        // 如果你使用其他类型的 Adapter，需要在这里添加相应的逻辑
        // 例如，如果你的 Adapter 直接管理 View
        // val currentView = getChildAt(currentItem)
        // return currentView?.findViewById(R.id.your_scrollable_view_id)

        return null
    }
}
