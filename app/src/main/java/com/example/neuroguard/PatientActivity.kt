package com.example.neuroguard

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PatientActivity : AppCompatActivity() {

    private lateinit var layoutStressStatus: LinearLayout
    private lateinit var tvStressState: TextView
    private lateinit var tvHeartRate: TextView
    private lateinit var tvGsrValue: TextView
    private lateinit var btnStartBreathing: Button
    private lateinit var btnSimulateReading: Button
    private lateinit var btnOpenHistory: Button
    private lateinit var btnNearbyHospital: Button
    private lateinit var btnSos: Button

    private val db: AppDatabase? by lazy {
        try {
            AppDatabase.getInstance(applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    private lateinit var sqliteHelper: VitalDatabaseHelper
    private val sensorEngine = MockSensorEngine()

    private var currentHeartRate: Int = 72
    private var currentGsr: Float = 320f
    private var currentStatus: String = "Normal"
    private var isStressed: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient)

        sqliteHelper = VitalDatabaseHelper(this)
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
        btnNearbyHospital = findViewById(R.id.btnNearbyHospital)
        btnSos = findViewById(R.id.btnSos)
    }

    private fun setupClickListeners() {
        // Explicit Intent: Guided Breathing Activity
        btnStartBreathing.setOnClickListener {
            val intent = Intent(this, BreathingActivity::class.java)
            startActivity(intent)
        }

        // Mock Sensor Data Generation & Persistence
        btnSimulateReading.setOnClickListener {
            val result = sensorEngine.generateSimulatedReading()
            currentHeartRate = result.heartRate
            currentGsr = result.gsr
            currentStatus = result.status
            isStressed = result.isStressed

            updatePatientUI(currentHeartRate, currentGsr, currentStatus, isStressed)

            // Save to SQLite and Room Database asynchronously
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    sqliteHelper.insertReading(currentHeartRate, currentGsr, currentStatus)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                try {
                    val reading = Reading(
                        heartrate = currentHeartRate,
                        gsr = currentGsr,
                        stresslevel = currentStatus,
                        note = if (isStressed) "High stress detected" else "Normal reading"
                    )
                    db?.readingDao()?.insert(reading)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        // Explicit Intent with Extras: History Activity
        btnOpenHistory.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java).apply {
                putExtra("EXTRA_HEART_RATE", currentHeartRate)
                putExtra("EXTRA_GSR", currentGsr)
                putExtra("EXTRA_STATUS", currentStatus)
            }
            startActivity(intent)
        }

        // Implicit Intent: Open Maps for Nearby Hospitals
        btnNearbyHospital.setOnClickListener {
            try {
                val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=hospital"))
                startActivity(mapIntent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // Implicit Intent: Emergency SOS Dial
        btnSos.setOnClickListener {
            try {
                val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:01000000000"))
                startActivity(callIntent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun updatePatientUI(heartRate: Int, gsr: Float, statusText: String, isStressed: Boolean) {
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
        outState.putInt("heartRate", currentHeartRate)
        outState.putFloat("gsrValue", currentGsr)
        outState.putString("stressState", currentStatus)
        outState.putBoolean("isStressed", isStressed)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentHeartRate = savedInstanceState.getInt("heartRate", 72)
        currentGsr = savedInstanceState.getFloat("gsrValue", 320f)
        currentStatus = savedInstanceState.getString("stressState", "Normal")
        isStressed = savedInstanceState.getBoolean("isStressed", false)

        updatePatientUI(currentHeartRate, currentGsr, currentStatus, isStressed)
    }
}
