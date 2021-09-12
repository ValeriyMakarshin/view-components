package com.hodzi.viewcomponents.ui.chooser.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import androidx.constraintlayout.motion.widget.MotionLayout
import com.hodzi.viewcomponents.R


// SwipeLayout ?
class ChooserView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {
    init {
        val view = inflate(getContext(), R.layout.view_chooser, this)
        val motionLayout = view.findViewById<MotionLayout>(R.id.motion_layout)
        motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                Log.i(TAG, "onTransitionStarted")
            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
                Log.i(TAG, "onTransitionChange")
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                Log.i(TAG, "onTransitionCompleted")
            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
                Log.i(TAG, "onTransitionTrigger")
            }
        })
    }

    companion object {
        private const val TAG = "ChooserView"
    }
}