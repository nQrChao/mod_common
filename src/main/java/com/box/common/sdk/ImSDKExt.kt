package com.box.common.sdk

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.box.common.R
import com.box.other.blankj.utilcode.util.ColorUtils
import com.box.other.blankj.utilcode.util.Logs
import com.box.other.xpopup.XPopup
import com.box.other.xpopup.core.BasePopupView
import com.box.other.xpopup.impl.LoadingPopupView

@SuppressLint("StaticFieldLeak")
private var loadingPopup: LoadingPopupView? = null
private var callView: BasePopupView? = null
val HANDLER: Handler = Handler(Looper.getMainLooper())

fun isShowDialog(): Boolean {
    return loadingPopup != null && loadingPopup!!.isShow
}


fun dismissCallingExt() {
    HANDLER.postDelayed({
        callView?.dismiss()
        callView = null
    }, 1000)
}

fun AppCompatActivity.showLoadingExt(message: String = "请求网络中") {
    Logs.e("showLoadingExt")
        loadingPopup = XPopup.Builder(this)
            .dismissOnTouchOutside(false)
            .dismissOnBackPressed(false)
            .isLightNavigationBar(true) //
            .hasStatusBar(true)
            .isDestroyOnDismiss(true)
            .animationDuration(10)
            .navigationBarColor(ColorUtils.getColor(R.color.xpop_shadow))
            .isLightStatusBar(true)
            .hasNavigationBar(true)
            .asLoading(message, LoadingPopupView.Style.ProgressBar)
            .show() as LoadingPopupView
}


fun Fragment.showLoadingExt(message: String = "请求网络中") {
    loadingPopup = null
    activity?.let {
        HANDLER.postDelayed(Runnable {
            loadingPopup = XPopup.Builder(activity)
                .dismissOnTouchOutside(false)
                .dismissOnBackPressed(false)
                .isLightNavigationBar(true) //
                .isDestroyOnDismiss(true)
                .hasStatusBar(true)
                .animationDuration(10)
                .navigationBarColor(ColorUtils.getColor(R.color.xpop_shadow))
                .isLightStatusBar(true)
                .hasNavigationBar(true)
                //.asLoading(null, R.layout.custom_loading_popup)
                .asLoading(message, LoadingPopupView.Style.ProgressBar)
                .show() as LoadingPopupView
        }, 300)
    }
}


fun dismissLoadingExt() {
    loadingPopup?.dismiss()
    loadingPopup = null
}


fun isAlphaNumeric(input: String): Boolean {
    val regex = "^[a-zA-Z0-9]+$"
    return input.matches(regex.toRegex()) // 返回true表示输入字符串只包含字母和数字，false表示不仅限于这两种类型
}

fun isLetterDigit(str: String): Boolean {
    var isDigit = false
    var isLetter = false
    for (i in str) {
        if (Character.isDigit(i)) {
            isDigit = true
        }
        if (Character.isLetter(i)) {
            isLetter = true
        }
    }
    val regex = "^[a-zA-Z0-9]+$"
    return isDigit && isLetter && str.matches(regex.toRegex())
}
