package com.hodzi.viewcomponents

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.FloatRange
import androidx.core.content.ContextCompat
import com.hodzi.viewcomponents.utils.dp
import com.hodzi.viewcomponents.utils.sp


@SuppressLint("ViewConstructor")
class CustomProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private val progressPaint: Paint = getDefaultPaint()
    private val backgroundPaint: Paint = getDefaultPaint()
    private val textPaint = Paint().apply {
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    private var valueAnimator: ValueAnimator? = null
    private var rectangle: RectF? = null
    private var margin: Float
    private var progress: Float = 0f
    private var animationProgress: Float = progress

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomProgressBar, defStyleAttr, 0)
        setProgress(typedArray.getFloat(R.styleable.CustomProgressBar_progress_value, 0f), force = true)

        val progressBackgroundColor = typedArray.getColor(
            R.styleable.CustomProgressBar_progress_background_color,
            ContextCompat.getColor(context, R.color.progress_background)
        )
        backgroundPaint.color = progressBackgroundColor

        val textColor = typedArray.getColor(
            R.styleable.CustomProgressBar_text_color,
            ContextCompat.getColor(context, R.color.progress_background)
        )
        textPaint.color = textColor

        val progressWidth = typedArray.getDimension(
            R.styleable.CustomProgressBar_progress_stroke_width,
            20.dp()
        )
        backgroundPaint.strokeWidth = progressWidth
        progressPaint.strokeWidth = progressWidth
        val textSize = typedArray.getDimension(
            R.styleable.CustomProgressBar_text_size,
            20.sp()
        )
        textPaint.textSize = textSize
        typedArray.recycle()

        progressPaint.apply {
            shader = SweepGradient(
                500f,
                1500f,
                ContextCompat.getColor(context, R.color.progress_color_start),
                ContextCompat.getColor(context, R.color.progress_color_end)
            )
        }
        margin = 23.dp()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (rectangle == null) {
            rectangle = RectF(0f + margin, 0f + margin, width.toFloat() - margin, height.toFloat() - margin)
        }
        canvas.drawArc(rectangle!!, START_ANGLE, SWEEP_ANGLE, false, backgroundPaint)
        canvas.drawArc(rectangle!!, START_ANGLE, animationProgress * SWEEP_ANGLE, false, progressPaint)
        canvas.drawText("${(animationProgress * 100).toInt()}%", width.toFloat() / 2, height.toFloat() / 2, textPaint)
    }

    fun getProgress(): Float {
        return progress
    }

    fun setProgress(
        @FloatRange(from = 0.0, to = 1.0) progress: Float,
        force: Boolean = false,
    ) {
        this.progress = progress
        if (force) {
            stopAnimation()
            animationProgress = progress
            invalidate()
        } else {
            animateView(progress)
        }
    }

    private fun getDefaultPaint(): Paint {
        return Paint().apply {
            isAntiAlias = true
            strokeCap = Paint.Cap.ROUND
            style = Paint.Style.STROKE
            strokeWidth = PROGRESS_STROKE_WIDTH.dp()
        }
    }

    private fun animateView(finishValue: Float) {
        stopAnimation()
        valueAnimator = ValueAnimator.ofFloat(animationProgress, finishValue).apply {
            interpolator = AccelerateDecelerateInterpolator()
            duration = ANIMATION_DURATION
            addUpdateListener {
                animationProgress = it.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    private fun stopAnimation() {
        valueAnimator?.cancel()
        valueAnimator = null
    }

    companion object {
        private const val PROGRESS_STROKE_WIDTH = 23
        private const val START_ANGLE = 160f
        private const val SWEEP_ANGLE = (2 * (360 - (90 + START_ANGLE)) % 360 + 360) % 360
        private const val ANIMATION_DURATION = 300L
    }
}