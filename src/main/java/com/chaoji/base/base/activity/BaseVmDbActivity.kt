package com.chaoji.base.base.activity

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.chaoji.other.blankj.utilcode.util.KeyboardUtils
import com.chaoji.base.base.action.BundleAction
import com.chaoji.base.base.action.HandlerAction
import com.chaoji.base.base.action.KeyboardAction
import com.chaoji.base.base.action.TitleBarAction
import com.chaoji.base.base.viewmodel.BaseViewModel
import com.chaoji.base.ext.getVmClazz
import com.chaoji.base.network.NetState
import com.chaoji.base.network.NetworkStateManager
import com.chaoji.other.hjq.titlebar.TitleBar
import com.chaoji.common.R
import com.chaoji.common.databinding.BaseFragmentBinding
import com.chaoji.im.sdk.dismissLoadingExt
import com.chaoji.im.sdk.eventViewModel
import com.chaoji.im.sdk.showLoadingExt
import com.chaoji.other.blankj.utilcode.util.Logs
import com.chaoji.other.immersionbar.ImmersionBar

abstract class BaseVmDbActivity<VM : BaseViewModel, DB : ViewDataBinding> : AppCompatActivity(), KeyboardAction, HandlerAction, TitleBarAction, BundleAction {
    lateinit var mViewModel: VM
    lateinit var mDataBinding: DB
    private var immersionBar: ImmersionBar? = null
    private var titleBar: TitleBar? = null
    open fun showLoading(message: String) {
        showLoadingExt(message)
    }

    open fun dismissLoading() {
        dismissLoadingExt()
    }

    abstract fun layoutId(): Int

