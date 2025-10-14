package com.box.other.spannable.span

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

/**
 * 创建字体颜色/字体样式/可点击效果
 * @param color 字体颜色
 * @param typeface 字体样式
 * @param onClick 点击事件 要求设置[ClickableMovementMethod]或者[LinkMovementMethod], 否则点击事件是无效的, 此为Android官方限制
 */
class HighlightSpan @JvmOverloads constructor(
    @ColorInt val color: Int? = null,
    val typeface: Typeface? = null,
    val onClick: ((View) -> Unit)? = null
) : ClickableSpan() {

    /**
     * 创建字体颜色/字体样式/可点击效果
     * @param color 字体颜色
     * @param typeface 字体样式
     * @param onClick 点击事件 要求设置[ClickableMovementMethod]或者[LinkMovementMethod], 否则点击事件是无效的, 此为Android官方限制
     */
    @JvmOverloads
    constructor(
        color: String,
        typeface: Typeface? = null,
        onClick: ((View) -> Unit)? = null
    ) : this(Color.parseColor(color), typeface, onClick)

    /**
     * 创建字体颜色/字体样式/可点击效果
     * @param colorRes 字体颜色
     * @param typeface 字体样式
     * @param onClick 点击事件 要求设置[ClickableMovementMethod]或者[LinkMovementMethod], 否则点击事件是无效的, 此为Android官方限制
     */
    @JvmOverloads
    constructor(
        context: Context,
        @ColorRes colorRes: Int,
        typeface: Typeface? = null,
        onClick: ((View) -> Unit)? = null
    ) : this(ContextCompat.getColor(context, colorRes), typeface, onClick)

    override fun updateDrawState(ds: TextPaint) {
        color?.let(ds::setColor)
        typeface?.let(ds::setTypeface)
    }

    override fun onClick(widget: View) {
        onClick?.invoke(widget)
    }
}