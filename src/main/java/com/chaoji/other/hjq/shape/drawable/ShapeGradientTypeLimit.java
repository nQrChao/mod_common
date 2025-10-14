package com.chaoji.other.hjq.shape.drawable;


import androidx.annotation.IntDef;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *    desc   : Shape 渐变类型赋值限制
 */
@IntDef({ShapeGradientType.LINEAR_GRADIENT, ShapeGradientType.RADIAL_GRADIENT, ShapeGradientType.SWEEP_GRADIENT})
@Retention(RetentionPolicy.SOURCE)
public @interface ShapeGradientTypeLimit {}