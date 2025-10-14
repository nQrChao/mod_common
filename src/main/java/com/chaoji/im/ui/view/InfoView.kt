package com.chaoji.im.ui.view

import android.annotation.SuppressLint
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.chaoji.base.base.action.ActivityAction
import com.chaoji.base.base.action.ClickAction
import com.chaoji.im.appContext
import com.chaoji.im.getDetailedInformation
import com.chaoji.im.sdk.ImSDK
import com.chaoji.common.R
import com.chaoji.common.databinding.ViewTestBind
import com.chaoji.other.blankj.utilcode.util.ActivityUtils
import com.chaoji.other.blankj.utilcode.util.ClipboardUtils
import com.chaoji.other.blankj.utilcode.util.IntentUtils
import com.chaoji.other.hjq.toast.Toaster
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@SuppressLint("ViewConstructor")
class InfoView(activity: AppCompatActivity) : LinearLayout(activity), ClickAction, ActivityAction {
    private lateinit var testBind: ViewTestBind
    private lateinit var builder: StringBuilder

    init {
        onCreate()
    }
    /****************************************初始化onCreate */
    fun onCreate() {
        testBind = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.item_infoview,
            this,
            true
        )
        GlobalScope.launch {
            builder = getDetailedInformation(appContext, true)
        }
        post {
            testBind.tip.text = builder
        }
        testBind.tip.movementMethod = ScrollingMovementMethod.getInstance()
        setOnClickListener(testBind.testBtn, testBind.ivSend)
        testBind.llLogInfo.visibility = GONE
    }

    /****************************************点击事件 */
    override fun onClick(view: View) {
        if (view === testBind.testBtn) {
            GlobalScope.launch {
                builder = getDetailedInformation(appContext, true)
            }
            post {
                testBind.tip.text = builder
                testBind.llLogInfo.visibility =
                    if (testBind.llLogInfo.visibility == GONE) VISIBLE else GONE
            }

        } else if (view === testBind.ivSend) {
            ClipboardUtils.copyText(builder.toString())
            Toaster.show("信息已复制")
            ImSDK.eventViewModelInstance.showInfoView.postValue(false)
            if (testBind.ivSend.isClickable) {
                ActivityUtils.startActivity(IntentUtils.getShareTextIntent(builder.toString()))
            }
        }
    }

    companion object {
        fun getView(activity: AppCompatActivity): InfoView {
            return InfoView(activity)
        }
    }
}