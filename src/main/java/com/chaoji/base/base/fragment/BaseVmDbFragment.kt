package com.chaoji.base.base.fragment

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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.chaoji.base.base.action.HandlerAction
import com.chaoji.base.base.action.KeyboardAction
import com.chaoji.base.base.viewmodel.BaseViewModel
import com.chaoji.base.ext.dismissLoadingExt
import com.chaoji.base.ext.getVmClazz
import com.chaoji.base.ext.showLoadingExt
import com.chaoji.base.network.NetState
import com.chaoji.base.network.NetworkStateManager

abstract class BaseVmDbFragment<VM : BaseViewModel, DB : ViewDataBinding> : Fragment(), HandlerAction, KeyboardAction {

    private val handler = Handler(Looper.getMainLooper())

    private var isFirst = true

    lateinit var mViewModel: VM

    lateinit var mActivity: AppCompatActivity

    open fun showLoading(message: String) {
        showLoadingExt(message)
    }

    open fun dismissLoading() {
        dismissLoadingExt()
    }

    private var _binding: DB? = null

    val mDataBinding: DB get() = _binding!!

    abstract fun layoutId(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutId(), null, false)
        mDataBinding.lifecycleOwner = this
        return mDataBinding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as AppCompatActivity
    }

    abstract fun createObserver()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isFirst = true
        mViewModel = createViewModel()
        initView(savedInstanceState)
        initTitle()
        createObserver()
        registerUIChange()
        initData()
    }

    override fun onResume() {
        super.onResume()
        onVisible()
    }

    open fun initData() {
    }

    open fun initTitle() {
    }

    abstract fun lazyLoadData()


    private fun onVisible() {
        if (lifecycle.currentState == Lifecycle.State.STARTED && isFirst) {
            // 延迟加载 防止 切换动画还没执行完毕时数据就已经加载好了，这时页面会有渲染卡顿
            handler.postDelayed({
                lazyLoadData()
                //在Fragment中，只有懒加载过了才能开启网络变化监听
                NetworkStateManager.instance.mNetworkStateCallback.observe(
                    this,
                    Observer {
                        //不是首次订阅时调用方法，防止数据第一次监听错误
                        if (!isFirst) {
                            onNetworkStateChanged(it)
                        }
                    })
                isFirst = false
            }, lazyLoadTime())
        }
    }

    open fun lazyLoadTime(): Long {
        return 300
    }

    abstract fun onNetworkStateChanged(it: NetState)

    private fun registerUIChange() {
        mViewModel.loadingChange.showDialog.observe(this, Observer {
            showLoading(it)
        })
        mViewModel.loadingChange.dismissDialog.observe(this, Observer {
            dismissLoading()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    abstract fun initView(savedInstanceState: Bundle?)

    private fun createViewModel(): VM {
        return ViewModelProvider(this)[getVmClazz(this)]
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }



}