package com.box.common.ui.xpop;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.box.other.blankj.utilcode.util.ColorUtils;
import com.box.common.R;
import com.box.other.xpopup.core.BubbleHorizontalAttachPopupView;
import com.box.other.xpopup.util.XPopupUtils;

public class XPopupBubbleHorizontalAttachCustom extends
        BubbleHorizontalAttachPopupView {
    @Override
    protected int getImplLayoutId() {
        return R.layout.xpopup_attach_custom;
    }

    private String content = "";
    private boolean complete = false;
    private View.OnClickListener checkListener;

    public XPopupBubbleHorizontalAttachCustom(@NonNull Context context) {
        super(context);
    }

    public XPopupBubbleHorizontalAttachCustom(@NonNull Context context, String content, boolean complete, View.OnClickListener checkListener) {
        super(context);
        this.content = content;
        this.complete = complete;
        this.checkListener = checkListener;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        setBubbleBgColor(Color.parseColor("#4D5063"));
        setBubbleShadowSize(XPopupUtils.dp2px(getContext(), 3));
        setBubbleShadowColor(Color.BLACK);
        getPopupImplView().setBackgroundResource(0);
        TextView contentView = findViewById(R.id.tv_zan);
        contentView.setText(content);

        TextView commentView = findViewById(R.id.tv_comment);
        commentView.setText(complete?"已完成":"去完成");
        commentView.setTextColor(complete? ColorUtils.getColor(R.color.white): ColorUtils.getColor(R.color.black));
        commentView.setBackgroundResource(complete?R.drawable.button_around_selector:R.drawable.button_apply_selector);

        contentView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        commentView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                checkListener.onClick(v);
            }
        });
    }

}
