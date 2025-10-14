package com.chaoji.base.base.action

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent


interface ActivityAction {

    fun getContext(): Context

    fun getActivity(): Activity? {
        var context: Context? = getContext()
        do {
            when (context) {
                is Activity -> {
                    return context
                }
                is ContextWrapper -> {
                    context = context.baseContext
                }
                else -> {
                    return null
                }
            }
        } while (context != null)
        return null
    }

    fun startActivity(clazz: Class<out Activity>) {
        startActivity(Intent(getContext(), clazz))
    }

    fun startActivity(intent: Intent) {
        if (getContext() !is Activity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        getContext().startActivity(intent)
    }
}