package com.box.common.ui.xpop

import android.annotation.SuppressLint
import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.box.other.blankj.utilcode.util.PathUtils
import com.box.other.blankj.utilcode.util.ScreenUtils
import com.box.base.utils.ImLog
import com.box.common.audio.AudioManager2
import com.box.common.R
import com.box.common.utils.Common
import com.box.other.xpopup.core.BasePopupView
import java.io.File

@SuppressLint("ViewConstructor")
class AudioPop(context: Context, var completeCall: (path: String, time: Long) -> Unit) :
    BasePopupView(context) {
    override fun getInnerLayoutId(): Int = R.layout.xpopup_audio

    lateinit var audio: AudioManager2
    var time: Long = 0L
    var state = -1

    lateinit var tip: TextView
    lateinit var leftView: View
    lateinit var rightView: View
    lateinit var audioL: LinearLayout


    override fun onCreate() {
        super.onCreate()
        audio = AudioManager2.getInstance(PathUtils.getInternalAppFilesPath() + File.separator + "audio/")
        tip = findViewById(R.id.tip)
        audioL = findViewById(R.id.ll_audio)
        leftView = findViewById(R.id.left_c)
        rightView = findViewById(R.id.right_c)
    }

    var start = false

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        leftView = findViewById(R.id.left_c)
        event?.let {
            when (it.action) {
                MotionEvent.ACTION_DOWN -> {
                    val y = event.y
                    if (y >= (ScreenUtils.getScreenHeight() - Common.dp2px(100f))) {
                        state = 1
                        audio.prepareAudio()
                        time = System.currentTimeMillis()
                        tip.text = "松开发送"
                        start = true
                        audioL.visibility = View.VISIBLE
                    }
                    ImLog.warnInfo("down1:x-${it.x}y-${it.y}")
                }

                MotionEvent.ACTION_MOVE -> {
                    val y = event.y
                    val x = event.x
//                    val rect = Rect()
//                    leftView.getGlobalVisibleRect(rect)
//                    if (rect.contains(x.toInt(), y.toInt())) {
//                        Logs.e("移动到leftView")
//                        Toaster.show("移动到leftView")
//                    }
                    if (start) {

                        if (y <= (ScreenUtils.getScreenHeight() - Common.dp2px(100f))) {
                            tip.text = "取消发送"
                            state = 2
                        }  else {
                            tip.text = "松开发送"
                            state = 1
                        }
                    }
                    ImLog.warnInfo("move1:x-${it.x}y-${it.y}")
                }

                MotionEvent.ACTION_UP -> {
                    if (start) {
                        if (state == 1) {
                            complete()
                        } else if (state == 2) {
                            dismiss()
                        }
                    }
                    ImLog.warnInfo("up1:x-${it.x}y-${it.y}")
                }
            }

        }

        return super.onTouchEvent(event)
    }

    override fun dismiss() {
        audio.release()
        super.dismiss()
    }

    private fun complete() {
        val path = audio.currentFilePath
        val duration = System.currentTimeMillis() - time
        completeCall.invoke(path, duration)
        dismiss()
    }

}