package com.box.common.ui.layout

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.box.other.hjq.titlebar.TitleBarSupport
import com.box.common.R

class SettingBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    companion object {

        const val NO_COLOR: Int = Color.TRANSPARENT

    }

    private val mainLayout: LinearLayout = LinearLayout(getContext())
    private val leftView: TextView = TextView(getContext())
    private val rightView: TextView = TextView(getContext())
    private val lineView: View = View(getContext())

    private var leftDrawableTint: Int = 0
    private var rightDrawableTint: Int = 0

    private var leftDrawableSize: Int = 0
    private var rightDrawableSize: Int = 0

    init {
        mainLayout.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL)
        val leftParams = LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        leftParams.gravity = Gravity.CENTER_VERTICAL
        leftView.layoutParams = leftParams

        val rightParams = LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT)
        rightParams.gravity = Gravity.CENTER_VERTICAL
        rightView.layoutParams = rightParams
        rightParams.weight=1f
        rightView.isSingleLine=true
        rightView.ellipsize=TextUtils.TruncateAt.END

        lineView.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, 1, Gravity.BOTTOM)
        leftView.gravity = Gravity.START or Gravity.CENTER_VERTICAL
        rightView.gravity = Gravity.END or Gravity.CENTER_VERTICAL
        leftView.isSingleLine = true
        rightView.isSingleLine = true
        leftView.ellipsize = TextUtils.TruncateAt.END
        rightView.ellipsize = TextUtils.TruncateAt.END
        leftView.setLineSpacing(resources.getDimension(R.dimen.idp_5), leftView.lineSpacingMultiplier)
        rightView.setLineSpacing(resources.getDimension(R.dimen.idp_5), rightView.lineSpacingMultiplier)
        leftView.setPaddingRelative(
            resources.getDimension(R.dimen.idp_15).toInt(),
            resources.getDimension(R.dimen.idp_10).toInt(),
            resources.getDimension(R.dimen.idp_15).toInt(),
            resources.getDimension(R.dimen.idp_10).toInt()
        )
        rightView.setPaddingRelative(
            resources.getDimension(R.dimen.idp_15).toInt(),
            resources.getDimension(R.dimen.idp_10).toInt(),
            resources.getDimension(R.dimen.idp_15).toInt(),
            resources.getDimension(R.dimen.idp_10).toInt()
        )
        val array: TypedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SettingBar)

        // 文本设置
        if (array.hasValue(R.styleable.SettingBar_bar_leftText)) {
            setLeftText(array.getString(R.styleable.SettingBar_bar_leftText))
        }
        if (array.hasValue(R.styleable.SettingBar_bar_rightText)) {
            setRightText(array.getString(R.styleable.SettingBar_bar_rightText))
        }

        if (array.hasValue(R.styleable.SettingBar_bar_leftTextHint)) {
            setLeftTextHint(array.getString(R.styleable.SettingBar_bar_leftTextHint))
        }
        if (array.hasValue(R.styleable.SettingBar_bar_rightTextHint)) {
            setRightTextHint(array.getString(R.styleable.SettingBar_bar_rightTextHint))
        }

        if (array.hasValue(R.styleable.SettingBar_bar_leftDrawableSize)) {
            setLeftDrawableSize(
                array.getDimensionPixelSize(
                    R.styleable.SettingBar_bar_leftDrawableSize,
                    0
                )
            )
        }
        if (array.hasValue(R.styleable.SettingBar_bar_rightDrawableSize)) {
            setRightDrawableSize(
                array.getDimensionPixelSize(
                    R.styleable.SettingBar_bar_rightDrawableSize,
                    0
                )
            )
        }

        if (array.hasValue(R.styleable.SettingBar_bar_leftDrawableTint)) {
            setLeftDrawableTint(
                array.getColor(
                    R.styleable.SettingBar_bar_leftDrawableTint,
                    NO_COLOR
                )
            )
        }
        if (array.hasValue(R.styleable.SettingBar_bar_rightDrawableTint)) {
            setRightDrawableTint(
                array.getColor(
                    R.styleable.SettingBar_bar_rightDrawableTint,
                    NO_COLOR
                )
            )
        }

        setLeftDrawablePadding(
            if (array.hasValue(R.styleable.SettingBar_bar_leftDrawablePadding)) array.getDimensionPixelSize(
                R.styleable.SettingBar_bar_leftDrawablePadding,
                0
            ) else resources.getDimension(R.dimen.idp_10).toInt()
        )
        setRightDrawablePadding(
            if (array.hasValue(R.styleable.SettingBar_bar_rightDrawablePadding))
                array.getDimensionPixelSize(R.styleable.SettingBar_bar_rightDrawablePadding, 0)
            else
                resources.getDimension(R.dimen.idp_10).toInt()
        )

        if (array.hasValue(R.styleable.SettingBar_bar_leftDrawable)) {
            setLeftDrawable(array.getDrawable(R.styleable.SettingBar_bar_leftDrawable))
        }
        if (array.hasValue(R.styleable.SettingBar_bar_rightDrawable)) {
            setRightDrawable(array.getDrawable(R.styleable.SettingBar_bar_rightDrawable))
        }

        setLeftTextColor(
            array.getColor(
                R.styleable.SettingBar_bar_leftTextColor,
                ContextCompat.getColor(getContext(), R.color.black80)
            )
        )
        val titleStyle = if (array.hasValue(R.styleable.SettingBar_bar_leftTextStyle)) array.getInt(R.styleable.SettingBar_bar_leftTextStyle, Typeface.NORMAL)
        else Typeface.NORMAL
        setLeftTextStyle(titleStyle)

        setRightTextColor(
            array.getColor(
                R.styleable.SettingBar_bar_rightTextColor,
                ContextCompat.getColor(getContext(), R.color.black60)
            )
        )

        setLeftTextSize(TypedValue.COMPLEX_UNIT_PX, array.getDimensionPixelSize(
            R.styleable.SettingBar_bar_leftTextSize, resources.getDimension(R.dimen.isp_15).toInt()).toFloat())
        setRightTextSize(TypedValue.COMPLEX_UNIT_PX, array.getDimensionPixelSize(
            R.styleable.SettingBar_bar_rightTextSize, resources.getDimension(R.dimen.isp_14).toInt()).toFloat())

        if (array.hasValue(R.styleable.SettingBar_bar_lineDrawable)) {
            setLineDrawable(array.getDrawable(R.styleable.SettingBar_bar_lineDrawable))
        } else {
            setLineDrawable(ColorDrawable(Color.parseColor("#ECECEC")))
        }
        if (array.hasValue(R.styleable.SettingBar_bar_lineVisible)) {
            setLineVisible(array.getBoolean(R.styleable.SettingBar_bar_lineVisible, true))
        }
        if (array.hasValue(R.styleable.SettingBar_bar_lineSize)) {
            setLineSize(array.getDimensionPixelSize(R.styleable.SettingBar_bar_lineSize, 0))
        }
        if (array.hasValue(R.styleable.SettingBar_bar_lineMargin)) {
            setLineMargin(array.getDimensionPixelSize(R.styleable.SettingBar_bar_lineMargin, 0))
        }
        if (background == null) {
            val drawable = StateListDrawable()
            drawable.addState(
                intArrayOf(android.R.attr.state_pressed),
                ColorDrawable(ContextCompat.getColor(getContext(), R.color.black5))
            )
            drawable.addState(
                intArrayOf(android.R.attr.state_selected),
                ColorDrawable(ContextCompat.getColor(getContext(), R.color.black5))
            )
            drawable.addState(
                intArrayOf(android.R.attr.state_focused),
                ColorDrawable(ContextCompat.getColor(getContext(), R.color.black5))
            )
            drawable.addState(
                intArrayOf(),
                ColorDrawable(ContextCompat.getColor(getContext(), R.color.white))
            )
            background = drawable

            isFocusable = true
            isClickable = true
        }
        array.recycle()
        mainLayout.addView(leftView)
        mainLayout.addView(rightView)
        addView(mainLayout, 0)
        addView(lineView, 1)
    }

    fun setLeftText(@StringRes id: Int): SettingBar = apply {
        setLeftText(resources.getString(id))
    }

    fun setLeftText(text: CharSequence?): SettingBar = apply {
        leftView.text = text
    }

    fun getLeftText(): CharSequence? {
        return leftView.text
    }

    fun setLeftTextHint(@StringRes id: Int): SettingBar = apply {
        setLeftTextHint(resources.getString(id))
    }

    fun setLeftTextHint(hint: CharSequence?): SettingBar = apply {
        leftView.hint = hint
    }

    fun setRightText(@StringRes id: Int): SettingBar = apply {
        setRightText(resources.getString(id))
    }

    fun setRightText(text: CharSequence?): SettingBar = apply {
        rightView.text = text
    }

    fun getRightText(): CharSequence? {
        return rightView.text
    }

    fun setRightTextHint(@StringRes id: Int): SettingBar = apply {
        setRightTextHint(resources.getString(id))
    }

    fun setRightTextHint(hint: CharSequence?): SettingBar = apply {
        rightView.hint = hint
    }

    fun setLeftDrawable(@DrawableRes id: Int): SettingBar = apply {
        setLeftDrawable(ContextCompat.getDrawable(context, id))
    }

    fun setLeftDrawable(drawable: Drawable?): SettingBar = apply {
        leftView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
        setLeftDrawableSize(leftDrawableSize)
        setLeftDrawableTint(leftDrawableTint)
    }

    fun getLeftDrawable(): Drawable? {
        return leftView.compoundDrawables[0]
    }

    fun setRightDrawable(@DrawableRes id: Int): SettingBar = apply {
        setRightDrawable(ContextCompat.getDrawable(context, id))
    }

    fun setRightDrawable(drawable: Drawable?): SettingBar = apply {
        rightView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
        setRightDrawableSize(rightDrawableSize)
        setRightDrawableTint(rightDrawableTint)
    }

    fun getRightDrawable(): Drawable? {
        return rightView.compoundDrawables[2]
    }

    fun setLeftDrawablePadding(padding: Int): SettingBar = apply {
        leftView.compoundDrawablePadding = padding
    }

    fun setRightDrawablePadding(padding: Int): SettingBar = apply {
        rightView.compoundDrawablePadding = padding
    }

    fun setLeftDrawableSize(size: Int): SettingBar = apply {
        leftDrawableSize = size
        val drawable: Drawable? = getLeftDrawable()
        if (drawable != null) {
            if (size > 0) {
                drawable.setBounds(0, 0, size, size)
            } else {
                drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            }
            leftView.setCompoundDrawables(drawable, null, null, null)
        }
    }

    fun setRightDrawableSize(size: Int): SettingBar = apply {
        rightDrawableSize = size
        val drawable: Drawable? = getRightDrawable()
        if (drawable != null) {
            if (size > 0) {
                drawable.setBounds(0, 0, size, size)
            } else {
                drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            }
            rightView.setCompoundDrawables(null, null, drawable, null)
        }
    }

    fun setLeftDrawableTint(color: Int): SettingBar = apply {
        leftDrawableTint = color
        val drawable: Drawable? = getLeftDrawable()
        if (drawable != null && color != NO_COLOR) {
            drawable.mutate()
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        }
    }

    fun setRightDrawableTint(color: Int): SettingBar = apply {
        rightDrawableTint = color
        val drawable: Drawable? = getRightDrawable()
        if (drawable != null && color != NO_COLOR) {
            drawable.mutate()
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        }
    }


    fun setLeftTextColor(@ColorInt color: Int): SettingBar = apply {
        leftView.setTextColor(color)
    }
    fun setLeftTextStyle(typeface: Int): SettingBar = apply {
        leftView.typeface = TitleBarSupport.getTextTypeface(typeface)
    }

    fun setRightTextColor(@ColorInt color: Int): SettingBar = apply {
        rightView.setTextColor(color)
    }

    fun setRightTextStyle(typeface: Typeface): SettingBar = apply {
        rightView.typeface = typeface
    }

    fun setLeftTextSize(unit: Int, size: Float): SettingBar = apply {
        leftView.setTextSize(unit, size)
    }


    fun setRightTextSize(unit: Int, size: Float): SettingBar = apply {
        rightView.setTextSize(unit, size)
    }


    fun setLineVisible(visible: Boolean): SettingBar = apply {
        lineView.visibility = if (visible) VISIBLE else GONE
    }

    fun setLineColor(@ColorInt color: Int): SettingBar = apply {
        setLineDrawable(ColorDrawable(color))
    }

    fun setLineDrawable(drawable: Drawable?): SettingBar = apply {
        lineView.background = drawable
    }

    fun setLineSize(size: Int): SettingBar = apply {
        var params: LayoutParams? = lineView.layoutParams as LayoutParams?
        if (params == null) {
            params = generateDefaultLayoutParams()
        }
        params?.height = size
        lineView.layoutParams = params
    }


    fun setLineMargin(margin: Int): SettingBar = apply {
        var params: LayoutParams? = lineView.layoutParams as LayoutParams?
        if (params == null) {
            params = generateDefaultLayoutParams()
        }
        params?.leftMargin = margin
        params?.rightMargin = margin
        lineView.layoutParams = params
    }


    fun getMainLayout(): LinearLayout {
        return mainLayout
    }

    fun getLeftView(): TextView {
        return leftView
    }

    fun getRightView(): TextView {
        return rightView
    }

    fun getLineView(): View {
        return lineView
    }
}