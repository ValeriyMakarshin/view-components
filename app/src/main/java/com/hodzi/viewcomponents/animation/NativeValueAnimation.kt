package com.hodzi.viewcomponents.animation

import android.animation.ValueAnimator
import android.view.animation.AccelerateDecelerateInterpolator

class NativeValueAnimation : BaseValueAnimation() {
    private var animator: ValueAnimator? = null

    override fun startAnimation(start: Float, end: Float, duration: Long, updateListener: (Float) -> Unit) {
        animator = ValueAnimator.ofFloat(start, end).apply {
            interpolator = AccelerateDecelerateInterpolator()
            this.duration = duration
            addUpdateListener {
                updateListener.invoke(it.animatedValue as Float)
            }
            start()
        }
    }

    override fun stopAnimation() {
        animator?.cancel()
        animator = null
    }
}