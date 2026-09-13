package com.example.skill_proof_mad_assignment_1

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProofScoreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_proof_score)

        val btnBack =
            findViewById<ImageButton>(R.id.btnBack)

        val tvScore =
            findViewById<TextView>(R.id.tvScore)

        val tvScoreMessage =
            findViewById<TextView>(R.id.tvScoreMessage)

        val tvSkillValue =
            findViewById<TextView>(R.id.tvSkillValue)

        val tvAssessmentValue =
            findViewById<TextView>(R.id.tvAssessmentValue)

        val tvEvidenceValue =
            findViewById<TextView>(R.id.tvEvidenceValue)

        val tvVerifiedValue =
            findViewById<TextView>(R.id.tvVerifiedValue)

        val progressSkill =
            findViewById<ProgressBar>(R.id.progressSkill)

        val progressAssessment =
            findViewById<ProgressBar>(R.id.progressAssessment)

        val progressEvidence =
            findViewById<ProgressBar>(R.id.progressEvidence)

        val progressVerified =
            findViewById<ProgressBar>(R.id.progressVerified)

        btnBack.setOnClickListener {
            finish()
        }

        /*
         * Current demo values.
         *
         * These will later come from SQLite/database:
         *
         * Skill level      = 70%
         * Assessment       = 80%
         * Evidence         = 60%
         * Verified proof   = 40%
         */

        val skillLevel = 70
        val assessmentScore = 80
        val evidenceScore = 60
        val verifiedScore = 40

        /*
         * Proof Score calculation
         *
         * Skill Level     → 30%
         * Assessment      → 30%
         * Evidence        → 25%
         * Verification    → 15%
         */

        val proofScore =
            (skillLevel * 0.30 +
                    assessmentScore * 0.30 +
                    evidenceScore * 0.25 +
                    verifiedScore * 0.15).toInt()

        tvScore.text = proofScore.toString()

        tvSkillValue.text =
            "$skillLevel%"

        tvAssessmentValue.text =
            "$assessmentScore%"

        tvEvidenceValue.text =
            "$evidenceScore%"

        tvVerifiedValue.text =
            "$verifiedScore%"

        progressSkill.progress =
            skillLevel

        progressAssessment.progress =
            assessmentScore

        progressEvidence.progress =
            evidenceScore

        progressVerified.progress =
            verifiedScore

        tvScoreMessage.text =
            when {
                proofScore >= 85 ->
                    "Excellent proof profile"

                proofScore >= 70 ->
                    "Strong proof profile"

                proofScore >= 50 ->
                    "Good start — keep building proof"

                else ->
                    "Build more evidence to strengthen your proof"
            }
    }
}