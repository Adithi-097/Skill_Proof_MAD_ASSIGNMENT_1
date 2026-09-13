package com.example.skill_proof_mad_assignment_1

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CareerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_career)

        val btnBack =
            findViewById<ImageButton>(R.id.btnBack)

        val tvReadinessScore =
            findViewById<TextView>(R.id.tvReadinessScore)

        val tvReadinessMessage =
            findViewById<TextView>(R.id.tvReadinessMessage)

        val readinessProgress =
            findViewById<ProgressBar>(R.id.readinessProgress)

        val tvStrongSkills =
            findViewById<TextView>(R.id.tvStrongSkills)

        val tvImproveSkills =
            findViewById<TextView>(R.id.tvImproveSkills)

        val tvRecommendations =
            findViewById<TextView>(R.id.tvRecommendations)

        btnBack.setOnClickListener {
            finish()
        }

        /*
         * Current SkillProof demo data.
         *
         * These values will later come from SQLite.
         */

        val proofScore = 66
        val assessmentScore = 80

        val strongSkills = listOf(
            "Python",
            "Kotlin"
        )

        val skillsToImprove = listOf(
            "SQL",
            "Java"
        )

        /*
         * Career Readiness calculation.
         *
         * Proof Score      → 60%
         * Assessment       → 40%
         */

        val readinessScore =
            (proofScore * 0.60 +
                    assessmentScore * 0.40).toInt()

        tvReadinessScore.text =
            "$readinessScore%"

        readinessProgress.progress =
            readinessScore

        tvReadinessMessage.text =
            when {
                readinessScore >= 85 ->
                    "You are highly prepared for your target role."

                readinessScore >= 70 ->
                    "You are on the right track. Strengthen your weak areas."

                readinessScore >= 50 ->
                    "Good start. Build more proof to improve your readiness."

                else ->
                    "Keep building skills, assessments and evidence."
            }

        tvStrongSkills.text =
            strongSkills.joinToString(
                separator = "\n"
            ) {
                "✓  $it"
            }

        tvImproveSkills.text =
            skillsToImprove.joinToString(
                separator = "\n"
            ) {
                "⚠  $it"
            }

        tvRecommendations.text =
            "• Take assessments for your weak skills\n\n" +
                    "• Add at least one project as evidence\n\n" +
                    "• Add certificates or GitHub repositories\n\n" +
                    "• Verify your strongest evidence\n\n" +
                    "• Improve skills with low assessment scores"
    }
}