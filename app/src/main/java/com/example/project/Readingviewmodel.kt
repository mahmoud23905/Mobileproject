package com.example.project

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ReadingViewModel(application: Application, viewModelScope: CoroutineScope) : AndroidViewModel(application) {

    private val repository = ReadingRepository(
        AppDatabase.getInstance(application).readingDao()
    )

    val readings: StateFlow<List<Reading>> = repository.allReadings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val latestReading: StateFlow<Reading?> = repository.latestReading
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun addReading(reading: Reading) = viewModelScope.launch {
        repository.addReading(reading)
    }

    fun updateNote(id: Int, note: String) = viewModelScope.launch {
        repository.updateNote(id, note)
    }

    fun deleteReading(reading: Reading) = viewModelScope.launch {
        repository.deleteReading(reading)
    }

    fun clearAll() = viewModelScope.launch {
        repository.clearAll()
    }
}