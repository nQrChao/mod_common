package com.chaoji.im.ui.widget.circleprogress;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.chaoji.common.R;


public class CircleProgress extends View {

    private static final String TAG = CircleProgress.class.getSimpleName();
    private Context mContext;
    private int mDefaultSize;
    private boolean antiAlias;
    private TextPaint mHintPaint;
    private CharSequence mHint;
    private int mHintColor;
    private float mHintSize;
    private float mHintOffset;

    private TextPaint mUnitPaint;
    private CharSequence mUnit;
    private int mUnitColor;
    private float mUnitSize;
    private float mUnitOffset;

    private TextPaint mValuePaint;
    private float mValue;
    private float mMaxValue;
    private float mValueOffset;
    private int mPrecision;
    private String mPrecisionFormat;
    private int mValueColor;
    private float mValueSize;

    private Paint mArcPaint;
    private float mArcWidth;
    private float mStartAngle, mSweepAngle;
    private RectF mRectF;
    private SweepGradient mSweepGradient;
    private int[] mGradientColors = {Color.GREEN, Color.YELLOW, Color.RED};
    private float mPercent;
    private long mAnimTime;
    private ValueAnimator mAnimator;

    private Paint mBgArcPaint;
    private int mBgArcColor;
    private float mBgArcWidth;

    private Point mCenterPoint;
    private float mRadius;
    private float mTextOffsetPercentInRadius;

