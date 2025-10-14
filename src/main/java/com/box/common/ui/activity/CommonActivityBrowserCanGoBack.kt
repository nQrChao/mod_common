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
import androidx.lifecycle.viewModelScope
import com.box.base.base.activity.BaseVmDbActivity
import com.box.base.network.NetState
import com.box.base.base.action.StatusAction
import com.box.common.databinding.CommonActivityBrowserCangobackBinding
import com.box.common.getDetailedInformation
import com.box.common.sdk.ImSDK
import com.box.common.ui.layout.StatusLayout
import com.box.common.ui.view.BrowserView
import com.box.common.utils.MMKVUtil
import com.box.common.R as RC
import com.box.other.blankj.utilcode.util.ActivityUtils
import com.box.other.blankj.utilcode.util.Logs
import com.box.other.blankj.utilcode.util.StringUtils
import com.box.other.hjq.toast.Toaster
import com.box.other.immersionbar.immersionBar
import com.box.other.scwang.smart.refresh.layout.api.RefreshLayout
import com.box.other.scwang.smart.refresh.layout.listener.OnRefreshListener
import kotlinx.coroutines.launch

class CommonActivityBrowserCanGoBack : BaseVmDbActivity<CommonActivityBrowserCanGoBackModel, CommonActivityBrowserCangobackBinding>(), StatusAction, OnRefreshListener {
    override fun layoutId(): Int = RC.layout.common_activity_browser_cangoback

    companion object {
        const val INTENT_KEY_IN_URL: String = "url"
        fun start(context: Context, url: String) {
            if (TextUtils.isEmpty(url)) {
                return
            }
            val intent = Intent(context, CommonActivityBrowserCanGoBack::class.java)
            intent.putExtra(INTENT_KEY_IN_URL, url)
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
            navigationBarColor(RC.color.white)
            init()
        }
        mDataBinding.wvBrowserView.setLifecycleOwner(this)
        mDataBinding.slBrowserRefresh.setOnRefreshListener(this)
        showLoading()
        mDataBinding.wvBrowserView.apply {
            setBrowserViewClient(AppBrowserViewClient())
            setBrowserChromeClient(AppBrowserChromeClient(this))
            loadUrl(intent.getStringExtra(INTENT_KEY_IN_URL) ?: "")
        }

        getTitleBar()?.rightView?.setOnLongClickListener {
            Toaster.show("rightView")
            mDataBinding.hlBrowserHint.visibility = View.GONE
            mDataBinding.nestedScrollView.visibility = View.VISIBLE
            mDataBinding.textLayout.visibility = View.VISIBLE
            mDataBinding.textView.visibility = View.VISIBLE
            ImSDK.eventViewModelInstance.viewModelScope.launch {
                val text = getDetailedInformation(this@CommonActivityBrowserCanGoBack, true)
                val marketInitStr = MMKVUtil.getMarketInit()
                if (!StringUtils.isEmpty(marketInitStr)) {
                    text.append("\n\nMarketInit:${marketInitStr}")
                    Logs.e("marketInitStr:", text.toString())
                } else {
                    text.append("\n\nNO-MarketInit")
                }
                Logs.e("text.toString():", text.toString())
                mDataBinding.textView.text = text.toString()

            }
            true
        }

        getTitleBar()?.titleView?.setOnLongClickListener {
            Logs.getConfig().setLogSwitch(true)
            true
        }

        getTitleBar()?.leftView?.setOnClickListener {
            mDataBinding.wvBrowserView.apply {
                if (canGoBack()) {
                    goBack()
                } else {
                    finish()
                }
            }
        }

    }

    override fun createObserver() {

    }

    override fun onNetworkStateChanged(it: NetState) {
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        mDataBinding.wvBrowserView.apply {
            if (keyCode == KeyEvent.KEYCODE_BACK && canGoBack()) {
                if (canGoBack()) {
                    goBack()
                } else {
                    finish()
                }
                return true
            }
        }
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


}