package com.hodzi.viewcomponents.ui.progress.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.FloatRange
import androidx.core.content.ContextCompat
import com.hodzi.viewcomponents.R
import com.hodzi.viewcomponents.animation.BaseValueAnimation
import com.hodzi.viewcomponents.animation.ValueAnimationCombiner


@SuppressLint("ViewConstructor")
class CustomProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private val progressPaint: Paint = getDefaultPaint()
    private val backgroundPaint: Paint = getDefaultPaint()
    private val textPaint = Paint().apply {
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    private var previousHeight = 0
    private var animation: BaseValueAnimation? = null
    private var rectangle: RectF = RectF()
    private var rectangleMargin: Float = 0f
    private var progress: Float = 0f
    private var animationProgress: Float = progress
    private var defaultProgressStrokeWidth: Float = 0f
    private var defaultTextStrokeWidth: Float = 0f

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomProgressBar, defStyleAttr, 0)
        setProgress(typedArray.getFloat(R.styleable.CustomProgressBar_progress_value, 20f), force = true)

        val progressBackgroundColor = typedArray.getColor(
            R.styleable.CustomProgressBar_progress_background_color,
            ContextCompat.getColor(context, R.color.progress_background)
        )
        backgroundPaint.color = progressBackgroundColor

        defaultTextStrokeWidth = typedArray.getDimension(
            R.styleable.CustomProgressBar_text_size,
            0f
        )
        defaultProgressStrokeWidth = typedArray.getDimension(
            R.styleable.CustomProgressBar_progress_stroke_width,
            0f
        )
        typedArray.recycle()

        updateStrokeWidth()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)

        val widthSpec = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpec = MeasureSpec.getSize(widthMeasureSpec)

        updateShaderIfNeed(heightSpec)
        textPaint.shader = progressPaint.shader

        rectangle.set(
            0f + rectangleMargin,
            0f + rectangleMargin,
            widthSpec.toFloat() - rectangleMargin,
            heightSpec.toFloat() - rectangleMargin
        )
        updateStrokeWidth(widthSpec)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawArc(rectangle, START_ANGLE, SWEEP_ANGLE, false, backgroundPaint)
        if (animationProgress != 0f) {
            canvas.drawArc(rectangle, START_ANGLE, animationProgress * SWEEP_ANGLE, false, progressPaint)
        }
        canvas.drawText("${(animationProgress * 100).toInt()}%", width.toFloat() / 2, height.toFloat() / 2, textPaint)
    }

    fun getProgress(): Float {
        return progress
    }

    fun setProgress(@FloatRange(from = 0.0, to = 1.0) progress: Float, force: Boolean = false) {
        this.progress = progress.coerceIn(0f, 1f)
        stopAnimation()
        if (force) {
            animationProgress = this.progress
            invalidate()
        } else {
            animateView(this.progress)
        }
    }

    private fun updateStrokeWidth(wight: Int = 0) {
        updateProgressStrokeWidth(wight)
        updateTextStrokeWidth(wight)
    }

    private fun updateProgressStrokeWidth(wight: Int) {
        val progressWidth = if (defaultProgressStrokeWidth == 0f) {
            wight / 10f
        } else {
            defaultProgressStrokeWidth
        }
        backgroundPaint.strokeWidth = progressWidth
        progressPaint.strokeWidth = progressWidth
        rectangleMargin = progressWidth / 2 + 1
    }

    private fun updateTextStrokeWidth(wight: Int) {
        val textWidth = if (defaultTextStrokeWidth == 0f) {
            wight / 10f
        } else {
            defaultTextStrokeWidth
        }
        textPaint.textSize = textWidth
    }

    private fun updateShaderIfNeed(heightSpec: Int) {
        if (previousHeight == heightSpec)
            return

        previousHeight = heightSpec
        val gradient = LinearGradient(
            0f,
            y,
            0f,
            y + (heightSpec / 2),
            ContextCompat.getColor(context, R.color.progress_color_end),
            ContextCompat.getColor(context, R.color.progress_color_start),
            Shader.TileMode.MIRROR
        )
        progressPaint.shader = gradient
        textPaint.shader = gradient
    }

    private fun getDefaultPaint(): Paint {
        return Paint().apply {
            isAntiAlias = true
            strokeCap = Paint.Cap.ROUND
            style = Paint.Style.STROKE
        }
    }

    private fun animateView(finishValue: Float) {
        animation = ValueAnimationCombiner(context).apply {
            startAnimation(animationProgress, finishValue, ANIMATION_DURATION) { newValue ->
                animationProgress = newValue
                invalidate()
            }
        }
    }

    private fun stopAnimation() {
        animation?.stopAnimation()
        animation = null
    }

    companion object {
        private const val START_ANGLE = 160f
        private const val SWEEP_ANGLE = (2 * (360 - (90 + START_ANGLE)) % 360 + 360) % 360
        private const val ANIMATION_DURATION = 300L
    }
}