package com.example.neuroguard

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class VitalDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_HEART_RATE INTEGER,
                $COLUMN_GSR REAL,
                $COLUMN_STATUS TEXT,
                $COLUMN_TIMESTAMP INTEGER
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertReading(heartRate: Int, gsr: Float, status: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_HEART_RATE, heartRate)
            put(COLUMN_GSR, gsr)
            put(COLUMN_STATUS, status)
            put(COLUMN_TIMESTAMP, System.currentTimeMillis())
        }
        return db.insert(TABLE_NAME, null, values)
    }

    companion object {
        private const val DATABASE_NAME = "vitals.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "vitals"
        private const val COLUMN_ID = "id"
        private const val COLUMN_HEART_RATE = "heart_rate"
        private const val COLUMN_GSR = "gsr"
        private const val COLUMN_STATUS = "status"
        private const val COLUMN_TIMESTAMP = "timestamp"
    }
}