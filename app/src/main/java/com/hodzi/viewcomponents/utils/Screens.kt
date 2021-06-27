package com.hodzi.viewcomponents.utils

import android.content.res.Resources

fun Int.dp(): Float {
    val scale = density()
    return Math.floor((this * scale).toDouble()).toFloat()
}

fun dpFloat(dp: Float): Float {
    return dp * density()
}

fun density(): Float {
    val displayMetrics = Resources.getSystem().displayMetrics
    return displayMetrics.density
}
