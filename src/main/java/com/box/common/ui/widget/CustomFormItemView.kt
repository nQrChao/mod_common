package com.box.common.ui.widget

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.box.com.R

class CustomFormItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private val tvLabel: TextView
    private val etInput: EditText

    init {
        LayoutInflater.from(context).inflate(R.layout.view_custom_form_item, this, true)
        // 设置为垂直布局，确保父组件使用 match_parent/wrap_content
        orientation = HORIZONTAL
        tvLabel = findViewById(R.id.tv_label)
        etInput = findViewById(R.id.et_input)
        // 读取 XML 中设置的自定义属性
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomFormItemView,
            0, 0
        ).apply {
            try {
                // 读取 label_text
                tvLabel.text = getString(R.styleable.CustomFormItemView_label_text) ?: ""
                // 读取 input_hint
                etInput.hint = getString(R.styleable.CustomFormItemView_input_hint) ?: ""
                // 处理 input_type
                val inputTypeEnum = getInt(R.styleable.CustomFormItemView_input_type, 0)
                etInput.inputType = when (inputTypeEnum) {
                    1 -> InputType.TYPE_CLASS_NUMBER // number
                    2 -> InputType.TYPE_CLASS_PHONE  // phone
                    else -> InputType.TYPE_CLASS_TEXT // text
                }
            } finally {
                // 释放资源
                recycle()
            }
        }
    }

    /**
     * 对外提供获取输入内容的方法
     */
    fun getInputText(): String {
        return etInput.text.toString().trim()
    }

    /**
     * 对外提供设置输入内容的方法 (可选)
     */
    fun setInputText(text: String) {
        etInput.setText(text)
    }
}