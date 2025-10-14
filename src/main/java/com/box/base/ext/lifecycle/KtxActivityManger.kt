package com.box.base.ext.lifecycle

import android.app.Activity
import java.util.*

object KtxActivityManger {
    private val mActivityList = LinkedList<Activity>()
    val currentActivity: Activity?
        get() =
            if (mActivityList.isEmpty()) null
            else mActivityList.last

    fun pushActivity(activity: Activity) {
        if (mActivityList.contains(activity)) {
            if (mActivityList.last != activity) {
                mActivityList.remove(activity)
                mActivityList.add(activity)
            }
        } else {
            mActivityList.add(activity)
        }
    }

    fun popActivity(activity: Activity) {
        mActivityList.remove(activity)
    }

    fun finishCurrentActivity() {
        currentActivity?.finish()
    }


    fun finishActivity(activity: Activity) {
        mActivityList.remove(activity)
        activity.finish()
    }

    fun finishActivity(clazz: Class<*>) {
        for (activity in mActivityList) {
            if (activity.javaClass == clazz) {
                mActivityList.remove(activity)
                activity.finish()
                return
            }
        }
    }


    fun finishAllActivity() {
        for (activity in mActivityList) {
            activity.finish()
        }
        mActivityList.clear()
    }
}