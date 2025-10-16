package com.box.common.ui.activity.ademo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.box.base.base.activity.BaseModVmDbActivity
import com.box.other.blankj.utilcode.util.ActivityUtils
import com.box.base.network.NetState
import com.box.com.R
import com.box.com.databinding.ActivityDemoBinding

class ActivityDemo : BaseModVmDbActivity<ActivityDemoModel, ActivityDemoBinding>() {
    override val mViewModel: ActivityDemoModel by viewModels()
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