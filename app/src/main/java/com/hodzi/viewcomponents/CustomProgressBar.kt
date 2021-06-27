package com.hodzi.viewcomponents

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.provider.Settings
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.FloatRange
import androidx.core.content.ContextCompat
import com.hodzi.viewcomponents.utils.dp
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlin.math.abs


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
    private var valueAnimator: ValueAnimator? = null
    private var jobAnimation: Job? = null
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
        val durationScale =
            Settings.Global.getFloat(context.contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f)
        if (durationScale == 0f) {
            startFlowAnimation(finishValue)
        } else {
            startNativeAnimation(finishValue)
        }
    }
    private fun startFlowAnimation(finishValue: Float) {
        val flowProgress: Flow<Float> = flow {
            val start = (animationProgress * 100).toInt()
            val end = (finishValue * 100).toInt()
            if (start == end) {
                return@flow
            }

            val range = if (start < end) (start + 1)..end else ((start - 1) downTo end)
            val size = abs(end - start)
            val durationStep = ANIMATION_DURATION / size
            for (i in range) {
                delay(durationStep)
                emit((i / 100f))
            }
        }

        jobAnimation = CoroutineScope(Dispatchers.Main).launch {
            flowProgress.collect(::animationUpdate)
        }
    }

    private fun startNativeAnimation(finishValue: Float) {
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

    private fun animationUpdate(newValue: Float) {
        animationProgress = newValue
        invalidate()
    }

    private fun stopAnimation() {
        jobAnimation?.cancel()
        jobAnimation = null
        valueAnimator?.cancel()
        valueAnimator = null
    }

    companion object {
        private const val START_ANGLE = 160f
        private const val SWEEP_ANGLE = (2 * (360 - (90 + START_ANGLE)) % 360 + 360) % 360
        private const val ANIMATION_DURATION = 300L
    }
}