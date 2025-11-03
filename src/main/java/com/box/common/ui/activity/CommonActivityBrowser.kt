package com.box.common.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.webkit.WebView
import androidx.activity.viewModels
import androidx.lifecycle.viewModelScope
import com.box.base.base.action.StatusAction
import com.box.base.base.activity.BaseModVmDbActivity
import com.box.base.base.viewmodel.BaseViewModel
import com.box.base.callback.databind.BooleanObservableField
import com.box.base.network.NetState
import com.box.com.databinding.CommonActivityBrowserBinding
import com.box.common.MMKVConfig
import com.box.common.eventViewModel
import com.box.common.getDetailedInformation
import com.box.common.ui.layout.StatusLayout
import com.box.common.ui.view.BrowserView
import com.box.common.utils.logsE
import com.box.other.blankj.utilcode.util.ActivityUtils
import com.box.other.blankj.utilcode.util.GsonUtils
import com.box.other.blankj.utilcode.util.Logs
import com.box.other.blankj.utilcode.util.StringUtils
import com.box.other.hjq.toast.Toaster
import com.box.other.immersionbar.immersionBar
import com.box.other.scwang.smart.refresh.layout.api.RefreshLayout
import com.box.other.scwang.smart.refresh.layout.listener.OnRefreshListener
import kotlinx.coroutines.launch
import com.box.com.R as RC

class CommonActivityBrowser : BaseModVmDbActivity<CommonActivityBrowser.Model, CommonActivityBrowserBinding>(), StatusAction, OnRefreshListener {
    override val mViewModel: Model by viewModels()
    override fun layoutId(): Int = RC.layout.common_activity_browser

    companion object {
        const val INTENT_KEY_URL: String = "url"
        fun start(context: Context, url: String) {
            if (TextUtils.isEmpty(url)) {
                return
            }
            val intent = Intent(context, CommonActivityBrowser::class.java)
            intent.putExtra(INTENT_KEY_URL, url)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            ActivityUtils.startActivity(intent)
        }

    }

    override fun initView(savedInstanceState: Bundle?) {
        mDataBinding.vm = mViewModel
        mDataBinding.click = ProxyClick()
        immersionBar {
            titleBar(mDataBinding.titleBar)
            navigationBarColor(RC.color.white)
            init()
        }
        mDataBinding.wvBrowserView.setLifecycleOwner(this)
        mDataBinding.slBrowserRefresh.setOnRefreshListener(this)
        showLoading()
        mDataBinding.wvBrowserView.apply {
            setBrowserViewClient(AppBrowserViewClient())
            setBrowserChromeClient(AppBrowserChromeClient(this))
            loadUrl(intent.getStringExtra(INTENT_KEY_URL) ?: "")
        }

        getTitleBar()?.rightView?.setOnLongClickListener {
            Toaster.show("rightView")
            mDataBinding.hlBrowserHint.visibility = View.GONE
            mDataBinding.nestedScrollView.visibility = View.VISIBLE
            mDataBinding.textLayout.visibility = View.VISIBLE
            mDataBinding.textView.visibility = View.VISIBLE
            eventViewModel.viewModelScope.launch {
                val text = getDetailedInformation(this@CommonActivityBrowser, true)
                val modInitStr = GsonUtils.toJson(MMKVConfig.modInit)
                if (!StringUtils.isEmpty(modInitStr)) {
                    text.append("\n\nMarketInit:${modInitStr}")
                    logsE("marketInitStr:${text}")
                } else {
                    text.append("\n\nNO-MarketInit")
                }
                logsE( "text.toString():${text}")
                mDataBinding.textView.text = text.toString()

            }
            true
        }

        getTitleBar()?.titleView?.setOnLongClickListener {
            Logs.getConfig().setLogSwitch(true)
            true
        }

        getTitleBar()?.leftView?.setOnClickListener {
            if (mDataBinding.wvBrowserView.canGoBack()) {
                mDataBinding.wvBrowserView.goBack()
            } else {
                finish()
            }
        }

    }

    override fun createObserver() {

    }

    override fun onNetworkStateChanged(it: NetState) {
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        // 只有在按下返回键且 WebView 可以返回时才处理
        if (keyCode == KeyEvent.KEYCODE_BACK && mDataBinding.wvBrowserView.canGoBack()) {
            mDataBinding.wvBrowserView.goBack()
            return true
        }
        // 其他情况，或 WebView 无法返回，调用父类方法
        return super.onKeyDown(keyCode, event)
    }


    private fun reload() {
        mDataBinding.wvBrowserView.reload()
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        reload()
    }

    private inner class AppBrowserViewClient : BrowserView.BrowserViewClient() {
        override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
            // 这里为什么要用延迟呢？因为加载出错之后会先调用 onReceivedError 再调用 onPageFinished
            post {
                showError(object : StatusLayout.OnRetryListener {
                    override fun onRetry(layout: StatusLayout) {
                        reload()
                    }
                })
            }
        }

        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
            mDataBinding.pbBrowserProgress.visibility = View.VISIBLE
        }

        override fun onPageFinished(view: WebView, url: String) {
            mDataBinding.pbBrowserProgress.visibility = View.GONE
            mDataBinding.slBrowserRefresh.finishRefresh()
            showComplete()
        }
    }

    private inner class AppBrowserChromeClient constructor(view: BrowserView) : BrowserView.BrowserChromeClient(view) {


        override fun onReceivedTitle(view: WebView, title: String?) {
            if (title == null) {
                return
            }
            mViewModel.titleT.value = title
        }

        override fun onReceivedIcon(view: WebView, icon: Bitmap?) {
            if (icon == null) {
                return
            }
            //setRightIcon(BitmapDrawable(resources, icon))
        }

        override fun onProgressChanged(view: WebView, newProgress: Int) {
            mDataBinding.pbBrowserProgress.progress = newProgress
        }
    }


    override fun getStatusLayout(): StatusLayout {
        return mDataBinding.hlBrowserHint
    }


    /**********************************************Click**************************************************/
    inner class ProxyClick {

    }


    /**********************************************Model**************************************************/
    class Model : BaseViewModel(title = "　　　", rightTitle = "　　　") {
        var canGoBack = BooleanObservableField(true)


    }


}