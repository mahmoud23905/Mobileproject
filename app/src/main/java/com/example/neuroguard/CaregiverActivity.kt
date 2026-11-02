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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CaregiverActivity : AppCompatActivity() {

    private lateinit var listViewHistory: ListView
    private lateinit var tvAlertStatus: TextView
    private lateinit var btnBack: Button

    private lateinit var db: AppDatabase
    private var readingsList: MutableList<Reading> = mutableListOf()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_caregiver_history)

        // Initialize views via findViewById (Traditional Views approach)
        listViewHistory = findViewById(R.id.listViewHistory)
        tvAlertStatus = findViewById(R.id.tvAlertStatus)
        btnBack = findViewById(R.id.btnBack)

        // Initialize Room Database instance
        db = AppDatabase.getInstance(this)

        // Load historical records from SQLite/Room
        loadHistoryData()

        // Handle item click for UPDATE (Editing observation notes)
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
        // Run database queries off the main thread using lifecycleScope
        lifecycleScope.launch(Dispatchers.IO) {
            readingsList = db.readingDao().getallreadings().first().toMutableList()

            val displayStrings = readingsList.map {
                "HR: ${it.heartrate} BPM | Status: ${it.stresslevel}\nNote: ${it.note}"
            }

            withContext(Dispatchers.Main) {
                // Update Alert Banner based on latest record
                updateAlertBanner()

                // Populate ListView using ArrayAdapter
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
                // Perform database update (CRUD: Update)
                db.readingDao().updateNote(record.id, newNote)
                loadHistoryData() // Refresh list
            }
            Toast.makeText(this, "Note updated successfully", Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("Delete Record") { _, _ ->
            lifecycleScope.launch(Dispatchers.IO) {
                // Perform database delete (CRUD: Delete)
                db.readingDao().delete(record)
                loadHistoryData() // Refresh list
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
