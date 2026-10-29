package com.example.project

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReadingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reading: Reading): Long
    @Query("SELECT * FROM readings ORDER BY timestamp DESC")
    fun getallreadings(): Flow<List<Reading>>
    @Query("SELECT * FROM readings ORDER BY timestamp DESC LIMIT 1")
    fun getLatestReading(): Flow<Reading?>
    @Query("UPDATE readings SET note = :note WHERE id = :id")
    suspend fun updateNote(id: Int, note: String)
    @Delete
    suspend fun delete(reading: Reading)
    @Query("DELETE FROM readings")
    suspend fun deleteAll()
    fun getAllReadings(): Flow<List<Reading>>

}