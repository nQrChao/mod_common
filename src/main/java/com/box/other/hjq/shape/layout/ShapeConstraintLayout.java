package com.box.other.hjq.shape.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.box.common.R;
import com.box.other.hjq.shape.builder.ShapeDrawableBuilder;
import com.box.other.hjq.shape.config.IGetShapeDrawableBuilder;
import com.box.other.hjq.shape.styleable.ShapeConstraintLayoutStyleable;


/**
 *    desc   : 支持直接定义 Shape 背景的 ConstraintLayout
 */
public class ShapeConstraintLayout extends ConstraintLayout implements IGetShapeDrawableBuilder {

    private static final ShapeConstraintLayoutStyleable STYLEABLE = new ShapeConstraintLayoutStyleable();

    private final ShapeDrawableBuilder mShapeDrawableBuilder;

    public ShapeConstraintLayout(Context context) {
        this(context, null);
    }

    public ShapeConstraintLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShapeConstraintLayout);
        mShapeDrawableBuilder = new ShapeDrawableBuilder(this, typedArray, STYLEABLE);
        typedArray.recycle();

        mShapeDrawableBuilder.intoBackground();
    }

    @Override
    public ShapeDrawableBuilder getShapeDrawableBuilder() {
        return mShapeDrawableBuilder;
    }
}