    public CircleProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        mDefaultSize = MiscUtil.dipToPx(mContext, Constant.DEFAULT_SIZE);
        mAnimator = new ValueAnimator();
        mRectF = new RectF();
        mCenterPoint = new Point();
        initAttrs(attrs);
        initPaint();
        setValue(mValue);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);

        antiAlias = typedArray.getBoolean(R.styleable.CircleProgressBar_antiAlias, Constant.ANTI_ALIAS);

        mHint = typedArray.getString(R.styleable.CircleProgressBar_hint);
        mHintColor = typedArray.getColor(R.styleable.CircleProgressBar_hintColor, Color.BLACK);
        mHintSize = typedArray.getDimension(R.styleable.CircleProgressBar_hintSize, Constant.DEFAULT_HINT_SIZE);

        mValue = typedArray.getFloat(R.styleable.CircleProgressBar_value, Constant.DEFAULT_VALUE);
        mMaxValue = typedArray.getFloat(R.styleable.CircleProgressBar_maxValue, Constant.DEFAULT_MAX_VALUE);
        mPrecision = typedArray.getInt(R.styleable.CircleProgressBar_precision, 0);
        mPrecisionFormat = MiscUtil.getPrecisionFormat(mPrecision);
        mValueColor = typedArray.getColor(R.styleable.CircleProgressBar_valueColor, Color.BLACK);
        mValueSize = typedArray.getDimension(R.styleable.CircleProgressBar_valueSize, Constant.DEFAULT_VALUE_SIZE);

        mUnit = typedArray.getString(R.styleable.CircleProgressBar_unit);
        mUnitColor = typedArray.getColor(R.styleable.CircleProgressBar_unitColor, Color.BLACK);
        mUnitSize = typedArray.getDimension(R.styleable.CircleProgressBar_unitSize, Constant.DEFAULT_UNIT_SIZE);

        mArcWidth = typedArray.getDimension(R.styleable.CircleProgressBar_arcWidth, Constant.DEFAULT_ARC_WIDTH);
        mStartAngle = typedArray.getFloat(R.styleable.CircleProgressBar_startAngle, Constant.DEFAULT_START_ANGLE);
        mSweepAngle = typedArray.getFloat(R.styleable.CircleProgressBar_sweepAngle, Constant.DEFAULT_SWEEP_ANGLE);

        mBgArcColor = typedArray.getColor(R.styleable.CircleProgressBar_bgArcColor, Color.WHITE);
        mBgArcWidth = typedArray.getDimension(R.styleable.CircleProgressBar_bgArcWidth, Constant.DEFAULT_ARC_WIDTH);
        mTextOffsetPercentInRadius = typedArray.getFloat(R.styleable.CircleProgressBar_textOffsetPercentInRadius, 0.33f);

        //mPercent = typedArray.getFloat(R.styleable.CircleProgressBar_percent, 0);
        mAnimTime = typedArray.getInt(R.styleable.CircleProgressBar_animTime, Constant.DEFAULT_ANIM_TIME);

        int gradientArcColors = typedArray.getResourceId(R.styleable.CircleProgressBar_arcColors, 0);
        if (gradientArcColors != 0) {
            try {
                int[] gradientColors = getResources().getIntArray(gradientArcColors);
                if (gradientColors.length == 0) {
                    int color = getResources().getColor(gradientArcColors);
                    mGradientColors = new int[2];
                    mGradientColors[0] = color;
                    mGradientColors[1] = color;
                } else if (gradientColors.length == 1) {
                    mGradientColors = new int[2];
                    mGradientColors[0] = gradientColors[0];
                    mGradientColors[1] = gradientColors[0];
                } else {
                    mGradientColors = gradientColors;
                }
            } catch (Resources.NotFoundException e) {
                throw new Resources.NotFoundException("the give resource not found.");
            }
        }

        typedArray.recycle();
    }

    private void initPaint() {
        mHintPaint = new TextPaint();
        mHintPaint.setAntiAlias(antiAlias);
        mHintPaint.setTextSize(mHintSize);
        mHintPaint.setColor(mHintColor);
        mHintPaint.setTextAlign(Paint.Align.CENTER);

        mValuePaint = new TextPaint();
        mValuePaint.setAntiAlias(antiAlias);
        mValuePaint.setTextSize(mValueSize);
        mValuePaint.setColor(mValueColor);
        mValuePaint.setTypeface(Typeface.DEFAULT_BOLD);
        mValuePaint.setTextAlign(Paint.Align.CENTER);

        mUnitPaint = new TextPaint();
        mUnitPaint.setAntiAlias(antiAlias);
        mUnitPaint.setTextSize(mUnitSize);
        mUnitPaint.setColor(mUnitColor);
        mUnitPaint.setTextAlign(Paint.Align.CENTER);

        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(antiAlias);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(mArcWidth);
        mArcPaint.setStrokeCap(Paint.Cap.ROUND);

        mBgArcPaint = new Paint();
        mBgArcPaint.setAntiAlias(antiAlias);
        mBgArcPaint.setColor(mBgArcColor);
        mBgArcPaint.setStyle(Paint.Style.STROKE);
        mBgArcPaint.setStrokeWidth(mBgArcWidth);
        mBgArcPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MiscUtil.measure(widthMeasureSpec, mDefaultSize),
                MiscUtil.measure(heightMeasureSpec, mDefaultSize));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "onSizeChanged: w = " + w + "; h = " + h + "; oldw = " + oldw + "; oldh = " + oldh);
        float maxArcWidth = Math.max(mArcWidth, mBgArcWidth);
        int minSize = Math.min(w - getPaddingLeft() - getPaddingRight() - 2 * (int) maxArcWidth,
                h - getPaddingTop() - getPaddingBottom() - 2 * (int) maxArcWidth);
        mRadius = minSize / 2;
        mCenterPoint.x = w / 2;
        mCenterPoint.y = h / 2;
        mRectF.left = mCenterPoint.x - mRadius - maxArcWidth / 2;
        mRectF.top = mCenterPoint.y - mRadius - maxArcWidth / 2;
        mRectF.right = mCenterPoint.x + mRadius + maxArcWidth / 2;
        mRectF.bottom = mCenterPoint.y + mRadius + maxArcWidth / 2;
        mValueOffset = mCenterPoint.y + getBaselineOffsetFromY(mValuePaint);
        mHintOffset = mCenterPoint.y - mRadius * mTextOffsetPercentInRadius + getBaselineOffsetFromY(mHintPaint);
        mUnitOffset = mCenterPoint.y + mRadius * mTextOffsetPercentInRadius + getBaselineOffsetFromY(mUnitPaint);
        updateArcPaint();
    }

    private float getBaselineOffsetFromY(Paint paint) {
        return MiscUtil.measureTextHeight(paint) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawText(canvas);
        drawArc(canvas);
    }

    private void drawText(Canvas canvas) {
        // float textWidth = mValuePaint.measureText(mValue.toString());
        // float x = mCenterPoint.x - textWidth / 2;
        //canvas.drawText(String.format(mPrecisionFormat, mValue), mCenterPoint.x, mValueOffset, mValuePaint);
        canvas.drawText(mValue+"", mCenterPoint.x, mValueOffset, mValuePaint);

        if (mHint != null) {
            canvas.drawText(mHint.toString(), mCenterPoint.x, mHintOffset, mHintPaint);
        }

        if (mUnit != null) {
            canvas.drawText(mUnit.toString(), mCenterPoint.x, mUnitOffset, mUnitPaint);
        }
    }

    private void drawArc(Canvas canvas) {
        canvas.save();
        float currentAngle = mSweepAngle * mPercent;
        canvas.rotate(mStartAngle, mCenterPoint.x, mCenterPoint.y);
        canvas.drawArc(mRectF, currentAngle, mSweepAngle - currentAngle + 2, false, mBgArcPaint);
        canvas.drawArc(mRectF, 2, currentAngle, false, mArcPaint);
        canvas.restore();
    }

    private void updateArcPaint() {
        mSweepGradient = new SweepGradient(mCenterPoint.x, mCenterPoint.y, mGradientColors, null);
        mArcPaint.setShader(mSweepGradient);
    }

    public boolean isAntiAlias() {
        return antiAlias;
    }

    public CharSequence getHint() {
        return mHint;
    }

    public void setHint(CharSequence hint) {
        mHint = hint;
    }

    public CharSequence getUnit() {
        return mUnit;
    }

    public void setUnit(CharSequence unit) {
        mUnit = unit;
    }

    public float getValue() {
        return mValue;
    }

    public void setValue(float value) {
        if (value > mMaxValue) {
            value = mMaxValue;
        }
        float start = mPercent;
        float end = value / mMaxValue;
        startAnimator(start, end, mAnimTime);
    }

    private void startAnimator(float start, float end, long animTime) {
        mAnimator = ValueAnimator.ofFloat(start, end);
        mAnimator.setDuration(animTime);
        mAnimator.addUpdateListener(animation -> {
            mPercent = (float) animation.getAnimatedValue();
            mValue = mPercent * mMaxValue;
            invalidate();
        });
        mAnimator.start();
    }


    public float getMaxValue() {
        return mMaxValue;
    }

    public void setMaxValue(float maxValue) {
        mMaxValue = maxValue;
    }

    public int getPrecision() {
        return mPrecision;
    }

    public void setPrecision(int precision) {
        mPrecision = precision;
        mPrecisionFormat = MiscUtil.getPrecisionFormat(precision);
    }

    public int[] getGradientColors() {
        return mGradientColors;
    }

    public void setGradientColors(int[] gradientColors) {
        mGradientColors = gradientColors;
        updateArcPaint();
    }

    public long getAnimTime() {
        return mAnimTime;
    }

    public void setAnimTime(long animTime) {
        mAnimTime = animTime;
    }

    public void reset() {
        startAnimator(mPercent, 0.0f, 1000L);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
