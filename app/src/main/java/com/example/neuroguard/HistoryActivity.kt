package com.example.neuroguard

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_history)

        val tvPassedData: TextView = findViewById(R.id.tvPassedData)

        val heartRate = intent.getIntExtra("EXTRA_HEART_RATE", 72)
        val gsr = intent.getFloatExtra("EXTRA_GSR", 320f)
        val status = intent.getStringExtra("EXTRA_STATUS") ?: "Normal"

        lifecycleScope.launch(Dispatchers.IO) {
            var totalCount = 0
            try {
                totalCount = db?.readingDao()?.getallreadings()?.firstOrNull()?.size ?: 0
            } catch (e: Exception) {
                e.printStackTrace()
            }

            withContext(Dispatchers.Main) {
                tvPassedData.text = "Latest Transferred Reading Details:\n\n" +
                        "• Heart Rate: $heartRate BPM\n" +
                        "• GSR Value: ${gsr.toInt()}\n" +
                        "• Stress Level: $status\n\n" +
                        "Total Room Database Records: $totalCount"
            }
        }
    }
}
