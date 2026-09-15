package com.example.skill_proof_mad_assignment_1

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton

class AssessmentResultActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assessment_result)

        val tvSkillName = findViewById<TextView>(R.id.tvSkillName)
        val tvPercentage = findViewById<TextView>(R.id.tvPercentage)
        val tvScore = findViewById<TextView>(R.id.tvScore)
        val tvPerformance = findViewById<TextView>(R.id.tvPerformance)
        val tvMessage = findViewById<TextView>(R.id.tvMessage)

        val btnBack = findViewById<MaterialButton>(R.id.btnBack)
        val btnRetake = findViewById<MaterialButton>(R.id.btnRetake)
        val btnDone = findViewById<MaterialButton>(R.id.btnDone)

        // Get result data from AssessmentActivity
        val skill = intent.getStringExtra("skill") ?: "Unknown Skill"
        val score = intent.getIntExtra("score", 0)
        val total = intent.getIntExtra("total", 5)
        val percentage = intent.getIntExtra("percentage", 0)

        // Display result
        tvSkillName.text = skill
        tvPercentage.text = "$percentage%"
        tvScore.text = "$score / $total Correct"

        // Performance classification
        val performance: String
        val message: String

        when {
            percentage >= 90 -> {
                performance = "Excellent"
                message = "Outstanding performance! You have a strong understanding of this skill."
            }

            percentage >= 75 -> {
                performance = "Very Good"
                message = "Great work! Keep practicing to strengthen your knowledge."
            }

            percentage >= 60 -> {
                performance = "Good"
                message = "Good effort! A little more practice can improve your score."
            }

            percentage >= 40 -> {
                performance = "Needs Improvement"
                message = "Keep learning and practicing. You can improve your skills."
            }

            else -> {
                performance = "Beginner"
                message = "Start with the basics and practice regularly to build confidence."
            }
        }

        tvPerformance.text = performance
        tvMessage.text = message

        // Back button
        btnBack.setOnClickListener {
            finish()
        }

        // Retake assessment
        btnRetake.setOnClickListener {
            val intent = Intent(this, AssessmentActivity::class.java)
            intent.putExtra("selected_skill", skill)
            startActivity(intent)
            finish()
        }

        // Done → Dashboard
        btnDone.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)

            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_SINGLE_TOP

            startActivity(intent)
            finish()
        }
    }
}