package com.box.base.base.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import android.view.Window
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.box.other.blankj.utilcode.util.KeyboardUtils
import com.box.base.base.action.BundleAction
import com.box.base.base.action.HandlerAction
import com.box.base.base.action.KeyboardAction
import com.box.base.base.action.TitleBarAction
import com.box.base.base.viewmodel.BaseViewModel
import com.box.base.network.NetState
import com.box.base.network.NetworkStateManager
import com.box.other.hjq.titlebar.TitleBar
import com.box.com.R
import com.box.mod.game.ModComService
import com.box.other.immersionbar.ImmersionBar
import com.box.other.xpopup.XPopup
import com.box.other.xpopup.impl.LoadingPopupView

abstract class BaseModVmDbActivity<VM : BaseViewModel, DB : ViewDataBinding> : AppCompatActivity(), KeyboardAction, HandlerAction, TitleBarAction, BundleAction {
    protected abstract val mViewModel: VM
    protected lateinit var mDataBinding: DB

    private val immersionBar: ImmersionBar by lazy {
        createStatusBarConfig()
    }
    private val internalTitleBar: TitleBar? by lazy {
        obtainTitleBar(findViewById(Window.ID_ANDROID_CONTENT))
    }

    //内置 LoadingDialog 管理
    private var loadingPopup: LoadingPopupView? = null
    private val mHandler by lazy { Handler(Looper.getMainLooper()) }
    private val showLoadingRunnable = Runnable {
        if (loadingPopup == null) {
            loadingPopup = XPopup.Builder(this)
                .dismissOnTouchOutside(false)
                .isDestroyOnDismiss(true)
                .asLoading("请求网络中...")
                .show() as LoadingPopupView
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDataBinding()
        initBaseObservers()
        initView(savedInstanceState)
        createObserver()
        // 修复软键盘相关的 Bug
        KeyboardUtils.fixAndroidBug5497(this)
    }
    /**
     * 初始化 DataBinding。
     * - 将视图绑定和 ViewModel 绑定集中在此方法中。
     */
    private fun initDataBinding() {
        //    - 直接设置子类的布局，可以减少视图层级，提升渲染性能。
        //    - 如果确实需要一个公共容器，建议通过 <include> 标签在子布局中实现。
        mDataBinding = DataBindingUtil.setContentView(this, layoutId())
        // 将 ViewModel 和生命周期所有者绑定到 DataBinding
        mDataBinding.lifecycleOwner = this
        // 假设布局文件中有一个名为 "vm" 的变量
        //mDataBinding.setVariable(BR.vm, mViewModel)
    }

    /**
     * 注册基础的 UI 变化监听。
     * - 将加载弹窗、标题栏更新、网络状态等公共逻辑的观察者放在一起。
     */
    private fun initBaseObservers() {
        // 标题栏相关监听
        internalTitleBar?.setOnTitleBarListener(this)
        mViewModel.titleT.observe(this) { internalTitleBar?.title = it }
        mViewModel.leftTitleT.observe(this) { internalTitleBar?.leftTitle = it }
        mViewModel.rightTitleT.observe(this) { internalTitleBar?.rightTitle = it }
        mViewModel.titleLine.observe(this) { internalTitleBar?.setLineVisible(it) }
        mViewModel.leftClick.observe(this) { if (it) finish() }

        // 加载弹窗监听
        mViewModel.loadingChange.showDialog.observe(this) { showLoading(it) }
        mViewModel.loadingChange.dismissDialog.observe(this) { dismissLoading() }

        // 网络状态变化监听
        NetworkStateManager.instance.mNetworkStateCallback.observe(this) {
            onNetworkStateChanged(it)
        }

        // 沉浸式状态栏初始化
        if (isStatusBarEnabled()) {
            setupImmersionBar()
        }
    }

    /**
     * 指定子类的布局资源 ID。
     * @return 布局 ID
     */
    abstract fun layoutId(): Int

    /**
     * 初始化视图。在 `onCreate` 中被调用，用于替代原有的 `initView`。
     * @param savedInstanceState 状态保存实例
     */
    abstract fun initView(savedInstanceState: Bundle?)

    /**
     * 订阅 ViewModel 中的 LiveData。在 `onCreate` 中被调用，用于替代原有的 `createObserver`。
     */
    abstract fun createObserver()

    /**
     * 当网络状态发生变化时调用。
     * @param netState 网络状态对象
     */
    abstract fun onNetworkStateChanged(netState: NetState)


    // --- 公共方法封装 ---

    // 【改造】2. 重写 showLoading 方法，实现内置管理
    open fun showLoading(message: String) {
        mHandler.removeCallbacks(showLoadingRunnable)
        mHandler.postDelayed(showLoadingRunnable, 300)
    }

    // 【改造】3. 重写 dismissLoading 方法，实现内置管理
    open fun dismissLoading() {
        mHandler.removeCallbacks(showLoadingRunnable)
        loadingPopup?.dismiss()
        loadingPopup = null
    }

    // 【新增】4. 在 onDestroy 中确保资源被释放
    override fun onDestroy() {
        super.onDestroy()
        dismissLoading() // 确保 Activity 销毁时弹窗关闭
        mHandler.removeCallbacksAndMessages(null)
    }


    override fun setTitle(@StringRes id: Int) {
        setTitle(getString(id))
    }

    override fun setTitle(title: CharSequence?) {
        internalTitleBar?.title = title
    }

    // --- TitleBarAction 实现 ---

    override fun getTitleBar(): TitleBar? {
        return internalTitleBar
    }

    override fun onLeftClick(view: TitleBar) {
        finish()
    }

    // --- 转场动画 ---

    override fun startActivityForResult(intent: Intent, requestCode: Int, options: Bundle?) {
        super.startActivityForResult(intent, requestCode, options)
        // 【修正】进行版本判断
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(OVERRIDE_TRANSITION_OPEN, R.anim.right_in_activity, R.anim.right_out_activity)
        } else {
            @Suppress("DEPRECATION")
            overridePendingTransition(R.anim.right_in_activity, R.anim.right_out_activity)
        }
    }

