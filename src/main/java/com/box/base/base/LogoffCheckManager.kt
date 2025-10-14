package com.box.base.base

import android.view.*
import java.util.*

class LogoffCheckManager private constructor(view: View, alpha: Boolean) {

    companion object {

        fun with(): Builder {
            return Builder()
        }
    }

    private val view: View

    private val alpha: Boolean

    private var completeSet: MutableList<Boolean> = mutableListOf()


    init {
        this.view = view
        this.alpha = alpha
    }

    fun addViews(completes: MutableList<Boolean>) {
        completeSet.addAll(completes)
        // 触发一次监听
        notifyChanged()
    }

    fun addViews(vararg completes: Boolean) {
        for (complete: Boolean in completes) {
            // 避免重复添加
            if (!completeSet.contains(complete)) {
                completeSet.add(complete)
            }
        }
        notifyChanged()
    }

    fun removeViews(vararg completes: Boolean) {
        if (completeSet.isEmpty()) {
            return
        }
        for (complete: Boolean in completes) {
            completeSet.remove(complete)
        }
        notifyChanged()
    }

    fun removeAllViews() {
        completeSet.clear()
    }


    private fun notifyChanged() {
        for (view: Boolean in completeSet) {
            if (!view) {
                setEnabled(false)
                return
            }
        }

    }

    fun setEnabled(enabled: Boolean) {
        if (enabled == view.isEnabled) {
            return
        }
        if (enabled) {
            view.isEnabled = true
            if (alpha) {
                view.alpha = 1f
            }
        } else {
            view.isEnabled = false
            if (alpha) {
                view.alpha = 0.5f
            }
        }
    }

    class Builder {

        private var view: View? = null

        private var alpha: Boolean = false

        private val completeSet: MutableList<Boolean> = ArrayList()

        fun addView(complete: Boolean): Builder = apply {
                completeSet.add(complete)
        }

        fun setMain(view: View): Builder = apply {
            this.view = view
        }

        fun setAlpha(alpha: Boolean): Builder = apply {
            this.alpha = alpha
        }

        fun build(): LogoffCheckManager {
            if (view == null) {
                throw IllegalArgumentException("are you ok?")
            }
            val helper = LogoffCheckManager(view!!, alpha)
            helper.addViews(completeSet)
            return helper
        }
    }

}