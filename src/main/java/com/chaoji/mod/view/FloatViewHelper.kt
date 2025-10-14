package com.chaoji.mod.view

import android.app.Activity
import android.graphics.drawable.Drawable
import android.view.Gravity
import com.chaoji.im.utils.floattoast.XToast
import com.chaoji.im.utils.floattoast.draggable.SpringScaleDraggable
import com.chaoji.other.blankj.utilcode.util.SizeUtils

/**
 * Activity 级悬浮窗的帮助对象 (Kotlin 单例)
 * 用于在不同的 Activity 中方便地创建样式统一的悬浮窗
 */
object FloatViewHelper {
    /**
     * [已重构] 创建并显示本地游戏悬浮窗
     * @param activity 当前的 Activity 实例
     * @return 返回创建的 XToast 实例，以便 Activity 可以管理其生命周期
     */
    fun createModFloatView(
        activity: Activity,
        icon: Drawable,
        onClickView: () -> Unit?
    ): XToast<*> {

        // 1. 创建您的自定义视图实例
        val modFloatView = ModFloatView.getView(activity, icon, onClickView)

        // 2. 创建可拖拽处理类实例，并设置默认停靠在右侧
        val draggable =
            SpringScaleDraggable(
                0.6f,
                5000L,
                SpringScaleDraggable.RIGHT,
                false
            )

        // 3. 设置监听器，将拖拽行为与UI更新连接起来
        draggable.setOnSideChangedListener { side -> // 当拖拽类报告位置变化时，立即通知自定义视图更新其内容
            modFloatView.updateViewForSide(side)
        }

        // 4. 创建 XToast 实例，并将视图和拖拽处理器设置进去
        val floatToast = XToast<XToast<*>>(activity)
            .setContentView(modFloatView)
            .setGravity(Gravity.END or Gravity.TOP)
            .setYOffset(SizeUtils.dp2px(252f))
            .setDraggable(draggable)

        floatToast.show()
        // 6. 返回创建的实例
        return floatToast
    }

    fun createDownFloatView(
        activity: Activity,
        icon: Drawable,
        fileUrl:String,
        onClickView: () -> Unit?,
        downOver: () -> Unit?
    ): XToast<*> {
        val downFloatView = DownFloatView.getView(activity, icon,fileUrl, onClickView,downOver)
        val draggable =
            SpringScaleDraggable(
                0.6f,
                5000L,
                SpringScaleDraggable.ORIENTATION_HORIZONTAL,
                false
            )
        draggable.setOnSideChangedListener { side ->
            downFloatView.updateViewForSide(side)
        }

        val floatToast = XToast<XToast<*>>(activity)
            .setContentView(downFloatView)
            .setGravity(Gravity.CENTER)
            .setYOffset(SizeUtils.dp2px(25f))
            .setDraggable(draggable)
        floatToast.show()
        return floatToast
    }
}
