package com.example.skill_proof_mad_assignment_1

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProofScoreActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_proof_score)

        databaseHelper = DatabaseHelper(this)

        // -----------------------------
        // Find views
        // -----------------------------

        val btnBack =
            findViewById<ImageButton>(R.id.btnBack)

        val btnRefresh =
            findViewById<ImageButton>(R.id.btnRefresh)

        val tvProofScore =
            findViewById<TextView>(R.id.tvProofScore)

        val tvSkillName =
            findViewById<TextView>(R.id.tvSkillName)

        val tvProofLevel =
            findViewById<TextView>(R.id.tvProofLevel)

        val tvSkillLevelScore =
            findViewById<TextView>(R.id.tvSkillLevelScore)

        val tvAssessmentScore =
            findViewById<TextView>(R.id.tvAssessmentScore)

        val tvEvidenceScore =
            findViewById<TextView>(R.id.tvEvidenceScore)

        val tvVerificationScore =
            findViewById<TextView>(R.id.tvVerificationScore)

        val progressSkillLevel =
            findViewById<ProgressBar>(R.id.progressSkillLevel)

        val progressAssessment =
            findViewById<ProgressBar>(R.id.progressAssessment)

        val progressEvidence =
            findViewById<ProgressBar>(R.id.progressEvidence)

        val progressVerification =
            findViewById<ProgressBar>(R.id.progressVerification)

        val tvRecommendation =
            findViewById<TextView>(R.id.tvRecommendation)

        // -----------------------------
        // Back button
        // -----------------------------

        btnBack.setOnClickListener {
            finish()
        }

        // -----------------------------
        // Refresh button
        // -----------------------------

        btnRefresh.setOnClickListener {

            loadProofScore(
                tvProofScore,
                tvSkillName,
                tvProofLevel,
                tvSkillLevelScore,
                tvAssessmentScore,
                tvEvidenceScore,
                tvVerificationScore,
                progressSkillLevel,
                progressAssessment,
                progressEvidence,
                progressVerification,
                tvRecommendation
            )
        }

        // -----------------------------
        // Load initial data
        // -----------------------------

        loadProofScore(
            tvProofScore,
            tvSkillName,
            tvProofLevel,
            tvSkillLevelScore,
            tvAssessmentScore,
            tvEvidenceScore,
            tvVerificationScore,
            progressSkillLevel,
            progressAssessment,
            progressEvidence,
            progressVerification,
            tvRecommendation
        )
    }

    override fun onResume() {
        super.onResume()

        if (::databaseHelper.isInitialized) {

            val tvProofScore =
                findViewById<TextView>(R.id.tvProofScore)

            val tvSkillName =
                findViewById<TextView>(R.id.tvSkillName)

            val tvProofLevel =
                findViewById<TextView>(R.id.tvProofLevel)

            val tvSkillLevelScore =
                findViewById<TextView>(R.id.tvSkillLevelScore)

            val tvAssessmentScore =
                findViewById<TextView>(R.id.tvAssessmentScore)

            val tvEvidenceScore =
                findViewById<TextView>(R.id.tvEvidenceScore)

            val tvVerificationScore =
                findViewById<TextView>(R.id.tvVerificationScore)

            val progressSkillLevel =
                findViewById<ProgressBar>(R.id.progressSkillLevel)

            val progressAssessment =
                findViewById<ProgressBar>(R.id.progressAssessment)

            val progressEvidence =
                findViewById<ProgressBar>(R.id.progressEvidence)

            val progressVerification =
                findViewById<ProgressBar>(R.id.progressVerification)

            val tvRecommendation =
                findViewById<TextView>(R.id.tvRecommendation)

            loadProofScore(
                tvProofScore,
                tvSkillName,
                tvProofLevel,
                tvSkillLevelScore,
                tvAssessmentScore,
                tvEvidenceScore,
                tvVerificationScore,
                progressSkillLevel,
                progressAssessment,
                progressEvidence,
                progressVerification,
                tvRecommendation
            )
        }
    }

    private fun loadProofScore(
        tvProofScore: TextView,
        tvSkillName: TextView,
        tvProofLevel: TextView,
        tvSkillLevelScore: TextView,
        tvAssessmentScore: TextView,
        tvEvidenceScore: TextView,
        tvVerificationScore: TextView,
        progressSkillLevel: ProgressBar,
        progressAssessment: ProgressBar,
        progressEvidence: ProgressBar,
        progressVerification: ProgressBar,
        tvRecommendation: TextView
    ) {

        // -----------------------------
        // Get skills
        // -----------------------------

        val skills =
            databaseHelper.getAllSkills()

        // -----------------------------
        // No skill added
        // -----------------------------

        if (skills.isEmpty()) {

            tvProofScore.text = "0"
            tvSkillName.text = "No skill added yet"
            tvProofLevel.text = "Beginner"

            tvSkillLevelScore.text = "0%"
            tvAssessmentScore.text = "0%"
            tvEvidenceScore.text = "0%"
            tvVerificationScore.text = "0%"

            progressSkillLevel.progress = 0
            progressAssessment.progress = 0
            progressEvidence.progress = 0
            progressVerification.progress = 0

            tvRecommendation.text =
                "Add a skill, complete an assessment, and add supporting evidence to build your Proof Score."

            return
        }

        // -----------------------------
        // Use current/latest skill
        // -----------------------------

        val skill =
            skills.first()

        val skillName =
            skill.name

        val skillLevel =
            skill.progress.coerceIn(0, 100)

        // -----------------------------
        // Get assessment score
        // -----------------------------

        val assessmentScore =
            databaseHelper.getAssessmentPercentageForSkill(
                skillName
            ).coerceIn(0, 100)

        // -----------------------------
        // Get evidence score
        // -----------------------------

        val evidenceCount =
            databaseHelper.getEvidenceCountForSkill(
                skillName
            )

        val verifiedEvidenceCount =
            databaseHelper.getVerifiedEvidenceCountForSkill(
                skillName
            )

        val evidenceScore =
            when {
                evidenceCount >= 5 -> 100
                evidenceCount == 4 -> 90
                evidenceCount == 3 -> 80
                evidenceCount == 2 -> 65
                evidenceCount == 1 -> 45
                else -> 0
            }

        // -----------------------------
        // Verification score
        // -----------------------------

        val verificationScore =
            if (evidenceCount == 0) {
                0
            } else {
                (
                        verifiedEvidenceCount * 100
                        ) / evidenceCount
            }.coerceIn(0, 100)

        // -----------------------------
        // Proof Score
        //
        // Skill Level     = 30%
        // Assessment      = 30%
        // Evidence        = 25%
        // Verification    = 15%
        // -----------------------------

        val proofScore =
            (
                    skillLevel * 0.30 +
                            assessmentScore * 0.30 +
                            evidenceScore * 0.25 +
                            verificationScore * 0.15
                    ).toInt().coerceIn(0, 100)

        // -----------------------------
        // Display basic information
        // -----------------------------

        tvProofScore.text =
            proofScore.toString()

        tvSkillName.text =
            "Skill: $skillName"

        tvSkillLevelScore.text =
            "$skillLevel%"

        tvAssessmentScore.text =
            "$assessmentScore%"

        tvEvidenceScore.text =
            "$evidenceScore%"

        tvVerificationScore.text =
            "$verificationScore%"

        // -----------------------------
        // Progress bars
        // -----------------------------

        progressSkillLevel.progress =
            skillLevel

        progressAssessment.progress =
            assessmentScore

        progressEvidence.progress =
            evidenceScore

        progressVerification.progress =
            verificationScore

        // -----------------------------
        // Proof level
        // -----------------------------

        tvProofLevel.text =
            when {
                proofScore >= 90 ->
                    "Expert Proof"

                proofScore >= 75 ->
                    "Proven Skill"

                proofScore >= 60 ->
                    "Competent"

                proofScore >= 40 ->
                    "Developing"

                else ->
                    "Beginner"
            }

        // -----------------------------
        // Recommendations
        // -----------------------------

        val recommendations =
            mutableListOf<String>()

        if (skillLevel < 70) {

            recommendations.add(
                "• Improve your practical skill level through projects."
            )
        }

        if (assessmentScore < 70) {

            recommendations.add(
                "• Complete or improve your skill assessment."
            )
        }

        if (evidenceCount < 3) {

            recommendations.add(
                "• Add more evidence such as projects, certificates or GitHub repositories."
            )
        }

        if (
            evidenceCount > 0 &&
            verifiedEvidenceCount < evidenceCount
        ) {

            recommendations.add(
                "• Verify your remaining evidence."
            )
        }

        if (recommendations.isEmpty()) {

            recommendations.add(
                "• Excellent! Keep adding advanced projects and maintaining your skill proof."
            )
        }

        tvRecommendation.text =
            recommendations.joinToString("\n")
    }
}