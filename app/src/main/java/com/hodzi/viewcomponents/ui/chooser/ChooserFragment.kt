package com.hodzi.viewcomponents.ui.chooser

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.hodzi.viewcomponents.R
import com.hodzi.viewcomponents.ui.chooser.view.ChooserView

class ChooserFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_chooser, container, false)
    }

    private val colorPool = listOf(Color.BLUE, Color.RED, Color.GREEN)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val chooserView = view.findViewById<ChooserView>(R.id.chooser_view)
        var index = 0
        chooserView.setSwipeView(getView(requireContext(), index))
        chooserView.setNextView(getView(requireContext(), ++index))
        chooserView.setSwipeListener {
            chooserView.setSwipeView(getView(requireContext(), index))
            chooserView.setNextView(getView(requireContext(), ++index))
        }
    }

    // todo: use viewHolder
    private fun getView(context: Context, index: Int): View {
        val textView = TextView(context)
        textView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        textView.setBackgroundColor(colorPool[index % colorPool.size])
        textView.text = index.toString()
        return textView
    }
}