package com.chaoji.im.ui.xpop

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.chaoji.base.base.action.ClickAction
import com.chaoji.base.base.action.KeyboardAction
import com.chaoji.common.R
import com.chaoji.other.xpopup.impl.FullScreenPopupView
import okhttp3.internal.userAgent

@SuppressLint("ViewConstructor")
class XPopupFullScreenWeb(
    context: Context,
    var url: String,
    private var cancel: (() -> Unit)?,
    private var sure: (() -> Unit)?
) :
    FullScreenPopupView(context), ClickAction, KeyboardAction {
    override fun getImplLayoutId(): Int = R.layout.lc_old_web_view
    var leftView: ImageView? = null
    var webView: WebView? = null
    var btnClose: Button? = null
    var titleView: TextView? = null
    override fun onCreate() {
        super.onCreate()
        leftView = findViewById(R.id.title_left)
        titleView = findViewById(R.id.title_center)
        webView = findViewById(R.id.web_view)
        btnClose = findViewById(R.id.btn_close)

        webView?.settings?.apply {
            javaScriptEnabled = true
            allowFileAccess = true
            cacheMode = WebSettings.LOAD_NO_CACHE
            domStorageEnabled = true
            setGeolocationEnabled(true)
            userAgentString = "$userAgentString whoami/chuanqizhanji-app "
        }
        webView?.loadUrl(url)

        webView?.webViewClient = object : WebViewClient() {
            override fun shouldInterceptRequest(
                view: WebView,
                request: WebResourceRequest
            ): WebResourceResponse? {
                val rix = request.url
                val sScheme = rix.scheme
                return super.shouldInterceptRequest(view, request)
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                val title = view.title
                titleView?.text = title
            }
        }
        setOnClickListener(R.id.title_left, R.id.btn_close)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.title_left -> {
                sure?.invoke()
                dismiss()
            }

            R.id.btn_close -> {
                cancel?.invoke()
                dismiss()
            }
        }
    }

    override fun dismiss() {
        super.dismiss()
        hideKeyboard(this)
    }


}