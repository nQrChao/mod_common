package com.box.common.ui.widget.bottombar;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;

import com.airbnb.lottie.LottieAnimationView;

class MyLottieAnimationView extends LottieAnimationView {

    public MyLottieAnimationView(Context context) {
        super(context);
    }

    public MyLottieAnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLottieAnimationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable parcelable = super.onSaveInstanceState();
        parcelable = null;
        return null;
    }
}
