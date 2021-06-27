package com.hodzi.viewcomponents.utils

import android.content.res.Resources
import kotlin.math.floor

fun Int.dp(): Float {
    val scale = getDisplayMetrics().density
    return floor((this * scale).toDouble()).toFloat()
}

fun Int.sp(): Float {
    val scale: Float = getDisplayMetrics().scaledDensity
    return (this * scale + 0.5f)
}

private fun getDisplayMetrics() = Resources.getSystem().displayMetrics
