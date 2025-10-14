package com.box.other.hjq.shape.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import com.box.common.R;
import com.box.other.hjq.shape.builder.ShapeDrawableBuilder;
import com.box.other.hjq.shape.config.IGetShapeDrawableBuilder;
import com.box.other.hjq.shape.styleable.ShapeViewStyleable;

/**
 *    desc   : 支持直接定义 Shape 背景的 View
 */
public class ShapeView extends View implements IGetShapeDrawableBuilder {

    private static final ShapeViewStyleable STYLEABLE = new ShapeViewStyleable();

    private final ShapeDrawableBuilder mShapeDrawableBuilder;

    public ShapeView(Context context) {
        this(context, null);
    }

    public ShapeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShapeView);
        mShapeDrawableBuilder = new ShapeDrawableBuilder(this, typedArray, STYLEABLE);
        typedArray.recycle();

        mShapeDrawableBuilder.intoBackground();
    }

    @Override
    public ShapeDrawableBuilder getShapeDrawableBuilder() {
        return mShapeDrawableBuilder;
    }
}