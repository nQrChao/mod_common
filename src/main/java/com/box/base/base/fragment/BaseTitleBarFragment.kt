package com.box.base.base.fragment

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding

import com.box.base.base.action.HandlerAction
import com.box.base.base.action.KeyboardAction
import com.box.base.base.action.TitleBarAction
import com.box.base.base.viewmodel.BaseViewModel
import com.box.other.hjq.titlebar.TitleBar
import com.box.common.R
import com.box.common.sdk.eventViewModel
import com.box.other.immersionbar.ImmersionBar

abstract class BaseTitleBarFragment<VM : BaseViewModel, DB : ViewDataBinding> : BaseVmDbFragment<VM, DB>(), TitleBarAction, HandlerAction, KeyboardAction {

    private var titleBar: TitleBar? = null
    private var immersionBar: ImmersionBar? = null

    override fun initTitle() {
        val titleBar = getTitleBar()
        titleBar?.setOnTitleBarListener(this)
        if (titleBar != null) {
            titleBar.title = mViewModel.titleT.value
            titleBar.leftTitle = mViewModel.leftTitleT.value
            titleBar.rightTitle = mViewModel.rightTitleT.value
        }
        if (isStatusBarEnabled()) {
            getStatusBarConfig().init()
            if (titleBar != null) {
                ImmersionBar.setTitleBar(this, titleBar)
            }
        }
    }
    fun isLogin(): Boolean {
        return eventViewModel.isLogin.value ?: false
    }
    override fun onResume() {
        super.onResume()
        if (isStatusBarEnabled()) {
            getStatusBarConfig().init()
        }
    }


    open fun isStatusBarEnabled(): Boolean {
        return mViewModel.isStatusBarEnabled
    }

    open fun getStatusBarConfig(): ImmersionBar {
        if (immersionBar == null) {
            immersionBar = createStatusBarConfig()
        }
        return immersionBar!!
    }

    private fun createStatusBarConfig(): ImmersionBar {
        return ImmersionBar.with(this)
            .hideBar(mViewModel.barHidT.value)
            .navigationBarColor(R.color.white)
            .autoDarkModeEnable(true, 0.2f)
    }


    open fun isStatusBarDarkFont(): Boolean {
        return true
    }


    override fun getTitleBar(): TitleBar? {
        if (titleBar == null) {
            titleBar = obtainTitleBar(view as ViewGroup)
        }
        return titleBar
    }


}