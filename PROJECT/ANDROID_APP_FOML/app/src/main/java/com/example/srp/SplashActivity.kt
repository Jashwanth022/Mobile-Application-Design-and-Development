package com.example.srp

import android.content.Intent
import android.os.Bundle
import android.view.animation.OvershootInterpolator
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Keep splash always dark
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val logoContainer = findViewById<LinearLayout>(R.id.logoContainer)

        val tvLogo = findViewById<android.widget.TextView>(R.id.tvLogo)
        val tvAppName = findViewById<android.widget.TextView>(R.id.tvAppName)
        val tvTagline = findViewById<android.widget.TextView>(R.id.tvTagline)

        // Initial states
        tvLogo.alpha = 0f
        tvLogo.scaleX = 0f
        tvLogo.scaleY = 0f
        
        tvAppName.alpha = 0f
        tvAppName.translationY = 50f
        
        tvTagline.alpha = 0f

        // Sequence Animation
        tvLogo.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(1500)
            .setInterpolator(OvershootInterpolator(2f))
            .start()

        tvAppName.animate()
            .alpha(1f)
            .translationY(0f)
            .setStartDelay(800)
            .setDuration(1200)
            .start()

        tvTagline.animate()
            .alpha(1f)
            .setStartDelay(1500)
            .setDuration(1000)
            .withEndAction {
                // Navigate to MainActivity after a total of ~3 seconds
                logoContainer.postDelayed({
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }, 500)
            }
            .start()
    }
}