package com.hodzi.viewcomponents

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private var currentProgress = .15f

    private lateinit var progressEditText: EditText
    private lateinit var updateProgressButton: Button
    private lateinit var plusProgressButton: Button

    private val progressItems = mutableListOf<CustomProgressBar>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressItems.add(findViewById(R.id.custom_progress_main))
//        progressItems.add(findViewById(R.id.custom_progress_test_1))
//        progressItems.add(findViewById(R.id.custom_progress_test_2))
//        progressItems.add(findViewById(R.id.custom_progress_test_3))
        progressEditText = findViewById(R.id.progress_value)
        updateProgressButton = findViewById(R.id.update_progress)
        plusProgressButton = findViewById(R.id.plus_progress)
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