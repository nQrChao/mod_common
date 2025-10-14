package com.chaoji.im.ui.activity.ademo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.chaoji.other.blankj.utilcode.util.ActivityUtils
import com.chaoji.base.base.activity.BaseVmDbActivity
import com.chaoji.base.network.NetState
import com.chaoji.common.R
import com.chaoji.common.databinding.ActivityDemoBinding

class ActivityDemo : BaseVmDbActivity<ActivityDemoModel, ActivityDemoBinding>() {
    override fun layoutId(): Int = R.layout._activity_demo

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ActivityDemo::class.java)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            ActivityUtils.startActivity(intent)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDataBinding.vm = mViewModel
        mDataBinding.click = ProxyClick()


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
}