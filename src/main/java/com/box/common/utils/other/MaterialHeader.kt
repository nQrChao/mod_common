package com.box.common.utils.other

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.box.common.R
import com.box.other.scwang.smart.refresh.header.material.CircleImageView
import com.box.other.scwang.smart.refresh.header.material.MaterialProgressDrawable
import com.box.other.scwang.smart.refresh.layout.api.RefreshHeader
import com.box.other.scwang.smart.refresh.layout.api.RefreshKernel
import com.box.other.scwang.smart.refresh.layout.api.RefreshLayout
import com.box.other.scwang.smart.refresh.layout.constant.RefreshState
import com.box.other.scwang.smart.refresh.layout.constant.SpinnerStyle
import com.box.other.scwang.smart.refresh.layout.simple.SimpleComponent
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow


class MaterialHeader @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    SimpleComponent(context, attrs, 0), RefreshHeader {

    companion object {

        const val BALL_STYLE_LARGE: Int = 0
        const val BALL_STYLE_DEFAULT: Int = 1

        private val CIRCLE_BG_LIGHT: Int = Color.parseColor("#FAFAFA")
        private const val MAX_PROGRESS_ANGLE: Float = 0.8f
    }

    private var finished: Boolean = false
    private var circleDiameter: Int
    private var circleView: ImageView
    private var progressDrawable: MaterialProgressDrawable
    private var waveHeight: Int = 0
    private var headHeight: Int = 0
    private var bezierPath: Path
    private var bezierPaint: Paint
    private var refreshState: RefreshState? = null
    private var showBezierWave: Boolean = false
    private var scrollableWhenRefreshing: Boolean = true

