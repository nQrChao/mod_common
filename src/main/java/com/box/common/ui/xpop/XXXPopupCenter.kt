package com.box.common.ui.xpop

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.box.base.base.action.ClickAction
import com.box.base.base.action.KeyboardAction
import com.box.common.R
import com.box.common.utils.UtilTime
import com.box.common.countClick
import com.box.common.generateTotpNumber
import com.box.common.sdk.ImSDK
import com.box.common.verifyTOTP
import com.box.other.xpopup.core.CenterPopupView
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

@SuppressLint("ViewConstructor")
class XXXPopupCenter(context: Context) :
    CenterPopupView(context), ClickAction, KeyboardAction {
    override fun getImplLayoutId(): Int = R.layout.xpopup_xxx


    private var titleLeft: TextView? = null
    private var titleText: TextView? = null
    private var titleRight: TextView? = null

    private var editText: EditText? = null
    private var timeText: TextView? = null
    private var leftButton: Button? = null
    private var rightButton: Button? = null
    private var mHandler: Handler = Handler(Looper.getMainLooper())
    private var executorService: ScheduledExecutorService? = null




    @SuppressLint("SetTextI18n")
    override fun onCreate() {
        super.onCreate()
        editText = findViewById<EditText>(R.id.editText)
        timeText = findViewById<TextView>(R.id.tv_time)
        titleLeft = findViewById<TextView>(R.id.tv_title1)
        titleText = findViewById<TextView>(R.id.tv_title2)
        titleRight = findViewById<TextView>(R.id.tv_title3)
        leftButton = findViewById<Button>(R.id.a_btn)
        rightButton = findViewById<Button>(R.id.b_btn)

        setOnClickListener(leftButton, rightButton, titleLeft, titleRight)

        mHandler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                timeText?.text = UtilTime.getCurrentDataString()
            }
        }

        if (executorService == null) {
            executorService = Executors.newScheduledThreadPool(1)
            executorService?.scheduleWithFixedDelay({ mHandler.sendEmptyMessage(1) }, 1, 1, TimeUnit.SECONDS)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onClick(view: View) {
        when (view) {
            leftButton -> {
                if (verifyTOTP("LOG", editText?.text.toString())) {
                    ImSDK.eventViewModelInstance.showLogView.postValue(true)
                    dismiss()
                }
            }

            rightButton -> {
                if (verifyTOTP("INFO", editText?.text.toString())) {
                    ImSDK.eventViewModelInstance.showInfoView.postValue(true)
                    dismiss()
                }
            }

            titleLeft -> {
                countClick {
                    titleText?.text = "LOG"
                    generateTotpNumber("LOG")
                }
            }

            titleRight -> {
                countClick {
                    titleText?.text = "INFO"
                    generateTotpNumber("INFO")
                }
            }
        }
    }


    override fun dismiss() {
        super.dismiss()
        hideKeyboard(this)
    }





}