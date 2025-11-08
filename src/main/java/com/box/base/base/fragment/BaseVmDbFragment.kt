package com.box.base.base.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.box.base.base.action.HandlerAction
import com.box.base.base.action.KeyboardAction
import com.box.base.base.viewmodel.BaseViewModel
import com.box.base.network.NetState
import com.box.base.network.NetworkStateManager
import com.box.common.appViewModel
import com.box.common.data.model.ModUserInfo
import com.box.common.eventViewModel
import com.box.other.xpopup.XPopup
import com.box.other.xpopup.impl.LoadingPopupView

abstract class BaseVmDbFragment<VM : BaseViewModel, DB : ViewDataBinding> : Fragment(), HandlerAction, KeyboardAction {
    private var _binding: DB? = null
    protected val mDataBinding: DB get() = _binding!!
    protected abstract val mViewModel: VM

    protected lateinit var mActivity: AppCompatActivity

    private var isFirstLoad = true
    private val mHandler by lazy { Handler(Looper.getMainLooper()) }

    // 内置 LoadingDialog 管理
    private var loadingPopup: LoadingPopupView? = null
    private val showLoadingRunnable = Runnable {
        if (loadingPopup == null) {
            loadingPopup = XPopup.Builder(requireActivity())
                .dismissOnTouchOutside(false)
                .isDestroyOnDismiss(true)
                .asLoading("请求网络中...")
                .show() as LoadingPopupView
        }
    }

    abstract fun layoutId(): Int
    abstract fun initView(savedInstanceState: Bundle?)
    abstract fun createObserver()
    abstract fun lazyLoadData()
    abstract fun onNetworkStateChanged(netState: NetState)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as AppCompatActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutId(), container, false)
        mDataBinding.lifecycleOwner = viewLifecycleOwner
        return mDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(savedInstanceState)
        initBaseObservers()
        createObserver()
    }

    override fun onResume() {
        super.onResume()
        if (isFirstLoad) {
            mHandler.postDelayed({
                lazyLoadData()
            }, lazyLoadTime())
            isFirstLoad = false
        }
    }

    fun isLogin(): Boolean {
        return eventViewModel.isLogin.value ?: false
    }

    fun userInfo(): ModUserInfo? {
        return appViewModel.modUserInfo.value
    }
    /**
     * - 以便子类可以重写此方法，添加自己的观察者
     */
    protected open fun initBaseObservers() {
        // 加载弹窗监听
        mViewModel.loadingChange.showDialog.observe(viewLifecycleOwner) { showLoading(it) }
        mViewModel.loadingChange.dismissDialog.observe(viewLifecycleOwner) { dismissLoading() }

        // 网络状态监听
        NetworkStateManager.instance.mNetworkStateCallback.observe(viewLifecycleOwner) {
            onNetworkStateChanged(it)
        }
    }

    open fun showLoading(message: String) {
        mHandler.removeCallbacks(showLoadingRunnable)
        mHandler.postDelayed(showLoadingRunnable, 300)
    }

    open fun dismissLoading() {
        mHandler.removeCallbacks(showLoadingRunnable)
        loadingPopup?.dismiss()
        loadingPopup = null
    }

    open fun lazyLoadTime(): Long = 200L

    override fun onDestroyView() {
        super.onDestroyView()
        dismissLoading() // 确保 Fragment 视图销毁时弹窗关闭
        mHandler.removeCallbacksAndMessages(null)
        _binding = null
    }


    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
    }
}