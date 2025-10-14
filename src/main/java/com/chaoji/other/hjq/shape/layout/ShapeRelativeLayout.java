package com.chaoji.other.hjq.shape.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import com.chaoji.common.R;
import com.chaoji.other.hjq.shape.builder.ShapeDrawableBuilder;
import com.chaoji.other.hjq.shape.config.IGetShapeDrawableBuilder;
import com.chaoji.other.hjq.shape.styleable.ShapeRelativeLayoutStyleable;

/**
 *    desc   : 支持直接定义 Shape 背景的 RelativeLayout
 */
public class ShapeRelativeLayout extends RelativeLayout implements IGetShapeDrawableBuilder {

    private static final ShapeRelativeLayoutStyleable STYLEABLE = new ShapeRelativeLayoutStyleable();

    private final ShapeDrawableBuilder mShapeDrawableBuilder;

    public ShapeRelativeLayout(Context context) {
        this(context, null);
    }

    public ShapeRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShapeRelativeLayout);
        mShapeDrawableBuilder = new ShapeDrawableBuilder(this, typedArray, STYLEABLE);
        typedArray.recycle();

        mShapeDrawableBuilder.intoBackground();
    }

    @Override
    public ShapeDrawableBuilder getShapeDrawableBuilder() {
        return mShapeDrawableBuilder;
    }
}