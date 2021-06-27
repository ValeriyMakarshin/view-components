package com.hodzi.viewcomponents.animation

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlin.math.abs

class FlowValueAnimation : BaseValueAnimation() {
    private var jobAnimation: Job? = null

    override fun startAnimation(start: Float, end: Float, duration: Long, updateListener: (Float) -> Unit) {
        val flowProgress: Flow<Float> = flow {
            val startInt = (start * 100).toInt()
            val endInt = (end * 100).toInt()
            if (startInt == endInt) {
                return@flow
            }

            val range = if (startInt < endInt) (startInt + 1)..endInt else ((startInt - 1) downTo endInt)
            val size = abs(endInt - startInt)
            val durationStep = duration / size
            for (i in range) {
                delay(durationStep)
                emit((i / 100f))
            }
        }

        jobAnimation = CoroutineScope(Dispatchers.Main).launch {
            flowProgress.collect { updateListener.invoke(it) }
        }

    }

    override fun stopAnimation() {
        jobAnimation?.cancel()
        jobAnimation = null
    }
}