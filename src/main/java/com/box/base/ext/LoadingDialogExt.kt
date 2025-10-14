package com.box.base.ext

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.box.other.blankj.utilcode.util.ColorUtils
import com.box.other.blankj.utilcode.util.Logs
import com.box.common.R
import com.box.other.xpopup.XPopup
import com.box.other.xpopup.impl.LoadingPopupView

@SuppressLint("StaticFieldLeak")
private var loadingPopup: LoadingPopupView? = null

val HANDLER: Handler = Handler(Looper.getMainLooper())

fun isShowDialog(): Boolean {
    return loadingPopup != null && loadingPopup!!.isShow
}

fun AppCompatActivity.showLoadingExt(message: String = "请求网络中") {
    Logs.e("showLoadingExt")
    HANDLER.postDelayed(Runnable {
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
    }, 300)

}


fun Fragment.showLoadingExt(message: String = "请求网络中") {
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
    HANDLER.postDelayed({
        loadingPopup?.dismiss()
        loadingPopup = null
    }, 2000)

}



