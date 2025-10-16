package com.box.base.base.fragment

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.box.base.base.action.TitleBarAction
import com.box.base.base.viewmodel.BaseViewModel
import com.box.com.R
import com.box.other.hjq.titlebar.TitleBar
import com.box.other.immersionbar.ImmersionBar

abstract class BaseTitleBarFragment<VM : BaseViewModel, DB : ViewDataBinding> :
    BaseVmDbFragment<VM, DB>(), TitleBarAction {

    private val titleBar: TitleBar? by lazy {
        obtainTitleBar(view as? ViewGroup)
    }

    private val immersionBar: ImmersionBar by lazy {
        createStatusBarConfig()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupStatusBar()
    }

    override fun initBaseObservers() {
        super.initBaseObservers()
        titleBar?.setOnTitleBarListener(this)
        mViewModel.titleT.observe(viewLifecycleOwner) { titleBar?.title = it }
        mViewModel.leftTitleT.observe(viewLifecycleOwner) { titleBar?.leftTitle = it }
        mViewModel.rightTitleT.observe(viewLifecycleOwner) { titleBar?.rightTitle = it }
    }

    override fun onResume() {
        super.onResume()
        if (isStatusBarEnabled()) {
            immersionBar.init()
        }
    }

    protected open fun isStatusBarEnabled(): Boolean = mViewModel.isStatusBarEnabled
    protected open fun isStatusBarDarkFont(): Boolean = true

    private fun setupStatusBar() {
        if (isStatusBarEnabled()) {
            immersionBar.init()
            titleBar?.let { ImmersionBar.setTitleBar(this, it) }
        }
    }

    private fun createStatusBarConfig(): ImmersionBar {
        return ImmersionBar.with(this)
            .hideBar(mViewModel.barHidT.value)
            .statusBarDarkFont(isStatusBarDarkFont())
            .navigationBarColor(R.color.white)
            .autoDarkModeEnable(true, 0.2f)
    }

    override fun getTitleBar(): TitleBar? {
        return titleBar
    }
}