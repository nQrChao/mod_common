package com.box.common.event

import android.app.Activity

import com.box.other.blankj.utilcode.util.ColorUtils

import com.box.common.event.ImEvent.Companion.Instance

import com.box.common.R

import com.box.other.xpopup.XPopup
import com.box.other.xpopup.core.BasePopupView
import com.box.other.xpopup.enums.PopupAnimation
import com.box.other.xpopup.interfaces.SimpleCallback


val imEvent: ImEvent by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
    Instance
}

class ImEvent {
    companion object {
        val Instance: ImEvent by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            ImEvent()
        }
        var callView: BasePopupView? = null
        var takeCallPopupView: BasePopupView? = null
    }

    fun init() {

    }


    fun getCallingPop(activity: Activity) {
        XPopup.Builder(activity)
            .animationDuration(500)
            .navigationBarColor(ColorUtils.getColor(R.color.xpop_call_nv_color))
            .hasNavigationBar(true)
            .isDestroyOnDismiss(true)
            .isLightStatusBar(false)
            .hasShadowBg(true)
            .hasStatusBarShadow(false)
            .dismissOnBackPressed(false)
            .dismissOnTouchOutside(false)
            .popupAnimation(PopupAnimation.TranslateFromTop)
            .setPopupCallback(object : SimpleCallback() {
                override fun onShow(popupView: BasePopupView?) {
                    super.onShow(popupView)
                }

                override fun onDismiss(popupView: BasePopupView?) {
                    super.onDismiss(popupView)
                }
            })
            .asCustom(takeCallPopupView)
            .show()
    }


}