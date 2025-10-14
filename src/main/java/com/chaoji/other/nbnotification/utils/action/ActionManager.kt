package com.chaoji.other.nbnotification.utils.action

import android.app.Application
import com.chaoji.other.nbnotification.utils.action.ActionActivityCallbacks

object ActionManager {

    private var isInit = false

    fun init(application: Application){
        if(isInit){
            return
        }
        isInit = true
        application.registerActivityLifecycleCallbacks(ActionActivityCallbacks())
    }


    fun isInit():Boolean{
        return isInit
    }
}