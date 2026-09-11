package com.example.skill_proof_mad_assignment_1

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        // Load fade-in animation
        val fadeAnimation =
            AnimationUtils.loadAnimation(this, R.anim.fade_in)

        // Apply animation to splash elements
        findViewById<android.view.View>(R.id.logoCard)
            .startAnimation(fadeAnimation)

        findViewById<android.view.View>(R.id.tvAppName)
            .startAnimation(fadeAnimation)

        findViewById<android.view.View>(R.id.tvTagline)
            .startAnimation(fadeAnimation)

        // Open Login screen after 2.5 seconds
        Handler(Looper.getMainLooper()).postDelayed({

            val intent = Intent(
                this,
                LoginActivity::class.java
            )

            startActivity(intent)
            finish()

        }, 2500)
    }
}