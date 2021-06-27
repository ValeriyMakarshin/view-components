package com.hodzi.viewcomponents.animation

import android.content.Context
import android.provider.Settings

class ValueAnimationCombiner(private val context: Context) : BaseValueAnimation() {
    private val flowValueAnimation = FlowValueAnimation()
    private val nativeValueAnimation = NativeValueAnimation()
    private var ranAnimation: BaseValueAnimation? = null

    override fun startAnimation(start: Float, end: Float, duration: Long, updateListener: (Float) -> Unit) {
        val durationScale = Settings.Global.getFloat(
            context.contentResolver,
            Settings.Global.ANIMATOR_DURATION_SCALE,
            1f
        )
        val animation = if (durationScale == 0f) {
            flowValueAnimation
        } else {
            nativeValueAnimation
        }
        animation.startAnimation(start, end, duration, updateListener)
        ranAnimation = animation
    }

    override fun stopAnimation() {
        ranAnimation?.stopAnimation()
    }
}