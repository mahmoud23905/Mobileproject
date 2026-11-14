package com.example.neuroguard

import android.app.Activity
import android.os.Bundle
import android.widget.TextView

class HistoryActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val tvPassedData: TextView = findViewById(R.id.tvPassedData)

        val heartRate = intent.getIntExtra("EXTRA_HEART_RATE", 72)
        val gsr = intent.getFloatExtra("EXTRA_GSR", 320f)
        val status = intent.getStringExtra("EXTRA_STATUS") ?: "Normal"

        tvPassedData.text = "Latest Reading Details:\n\nHeart Rate: $heartRate BPM\nGSR Value: ${gsr.toInt()}\nStatus: $status"
    }
}