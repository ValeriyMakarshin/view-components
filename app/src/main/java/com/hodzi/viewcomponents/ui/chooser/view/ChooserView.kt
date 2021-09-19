package com.hodzi.viewcomponents.ui.chooser.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import com.hodzi.viewcomponents.R


class ChooserView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {
    private var onSwipe: (() -> Unit)? = null

    private val nextContainer: ViewGroup
    private val swipeContainer: ViewGroup

    init {
        val view = inflate(getContext(), R.layout.view_chooser, this)
        val motionLayout = view.findViewById<MotionLayout>(R.id.motion_layout)
        motionLayout.setTransitionListener(object : TransitionAdapter() {
            override fun onTransitionCompleted(p0: MotionLayout, currentId: Int) {
                if (currentId in listOf(R.id.like_finish, R.id.dislike_finish) && p0.progress == 1f) {
                    p0.setTransition(R.id.start, R.id.like)
                    p0.progress = 0f
                    onSwipe?.invoke()
                }
            }
        })
        nextContainer = view.findViewById(R.id.next_layout)
        swipeContainer = view.findViewById(R.id.swipe_layout)
    }

    fun setSwipeListener(onSwipe: () -> Unit) {
        this.onSwipe = onSwipe
    }

    fun setNextView(nextView: View) {
        nextContainer.removeAllViews()
        nextContainer.addView(nextView)
    }

    fun setSwipeView(swipeView: View) {
        swipeContainer.removeAllViews()
        swipeContainer.addView(swipeView)
    }

    companion object {
        private const val TAG = "ChooserView"
    }
}