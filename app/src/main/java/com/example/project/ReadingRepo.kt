package com.example.project

import kotlinx.coroutines.flow.Flow

class ReadingRepository(private val dao: ReadingDao) {

    val allReadings: Flow<List<Reading>> = dao.getAllReadings()
    val latestReading: Flow<Reading?> = dao.getLatestReading()

    suspend fun addReading(reading: Reading) = dao.insert(reading)
    suspend fun updateNote(id: Int, note: String) = dao.updateNote(id, note)
    suspend fun deleteReading(reading: Reading) = dao.delete(reading)
    suspend fun clearAll() = dao.deleteAll()
}