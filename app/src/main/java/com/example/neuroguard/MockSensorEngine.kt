package com.example.neuroguard

import kotlin.random.Random

class MockSensorEngine {
    fun generateHeartRate(): Int {
        return Random.nextInt(60, 135)
    }

    fun generateGSR(): Float {
        return Random.nextDouble(200.0, 950.0).toFloat()
    }
}