    init {
        mSpinnerStyle = SpinnerStyle.MatchLayout
        minimumHeight = resources.getDimension(R.dimen.idp_100).toInt()
        progressDrawable = MaterialProgressDrawable(this)
        progressDrawable.setColorSchemeColors(
            Color.parseColor("#0099CC"),
            Color.parseColor("#FF4444"),
            Color.parseColor("#669900"),
            Color.parseColor("#AA66CC"),
            Color.parseColor("#FF8800"))
        circleView = CircleImageView(context, CIRCLE_BG_LIGHT)
        circleView.setImageDrawable(progressDrawable)
        circleView.alpha = 0f
        addView(circleView)
        circleDiameter = resources.getDimension(R.dimen.idp_40).toInt()
        bezierPath = Path()
        bezierPaint = Paint()
        bezierPaint.isAntiAlias = true
        bezierPaint.style = Paint.Style.FILL
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.MaterialHeaderIm)
        showBezierWave = typedArray.getBoolean(R.styleable.MaterialHeaderIm_srlShowBezierWaveIm, showBezierWave)
        scrollableWhenRefreshing = typedArray.getBoolean(R.styleable.MaterialHeaderIm_srlScrollableWhenRefreshingIm, scrollableWhenRefreshing)
        bezierPaint.color = typedArray.getColor(R.styleable.MaterialHeaderIm_srlPrimaryColorIm, Color.parseColor("#11BBFF"))
        if (typedArray.hasValue(R.styleable.MaterialHeaderIm_srlShadowRadiusIm)) {
            val radius: Int = typedArray.getDimensionPixelOffset(R.styleable.MaterialHeaderIm_srlShadowRadiusIm, 0)
            val color: Int = typedArray.getColor(R.styleable.MaterialHeaderIm_mhShadowColorIm, Color.parseColor("#000000"))
            bezierPaint.setShadowLayer(radius.toFloat(), 0f, 0f, color)
            setLayerType(LAYER_TYPE_SOFTWARE, null)
        }
        showBezierWave = typedArray.getBoolean(R.styleable.MaterialHeaderIm_mhShowBezierWaveIm, showBezierWave)
        scrollableWhenRefreshing = typedArray.getBoolean(R.styleable.MaterialHeaderIm_mhScrollableWhenRefreshingIm, scrollableWhenRefreshing)
        if (typedArray.hasValue(R.styleable.MaterialHeaderIm_mhPrimaryColorIm)) {
            bezierPaint.color = typedArray.getColor(R.styleable.MaterialHeaderIm_mhPrimaryColorIm, Color.parseColor("#11BBFF"))
        }
        if (typedArray.hasValue(R.styleable.MaterialHeaderIm_mhShadowRadiusIm)) {
            val radius: Int = typedArray.getDimensionPixelOffset(R.styleable.MaterialHeaderIm_mhShadowRadiusIm, 0)
            val color: Int = typedArray.getColor(R.styleable.MaterialHeaderIm_mhShadowColorIm, Color.parseColor("#000000"))
            bezierPaint.setShadowLayer(radius.toFloat(), 0f, 0f, color)
            setLayerType(LAYER_TYPE_SOFTWARE, null)
        }
        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec))

        circleView.measure(MeasureSpec.makeMeasureSpec(circleDiameter, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(circleDiameter, MeasureSpec.EXACTLY))
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        if (childCount == 0) {
            return
        }
        val width: Int = measuredWidth
        val circleWidth: Int = circleView.measuredWidth
        val circleHeight: Int = circleView.measuredHeight
        if (isInEditMode && headHeight > 0) {
            val circleTop: Int = headHeight - circleHeight / 2
            circleView.layout((width / 2 - circleWidth / 2), circleTop,
                (width / 2 + circleWidth / 2), circleTop + circleHeight)
            progressDrawable.showArrow(true)
            progressDrawable.setStartEndTrim(0f, MAX_PROGRESS_ANGLE)
            progressDrawable.setArrowScale(1f)
            circleView.alpha = 1f
            circleView.visibility = VISIBLE
        } else {
            circleView.layout((width / 2 - circleWidth / 2), -circleHeight, (width / 2 + circleWidth / 2), 0)
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        if (showBezierWave) {
            bezierPath.reset()
            bezierPath.lineTo(0f, headHeight.toFloat())
            bezierPath.quadTo(measuredWidth / 2f, headHeight + waveHeight * 1.9f, measuredWidth.toFloat(), headHeight.toFloat())
            bezierPath.lineTo(measuredWidth.toFloat(), 0f)
            canvas.drawPath(bezierPath, bezierPaint)
        }
        super.dispatchDraw(canvas)
    }

    override fun onInitialized(kernel: RefreshKernel, height: Int, maxDragHeight: Int) {
        if (!showBezierWave) {
            kernel.requestDefaultTranslationContentFor(this, false)
        }
        if (isInEditMode) {
            headHeight = height / 2
            waveHeight = headHeight
        }
    }

    override fun onMoving(dragging: Boolean, percent: Float, offset: Int, height: Int, maxDragHeight: Int) {
        if (refreshState == RefreshState.Refreshing) {
            return
        }
        if (showBezierWave) {
            headHeight = min(offset, height)
            waveHeight = max(0, offset - height)
            postInvalidate()
        }
        if (dragging || (!progressDrawable.isRunning && !finished)) {
            if (refreshState != RefreshState.Refreshing) {
                val originalDragPercent: Float = 1f * offset / height
                val dragPercent: Float = min(1f, abs(originalDragPercent))
                val adjustedPercent: Float = max(dragPercent - .4, 0.0).toFloat() * 5 / 3
                val extraOs: Float = (abs(offset) - height).toFloat()
                val tensionSlingshotPercent: Float = max(0f, (min(extraOs, height.toFloat() * 2) / height.toFloat()))
                val tensionPercent: Float = ((tensionSlingshotPercent / 4) - (tensionSlingshotPercent / 4).toDouble().pow(2.0)).toFloat() * 2f
                val strokeStart: Float = adjustedPercent * .8f
                progressDrawable.showArrow(true)
                progressDrawable.setStartEndTrim(0f, min(MAX_PROGRESS_ANGLE, strokeStart))
                progressDrawable.setArrowScale(min(1f, adjustedPercent))
                val rotation: Float = (-0.25f + (.4f * adjustedPercent) + (tensionPercent * 2)) * .5f
                progressDrawable.setProgressRotation(rotation)
            }
            val targetY: Float = offset / 2f + circleDiameter / 2f
            circleView.translationY = min(offset.toFloat(), targetY)
            circleView.alpha = min(1f, 4f * offset / circleDiameter)
        }
    }

    override fun onReleased(layout: RefreshLayout, height: Int, maxDragHeight: Int) {
        progressDrawable.start()
    }

    override fun onStateChanged(refreshLayout: RefreshLayout, oldState: RefreshState, newState: RefreshState) {
        refreshState = newState
        if (newState == RefreshState.PullDownToRefresh) {
            finished = false
            circleView.visibility = VISIBLE
            circleView.translationY = 0f
            circleView.scaleX = 1f
            circleView.scaleY = 1f
        }
    }

    override fun onFinish(layout: RefreshLayout, success: Boolean): Int {
        progressDrawable.stop()
        circleView.animate().scaleX(0f).scaleY(0f)
        finished = true
        return 0
    }

    fun setProgressBackgroundResource(@ColorRes id: Int): MaterialHeader = apply {
        setProgressBackgroundColor(ContextCompat.getColor(context, id))
    }

    fun setProgressBackgroundColor(@ColorInt color: Int): MaterialHeader = apply {
        circleView.setBackgroundColor(color)
    }

    fun setColorSchemeColors(@ColorInt vararg colors: Int): MaterialHeader = apply {
        progressDrawable.setColorSchemeColors(*colors)
    }

    fun setColorSchemeResources(@ColorRes vararg ids: Int): MaterialHeader = apply {
        val colors = IntArray(ids.size)
        for (i in ids.indices) {
            colors[i] = ContextCompat.getColor(context, ids[i])
        }
        setColorSchemeColors(*colors)
    }

    fun setBallStyle(style: Int): MaterialHeader = apply {
        if (style != BALL_STYLE_LARGE && style != BALL_STYLE_DEFAULT) {
            return@apply
        }
        circleDiameter = if (style == BALL_STYLE_LARGE) resources.getDimension(R.dimen.idp_56).toInt() else resources.getDimension(R.dimen.idp_40).toInt()
        circleView.setImageDrawable(null)
        progressDrawable.updateSizes(style)
        circleView.setImageDrawable(progressDrawable)
    }

    fun setShowBezierWave(show: Boolean): MaterialHeader = apply {
        showBezierWave = show
    }

    fun setScrollableWhenRefreshing(scrollable: Boolean): MaterialHeader = apply {
        scrollableWhenRefreshing = scrollable
    }
}