package com.box.common.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import com.box.base.base.activity.BaseModVmDbActivity
import com.box.base.base.viewmodel.BaseViewModel
import com.box.base.network.NetState
import com.box.com.R
import com.box.com.databinding.ActivityRichTextBinding
import com.box.other.blankj.utilcode.util.ActivityUtils
import com.box.other.immersionbar.immersionBar

class CommonActivityRichText :
    BaseModVmDbActivity<CommonActivityRichText.Model, ActivityRichTextBinding>() {
    override val mViewModel: Model by viewModels()
    override fun layoutId(): Int = R.layout.activity_rich_text
    override fun isStatusBarEnabled(): Boolean {
        return true
    }

    companion object {
        const val INTENT_KEY_HTML: String = "richHtml"
        const val INTENT_KEY_TITLE: String = "title"
        const val INTENT_KEY_COUNT: String = "COUNT"

        fun start(context: Context) {
            val intent = Intent(context, CommonActivityRichText::class.java)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            ActivityUtils.startActivity(intent)
        }

        fun start(context: Context, richText: String) {
            val intent = Intent(context, CommonActivityRichText::class.java)
            intent.putExtra(INTENT_KEY_HTML, richText)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            ActivityUtils.startActivity(intent)
        }

        fun start(context: Context, title: String, richText: String) {
            val intent = Intent(context, CommonActivityRichText::class.java)
            intent.putExtra(INTENT_KEY_TITLE, title)
            intent.putExtra(INTENT_KEY_HTML, richText)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            ActivityUtils.startActivity(intent)
        }

        fun start(context: Context, title: String, richText: String,hotCount:String) {
            val intent = Intent(context, CommonActivityRichText::class.java)
            intent.putExtra(INTENT_KEY_TITLE, title)
            intent.putExtra(INTENT_KEY_HTML, richText)
            intent.putExtra(INTENT_KEY_COUNT, hotCount)
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
            navigationBarColor(R.color.white)
            init()
        }
        //mDataBinding.titleBar.setTitle(intent.getStringExtra(INTENT_KEY_TITLE) ?: "A")
        mViewModel.titleT.value = intent.getStringExtra(INTENT_KEY_TITLE) ?: ""
        mViewModel.hotCount.value = intent.getStringExtra(INTENT_KEY_COUNT) ?: "-1"


        val html = """
    <html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
        <style>
            html, body {
                margin: 0;
                padding: 0 5px;
                font-size: 15px;
                line-height: 1.6;
                overflow-x: hidden; /* ✅ 禁止左右滑动 */
                word-wrap: break-word; /* ✅ 超长文本换行 */
                box-sizing: border-box;
            }
            div, p {
                text-indent: 2em;       
                margin: 0 0 1em 0;      
            }
            h1, h2, h3, h4, h5, h6 {
                text-indent: 0;         
                margin-top: 1em;
            }
            img, iframe, table {
                max-width: 100% !important;
                width: auto !important;
                height: auto;
                display: block;
                margin: 0 auto;
            }
        </style>
    </head>
    <body>
        ${intent.getStringExtra(INTENT_KEY_HTML) ?: ""}
    </body>
    </html>
""".trimIndent()


        mDataBinding.richWebview.loadDataWithBaseURL(
            null,
            html,
            "text/html",
            "utf-8",
            null
        )

    }


    override fun createObserver() {

    }

    override fun onNetworkStateChanged(it: NetState) {

    }

    /**********************************************Click**************************************************/

    inner class ProxyClick {
        fun saveInfo() {

        }
    }


    class Model : BaseViewModel(title = "", rightTitle = "") {
        val hotCount = MutableLiveData("-1")
    }
}