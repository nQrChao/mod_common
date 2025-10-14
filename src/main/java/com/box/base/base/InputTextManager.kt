package com.box.base.base

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.*
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.TextView
import java.util.*

class InputTextManager private constructor(view: View, alpha: Boolean) : TextWatcher {

    companion object {

        fun with(activity: Activity): Builder {
            return Builder(activity)
        }
    }

    private val view: View

    private val alpha: Boolean

    private var viewSet: MutableList<InputTextBean> = mutableListOf()

    private var listener: OnInputTextListener? = null

    init {
        this.view = view
        this.alpha = alpha
    }

    fun addViews(views: MutableList<InputTextBean>) {
        viewSet.addAll(views)
        for (textBean: InputTextBean in views) {
            textBean.textView.addTextChangedListener(this)
        }
        // 触发一次监听
        notifyChanged()
    }

    fun addViews(vararg views: InputTextBean) {
        for (textBean: InputTextBean in views) {
            // 避免重复添加
            if (!viewSet.contains(textBean)) {
                textBean.textView.addTextChangedListener(this)
                viewSet.add(textBean)
            }
        }
        notifyChanged()
    }

    fun removeViews(vararg views: InputTextBean) {
        if (viewSet.isEmpty()) {
            return
        }
        for (textBean: InputTextBean in views) {
            textBean.textView.removeTextChangedListener(this)
            viewSet.remove(textBean)
        }
        notifyChanged()
    }

    fun removeAllViews() {
        for (textBean: InputTextBean in viewSet) {
            textBean.textView.removeTextChangedListener(this)
        }
        viewSet.clear()
    }

    fun setListener(listener: OnInputTextListener?) {
        this.listener = listener
    }

    /**
     * [TextWatcher]
     */
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        notifyChanged()
    }

    fun notifyChanged() {
        for (textBean: InputTextBean in viewSet) {
            if ((textBean.textView.text.toString().length < textBean.length)) {
                setEnabled(false)
                return
            }
        }

        listener.let {
            if (it == null) {
                setEnabled(true)
                return
            }
            setEnabled(it.onInputChange(this))
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

    class Builder constructor(private val activity: Activity) {

        private var view: View? = null

        private var alpha: Boolean = false

        private val viewSet: MutableList<InputTextBean> = ArrayList()

        private var listener: OnInputTextListener? = null

        fun addView(view: TextView?): Builder = apply {
            if (view != null) {
                viewSet.add(InputTextBean(view, 1))
            }
        }

        fun addView(view: TextView?, length: Int): Builder = apply {
            if (view != null) {
                viewSet.add(InputTextBean(view, length))
            }
        }

        fun addView(view: InputTextBean?): Builder = apply {
            if (view != null) {
                viewSet.add(view)
            }
        }

        fun setMain(view: View): Builder = apply {
            this.view = view
        }

        fun setAlpha(alpha: Boolean): Builder = apply {
            this.alpha = alpha
        }

        fun setListener(listener: OnInputTextListener?): Builder = apply {
            this.listener = listener
        }

        fun build(): InputTextManager {
            if (view == null) {
                throw IllegalArgumentException("are you ok?")
            }
            val helper = InputTextManager(view!!, alpha)
            helper.addViews(viewSet)
            helper.setListener(listener)
            TextInputLifecycle.register(activity, helper)
            return helper
        }
    }

    private class TextInputLifecycle private constructor(
        private var activity: Activity?,
        private var textHelper: InputTextManager?
    ) : ActivityLifecycleCallbacks {

        companion object {

            fun register(activity: Activity, helper: InputTextManager?) {
                val lifecycle = TextInputLifecycle(activity, helper)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    activity.registerActivityLifecycleCallbacks(lifecycle)
                } else {
                    activity.application.registerActivityLifecycleCallbacks(lifecycle)
                }
            }
        }

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

        override fun onActivityStarted(activity: Activity) {}

        override fun onActivityResumed(activity: Activity) {}

        override fun onActivityPaused(activity: Activity) {}

        override fun onActivityStopped(activity: Activity) {}

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

        override fun onActivityDestroyed(activity: Activity) {
            if (this.activity !== activity) {
                return
            }
            textHelper?.removeAllViews()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                this.activity?.unregisterActivityLifecycleCallbacks(this)
            } else {
                this.activity?.application?.unregisterActivityLifecycleCallbacks(this)
            }
            textHelper = null
            this.activity = null
        }
    }


    interface OnInputTextListener {

        fun onInputChange(manager: InputTextManager?): Boolean
    }
}