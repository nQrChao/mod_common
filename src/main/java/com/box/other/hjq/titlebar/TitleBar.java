package com.box.other.hjq.titlebar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.box.other.hjq.titlebar.style.LightBarStyle;
import com.box.other.hjq.titlebar.style.NightBarStyle;
import com.box.other.hjq.titlebar.style.RippleBarStyle;
import com.box.other.hjq.titlebar.style.TransparentBarStyle;
import com.box.com.R;


@SuppressWarnings({"unused", "UnusedReturnValue"})
public class TitleBar extends FrameLayout implements View.OnClickListener, View.OnLayoutChangeListener {

    private static final String LOG_TAG = "TitleBar";

    private static ITitleBarStyle sGlobalStyle;
    private final ITitleBarStyle mCurrentStyle;
    private OnTitleBarListener mListener;
    private final TextView mLeftView, mTitleView, mRightView;
    private final View mLineView;
    private int mLeftHorizontalPadding, mTitleHorizontalPadding, mRightHorizontalPadding;
    private int mVerticalPadding;
    private int mLeftIconWidth, mLeftIconHeight;
    private int mTitleIconWidth, mTitleIconHeight;
    private int mRightIconWidth, mRightIconHeight;
    private int mLeftIconGravity, mTitleIconGravity, mRightIconGravity;
    private int mLeftIconTint, mTitleIconTint, mRightIconTint = TitleBarSupport.NO_COLOR;

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (sGlobalStyle == null) {
            sGlobalStyle = new LightBarStyle();
        }

