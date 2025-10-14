package com.chaoji.other.hjq.shape.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import com.chaoji.common.R;
import com.chaoji.other.hjq.shape.builder.ShapeDrawableBuilder;
import com.chaoji.other.hjq.shape.config.IGetShapeDrawableBuilder;
import com.chaoji.other.hjq.shape.styleable.ShapeLinearLayoutStyleable;

/**
 *    desc   : 支持直接定义 Shape 背景的 LinearLayout
 */
public class ShapeLinearLayout extends LinearLayout implements IGetShapeDrawableBuilder {

    private static final ShapeLinearLayoutStyleable STYLEABLE = new ShapeLinearLayoutStyleable();

    private final ShapeDrawableBuilder mShapeDrawableBuilder;

    public ShapeLinearLayout(Context context) {
        this(context, null);
    }

    public ShapeLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShapeLinearLayout);
        mShapeDrawableBuilder = new ShapeDrawableBuilder(this, typedArray, STYLEABLE);
        typedArray.recycle();

        mShapeDrawableBuilder.intoBackground();
    }

    @Override
    public ShapeDrawableBuilder getShapeDrawableBuilder() {
        return mShapeDrawableBuilder;
    }
}