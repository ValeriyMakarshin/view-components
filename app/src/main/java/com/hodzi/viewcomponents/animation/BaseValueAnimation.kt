package com.hodzi.viewcomponents.animation

abstract class BaseValueAnimation {
    abstract fun startAnimation(start: Float, end: Float, duration: Long, updateListener: (Float) -> Unit)

    abstract fun stopAnimation()
}