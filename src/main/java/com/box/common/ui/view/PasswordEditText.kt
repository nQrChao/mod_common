package com.box.common.ui.view;

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.View.OnTouchListener
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.box.common.R

@Suppress("ClickableViewAccessibility")
class PasswordEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.editTextStyle) :
    RegexEditText(context, attrs, defStyleAttr),
    OnTouchListener, OnFocusChangeListener, TextWatcher {

    private var currentDrawable: Drawable
    private val visibleDrawable: Drawable = DrawableCompat.wrap(ContextCompat.getDrawable(context, R.drawable.password_off_ic)!!)
    private val invisibleDrawable: Drawable
    private var touchListener: OnTouchListener? = null
    private var focusChangeListener: OnFocusChangeListener? = null

    init {
        visibleDrawable.setBounds(0, 0, visibleDrawable.intrinsicWidth, visibleDrawable.intrinsicHeight)
        invisibleDrawable = DrawableCompat.wrap(ContextCompat.getDrawable(context, R.drawable.password_on_ic)!!)
        invisibleDrawable.setBounds(0, 0, invisibleDrawable.intrinsicWidth, invisibleDrawable.intrinsicHeight)
        currentDrawable = visibleDrawable

        addInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)
        if (getInputRegex() == null) {
            setInputRegex(REGEX_NONNULL)
        }
        setDrawableVisible(false)
        super.setOnTouchListener(this)
        super.setOnFocusChangeListener(this)
        super.addTextChangedListener(this)
    }

    private fun setDrawableVisible(visible: Boolean) {
        if (currentDrawable.isVisible == visible) {
            return
        }
        currentDrawable.setVisible(visible, false)
        val drawables: Array<Drawable?> = compoundDrawablesRelative
        setCompoundDrawablesRelative(drawables[0], drawables[1], if (visible) currentDrawable else null, drawables[3])
    }

    private fun refreshDrawableStatus() {
        val drawables: Array<Drawable?> = compoundDrawablesRelative
        setCompoundDrawablesRelative(drawables[0], drawables[1], currentDrawable, drawables[3])
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
    override fun onFocusChange(view: View?, hasFocus: Boolean) {
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
            touchDrawable = x > width - currentDrawable.intrinsicWidth - paddingEnd && x < width - paddingEnd
        } else if (layoutDirection == LAYOUT_DIRECTION_RTL) {
            touchDrawable = x > paddingStart &&
                    x < paddingStart + currentDrawable.intrinsicWidth
        }
        if (currentDrawable.isVisible && touchDrawable) {
            if (event.action == MotionEvent.ACTION_UP) {
                if (currentDrawable === visibleDrawable) {
                    currentDrawable = invisibleDrawable
                    transformationMethod = HideReturnsTransformationMethod.getInstance()
                    refreshDrawableStatus()
                } else if (currentDrawable === invisibleDrawable) {
                    currentDrawable = visibleDrawable
                    transformationMethod = PasswordTransformationMethod.getInstance()
                    refreshDrawableStatus()
                }
                val editable: Editable? = text
                if (editable != null) {
                    setSelection(editable.toString().length)
                }
            }
            return true
        }
        return touchListener != null && touchListener!!.onTouch(view, event)
    }

    /**
     * [TextWatcher]
     */
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (isFocused && s != null) {
            setDrawableVisible(s.isNotEmpty())
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun afterTextChanged(s: Editable?) {}
}