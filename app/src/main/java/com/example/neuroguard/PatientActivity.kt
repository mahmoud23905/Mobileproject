package com.example.neuroguard

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class PatientActivity : Activity() {

    private lateinit var layoutStressStatus: LinearLayout
    private lateinit var tvStressState: TextView
    private lateinit var tvHeartRate: TextView
    private lateinit var tvGsrValue: TextView
    private lateinit var btnStartBreathing: Button
    private lateinit var btnSimulateReading: Button
    private lateinit var btnOpenHistory: Button
    private lateinit var btnSos: Button
    private lateinit var dbHelper: VitalDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient)

        dbHelper = VitalDatabaseHelper(this)
        initViews()
        setStatusCardColor("#43A047")
        setupClickListeners()
    }

    private fun initViews() {
        layoutStressStatus = findViewById(R.id.layoutStressStatus)
        tvStressState = findViewById(R.id.tvStressState)
        tvHeartRate = findViewById(R.id.tvHeartRate)
        tvGsrValue = findViewById(R.id.tvGsrValue)
        btnStartBreathing = findViewById(R.id.btnStartBreathing)
        btnSimulateReading = findViewById(R.id.btnSimulateReading)
        btnOpenHistory = findViewById(R.id.btnOpenHistory)
        btnSos = findViewById(R.id.btnSos)
    }

    private fun setupClickListeners() {
        btnStartBreathing.setOnClickListener {
            val intent = Intent(this, BreathingActivity::class.java)
            startActivity(intent)
        }

        btnSimulateReading.setOnClickListener {
            val isStressed = (0..10).random() > 5
            val hr = if (isStressed) (110..140).random() else (68..85).random()
            val gsr = if (isStressed) (750..950).random().toFloat() else (220..400).random().toFloat()
            val status = if (isStressed) "High Stress (Stressed)" else "Normal"

            updatePatientUI(hr, gsr, status, isStressed)
            dbHelper.insertReading(hr, gsr, status)
        }

        btnOpenHistory.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }

        btnSos.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:01000000000")
            }
            startActivity(callIntent)
        }
    }

    fun updatePatientUI(heartRate: Int, gsr: Float, statusText: String, isStressed: Boolean) {
        tvHeartRate.text = heartRate.toString()
        tvGsrValue.text = gsr.toInt().toString()
        tvStressState.text = statusText

        if (isStressed) {
            setStatusCardColor("#E53935")
            btnStartBreathing.visibility = View.VISIBLE
        } else {
            setStatusCardColor("#43A047")
            btnStartBreathing.visibility = View.GONE
        }
    }

    private fun setStatusCardColor(hexColor: String) {
        val background = layoutStressStatus.background.mutate() as GradientDrawable
        background.setColor(Color.parseColor(hexColor))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("heartRate", tvHeartRate.text.toString())
        outState.putString("gsrValue", tvGsrValue.text.toString())
        outState.putString("stressState", tvStressState.text.toString())
        outState.putBoolean("isBreathingVisible", btnStartBreathing.visibility == View.VISIBLE)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val heartRate = savedInstanceState.getString("heartRate", "72")
        val gsrValue = savedInstanceState.getString("gsrValue", "320")
        val stressState = savedInstanceState.getString("stressState", "Normal")
        val isBreathingVisible = savedInstanceState.getBoolean("isBreathingVisible", false)

        tvHeartRate.text = heartRate
        tvGsrValue.text = gsrValue
        tvStressState.text = stressState

        if (isBreathingVisible) {
            setStatusCardColor("#E53935")
            btnStartBreathing.visibility = View.VISIBLE
        } else {
            setStatusCardColor("#43A047")
            btnStartBreathing.visibility = View.GONE
        }
    }
}