package com.box.other.hjq.shape.drawable;



import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *    desc   : Shape 形状类型赋值限制
 */
@IntDef({ShapeType.RECTANGLE, ShapeType.OVAL,
        ShapeType.LINE, ShapeType.RING})
@Retention(RetentionPolicy.SOURCE)
public @interface ShapeTypeLimit {}