    private lateinit var baseBinding: BaseFragmentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 使用DataBinding标准API加载基础容器布局
        baseBinding = DataBindingUtil.setContentView(this, R.layout.base_fragment)
        val childLayoutId = layoutId()
        Logs.e("BaseActivity", "Child Layout ID: $childLayoutId") // 添加这行
        // 动态加载子类布局并绑定
        mDataBinding = DataBindingUtil.inflate(
            layoutInflater,
            layoutId(),
            baseBinding.content,
            false
        )
        if (mDataBinding?.root != null) {
            baseBinding.content.addView(mDataBinding.root)
        } else {
            Logs.e("BaseActivity", "mDataBinding.root is null after inflation!")
        }
        // 绑定生命周期
        baseBinding.content.post {
            mDataBinding.lifecycleOwner = this
            baseBinding.lifecycleOwner = this
        }
        init(savedInstanceState)
    }

    private fun validateDataBinding() {
        // 强制检查布局标记
        if (!::mDataBinding.isInitialized) {
            throw IllegalStateException("DataBinding未正确初始化，请检查布局文件是否包含<layout>标签")
        }
    }

    private fun init(savedInstanceState: Bundle?) {
//        mViewModel = createViewModel()
//        baseBinding.vm = mViewModel
        mViewModel = ViewModelProvider(this)[getVmClazz(this)]
        baseBinding.vm = mViewModel

        titleBar = getTitleBar()
        titleBar?.setOnTitleBarListener(this)

        if (isStatusBarEnabled()) {
            getStatusBarConfig().init()
            if (titleBar != null) {
                ImmersionBar.setTitleBar(this, titleBar)
            }
        }

        mViewModel.titleT.observe(this) {
            titleBar?.title = it
        }
        mViewModel.leftTitleT.observe(this) {
            titleBar?.leftTitle = it
        }
        mViewModel.rightTitleT.observe(this) {
            if (it == "交易须知") {
                titleBar?.setRightIcon(R.drawable.info_ic)
                titleBar?.setRightIconPadding(15)
                titleBar?.setRightIconSize(50, 50)
                titleBar?.setRightIconGravity(Gravity.START)
            } else if (it == "没有账号？立即注册 >") {
                titleBar?.setRightTitleColor(R.color.mod_url_text_color)
            }
            titleBar?.setRightTitleColor(R.color.text_default_color)
            titleBar?.rightTitle = it
        }

        mViewModel.titleLine.observe(this) {
            titleBar?.setLineVisible(it)
        }

        registerUiChange()
        initView(savedInstanceState)
        KeyboardUtils.fixAndroidBug5497(this)
        createObserver()
        mViewModel.leftClick.observe(this) {
            if (it) {
                finish()
            }
        }

        NetworkStateManager.instance.mNetworkStateCallback.observe(this, Observer {
            onNetworkStateChanged(it)
        })

    }

    abstract fun onNetworkStateChanged(it: NetState)

    abstract fun initView(savedInstanceState: Bundle?)

    override fun setTitle(@StringRes id: Int) {
        title = getString(id)
    }

    override fun setTitle(title: CharSequence?) {
        setTitle(title)
        getTitleBar()?.title = title
    }

    override fun getTitleBar(): TitleBar? {
        if (titleBar == null) {
            titleBar = obtainTitleBar(getContentView())
        }
        return titleBar
    }

    override fun onLeftClick(view: TitleBar) {
        finish()
    }

    override fun onRightClick(view: TitleBar) {
        super.onRightClick(view)
    }

    private fun createViewModel(): VM {
        return ViewModelProvider(this)[getVmClazz(this)]
    }

    abstract fun createObserver()

    fun isLogin(): Boolean {
        return eventViewModel.isLogin.value ?: false
    }


    private fun registerUiChange() {
        mViewModel.loadingChange.showDialog.observe(this, Observer {
            showLoading(it)
        })
        mViewModel.loadingChange.dismissDialog.observe(this, Observer {
            dismissLoading()
        })
    }

    override fun startActivityForResult(intent: Intent, requestCode: Int, options: Bundle?) {
        super.startActivityForResult(intent, requestCode, options)
        overridePendingTransition(R.anim.right_in_activity, R.anim.right_out_activity)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.left_in_activity, R.anim.left_out_activity)
    }


    protected open fun initSoftKeyboard() {
        getContentView()?.setOnClickListener {
            hideKeyboard(currentFocus)
        }
    }

    open fun getContentView(): ViewGroup? {
        return findViewById(Window.ID_ANDROID_CONTENT)
    }

    override fun startActivity(intent: Intent) {
        return super<AppCompatActivity>.startActivity(intent)
    }


    protected open fun isStatusBarEnabled(): Boolean {
        return mViewModel.isStatusBarEnabled
    }

    open fun isStatusBarDarkFont(): Boolean {
        return true
    }

    open fun getStatusBarConfig(): ImmersionBar {
        if (immersionBar == null) {
            immersionBar = createStatusBarConfig()
        }
        return immersionBar!!
    }

    open fun setStatusBarWhite(): ImmersionBar {
        getStatusBarConfig().statusBarColor(R.color.white)
        getTitleBar()?.setBackgroundResource(R.color.white)
        return immersionBar!!
    }

    open fun setStatusBarDark(): ImmersionBar {
        getStatusBarConfig().statusBarColor(R.color.dark_color)
        getTitleBar()?.setBackgroundResource(R.color.dark_color)
        return immersionBar!!
    }

    open fun setStatusBarTransparent(): ImmersionBar {
        getStatusBarConfig().statusBarColor(R.color.transparent)
        getTitleBar()?.setBackgroundResource(R.color.transparent)
        return immersionBar!!
    }


    protected open fun createStatusBarConfig(): ImmersionBar {
        return ImmersionBar.with(this)
            .hideBar(mViewModel.barHidT.value)
            .statusBarDarkFont(isStatusBarDarkFont())
            .navigationBarColor(R.color.home_navigation_color)
            .autoDarkModeEnable(true, 0.2f)
    }

    override fun getBundle(): Bundle? {
        return intent.extras
    }

}