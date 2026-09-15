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

        val fadeAnimation =
            AnimationUtils.loadAnimation(
                this,
                R.anim.fade_in
            )

        findViewById<android.view.View>(
            R.id.logoCard
        ).startAnimation(fadeAnimation)

        findViewById<android.view.View>(
            R.id.tvAppName
        ).startAnimation(fadeAnimation)

        findViewById<android.view.View>(
            R.id.tvTagline
        ).startAnimation(fadeAnimation)

        Handler(
            Looper.getMainLooper()
        ).postDelayed({

            checkLoginStatus()

        }, 2500)
    }

    private fun checkLoginStatus() {

        val sharedPreferences =
            getSharedPreferences(
                "SkillProofPrefs",
                MODE_PRIVATE
            )

        val isLoggedIn =
            sharedPreferences.getBoolean(
                "is_logged_in",
                false
            )

        val nextActivity =
            if (isLoggedIn) {

                DashboardActivity::class.java

            } else {

                LoginActivity::class.java
            }

        val intent =
            Intent(
                this,
                nextActivity
            )

        startActivity(intent)

        finish()
    }
}