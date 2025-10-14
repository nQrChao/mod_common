package com.box.common.ui.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
class CountdownView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    AppCompatTextView(context, attrs, defStyleAttr), Runnable {

    companion object {
        private const val TIME_UNIT: String = "S"
    }

    private var totalSecond: Int = 60
    private var currentSecond: Int = 0
    private var recordText: CharSequence? = null

    fun setTotalTime(totalTime: Int) {
        totalSecond = totalTime
    }

    fun start() {
        recordText = text
        isEnabled = false
        currentSecond = totalSecond
        post(this)
    }

    fun stop() {
        currentSecond = 0
        text = recordText
        isEnabled = true
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeCallbacks(this)
    }

    @Suppress("SetTextI18n")
    override fun run() {
        if (currentSecond == 0) {
            stop()
            return
        }
        currentSecond--
        text = "$currentSecond $TIME_UNIT"
        postDelayed(this, 1000)
    }
}