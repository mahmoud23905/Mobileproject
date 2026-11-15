package com.example.neuroguard

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var btnPatientPortal: Button
    private lateinit var btnCaregiverPortal: Button
    private lateinit var btnBreathingExercise: Button
    private lateinit var btnHistoryDetails: Button
    private lateinit var btnMainHospital: Button
    private lateinit var btnMainSos: Button

    private val db: AppDatabase? by lazy {
        try {
            AppDatabase.getInstance(applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        setupClickListeners()
    }

    private fun initViews() {
        btnPatientPortal = findViewById(R.id.btnPatientPortal)
        btnCaregiverPortal = findViewById(R.id.btnCaregiverPortal)
        btnBreathingExercise = findViewById(R.id.btnBreathingExercise)
        btnHistoryDetails = findViewById(R.id.btnHistoryDetails)
        btnMainHospital = findViewById(R.id.btnMainHospital)
        btnMainSos = findViewById(R.id.btnMainSos)
    }

    private fun setupClickListeners() {
        // Explicit Intent 1: Patient Interface Portal
        btnPatientPortal.setOnClickListener {
            val intent = Intent(this, PatientActivity::class.java)
            startActivity(intent)
        }

        // Explicit Intent 2: Caregiver Interface & History Portal
        btnCaregiverPortal.setOnClickListener {
            val intent = Intent(this, CaregiverActivity::class.java)
            startActivity(intent)
        }

        // Explicit Intent 3: Guided Breathing Exercise Activity
        btnBreathingExercise.setOnClickListener {
            val intent = Intent(this, BreathingActivity::class.java)
            startActivity(intent)
        }

        // Explicit Intent 4 with Data Transfer (Extras)
        btnHistoryDetails.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                var hr = 75
                var gsr = 320f
                var status = "Normal"

                try {
                    val latest = db?.readingDao()?.getLatestReading()?.firstOrNull()
                    if (latest != null) {
                        hr = latest.heartrate
                        gsr = latest.gsr
                        status = latest.stresslevel
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                val intent = Intent(this@MainActivity, HistoryActivity::class.java).apply {
                    putExtra("EXTRA_HEART_RATE", hr)
                    putExtra("EXTRA_GSR", gsr)
                    putExtra("EXTRA_STATUS", status)
                }
                startActivity(intent)
            }
        }

        // Implicit Intent 1: Open Hospital Map
        btnMainHospital.setOnClickListener {
            try {
                val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=hospital"))
                startActivity(mapIntent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // Implicit Intent 2: Emergency SOS Phone Dial
        btnMainSos.setOnClickListener {
            try {
                val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:01000000000"))
                startActivity(callIntent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
