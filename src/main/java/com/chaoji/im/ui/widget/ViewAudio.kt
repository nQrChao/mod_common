package com.chaoji.im.ui.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.chaoji.other.blankj.utilcode.util.ScreenUtils
import com.chaoji.base.utils.ImLog
import com.chaoji.im.utils.Common

class ViewAudio(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0):View(context, attrs, defStyleAttr, defStyleRes) {
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : this(context, attrs, defStyleAttr, 0)
    constructor(context: Context, attrs: AttributeSet? = null) : this(context, attrs, 0, 0)
    constructor(context: Context) : this(context, null, 0, 0)

    var paint:Paint = Paint()

    init {
        paint.color=Color.GRAY
        paint.isAntiAlias=true
        //setBackgroundColor(Color.RED)
    }



    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(ScreenUtils.getScreenWidth(),Common.dp2px(130f))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.style=Paint.Style.FILL
        paint.color=Color.GRAY
        canvas.drawArc( RectF(0f,0f,ScreenUtils.getScreenWidth().toFloat(),Common.dp2px(80f).toFloat()), -180f, 180f, false, paint)

        paint.style=Paint.Style.STROKE
        paint.strokeWidth=10f
        paint.color=Color.WHITE
        canvas.drawArc( RectF(0f,0f,ScreenUtils.getScreenWidth().toFloat(),Common.dp2px(80f).toFloat()), -180f, 180f, false, paint);

//        paint.color=Color.BLUE
//        paint.style=Paint.Style.STROKE
//        paint.strokeWidth=10f
//        canvas?.drawRect(0f,0f,ScreenUtils.getScreenWidth().toFloat(),Common.dp2px(30f).toFloat(),paint)
//        paint.style=Paint.Style.FILL
//        paint.color=Color.YELLOW
//        canvas?.drawArc( RectF(0f,0f,ScreenUtils.getScreenWidth().toFloat(),Common.dp2px(60f).toFloat()), -180f, 180f, false, paint);
        paint.style=Paint.Style.FILL
        paint.color=Color.GRAY
        canvas?.drawRect(Rect(0,Common.dp2px(40f),ScreenUtils.getScreenWidth(),Common.dp2px(130f)),paint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            when(it.action){
                MotionEvent.ACTION_DOWN->{
                    ImLog.warnInfo("down:x-${it.x}y-${it.y}")
                }
                MotionEvent.ACTION_MOVE->{
                    ImLog.warnInfo("move:x-${it.x}y-${it.y}")
                }
                MotionEvent.ACTION_UP->{
                    ImLog.warnInfo("up:x-${it.x}y-${it.y}")
                }
            }

        }

        return super.onTouchEvent(event)
    }
}