package com.box.common.ui.view;

import android.content.*
import android.content.res.TypedArray
import android.text.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.box.com.R
import java.util.regex.Pattern

open class RegexEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr), InputFilter {

    companion object {

        const val REGEX_MOBILE: String = "[1]\\d{0,10}"
        const val REGEX_CHINESE: String = "[\\u4e00-\\u9fa5]*"
        const val REGEX_ENGLISH: String = "[a-zA-Z]*"
        const val REGEX_NUMBER: String = "\\d*"
        const val REGEX_COUNT: String = "[1-9]\\d*"
        const val REGEX_NAME: String = "[[\\u4e00-\\u9fa5]|[a-zA-Z]|\\d]*"
        const val REGEX_NONNULL: String = "\\S+"
    }

    private var pattern: Pattern? = null

    init {
        val array: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.RegexEditText)
        if (array.hasValue(R.styleable.RegexEditText_inputRegex)) {
            setInputRegex(array.getString(R.styleable.RegexEditText_inputRegex))
        } else if (array.hasValue(R.styleable.RegexEditText_regexType)) {
            when (array.getInt(R.styleable.RegexEditText_regexType, 0)) {
                0x01 -> setInputRegex(REGEX_MOBILE)
                0x02 -> setInputRegex(REGEX_CHINESE)
                0x03 -> setInputRegex(REGEX_ENGLISH)
                0x04 -> setInputRegex(REGEX_NUMBER)
                0x05 -> setInputRegex(REGEX_COUNT)
                0x06 -> setInputRegex(REGEX_NAME)
                0x07 -> setInputRegex(REGEX_NONNULL)
            }
        }
        array.recycle()
    }


    fun hasInputType(type: Int): Boolean {
        return (inputType and type) != 0
    }


    fun addInputType(type: Int) {
        inputType = inputType or type
    }


    fun removeInputType(type: Int) {
        inputType = inputType and type.inv()
    }


    fun setInputRegex(regex: String?) {
        if (TextUtils.isEmpty(regex)) {
            return
        }
        pattern = Pattern.compile(regex!!)
        addFilters(this)
    }

    fun getInputRegex(): String? {
        if (pattern == null) {
            return null
        }
        return pattern!!.pattern()
    }

    fun addFilters(filter: InputFilter?) {
        if (filter == null) {
            return
        }
        val newFilters: Array<InputFilter?>?
        val oldFilters: Array<InputFilter?>? = filters
        if (oldFilters != null && oldFilters.isNotEmpty()) {
            newFilters = arrayOfNulls<InputFilter?>(oldFilters.size + 1)
            // 复制旧数组的元素到新数组中
            System.arraycopy(oldFilters, 0, newFilters, 0, oldFilters.size)
            newFilters[oldFilters.size] = filter
        } else {
            newFilters = arrayOfNulls<InputFilter?>(1)
            newFilters[0] = filter
        }
        super.setFilters(newFilters)
    }

    fun clearFilters() {
        super.setFilters(arrayOfNulls(0))
    }

    override fun filter(source: CharSequence?, start: Int, end: Int,
        dest: Spanned?, destStart: Int, destEnd: Int): CharSequence? {

        if (pattern == null) {
            return source
        }

        val begin: String = dest.toString().substring(0, destStart)
        val over: String = dest.toString().substring(
            destStart + (destEnd - destStart),
            destStart + (dest.toString().length - begin.length)
        )
        val result: String = begin + source + over

        if (destStart > destEnd - 1) {
            if (!pattern!!.matcher(result).matches()) {
                return ""
            }
        } else {
            if (!pattern!!.matcher(result).matches()) {
                if ("" != result) {
                    return dest.toString().substring(destStart, destEnd)
                }
            }
        }

        return source
    }
}