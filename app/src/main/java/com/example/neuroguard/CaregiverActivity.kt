package com.example.neuroguard

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CaregiverActivity : AppCompatActivity() {

    private lateinit var listViewHistory: ListView
    private lateinit var tvAlertStatus: TextView
    private lateinit var btnBack: Button

    private val db: AppDatabase? by lazy {
        try {
            AppDatabase.getInstance(applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    private var readingsList: MutableList<Reading> = mutableListOf()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_caregiver_history)

        listViewHistory = findViewById(R.id.listViewHistory)
        tvAlertStatus = findViewById(R.id.tvAlertStatus)
        btnBack = findViewById(R.id.btnBack)

        loadHistoryData()

        listViewHistory.setOnItemClickListener { _, _, position, _ ->
            if (position in readingsList.indices) {
                val selectedRecord = readingsList[position]
                showEditNoteDialog(selectedRecord)
            }
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadHistoryData() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val items = db?.readingDao()?.getallreadings()?.firstOrNull()
                readingsList = items?.toMutableList() ?: mutableListOf()
            } catch (e: Exception) {
                e.printStackTrace()
                readingsList = mutableListOf()
            }

            val displayStrings = readingsList.map {
                "HR: ${it.heartrate} BPM | Status: ${it.stresslevel}\nNote: ${it.note}"
            }

            withContext(Dispatchers.Main) {
                updateAlertBanner()

                adapter = ArrayAdapter(
                    this@CaregiverActivity,
                    android.R.layout.simple_list_item_1,
                    displayStrings
                )
                listViewHistory.adapter = adapter
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateAlertBanner() {
        val latest = readingsList.firstOrNull()
        if (latest != null) {
            tvAlertStatus.text = "Latest Status: ${latest.stresslevel}"
            if (latest.stresslevel.contains("Critical", ignoreCase = true) ||
                latest.stresslevel.contains("High", ignoreCase = true) ||
                latest.stresslevel.contains("Stressed", ignoreCase = true)
            ) {
                tvAlertStatus.setBackgroundColor(getColor(android.R.color.holo_red_dark))
            } else {
                tvAlertStatus.setBackgroundColor(getColor(android.R.color.holo_green_dark))
            }
        } else {
            tvAlertStatus.text = "No records found."
            tvAlertStatus.setBackgroundColor(getColor(android.R.color.darker_gray))
        }
    }

    private fun showEditNoteDialog(record: Reading) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Edit Caregiver Note")

        val input = EditText(this)
        input.setText(record.note)
        builder.setView(input)

        builder.setPositiveButton("Update") { _, _ ->
            val newNote = input.text.toString()
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    db?.readingDao()?.updateNote(record.id, newNote)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                loadHistoryData()
            }
            Toast.makeText(this, "Note updated successfully", Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("Delete Record") { _, _ ->
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    db?.readingDao()?.delete(record)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                loadHistoryData()
            }
            Toast.makeText(this, "Record deleted", Toast.LENGTH_SHORT).show()
        }

        builder.setNeutralButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    override fun onRestart() {
        super.onRestart()
        loadHistoryData()
    }
}
