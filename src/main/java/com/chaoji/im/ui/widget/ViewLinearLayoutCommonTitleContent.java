package com.chaoji.im.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.IdRes;

import com.chaoji.common.R;


public class ViewLinearLayoutCommonTitleContent extends LinearLayout {
    private static final String EXPAND = "展开详情";
    private static final String SHRINK = "收起";

    private TextView mTitle;
    private TextView mContent;
    private TextView mRedFlag;
    private ImageView editImg;
    private ImageView expandIcon;
    private LinearLayout expandLayout;
    private TextView expandTip;
    private View mUnderlineView;
    private int maxLine = 3;
    private int lineCount;
    private boolean showExpand = false;
    private boolean mShowRedFlag;

    public ViewLinearLayoutCommonTitleContent(Context context) {
        super(context);
        init(context, null);
    }

    public ViewLinearLayoutCommonTitleContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setOrientation(VERTICAL);
        setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT
                , LayoutParams.WRAP_CONTENT));
        View rootView = LayoutInflater.from(context).inflate(R.layout.common_title_content_layout, this);
        mTitle = (TextView) rootView.findViewById(R.id.common_title);
        mContent = (TextView) rootView.findViewById(R.id.common_content);
        mRedFlag = (TextView) findViewById(R.id.red_flag);
        mUnderlineView = rootView.findViewById(R.id.under_line);
        mContent.setMaxLines(maxLine);

        editImg = (ImageView) rootView.findViewById(R.id.edit_img);
        expandIcon = (ImageView) rootView.findViewById(R.id.expand_icon);
        expandTip = (TextView) rootView.findViewById(R.id.expand_tip);

        expandLayout = (LinearLayout) rootView.findViewById(R.id.expand_ctrl);
        expandLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                expandLayout.setSelected(!expandLayout.isSelected());
                if (expandLayout.isSelected()) {
                    expandIcon.setSelected(true);
                    mContent.setMaxLines(lineCount);
                    expandTip.setText(SHRINK);
                } else {
                    expandIcon.setSelected(false);
                    mContent.setMaxLines(maxLine);
                    expandTip.setText(EXPAND);
                }
            }
        });

        addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                change();
            }
        });


        mContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                change();
            }
        });

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.common_title_content);
            String title = a.getString(R.styleable.common_title_content_common_title);
            String content = a.getString(R.styleable.common_title_content_common_content);
            boolean editable = a.getBoolean(R.styleable.common_title_content_common_editable, true);
            mShowRedFlag = a.getBoolean(R.styleable.common_title_content_common_showredFlag, false);
            int editImgResId = a.getResourceId(R.styleable.common_title_content_common_edit_img, R.drawable.common_edit_selector);
            boolean mShowUnderline = a.getBoolean(R.styleable.common_title_content_common_showUnderline, true);
            a.recycle();
            if (title != null) {
                mTitle.setText(title);
            }
            if (content != null) {
                mContent.setText(content);
            }
            editImg.setImageResource(editImgResId);
            editable(editable);
            mUnderlineView.setVisibility(mShowUnderline ? VISIBLE : INVISIBLE);
        }
        mRedFlag.setVisibility(mShowRedFlag ? VISIBLE : INVISIBLE);
    }

    private void change() {
        lineCount = mContent.getLineCount();
        if (lineCount <= maxLine) {
            expandLayout.setVisibility(GONE);
            showExpand = false;
        } else {
            expandLayout.setVisibility(VISIBLE);
            showExpand = true;
            post(new Runnable() {
                @Override
                public void run() {
                    requestLayout();
                }
            });
        }
    }

    public void editable(boolean editable) {
        setEnabled(editable);
        setEditImgVisible(editable);
    }

    public void setEditImgVisible(boolean show) {
        if (show) {
            editImg.setVisibility(VISIBLE);
        } else {
            editImg.setVisibility(GONE);
        }
    }

    public ImageView getEditImg() {
        return editImg;
    }

    public void setMaxLine(int maxLine) {
        if (maxLine >= 0) {
            mContent.setMaxLines(maxLine);
        }
    }

    public void setEditImgRes(@IdRes int resId) {
        editImg.setImageResource(resId);
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

    public String getTitle() {
        return mTitle.getText().toString();
    }

    public void setContent(String content) {
        mContent.setText(content);
    }

    public String getContent() {
        return mContent.getText().toString();
    }

    public void showRedFlag(boolean isShow) {
        mRedFlag.setVisibility(isShow ? VISIBLE : INVISIBLE);
    }

    public void showUnderline(boolean isShow) {
        mUnderlineView.setVisibility(isShow ? VISIBLE : INVISIBLE);
    }

}
