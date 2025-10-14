package com.box.common.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.box.common.R;
import com.box.common.utils.GetFilePathFromUri;
import com.box.common.utils.UtilMediaFile;
import com.box.other.blankj.utilcode.util.AppUtils;


public class ViewRelativeLayoutFileUpload extends RelativeLayout {
    private ImageView res, bgMask;
    private ViewCirclePgBar circlePgBar;
    private String localUrl = "";

    public ViewRelativeLayoutFileUpload(Context context) {
        super(context);
        init();
    }

    public ViewRelativeLayoutFileUpload(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    void init() {
        res = new ImageView(getContext());
        bgMask = new ImageView(getContext());
        bgMask.setImageResource(R.mipmap.common_edit_icon_normal);

        LayoutParams params =
            new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        params.width = 80;
        params.height = 80;
        circlePgBar = new ViewCirclePgBar(getContext());
        circlePgBar.setLayoutParams(params);

        addView(res);
        addView(bgMask);
        addView(circlePgBar);

        setOnClickListener(v->{
            GetFilePathFromUri.openFile(getContext(), localUrl);
        });

    }

    public void setRes(String localUrl) {
        this.localUrl = localUrl;
        if (UtilMediaFile.isZIP(localUrl)) res.setImageDrawable(AppUtils.getAppIcon());
        else res.setImageResource(R.mipmap.common_edit_icon_focus);
    }

    public void setForegroundVisibility(boolean visibility) {
        bgMask.setVisibility(visibility ? GONE : VISIBLE);
        circlePgBar.setVisibility(visibility ? GONE : VISIBLE);
        if (visibility)
            circlePgBar.reset();
    }

    public void setProgress(int progress) {
        circlePgBar.setTargetProgress(progress);
    }

}
