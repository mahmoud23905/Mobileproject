package com.example.neuroguard

import kotlin.random.Random

class MockSensorEngine {
    data class SimulatedResult(
        val heartRate: Int,
        val gsr: Float,
        val status: String,
        val isStressed: Boolean
    )

    fun generateHeartRate(): Int {
        return Random.nextInt(60, 135)
    }

    fun generateGSR(): Float {
        return Random.nextDouble(200.0, 950.0).toFloat()
    }

    fun generateSimulatedReading(): SimulatedResult {
        val isStressed = Random.nextInt(0, 10) > 5
        val hr = if (isStressed) Random.nextInt(105, 140) else Random.nextInt(65, 85)
        val gsr = if (isStressed) Random.nextDouble(700.0, 950.0).toFloat() else Random.nextDouble(200.0, 450.0).toFloat()
        val status = if (isStressed) "High Stress (Stressed)" else "Normal"
        return SimulatedResult(hr, gsr, status, isStressed)
    }
}
