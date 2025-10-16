package com.box.mod.view

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.box.base.base.action.ActivityAction
import com.box.base.base.action.ClickAction
import com.box.com.R
import com.box.com.databinding.ViewFloatBind
import com.box.common.appContext
import com.box.common.glide.GlideApp
import com.box.common.utils.floattoast.draggable.SpringScaleDraggable
import com.box.common.data.model.ModModel

@SuppressLint("ViewConstructor")
class ModFloatView(activity: Activity, private var icon: Drawable, private var onClickView: () -> Unit?) :
    LinearLayout(activity), ClickAction, ActivityAction {
    private var modModel: ModModel = ViewModelProvider(getActivity() as AppCompatActivity)[ModModel::class.java]
    private lateinit var floatBind: ViewFloatBind

    init {
        onCreate()
    }

    fun onCreate() {
        floatBind = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.app_floatview,
            this,
            true
        )
        GlideApp.with(appContext)
            .load(icon)
            .transform(RoundedCorners(12))
            .error(R.drawable.tips_error_ic)
            .into(floatBind.floatImage)

        // 将点击事件设置在最外层的容器上
        floatBind.floatView.setOnClickListener { onClick(it) }
    }

    override fun onClick(view: View) {
        if (view === floatBind.floatView) {
            onClickView.invoke()
        }
    }

    /**
     * 根据传入的停靠位置，更新视图内部的UI元素。
     * @param side 停靠位置，值为 SpringHideDraggable.LEFT 或 SpringHideDraggable.RIGHT
     */
    //17 23
    fun updateViewForSide(side: Int) {
        val params = floatBind.floatImage.layoutParams as  ViewGroup.MarginLayoutParams
        val rightMarginInPx = context.resources.getDimensionPixelSize(R.dimen.idp_17)
        val leftMarginInPx = context.resources.getDimensionPixelSize(R.dimen.idp_24)
        //val container = floatBind.floatView as? ViewGroup ?: return // 获取子元素的容器
        // 为了重新排序，我们先移除它们
        //container.removeView(bgView)
        if (side == SpringScaleDraggable.LEFT) {
            // 当停在左边时
            params.leftMargin = leftMarginInPx
            floatBind.floatImage.layoutParams = params
            floatBind.floatBg.scaleX  = -1f
            //container.addView(bgView) // 再添加图标
        } else { // side == SpringHideDraggable.RIGHT
            // 当停在右边时
            params.leftMargin = rightMarginInPx
            floatBind.floatImage.layoutParams = params
            floatBind.floatBg.scaleX  = 1f
            //container.addView(bgView) // 先添加图标
        }
    }


    companion object {
        fun getView(activity: Activity, icon: Drawable, onClickView: () -> Unit?): ModFloatView {
            return ModFloatView(activity, icon, onClickView)
        }
    }
}
