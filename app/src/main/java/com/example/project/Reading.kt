package com.example.project

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName="readings")
data class Reading(
    @PrimaryKey(autoGenerate=true) val id: Int=0,
    val heartrate: Int,
    val gsr: Float,
    val stresslevel: String,
    val timestamp: Long=System.currentTimeMillis(),
    val note: String=""
)