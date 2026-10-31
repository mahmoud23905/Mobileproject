package com.example.neuroguard

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.Activity
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView

class BreathingActivity : Activity() {

    private lateinit var breathingCircle: FrameLayout
    private lateinit var tvBreathingInstruction: TextView
    private lateinit var btnFinishBreathing: Button

    private var breathingAnimator: ObjectAnimator? = null
    private var isInhaling = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_breathing)

        breathingCircle = findViewById(R.id.breathingCircle)
        tvBreathingInstruction = findViewById(R.id.tvBreathingInstruction)
        btnFinishBreathing = findViewById(R.id.btnFinishBreathing)

        startBreathingAnimation()

        btnFinishBreathing.setOnClickListener {
            finish()
        }
    }

    private fun startBreathingAnimation() {
        val scaleX = PropertyValuesHolder.ofFloat("scaleX", 1.0f, 1.5f)
        val scaleY = PropertyValuesHolder.ofFloat("scaleY", 1.0f, 1.5f)

        breathingAnimator = ObjectAnimator.ofPropertyValuesHolder(
            breathingCircle, scaleX, scaleY
        ).apply {
            duration = 4000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator()

            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationRepeat(animation: Animator) {
                    isInhaling = !isInhaling
                    tvBreathingInstruction.text = if (isInhaling) "Inhale..." else "Exhale..."
                }
            })
            start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        breathingAnimator?.cancel()
    }
}