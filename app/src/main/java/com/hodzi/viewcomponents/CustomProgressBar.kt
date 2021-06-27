package com.hodzi.viewcomponents

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.*
import androidx.core.content.ContextCompat
import com.hodzi.viewcomponents.utils.dp


@SuppressLint("ViewConstructor")
class CustomProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private val mainPaint: Paint = getDefaultPaint()
    private val backgroundPaint: Paint = getDefaultPaint().apply {
        color = ContextCompat.getColor(context, R.color.progress_background)
    }
    private val textPaint = Paint().apply {
        textAlign = Paint.Align.CENTER
        textSize = 23.dp()
    }

    private var valueAnimator : ValueAnimator? = null
    private var rectangle: RectF? = null
    private var margin: Float
    private var progress: Float = 0.93f
    private var animationProgress: Float = 0.93f
    private val startAngle = 140f
    private val sweepAngle = 260f

    init {
        mainPaint.apply {
            shader = SweepGradient (500f, 1500f, ContextCompat.getColor(context, R.color.progress_color_start), ContextCompat.getColor(context, R.color.progress_color_end))
        }
        margin = 23.dp()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (rectangle == null) {
            rectangle = RectF(0f + margin, 0f + margin, width.toFloat() - margin, height.toFloat() - margin)
        }
        canvas.drawArc(rectangle!!, startAngle, sweepAngle, false, backgroundPaint)
        canvas.drawArc(rectangle!!, startAngle, animationProgress * sweepAngle, false, mainPaint)
        canvas.drawText("${(animationProgress * 100).toInt()}%", width.toFloat() / 2, height.toFloat() / 2, textPaint)
    }

    // TODO:  add  range
    fun setProgress(progress: Float) {
        val oldValue = this.progress
        this.progress = progress
        invalidate()
        animateView(animationProgress, progress)
    }

    private fun getDefaultPaint(): Paint {
        return Paint().apply {
            isAntiAlias = true
            strokeCap = Paint.Cap.ROUND
            style = Paint.Style.STROKE
            strokeWidth = 23.dp()
        }
    }

    private fun animateView(from: Float, to: Float) {
        val mDuration = 1000
        valueAnimator?.cancel()
        valueAnimator = ValueAnimator.ofFloat(from, to).apply {
            interpolator = AccelerateDecelerateInterpolator()
            duration = mDuration.toLong()
            addUpdateListener {
                animationProgress = it.animatedValue as Float
                invalidate()
            }
            start()
        }
    }
}