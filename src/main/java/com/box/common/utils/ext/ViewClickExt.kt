package com.box.common.utils.ext // 或者你喜欢的任何工具类包名

import android.os.SystemClock
import android.view.View
import androidx.databinding.BindingAdapter
import com.box.com.R

/**
 * 为 View 添加一个带有防抖功能的点击监听器。
 * 这是一个扩展函数，可以像调用系统自带的 setOnClickListener 一样方便地使用。
 *
 * @param delayMillis 防抖的间隔时间，单位是毫秒，默认为 1000ms (1秒)
 * @param onSingleClick 当点击事件发生且不在防抖间隔内时执行的回调 lambda
 */
fun View.setOnSingleClickListener(delayMillis: Long = 1000L, onSingleClick: (View) -> Unit) {
    // 使用 setTag 和 lastClickTime 变量来为每个 View 单独记录点击时间
    // 每个 View 都有自己的 lastClickTime，互不干扰。
    // R.id.last_click_time 是一个在 ares/values/ids.xml 中定义的ID，用于作为 setTag 的 key。
    setOnClickListener {
        val lastClickTime = (it.getTag(R.id.last_click_time) as? Long) ?: 0L
        val currentTime = SystemClock.uptimeMillis()

        if (currentTime - lastClickTime > delayMillis) {
            // 更新当前 View 的最后点击时间
            it.setTag(R.id.last_click_time, currentTime)
            // 执行传入的点击逻辑
            onSingleClick(it)
        }
    }
}


/**
 * 用于 Data Binding 的防抖点击属性。
 *
 * 这个 Binding Adapter 允许我们在 XML 中使用 app:onSingleClick 和 app:singleClickDelay 属性。
 *
 * @param view 自动传入的、应用此属性的 View
 * @param onClickListener XML 中绑定的点击事件，例如 @{() -> viewModel.doSomething()}
 * @param delayMillis XML 中可选的延迟时间，例如 @{1500L}
 */
@BindingAdapter(value = ["onSingleClick", "singleClickDelay"], requireAll = false)
fun setOnSingleClick(view: View, onClickListener: View.OnClickListener?, delayMillis: Long?) {
    if (onClickListener == null) {
        // 如果没有设置点击事件，则清空监听器
        view.setOnClickListener(null)
        return
    }

    // 使用我们之前创建的 setOnSingleClickListener 扩展函数来实现功能
    view.setOnSingleClickListener(delayMillis ?: 1000L) {
        onClickListener.onClick(it)
    }
}