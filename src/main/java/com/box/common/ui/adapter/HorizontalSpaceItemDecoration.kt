package com.box.common.ui.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * 为 RecyclerView 的横向列表添加间距
 * @param horizontalSpaceInDp Int  你希望设置的水平间距，单位是 dp
 */
class HorizontalSpaceItemDecoration(private val horizontalSpaceInDp: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        // 将 dp 转换为像素
        val spaceInPixels = (horizontalSpaceInDp * parent.context.resources.displayMetrics.density).toInt()
        val position = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount ?: 0
        // 我们只关心左右间距
        outRect.top = 0
        outRect.bottom = 0
        // 为所有 item 设置右边距
        outRect.right = spaceInPixels
        // 如果是第一个 item，额外设置左边距（作为列表的起始边距）
        if (position == 0) {
            outRect.left = spaceInPixels
        } else {
            outRect.left = 0
        }
    }
}