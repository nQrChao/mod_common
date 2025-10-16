package com.box.common.ui.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import android.text.TextUtils
import android.util.AttributeSet
import android.webkit.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.box.base.base.action.ActivityAction
import com.box.base.base.activity.BaseModVmDbActivity

@Suppress("SetJavaScriptEnabled")
class BrowserView  @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.webViewStyle, defStyleRes: Int = 0) :
    NestedScrollWebView(context, attrs, defStyleAttr, defStyleRes),
    LifecycleEventObserver, ActivityAction {

    companion object {

        init {
            setWebContentsDebuggingEnabled(false)
        }
    }

    init {
        val settings: WebSettings = settings
        settings.allowFileAccess = true
        settings.setGeolocationEnabled(true)
        //settings.setSavePassword(true);
        settings.javaScriptEnabled = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.loadsImagesAutomatically = true
        settings.domStorageEnabled = true
        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

        isVerticalScrollBarEnabled = false
        isHorizontalScrollBarEnabled = false
    }


    override fun getUrl(): String? {
        return super.getOriginalUrl() ?: return super.getUrl()
    }

    fun setLifecycleOwner(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this)
    }

    /**
     * [LifecycleEventObserver]
     */
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_RESUME -> onResume()
            Lifecycle.Event.ON_STOP -> onPause()
            Lifecycle.Event.ON_DESTROY -> onDestroy()
            else -> {
            }
        }
    }


    fun onDestroy() {
        stopLoading()
        clearHistory()
        setBrowserChromeClient(null)
        setBrowserViewClient(null)
        removeAllViews()
        destroy()
    }


    @Deprecated("请使用 {@link BrowserViewClient}", ReplaceWith(
        "super.setWebViewClient(client)",
        "com.hjq.widget.layout.NestedScrollWebView"))
    override fun setWebViewClient(client: WebViewClient) {
        super.setWebViewClient(client)
    }

    fun setBrowserViewClient(client: BrowserViewClient?) {
        if (client == null) {
            super.setWebViewClient(WebViewClient())
            return
        }
        super.setWebViewClient(client)
    }


    @Deprecated("请使用 {@link BrowserChromeClient}", ReplaceWith(
        "super.setWebChromeClient(client)",
        "com.hjq.widget.layout.NestedScrollWebView"))
    override fun setWebChromeClient(client: WebChromeClient?) {
        super.setWebChromeClient(client)
    }

    fun setBrowserChromeClient(client: BrowserChromeClient?) {
        super.setWebChromeClient(client)
    }

    open class BrowserViewClient : WebViewClient() {

        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {

        }


        override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
            if (request.isForMainFrame) {
                onReceivedError(view, error.errorCode, error.description.toString(), request.url.toString())
            }
        }


        override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
            super.onReceivedError(view, errorCode, description, failingUrl)
        }


        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            return shouldOverrideUrlLoading(view, request.url.toString())
        }


        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            val scheme: String = Uri.parse(url).scheme ?: return false
            when (scheme) {
                "http", "https" -> view.loadUrl(url)
                "tel" -> dialing(view, url)
            }
            return true
        }

        protected fun dialing(view: WebView, url: String) {


        }
    }

    open class BrowserChromeClient constructor(private val webView: BrowserView) : WebChromeClient() {

        override fun onJsAlert(view: WebView, url: String, message: String, result: JsResult): Boolean {
            val activity: Activity = webView.getActivity() ?: return false

            return true
        }

        override fun onJsConfirm(view: WebView, url: String, message: String, result: JsResult): Boolean {
            val activity: Activity = webView.getActivity() ?: return false

            return true
        }

        override fun onJsPrompt(view: WebView, url: String, message: String, defaultValue: String, result: JsPromptResult): Boolean {
            val activity: Activity = webView.getActivity() ?: return false

            return true
        }

        override fun onGeolocationPermissionsShowPrompt(origin: String, callback: GeolocationPermissions.Callback) {
            val activity: Activity = webView.getActivity() ?: return

        }

        override fun onShowFileChooser(webView: WebView, callback: ValueCallback<Array<Uri>>, params: FileChooserParams): Boolean {
            val activity: Activity? = this.webView.getActivity()
            return true
        }

        private fun openSystemFileChooser(activity: BaseModVmDbActivity<*, *>, params: FileChooserParams, callback: ValueCallback<Array<Uri>>) {
            val intent: Intent = params.createIntent()
            val mimeTypes: Array<String>? = params.acceptTypes
            val multipleSelect: Boolean = params.mode == FileChooserParams.MODE_OPEN_MULTIPLE
            if (!mimeTypes.isNullOrEmpty() && !TextUtils.isEmpty(mimeTypes[0])) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                if (mimeTypes.size == 1) {
                    when (mimeTypes[0]) {
                        "image/*" -> {

                            return
                        }
                        "video/*" -> {

                            return
                        }
                    }
                }
            }

        }
    }
}