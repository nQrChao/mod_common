package com.chaoji.base.ext.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.chaoji.base.callback.livedata.BooleanLiveData

object KtxAppLifeObserver : LifecycleObserver {

    var isForeground = BooleanLiveData()

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private  fun onForeground() {
        isForeground.value = true
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun onBackground() {
        isForeground.value = false
    }

}