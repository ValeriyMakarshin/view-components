package com.hodzi.viewcomponents

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.lang.Float.parseFloat

class MainActivity : AppCompatActivity() {
    private var currentProgress = .15F

    private lateinit var customProgress: CustomProgressBar
    private lateinit var progressEditText: EditText
    private lateinit var updateProgressButton: Button
    private lateinit var plusProgressButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        customProgress = findViewById(R.id.custom_progress)
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
            updateProgress()
        }
        updateProgress()
    }

    private fun updateProgress() {
        currentProgress %= 1
        customProgress.setProgress(currentProgress)
        progressEditText.setText(customProgress.getProgress().toString())
    }
}