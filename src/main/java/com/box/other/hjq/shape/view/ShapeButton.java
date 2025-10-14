package com.box.other.hjq.shape.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

import com.box.common.R;
import com.box.other.hjq.shape.builder.ShapeDrawableBuilder;
import com.box.other.hjq.shape.builder.TextColorBuilder;
import com.box.other.hjq.shape.config.IGetShapeDrawableBuilder;
import com.box.other.hjq.shape.config.IGetTextColorBuilder;
import com.box.other.hjq.shape.styleable.ShapeButtonStyleable;

/**
 *    desc   : 支持直接定义 Shape 背景的 Button
 */
public class ShapeButton extends AppCompatButton implements
        IGetShapeDrawableBuilder, IGetTextColorBuilder {

    private static final ShapeButtonStyleable STYLEABLE = new ShapeButtonStyleable();

    private final ShapeDrawableBuilder mShapeDrawableBuilder;
    private final TextColorBuilder mTextColorBuilder;

    public ShapeButton(Context context) {
        this(context, null);
    }

    public ShapeButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShapeButton);
        mShapeDrawableBuilder = new ShapeDrawableBuilder(this, typedArray, STYLEABLE);
        mTextColorBuilder = new TextColorBuilder(this, typedArray, STYLEABLE);
        typedArray.recycle();

        mShapeDrawableBuilder.intoBackground();

        if (mTextColorBuilder.isTextGradientColorsEnable() || mTextColorBuilder.isTextStrokeColorEnable()) {
            setText(getText());
        } else {
            mTextColorBuilder.intoTextColor();
        }
    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
        if (mTextColorBuilder == null) {
            return;
        }
        mTextColorBuilder.setTextColor(color);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (mTextColorBuilder != null &&
                (mTextColorBuilder.isTextGradientColorsEnable() || mTextColorBuilder.isTextStrokeColorEnable())) {
            super.setText(mTextColorBuilder.buildTextSpannable(text), type);
        } else {
            super.setText(text, type);
        }
    }

    @Override
    public ShapeDrawableBuilder getShapeDrawableBuilder() {
        return mShapeDrawableBuilder;
    }

    @Override
    public TextColorBuilder getTextColorBuilder() {
        return mTextColorBuilder;
    }
}