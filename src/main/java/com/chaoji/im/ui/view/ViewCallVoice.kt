package com.chaoji.im.ui.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.chaoji.base.base.action.ActivityAction
import com.chaoji.base.base.action.ClickAction
import com.chaoji.common.R
import com.chaoji.common.databinding.ViewCallVoiceBind


class ViewCallVoice(context: Context?) : LinearLayout(context), ClickAction, ActivityAction {
    private var mBind: ViewCallVoiceBind? = null
    lateinit var mViewModel: ViewCallVoiceModel

    init {
        onCreate()
    }

    fun onCreate() {
        mBind = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.view_call_voice, this, true)
        mViewModel = ViewModelProvider(context as ViewModelStoreOwner)[ViewCallVoiceModel::class.java]
        mBind?.vm = mViewModel
    }

    /****************************************点击事件 */
    override fun onClick(view: View) {

    }
    companion object {
        fun getView(contexts: Context?): ViewCallVoice {
            val instance = ViewCallVoice(contexts)
            instance.tag = "VIEW_CALL_VOICE"
            return instance
        }
    }
}
