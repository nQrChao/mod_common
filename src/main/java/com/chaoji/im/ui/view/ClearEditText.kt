package com.chaoji.im.ui.view;

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.View.OnTouchListener
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.chaoji.common.R

@Suppress("ClickableViewAccessibility")
class ClearEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.editTextStyle) :
    RegexEditText(context, attrs, defStyleAttr),
    OnTouchListener, OnFocusChangeListener, TextWatcher {

    private val clearDrawable: Drawable = DrawableCompat.wrap(ContextCompat.getDrawable(context, R.drawable.input_delete_ic)!!)
    private var touchListener: OnTouchListener? = null
    private var clearListener: OnClearListener? = null
    private var focusChangeListener: OnFocusChangeListener? = null

    init {
        clearDrawable.setBounds(0, 0, clearDrawable.intrinsicWidth, clearDrawable.intrinsicHeight)
        setDrawableVisible(false)
        super.setOnTouchListener(this)
        super.setOnFocusChangeListener(this)
        super.addTextChangedListener(this)
    }

    private fun setDrawableVisible(visible: Boolean) {
        if (clearDrawable.isVisible == visible) {
            return
        }
        clearDrawable.setVisible(visible, false)
        val drawables: Array<Drawable> = compoundDrawablesRelative
        setCompoundDrawablesRelative(drawables[0], drawables[1],
            if (visible) clearDrawable else null, drawables[3])
    }

    override fun setOnFocusChangeListener(onFocusChangeListener: OnFocusChangeListener?) {
        focusChangeListener = onFocusChangeListener
    }

    override fun setOnTouchListener(onTouchListener: OnTouchListener?) {
        touchListener = onTouchListener
    }

    /**
     * [OnFocusChangeListener]
     */
    override fun onFocusChange(view: View, hasFocus: Boolean) {
        setDrawableVisible(hasFocus && !TextUtils.isEmpty(text))
        focusChangeListener?.onFocusChange(view, hasFocus)
    }

    /**
     * [OnTouchListener]
     */
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        val x: Int = event.x.toInt()

        var touchDrawable = false
        val layoutDirection: Int = layoutDirection
        if (layoutDirection == LAYOUT_DIRECTION_LTR) {
            touchDrawable = x > width - clearDrawable.intrinsicWidth - paddingEnd && x < width - paddingEnd
        } else if (layoutDirection == LAYOUT_DIRECTION_RTL) {
            touchDrawable = x > paddingStart && x < paddingStart + clearDrawable.intrinsicWidth
        }
        if (clearDrawable.isVisible && touchDrawable) {
            if (event.action == MotionEvent.ACTION_UP) {
                setText("")
                clearListener?.onClear()
            }
            return true
        }
        return touchListener?.onTouch(view, event) ?: false
    }

    fun setOnClearListener(listener: OnClearListener?) {
        clearListener = listener
    }

    interface OnClearListener {
        fun onClear()
    }
    /**
     * [TextWatcher]
     */
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (isFocused) {
            setDrawableVisible(s.isNotEmpty())
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun afterTextChanged(s: Editable?) {}
}