        final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TitleBar, 0, R.style.TitleBarDefaultStyle);

        // 标题栏样式设置
        switch (array.getInt(R.styleable.TitleBar_barStyle, 0)) {
            case 0x10:
                mCurrentStyle = new LightBarStyle();
                break;
            case 0x20:
                mCurrentStyle = new NightBarStyle();
                break;
            case 0x30:
                mCurrentStyle = new TransparentBarStyle();
                break;
            case 0x40:
                mCurrentStyle = new RippleBarStyle();
                break;
            default:
                mCurrentStyle = TitleBar.sGlobalStyle;
                break;
        }

        mTitleView = mCurrentStyle.createTitleView(context);
        mLeftView = mCurrentStyle.createLeftView(context);
        mRightView = mCurrentStyle.createRightView(context);
        mLineView = mCurrentStyle.createLineView(context);

        mTitleView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER));
        mLeftView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT, Gravity.START | Gravity.CENTER_VERTICAL));
        mRightView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT, Gravity.END | Gravity.CENTER_VERTICAL));
        mLineView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                mCurrentStyle.getLineSize(context), Gravity.BOTTOM));

        // 设置图标显示的重心
        setTitleIconGravity(array.getInt(R.styleable.TitleBar_titleIconGravity, mCurrentStyle.getTitleIconGravity(context)));
        setLeftIconGravity(array.getInt(R.styleable.TitleBar_leftIconGravity, mCurrentStyle.getLeftIconGravity(context)));
        setRightIconGravity(array.getInt(R.styleable.TitleBar_rightIconGravity, mCurrentStyle.getRightIconGravity(context)));

        // 设置图标显示的大小
        setTitleIconSize(array.getDimensionPixelSize(R.styleable.TitleBar_titleIconWidth, mCurrentStyle.getTitleIconWidth(context)),
                array.getDimensionPixelSize(R.styleable.TitleBar_titleIconHeight, mCurrentStyle.getTitleIconHeight(context)));
        setLeftIconSize(array.getDimensionPixelSize(R.styleable.TitleBar_leftIconWidth, mCurrentStyle.getLeftIconWidth(context)),
                array.getDimensionPixelSize(R.styleable.TitleBar_leftIconHeight, mCurrentStyle.getLeftIconHeight(context)));
        setRightIconSize(array.getDimensionPixelSize(R.styleable.TitleBar_rightIconWidth, mCurrentStyle.getRightIconWidth(context)),
                array.getDimensionPixelSize(R.styleable.TitleBar_rightIconHeight, mCurrentStyle.getRightIconHeight(context)));

        setTitleIconPadding(array.getDimensionPixelSize(R.styleable.TitleBar_titleIconPadding, mCurrentStyle.getTitleIconPadding(context)));
        setLeftIconPadding(array.getDimensionPixelSize(R.styleable.TitleBar_leftIconPadding, mCurrentStyle.getLeftIconPadding(context)));
        setRightIconPadding(array.getDimensionPixelSize(R.styleable.TitleBar_rightIconPadding, mCurrentStyle.getRightIconPadding(context)));

        if (array.hasValue(R.styleable.TitleBar_title)) {
            setTitle(array.getResourceId(R.styleable.TitleBar_title, 0) != R.string.bar_string_placeholder ?
                    array.getString(R.styleable.TitleBar_title) : mCurrentStyle.getTitle(context));
        }

        if (array.hasValue(R.styleable.TitleBar_leftTitle)) {
            setLeftTitle(array.getResourceId(R.styleable.TitleBar_leftTitle, 0) != R.string.bar_string_placeholder ?
                    array.getString(R.styleable.TitleBar_leftTitle) : mCurrentStyle.getLeftTitle(context));
        }

        if (array.hasValue(R.styleable.TitleBar_rightTitle)) {
            setRightTitle(array.getResourceId(R.styleable.TitleBar_rightTitle, 0) != R.string.bar_string_placeholder ?
                    array.getString(R.styleable.TitleBar_rightTitle) : mCurrentStyle.getRightTitle(context));
        }

        if (array.hasValue(R.styleable.TitleBar_titleIconTint)) {
            setTitleIconTint(array.getColor(R.styleable.TitleBar_titleIconTint, 0));
        }

        if (array.hasValue(R.styleable.TitleBar_leftIconTint)) {
            setLeftIconTint(array.getColor(R.styleable.TitleBar_leftIconTint, 0));
        }

        if (array.hasValue(R.styleable.TitleBar_rightIconTint)) {
            setRightIconTint(array.getColor(R.styleable.TitleBar_rightIconTint, 0));
        }

        if (array.hasValue(R.styleable.TitleBar_titleIcon)) {
            setTitleIcon(TitleBarSupport.getDrawable(context, array.getResourceId(R.styleable.TitleBar_titleIcon, 0)));
        }

        if (array.hasValue(R.styleable.TitleBar_leftIcon)) {
            setLeftIcon(array.getResourceId(R.styleable.TitleBar_leftIcon, 0) != R.drawable.bar_drawable_placeholder ?
                    TitleBarSupport.getDrawable(context, array.getResourceId(R.styleable.TitleBar_leftIcon, 0)) :
                    mCurrentStyle.getBackButtonDrawable(context));
        }

        if (array.hasValue(R.styleable.TitleBar_rightIcon)) {
            setRightIcon(TitleBarSupport.getDrawable(context, array.getResourceId(R.styleable.TitleBar_rightIcon, 0)));
        }

        setTitleColor(array.hasValue(R.styleable.TitleBar_titleColor) ?
                array.getColorStateList(R.styleable.TitleBar_titleColor) :
                mCurrentStyle.getTitleColor(context));
        setLeftTitleColor(array.hasValue(R.styleable.TitleBar_leftTitleColor) ?
                array.getColorStateList(R.styleable.TitleBar_leftTitleColor) :
                mCurrentStyle.getLeftTitleColor(context));
        setRightTitleColor(array.hasValue(R.styleable.TitleBar_rightTitleColor) ?
                array.getColorStateList(R.styleable.TitleBar_rightTitleColor) :
                mCurrentStyle.getRightTitleColor(context));

        setTitleSize(TypedValue.COMPLEX_UNIT_PX, array.hasValue(R.styleable.TitleBar_titleSize) ?
                array.getDimensionPixelSize(R.styleable.TitleBar_titleSize, 0) :
                mCurrentStyle.getTitleSize(context));
        setLeftTitleSize(TypedValue.COMPLEX_UNIT_PX, array.hasValue(R.styleable.TitleBar_leftTitleSize) ?
                array.getDimensionPixelSize(R.styleable.TitleBar_leftTitleSize, 0) :
                mCurrentStyle.getLeftTitleSize(context));
        setRightTitleSize(TypedValue.COMPLEX_UNIT_PX, array.hasValue(R.styleable.TitleBar_rightTitleSize) ?
                array.getDimensionPixelSize(R.styleable.TitleBar_rightTitleSize, 0) :
                mCurrentStyle.getRightTitleSize(context));

        int titleStyle = array.hasValue(R.styleable.TitleBar_titleStyle) ?
                array.getInt(R.styleable.TitleBar_titleStyle, Typeface.NORMAL) :
                mCurrentStyle.getTitleStyle(context);
        setTitleStyle(mCurrentStyle.getTitleTypeface(context, titleStyle), titleStyle);

        int leftTitleStyle = array.hasValue(R.styleable.TitleBar_leftTitleStyle) ?
                array.getInt(R.styleable.TitleBar_leftTitleStyle, Typeface.NORMAL) :
                mCurrentStyle.getLeftTitleStyle(context);
        setLeftTitleStyle(mCurrentStyle.getLeftTitleTypeface(context, leftTitleStyle), leftTitleStyle);

        int rightTitleStyle = array.hasValue(R.styleable.TitleBar_rightTitleStyle) ?
                array.getInt(R.styleable.TitleBar_rightTitleStyle, Typeface.NORMAL) :
                mCurrentStyle.getRightTitleStyle(context);
        setRightTitleStyle(mCurrentStyle.getRightTitleTypeface(context, rightTitleStyle), rightTitleStyle);

        TextUtils.TruncateAt titleOverflowMode = array.hasValue(R.styleable.TitleBar_titleOverflowMode) ?
                TitleBarSupport.convertIntToTruncateAtEnum(array.getInt(R.styleable.TitleBar_titleOverflowMode, TitleBarSupport.ELLIPSIZE_NONE)) :
                mCurrentStyle.getTitleOverflowMode(context);
        setTitleOverflowMode(titleOverflowMode);

        TextUtils.TruncateAt leftTitleOverflowMode = array.hasValue(R.styleable.TitleBar_leftTitleOverflowMode) ?
                TitleBarSupport.convertIntToTruncateAtEnum(array.getInt(R.styleable.TitleBar_leftTitleOverflowMode, TitleBarSupport.ELLIPSIZE_NONE)) :
                mCurrentStyle.getLeftTitleOverflowMode(context);
        setLeftTitleOverflowMode(leftTitleOverflowMode);

        TextUtils.TruncateAt rightTitleOverflowMode = array.hasValue(R.styleable.TitleBar_rightTitleOverflowMode) ?
                TitleBarSupport.convertIntToTruncateAtEnum(array.getInt(R.styleable.TitleBar_rightTitleOverflowMode, TitleBarSupport.ELLIPSIZE_NONE)) :
                mCurrentStyle.getRightTitleOverflowMode(context);
        setRightTitleOverflowMode(rightTitleOverflowMode);

        // 标题重心设置
        if (array.hasValue(R.styleable.TitleBar_titleGravity)) {
            setTitleGravity(array.getInt(R.styleable.TitleBar_titleGravity, Gravity.NO_GRAVITY));
        }

        // 设置背景
        if (array.hasValue(R.styleable.TitleBar_android_background)) {
            if (array.getResourceId(R.styleable.TitleBar_android_background, 0) == R.drawable.bar_drawable_placeholder) {
                TitleBarSupport.setBackground(this, mCurrentStyle.getTitleBarBackground(context));
            }
        }

        if (array.hasValue(R.styleable.TitleBar_leftBackground)) {
            setLeftBackground(array.getResourceId(R.styleable.TitleBar_leftBackground, 0) != R.drawable.bar_drawable_placeholder ?
                    array.getDrawable(R.styleable.TitleBar_leftBackground) : mCurrentStyle.getLeftTitleBackground(context));
        }

        if (array.hasValue(R.styleable.TitleBar_rightBackground)) {
            setRightBackground(array.getResourceId(R.styleable.TitleBar_rightBackground, 0) != R.drawable.bar_drawable_placeholder ?
                    array.getDrawable(R.styleable.TitleBar_rightBackground) : mCurrentStyle.getRightTitleBackground(context));
        }

        if (array.hasValue(R.styleable.TitleBar_leftForeground)) {
            setLeftForeground(array.getResourceId(R.styleable.TitleBar_leftForeground, 0) != R.drawable.bar_drawable_placeholder ?
                    array.getDrawable(R.styleable.TitleBar_leftForeground) : mCurrentStyle.getLeftTitleForeground(context));
        }

        if (array.hasValue(R.styleable.TitleBar_rightForeground)) {
            setRightForeground(array.getResourceId(R.styleable.TitleBar_rightForeground, 0) != R.drawable.bar_drawable_placeholder ?
                    array.getDrawable(R.styleable.TitleBar_rightForeground) : mCurrentStyle.getRightTitleForeground(context));
        }

        setLineVisible(array.getBoolean(R.styleable.TitleBar_lineVisible, mCurrentStyle.isLineVisible(context)));

        if (array.hasValue(R.styleable.TitleBar_lineDrawable)) {
            setLineDrawable(array.getResourceId(R.styleable.TitleBar_lineDrawable, 0) != R.drawable.bar_drawable_placeholder ?
                    array.getDrawable(R.styleable.TitleBar_lineDrawable) : mCurrentStyle.getLineDrawable(context));
        }

        if (array.hasValue(R.styleable.TitleBar_lineSize)) {
            setLineSize(array.getDimensionPixelSize(R.styleable.TitleBar_lineSize, 0));
        }

        mLeftHorizontalPadding = array.getDimensionPixelSize(R.styleable.TitleBar_leftHorizontalPadding, mCurrentStyle.getLeftHorizontalPadding(context));
        mTitleHorizontalPadding = array.getDimensionPixelSize(R.styleable.TitleBar_titleHorizontalPadding, mCurrentStyle.getTitleHorizontalPadding(context));
        mRightHorizontalPadding = array.getDimensionPixelSize(R.styleable.TitleBar_rightHorizontalPadding, mCurrentStyle.getRightHorizontalPadding(context));
        setChildHorizontalPadding(mLeftHorizontalPadding, mTitleHorizontalPadding, mRightHorizontalPadding);

        mVerticalPadding = array.getDimensionPixelSize(R.styleable.TitleBar_childVerticalPadding, mCurrentStyle.getChildVerticalPadding(context));
        setChildVerticalPadding(mVerticalPadding);

        array.recycle();

        addView(mTitleView, 0);
        addView(mLeftView, 1);
        addView(mRightView, 2);
        addView(mLineView, 3);

        addOnLayoutChangeListener(this);

        if (isInEditMode()) {
            measure(0, 0);
            mTitleView.measure(0, 0);
            mLeftView.measure(0, 0);
            mRightView.measure(0, 0);
            int horizontalMargin = Math.max(
                    mLeftView.getMeasuredWidth() + mLeftHorizontalPadding * 2,
                    mRightView.getMeasuredWidth() + mRightHorizontalPadding * 2);
            MarginLayoutParams layoutParams = (MarginLayoutParams) mTitleView.getLayoutParams();
            layoutParams.setMargins(horizontalMargin, 0, horizontalMargin, 0);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int titleBarWidth = this.getMeasuredWidth();
        int titleBarHeight = this.getMeasuredHeight();

        int leftViewMeasuredWidth = mLeftView.getMeasuredWidth();
        int leftViewMeasuredHeight = mLeftView.getMeasuredHeight();

        int titleViewMeasuredWidth = mTitleView.getMeasuredWidth();
        int titleViewMeasuredHeight = mTitleView.getMeasuredHeight();

        int rightViewMeasuredWidth = mRightView.getMeasuredWidth();
        int rightViewMeasuredHeight = mRightView.getMeasuredHeight();

        int maxEdgeWidth = Math.max(leftViewMeasuredWidth, rightViewMeasuredWidth);
        int calculateTotalWidth = maxEdgeWidth * 2 + titleViewMeasuredWidth;
        if (calculateTotalWidth <= titleBarWidth) {
            measureTitleBar((TitleBarSupport.containContent(mLeftView) ? leftViewMeasuredWidth : 0), titleViewMeasuredWidth,
                    (TitleBarSupport.containContent(mRightView) ? rightViewMeasuredWidth : 0), heightMeasureSpec);
            return;
        }

        int leftViewWidth;
        int titleViewWidth;
        int rightViewWidth;

        if (maxEdgeWidth > titleBarWidth / 3) {
            leftViewWidth = titleBarWidth / 4;
            titleViewWidth = titleBarWidth / 2;
            rightViewWidth = titleBarWidth / 4;
        } else {
            leftViewWidth = maxEdgeWidth;
            titleViewWidth = titleBarWidth - maxEdgeWidth * 2;
            rightViewWidth = maxEdgeWidth;
        }

        measureTitleBar((TitleBarSupport.containContent(mLeftView) ? leftViewWidth : 0), titleViewWidth,
                (TitleBarSupport.containContent(mRightView) ? rightViewWidth : 0), heightMeasureSpec);
    }

    private void measureTitleBar(int leftViewWidth, int titleViewWidth, int rightViewWidth, int titleBarHeightMeasureSpec) {
        int leftWidthMeasureSpec = MeasureSpec.makeMeasureSpec(leftViewWidth, MeasureSpec.EXACTLY);
        int titleWidthMeasureSpec = MeasureSpec.makeMeasureSpec(titleViewWidth, MeasureSpec.EXACTLY);
        int rightWidthMeasureSpec = MeasureSpec.makeMeasureSpec(rightViewWidth, MeasureSpec.EXACTLY);
        measureChildWithMargins(mLeftView, leftWidthMeasureSpec, 0, titleBarHeightMeasureSpec, 0);
        measureChildWithMargins(mTitleView, titleWidthMeasureSpec, 0, titleBarHeightMeasureSpec, 0);
        measureChildWithMargins(mRightView, rightWidthMeasureSpec, 0, titleBarHeightMeasureSpec, 0);
        int titleBarMeasuredHeight = this.getMeasuredHeight();
        if (titleBarMeasuredHeight != mLeftView.getMeasuredHeight()) {
            mLeftView.measure(leftWidthMeasureSpec, MeasureSpec.makeMeasureSpec(titleBarMeasuredHeight, MeasureSpec.EXACTLY));
        }

        if (titleBarMeasuredHeight != mTitleView.getMeasuredHeight()) {
            mTitleView.measure(titleWidthMeasureSpec, MeasureSpec.makeMeasureSpec(titleBarMeasuredHeight, MeasureSpec.EXACTLY));
        }

        if (titleBarMeasuredHeight != mRightView.getMeasuredHeight()) {
            mRightView.measure(rightWidthMeasureSpec, MeasureSpec.makeMeasureSpec(titleBarMeasuredHeight, MeasureSpec.EXACTLY));
        }
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if (!mLeftView.isClickable()) {
            mLeftView.setClickable(true);
        }
        if (!mTitleView.isClickable()) {
            mTitleView.setClickable(true);
        }
        if (!mRightView.isClickable()) {
            mRightView.setClickable(true);
        }

        mLeftView.setEnabled(TitleBarSupport.containContent(mLeftView));
        mTitleView.setEnabled(TitleBarSupport.containContent(mTitleView));
        mRightView.setEnabled(TitleBarSupport.containContent(mRightView));
    }

    /**
     * {@link OnClickListener}
     */

    @Override
    public void onClick(View view) {
        if (mListener == null) {
            return;
        }

        if (view == mLeftView) {
            mListener.onLeftClick(this);
        } else if (view == mRightView) {
            mListener.onRightClick(this);
        } else if (view == mTitleView) {
            mListener.onTitleClick(this);
        }
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        if (params.width == LayoutParams.WRAP_CONTENT) {
            params.width = LayoutParams.MATCH_PARENT;
        }

        int verticalPadding = 0;
        if (params.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            verticalPadding = mVerticalPadding;
        }

        setChildVerticalPadding(verticalPadding);
        super.setLayoutParams(params);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    public TitleBar setOnTitleBarListener(OnTitleBarListener listener) {
        mListener = listener;
        mTitleView.setOnClickListener(this);
        mLeftView.setOnClickListener(this);
        mRightView.setOnClickListener(this);
        return this;
    }

    public TitleBar setTitle(int id) {
        return setTitle(getResources().getString(id));
    }

    public TitleBar setTitle(CharSequence text) {
        mTitleView.setText(text);
        return this;
    }

    public CharSequence getTitle() {
        return mTitleView.getText();
    }

    public TitleBar setLeftTitle(int id) {
        return setLeftTitle(getResources().getString(id));
    }

    public TitleBar setLeftTitle(CharSequence text) {
        mLeftView.setText(text);
        return this;
    }

    public CharSequence getLeftTitle() {
        return mLeftView.getText();
    }

    public TitleBar setRightTitle(int id) {
        return setRightTitle(getResources().getString(id));
    }

    public TitleBar setRightTitle(CharSequence text) {
        mRightView.setText(text);
        return this;
    }

    public CharSequence getRightTitle() {
        return mRightView.getText();
    }

    public TitleBar setTitleStyle(int style) {
        return setTitleStyle(TitleBarSupport.getTextTypeface(style), style);
    }

    public TitleBar setTitleStyle(Typeface typeface, int style) {
        mTitleView.setTypeface(typeface, style);
        return this;
    }

    public TitleBar setLeftTitleStyle(int style) {
        return setLeftTitleStyle(TitleBarSupport.getTextTypeface(style), style);
    }

    public TitleBar setLeftTitleStyle(Typeface typeface, int style) {
        mLeftView.setTypeface(typeface, style);
        return this;
    }

    public TitleBar setRightTitleStyle(int style) {
        return setRightTitleStyle(TitleBarSupport.getTextTypeface(style), style);
    }

    public TitleBar setRightTitleStyle(Typeface typeface, int style) {
        mRightView.setTypeface(typeface, style);
        return this;
    }

    public TitleBar setTitleOverflowMode(TextUtils.TruncateAt where) {
        TitleBarSupport.setTextViewEllipsize(mTitleView, where);
        return this;
    }

    public TitleBar setLeftTitleOverflowMode(TextUtils.TruncateAt where) {
        TitleBarSupport.setTextViewEllipsize(mLeftView, where);
        return this;
    }

    public TitleBar setRightTitleOverflowMode(TextUtils.TruncateAt where) {
        TitleBarSupport.setTextViewEllipsize(mRightView, where);
        return this;
    }

    public TitleBar setTitleColor(int color) {
        return setTitleColor(ColorStateList.valueOf(color));
    }

    public TitleBar setTitleColor(ColorStateList color) {
        if (color != null) {
            mTitleView.setTextColor(color);
        }
        return this;
    }

    public TitleBar setLeftTitleColor(int color) {
        return setLeftTitleColor(ColorStateList.valueOf(color));
    }

    public TitleBar setLeftTitleColor(ColorStateList color) {
        if (color != null) {
            mLeftView.setTextColor(color);
        }
        return this;
    }

    public TitleBar setRightTitleColor(int color) {
        return setRightTitleColor(ColorStateList.valueOf(color));
    }

    public TitleBar setRightTitleColor(ColorStateList color) {
        if (color != null) {
            mRightView.setTextColor(color);
        }
        return this;
    }

    public TitleBar setTitleSize(float size) {
        return setTitleSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public TitleBar setTitleSize(int unit, float size) {
        mTitleView.setTextSize(unit, size);
        return this;
    }

    public TitleBar setLeftTitleSize(float size) {
        return setLeftTitleSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public TitleBar setLeftTitleSize(int unit, float size) {
        mLeftView.setTextSize(unit, size);
        return this;
    }

    public TitleBar setRightTitleSize(float size) {
        return setRightTitleSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public TitleBar setRightTitleSize(int unit, float size) {
        mRightView.setTextSize(unit, size);
        return this;
    }

    public TitleBar setTitleIcon(int id) {
        return setTitleIcon(TitleBarSupport.getDrawable(getContext(), id));
    }

    public TitleBar setTitleIcon(Drawable drawable) {
        TitleBarSupport.setDrawableTint(drawable, mTitleIconTint);
        TitleBarSupport.setDrawableSize(drawable, mTitleIconWidth, mTitleIconHeight);
        TitleBarSupport.setTextCompoundDrawable(mTitleView, drawable, mTitleIconGravity);
        return this;
    }

    public Drawable getTitleIcon() {
        return TitleBarSupport.getTextCompoundDrawable(mTitleView, mTitleIconGravity);
    }

    public TitleBar setLeftIcon(int id) {
        return setLeftIcon(TitleBarSupport.getDrawable(getContext(), id));
    }

    public TitleBar setLeftIcon(Drawable drawable) {
        TitleBarSupport.setDrawableTint(drawable, mLeftIconTint);
        TitleBarSupport.setDrawableSize(drawable, mLeftIconWidth, mLeftIconHeight);
        TitleBarSupport.setTextCompoundDrawable(mLeftView, drawable, mLeftIconGravity);
        return this;
    }

    public Drawable getLeftIcon() {
        return TitleBarSupport.getTextCompoundDrawable(mLeftView, mLeftIconGravity);
    }

    public TitleBar setRightIcon(int id) {
        return setRightIcon(TitleBarSupport.getDrawable(getContext(), id));
    }

    public TitleBar setRightIcon(Drawable drawable) {
        TitleBarSupport.setDrawableTint(drawable, mRightIconTint);
        TitleBarSupport.setDrawableSize(drawable, mRightIconWidth, mRightIconHeight);
        TitleBarSupport.setTextCompoundDrawable(mRightView, drawable, mRightIconGravity);
        return this;
    }

    public Drawable getRightIcon() {
        return TitleBarSupport.getTextCompoundDrawable(mRightView, mRightIconGravity);
    }

    public TitleBar setTitleIconSize(int width, int height) {
        mTitleIconWidth = width;
        mTitleIconHeight = height;
        TitleBarSupport.setDrawableSize(getTitleIcon(), width, height);
        return this;
    }

    public TitleBar setLeftIconSize(int width, int height) {
        mLeftIconWidth = width;
        mLeftIconHeight = height;
        TitleBarSupport.setDrawableSize(getLeftIcon(), width, height);
        return this;
    }

    public TitleBar setRightIconSize(int width, int height) {
        mRightIconWidth = width;
        mRightIconHeight = height;
        TitleBarSupport.setDrawableSize(getRightIcon(), width, height);
        return this;
    }

    public TitleBar setTitleIconPadding(int padding) {
        mTitleView.setCompoundDrawablePadding(padding);
        return this;
    }

    public TitleBar setLeftIconPadding(int padding) {
        mLeftView.setCompoundDrawablePadding(padding);
        return this;
    }

    public TitleBar setRightIconPadding(int padding) {
        mRightView.setCompoundDrawablePadding(padding);
        return this;
    }

    public TitleBar setTitleIconTint(int color) {
        mTitleIconTint = color;
        TitleBarSupport.setDrawableTint(getTitleIcon(), color);
        return this;
    }

    public TitleBar setLeftIconTint(int color) {
        mLeftIconTint = color;
        TitleBarSupport.setDrawableTint(getLeftIcon(), color);
        return this;
    }

    public TitleBar setRightIconTint(int color) {
        mRightIconTint = color;
        TitleBarSupport.setDrawableTint(getRightIcon(), color);
        return this;
    }

    public TitleBar clearTitleIconTint() {
        mTitleIconTint = TitleBarSupport.NO_COLOR;
        TitleBarSupport.clearDrawableTint(getTitleIcon());
        return this;
    }

    public TitleBar clearLeftIconTint() {
        mLeftIconTint = TitleBarSupport.NO_COLOR;
        TitleBarSupport.clearDrawableTint(getLeftIcon());
        return this;
    }

    public TitleBar clearRightIconTint() {
        mRightIconTint = TitleBarSupport.NO_COLOR;
        TitleBarSupport.clearDrawableTint(getRightIcon());
        return this;
    }

    public TitleBar setTitleIconGravity(int gravity) {
        Drawable drawable = getTitleIcon();
        mTitleIconGravity = gravity;
        if (drawable != null) {
            TitleBarSupport.setTextCompoundDrawable(mTitleView, drawable, gravity);
        }
        return this;
    }

    public TitleBar setLeftIconGravity(int gravity) {
        Drawable drawable = getLeftIcon();
        mLeftIconGravity = gravity;
        if (drawable != null) {
            TitleBarSupport.setTextCompoundDrawable(mLeftView, drawable, gravity);
        }
        return this;
    }

    public TitleBar setRightIconGravity(int gravity) {
        Drawable drawable = getRightIcon();
        mRightIconGravity = gravity;
        if (drawable != null) {
            TitleBarSupport.setTextCompoundDrawable(mRightView, drawable, gravity);
        }
        return this;
    }

    public TitleBar setLeftBackground(int id) {
        return setLeftBackground(TitleBarSupport.getDrawable(getContext(), id));
    }

    public TitleBar setLeftBackground(Drawable drawable) {
        TitleBarSupport.setBackground(mLeftView, drawable);
        return this;
    }

    public TitleBar setRightBackground(int id) {
        return setRightBackground(TitleBarSupport.getDrawable(getContext(), id));
    }

    public TitleBar setRightBackground(Drawable drawable) {
        TitleBarSupport.setBackground(mRightView, drawable);
        return this;
    }

    public TitleBar setLeftForeground(int id) {
        return setLeftForeground(TitleBarSupport.getDrawable(getContext(), id));
    }

    public TitleBar setLeftForeground(Drawable drawable) {
        TitleBarSupport.setForeground(mLeftView, drawable);
        return this;
    }

    public TitleBar setRightForeground(int id) {
        return setRightForeground(TitleBarSupport.getDrawable(getContext(), id));
    }

    public TitleBar setRightForeground(Drawable drawable) {
        TitleBarSupport.setForeground(mRightView, drawable);
        return this;
    }

    public TitleBar setLineVisible(boolean visible) {
        mLineView.setVisibility(visible ? VISIBLE : INVISIBLE);
        return this;
    }

    public TitleBar setLineColor(int color) {
        return setLineDrawable(new ColorDrawable(color));
    }

    public TitleBar setLineDrawable(Drawable drawable) {
        TitleBarSupport.setBackground(mLineView, drawable);
        return this;
    }

    public TitleBar setLineSize(int px) {
        ViewGroup.LayoutParams layoutParams = mLineView.getLayoutParams();
        layoutParams.height = px;
        mLineView.setLayoutParams(layoutParams);
        return this;
    }

    @SuppressLint("RtlHardcoded")
    public TitleBar setTitleGravity(int gravity) {
        gravity = TitleBarSupport.getAbsoluteGravity(this, gravity);

        if (gravity == Gravity.LEFT &&
                TitleBarSupport.containContent(TitleBarSupport.isLayoutRtl(getContext()) ? mRightView : mLeftView)) {
            Log.e(LOG_TAG, "Title center of gravity for the left, the left title can not have content");
            return this;
        }

        if (gravity == Gravity.RIGHT &&
                TitleBarSupport.containContent(TitleBarSupport.isLayoutRtl(getContext()) ? mLeftView : mRightView)) {
            Log.e(LOG_TAG, "Title center of gravity for the right, the right title can not have content");
            return this;
        }

        LayoutParams params = (LayoutParams) mTitleView.getLayoutParams();
        params.gravity = gravity;
        mTitleView.setLayoutParams(params);
        return this;
    }

    public TitleBar setChildVerticalPadding(int verticalPadding) {
        mVerticalPadding = verticalPadding;
        mLeftView.setPadding(mLeftHorizontalPadding, mVerticalPadding, mLeftHorizontalPadding, mVerticalPadding);
        mTitleView.setPadding(mTitleHorizontalPadding, mVerticalPadding, mTitleHorizontalPadding, mVerticalPadding);
        mRightView.setPadding(mRightHorizontalPadding, mVerticalPadding, mRightHorizontalPadding, mVerticalPadding);
        return this;
    }

    public TitleBar setChildHorizontalPadding(int leftHorizontalPadding, int titleHorizontalPadding, int rightHorizontalPadding) {
        mLeftHorizontalPadding = leftHorizontalPadding;
        mTitleHorizontalPadding = titleHorizontalPadding;
        mRightHorizontalPadding = rightHorizontalPadding;
        mLeftView.setPadding(mLeftHorizontalPadding, mVerticalPadding, mLeftHorizontalPadding, mVerticalPadding);
        mTitleView.setPadding(mTitleHorizontalPadding, mVerticalPadding, mTitleHorizontalPadding, mVerticalPadding);
        mRightView.setPadding(mRightHorizontalPadding, mVerticalPadding, mRightHorizontalPadding, mVerticalPadding);
        return this;
    }

    public TextView getLeftView() {
        return mLeftView;
    }

    public TextView getTitleView() {
        return mTitleView;
    }

    public TextView getRightView() {
        return mRightView;
    }

    public View getLineView() {
        return mLineView;
    }

    public ITitleBarStyle getCurrentStyle() {
        return mCurrentStyle;
    }

    public static void setDefaultStyle(ITitleBarStyle style) {
        sGlobalStyle = style;
    }
}