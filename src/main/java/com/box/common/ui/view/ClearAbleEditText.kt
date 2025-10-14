package com.box.common.ui.view

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.box.common.R

class ClearAbleEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    val editText: EditText
    init {
        LayoutInflater.from(context).inflate(R.layout.view_clearable_edit_text, this, true)

        editText = findViewById(R.id.internal_edit_text)
        val clearButton: ImageView = findViewById(R.id.button_clear)

        context.theme.obtainStyledAttributes(attrs, R.styleable.ClearAbleEditText, 0, 0).apply {
            try {
                editText.hint = getString(R.styleable.ClearAbleEditText_android_hint)
                editText.setText(getString(R.styleable.ClearAbleEditText_android_text))
                editText.gravity = getInt(R.styleable.ClearAbleEditText_android_gravity, editText.gravity)
                // 您可以在这里传递更多 EditText 的原生属性
            } finally {
                recycle()
            }
        }

        clearButton.setOnClickListener {
            editText.text.clear()
        }

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                clearButton.visibility = if (s.isNullOrEmpty()) GONE else VISIBLE
            }
        })
    }

    // 提供一个获取文本的公共方法
    fun getText(): Editable? = editText.text
}

// (可选但推荐) 为了让 DataBinding 的双向绑定 @={vm.platformName} 生效，添加以下适配器
@BindingAdapter("android:text")
fun setClearAbleEditText(view: ClearAbleEditText, text: String?) {
    if (text != view.getText().toString()) {
        view.editText.setText(text)
    }
}

@InverseBindingAdapter(attribute = "android:text", event = "android:textAttrChanged")
fun getClearAbleEditText(view: ClearAbleEditText): String {
    return view.getText().toString()
}

@BindingAdapter("android:textAttrChanged")
fun setClearAbleEditTextListener(view: ClearAbleEditText, listener: InverseBindingListener?) {
    view.editText.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            listener?.onChange()
        }
        override fun afterTextChanged(s: Editable?) {}
    })
}