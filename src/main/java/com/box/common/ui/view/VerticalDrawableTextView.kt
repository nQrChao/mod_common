package com.box.common.ui.view;

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class VerticalDrawableTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private val mTextPaint: TextPaint = paint
    private var mText: CharSequence? = null
    private var mDrawableTop: Drawable? = null
    private var mDrawablePadding: Int = 0

    init {
        mText = text
        mDrawableTop = compoundDrawables[1]
        mDrawablePadding = compoundDrawablePadding
    }

    override fun onDraw(canvas: Canvas) {
        if (mText.isNullOrEmpty()) {
            return
        }

        // 核心修改：在绘制前，确保画笔使用了最新的颜色和大小
        mTextPaint.color = currentTextColor
        mTextPaint.textSize = textSize
        mTextPaint.typeface = typeface

        // 计算文字高度和总高度
        val charHeight = mTextPaint.fontMetrics.bottom - mTextPaint.fontMetrics.top
        val textTotalHeight = charHeight * mText!!.length + mDrawablePadding * (mText!!.length - 1)

        // 计算 drawable 和文字的整体高度
        val drawableHeight = mDrawableTop?.intrinsicHeight ?: 0
        val contentTotalHeight = drawableHeight + mDrawablePadding + textTotalHeight

        // 计算绘制的起始 Y 坐标，以实现垂直居中，考虑 padding
        val startY = (height - paddingTop - paddingBottom - contentTotalHeight) / 2f + paddingTop

        // 绘制 drawableTop
        if (mDrawableTop != null) {
            val drawableWidth = mDrawableTop!!.intrinsicWidth
            val drawableX = (width - paddingLeft - paddingRight - drawableWidth) / 2f + paddingLeft

            canvas.save()
            canvas.translate(drawableX, startY)
            mDrawableTop!!.setBounds(0, 0, drawableWidth, drawableHeight)
            mDrawableTop!!.draw(canvas)
            canvas.restore()
        }

        // 绘制竖排文字
        val textStartX = (width - paddingLeft - paddingRight - mTextPaint.measureText("左")) / 2f + paddingLeft
        var textCurrentY = startY + drawableHeight + mDrawablePadding

        for (i in mText!!.indices) {
            val char = mText!![i].toString()
            canvas.drawText(char, textStartX, textCurrentY + charHeight, mTextPaint)
            textCurrentY += charHeight + mDrawablePadding
        }
    }
}