package com.hodzi.viewcomponents.ui.chooser

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val chooserView = view.findViewById<ChooserView>(R.id.chooser_view)
        chooserView.setAdapter(ChooserAdapter())
    }

    private class ChooserAdapter : ChooserView.Adapter<TextViewHolder> {
        override fun createViewHolder(context: Context): TextViewHolder {
            val textView = TextView(context)
            textView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            textView.gravity = Gravity.CENTER
            return TextViewHolder(textView)
        }
    }

    private class TextViewHolder(private val textView: TextView) : ChooserView.ViewHolder(textView) {
        override fun onBind(index: Int) {
            textView.text = index.toString()
            textView.setBackgroundColor(colorPool[index % colorPool.size])
        }
    }

    companion object {
        private val colorPool = listOf(Color.BLUE, Color.RED, Color.GREEN)
    }
}