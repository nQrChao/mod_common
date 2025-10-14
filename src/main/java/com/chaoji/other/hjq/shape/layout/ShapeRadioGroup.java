package com.chaoji.other.hjq.shape.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RadioGroup;

import com.chaoji.common.R;
import com.chaoji.other.hjq.shape.builder.ShapeDrawableBuilder;
import com.chaoji.other.hjq.shape.config.IGetShapeDrawableBuilder;
import com.chaoji.other.hjq.shape.styleable.ShapeRadioGroupStyleable;

/**
 *    desc   : 支持直接定义 Shape 背景的 RadioGroup
 */
public class ShapeRadioGroup extends RadioGroup implements IGetShapeDrawableBuilder {

    private static final ShapeRadioGroupStyleable STYLEABLE = new ShapeRadioGroupStyleable();

    private final ShapeDrawableBuilder mShapeDrawableBuilder;

    public ShapeRadioGroup(Context context) {
        this(context, null);
    }

    public ShapeRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShapeRadioGroup);
        mShapeDrawableBuilder = new ShapeDrawableBuilder(this, typedArray, STYLEABLE);
        typedArray.recycle();

        mShapeDrawableBuilder.intoBackground();
    }

    @Override
    public ShapeDrawableBuilder getShapeDrawableBuilder() {
        return mShapeDrawableBuilder;
    }
}