    override fun finish() {
        super.finish()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(OVERRIDE_TRANSITION_CLOSE, R.anim.left_in_activity, R.anim.left_out_activity)
        } else {
            @Suppress("DEPRECATION")
            overridePendingTransition(R.anim.left_in_activity, R.anim.left_out_activity)
        }
    }


    // --- 沉浸式状态栏配置 ---

    /**
     * 是否启用沉浸式状态栏。
     */
    protected open fun isStatusBarEnabled(): Boolean = mViewModel.isStatusBarEnabled

    /**
     * 状态栏文字是否为深色。
     */
    open fun isStatusBarDarkFont(): Boolean = true

    /**
     * 创建状态栏配置。
     */
    protected open fun createStatusBarConfig(): ImmersionBar {
        return ImmersionBar.with(this)
            .hideBar(mViewModel.barHidT.value) // 根据 ViewModel 状态隐藏或显示
            .statusBarDarkFont(isStatusBarDarkFont())
            .navigationBarColor(R.color.home_navigation_color)
            .autoDarkModeEnable(true, 0.2f)
    }

    /**
     * 应用沉浸式状态栏配置。
     */
    protected open fun setupImmersionBar() {
        immersionBar.init()
        // 将标题栏与状态栏关联，解决状态栏与标题栏重叠问题
        internalTitleBar?.let { ImmersionBar.setTitleBar(this, it) }
    }

    // 辅助方法，用于快速设置状态栏颜色
    open fun setStatusBarWhite() {
        immersionBar.statusBarColor(R.color.white).init()
        internalTitleBar?.setBackgroundResource(R.color.white)
    }

    open fun setStatusBarDark() {
        immersionBar.statusBarColor(R.color.dark_color).init()
        internalTitleBar?.setBackgroundResource(R.color.dark_color)
    }

    open fun setStatusBarTransparent() {
        immersionBar.statusBarColor(R.color.transparent).init()
        internalTitleBar?.setBackgroundResource(R.color.transparent)
    }

    // --- 其他接口实现 ---

    override fun getBundle(): Bundle? = intent.extras

}