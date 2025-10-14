package com.chaoji.base.base.action

import android.graphics.drawable.Drawable
import android.view.ViewGroup
import androidx.annotation.StringRes
import com.chaoji.other.hjq.titlebar.OnTitleBarListener
import com.chaoji.other.hjq.titlebar.TitleBar

interface TitleBarAction : OnTitleBarListener {

    fun getTitleBar(): TitleBar?

    override fun onLeftClick(view: TitleBar) {}

    override fun onTitleClick(view: TitleBar) {}

    override fun onRightClick(view: TitleBar) {}

    fun setTitle(@StringRes id: Int) {
        getTitleBar()?.setTitle(id)
    }

    fun setTitle(title: CharSequence?) {
        getTitleBar()?.title = title
    }

    fun setLeftTitle(id: Int) {
        getTitleBar()?.setLeftTitle(id)
    }

    fun setLeftTitle(text: CharSequence?) {
        getTitleBar()?.leftTitle = text
    }

    fun getLeftTitle(): CharSequence? {
        return getTitleBar()?.leftTitle
    }

    fun setRightTitle(id: Int) {
        getTitleBar()?.setRightTitle(id)
    }

    fun setRightTitle(text: CharSequence?) {
        getTitleBar()?.rightTitle = text
    }

    fun getRightTitle(): CharSequence? {
        return getTitleBar()?.rightTitle
    }

    fun setLeftIcon(id: Int) {
        getTitleBar()?.setLeftIcon(id)
    }

    fun setLeftIcon(drawable: Drawable?) {
        getTitleBar()?.leftIcon = drawable
    }

    fun getLeftIcon(): Drawable? {
        return getTitleBar()?.leftIcon
    }

    fun setRightIcon(id: Int) {
        getTitleBar()?.setRightIcon(id)
    }

    fun setRightIcon(drawable: Drawable?) {
        getTitleBar()?.rightIcon = drawable
    }

    fun getRightIcon(): Drawable? {
        return getTitleBar()?.rightIcon
    }

    fun obtainTitleBar(group: ViewGroup?): TitleBar? {
        if (group == null) {
            return null
        }
        for (i in 0 until group.childCount) {
            val view = group.getChildAt(i)
            if (view is TitleBar) {
                return view
            }
            if (view is ViewGroup) {
                val titleBar = obtainTitleBar(view)
                if (titleBar != null) {
                    return titleBar
                }
            }
        }
        return null
    }
}