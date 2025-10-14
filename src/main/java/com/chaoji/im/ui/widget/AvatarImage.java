package com.chaoji.im.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;

import com.chaoji.other.blankj.utilcode.util.RegexUtils;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chaoji.im.glide.GlideApp;
import com.chaoji.common.R;
import com.chaoji.other.raphets.roundimageview.RoundImageView;

public class AvatarImage extends FrameLayout {
    private RoundImageView roundImageView;
    private TextView nameTv;

    public AvatarImage(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public AvatarImage(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AvatarImage(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public RoundImageView getRoundImageView() {
        return roundImageView;
    }


    void init(Context context, AttributeSet attrs) {
        final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.AvatarImage, 0, R.style.AvatarImageStyle);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        roundImageView = new RoundImageView(context);
        roundImageView.setType(RoundImageView.TYPE_ROUND);
        roundImageView.setCornerRadius(6);
        roundImageView.setScaleType(ImageView.ScaleType.CENTER);
        addView(roundImageView, params);
        nameTv = new TextView(context);
        nameTv.setTextColor(Color.WHITE);
        nameTv.setPadding(5, 0, 5, 0);
        nameTv.setSingleLine(true);
        nameTv.setMaxLines(1);
        nameTv.setTextSize(TypedValue.COMPLEX_UNIT_PX,array.getDimensionPixelSize(R.styleable.AvatarImage_TSize, 14));
        nameTv.setGravity(Gravity.CENTER);
        addView(nameTv, params);
    }

    public void setTextSize(int textSize) {
        nameTv.setTextSize(textSize);
    }

    public void load(int resId) {
        roundImageView.setImageDrawable(AppCompatResources.getDrawable(getContext(), resId));
    }

    public void load(String name) {
        switch (name) {
            case "AI聊天（机器人）":
                roundImageView.setVisibility(VISIBLE);
                roundImageView.setImageResource(R.drawable.add_ic);
                break;
            case "新的朋友":
                roundImageView.setVisibility(VISIBLE);
                roundImageView.setImageResource(R.drawable.add_ic);
                break;
        }
    }

    public void load(Object url) {
        load(url, false, null);
    }

    public void load(Object url, String name) {
        load(url, false, name);
    }

    public void load(Object url, boolean isGroup) {
        load(url, isGroup, null);
    }

    public void load(Object url, boolean isGroup, String name) {
        int resId = isGroup ? R.drawable.status_order_ic : R.drawable.status_error_ic;
        roundImageView.setVisibility(GONE);
        nameTv.setVisibility(GONE);
        if (null == url || (url instanceof String && (String.valueOf(url).isEmpty() || String.valueOf(url).contains("ic_avatar")))) {
            if (TextUtils.isEmpty(name)) {
                roundImageView.setVisibility(VISIBLE);
                roundImageView.setImageDrawable(AppCompatResources.getDrawable(getContext(), resId));
            } else if (name.equals("AI聊天（机器人）")) {
                roundImageView.setVisibility(VISIBLE);
                roundImageView.setImageResource(R.drawable.add_ic);
            } else {
                nameTv.setVisibility(VISIBLE);
                name = name.substring(0, name.offsetByCodePoints(0, name.codePointCount(0, 1)));
//                if(!IMUtilKt.isEmoji(name)){
//                    name = name.substring(0, 1);
//                }
                setBackground(AppCompatResources.getDrawable(getContext(),
                        R.drawable.sty_radius_6_ff0089ff));
                nameTv.setVisibility(VISIBLE);
                nameTv.setText(name);
            }
        } else {
            roundImageView.setVisibility(VISIBLE);
            if (name.equals("AI聊天（机器人）") && url.equals("AI聊天（机器人）")) {
                roundImageView.setImageResource(R.drawable.add_ic);
            }  else {
                if (RegexUtils.isURL(String.valueOf(url))) {
                    GlideApp.with(getContext())
                            .load(url)
                            .error(resId)
                            .centerInside()
                            .into(new CustomTarget<Drawable>() {
                                @Override
                                public void onLoadStarted(@Nullable Drawable placeholder) {
                                    roundImageView.setImageResource(resId);
                                }

                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<
                                        ? super Drawable> transition) {
                                    roundImageView.setImageDrawable(resource);
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                    roundImageView.setImageDrawable(null);
                                }
                            });
                } else {
                    roundImageView.setImageResource(resId);
                }
            }

        }
    }
}
