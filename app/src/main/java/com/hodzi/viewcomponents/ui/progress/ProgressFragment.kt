package com.hodzi.viewcomponents.ui.progress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.hodzi.viewcomponents.R
import com.hodzi.viewcomponents.ui.progress.view.CustomProgressBar

class ProgressFragment : Fragment() {
    private var currentProgress = .15f

    private lateinit var progressEditText: EditText
    private lateinit var updateProgressButton: Button
    private lateinit var plusProgressButton: Button

    private val progressItems = mutableListOf<CustomProgressBar>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_progress, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listOf(
            R.id.custom_progress_main,
            R.id.custom_progress_test_1,
            R.id.custom_progress_test_2,
            R.id.custom_progress_test_3
        ).forEach { id -> progressItems.add(view.findViewById(id)) }

        progressEditText = view.findViewById(R.id.progress_value)
        updateProgressButton = view.findViewById(R.id.update_progress)
        plusProgressButton = view.findViewById(R.id.plus_progress)
        updateProgressButton.setOnClickListener {
            val textProgress = progressEditText.text.toString()
            val progressValue = textProgress.toFloatOrNull()
            progressValue?.let { currentProgress = it }
            updateProgress()
        }
        plusProgressButton.setOnClickListener {
            currentProgress += 0.10f
            currentProgress %= 1.1f
            updateProgress()
        }
        updateProgress()
    }

    private fun updateProgress() {
        progressItems.forEach { it.setProgress(currentProgress) }
        progressEditText.setText(currentProgress.toString())
    }
}