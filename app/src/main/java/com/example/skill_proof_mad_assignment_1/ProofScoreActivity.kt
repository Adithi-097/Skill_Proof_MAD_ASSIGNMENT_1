package com.example.skill_proof_mad_assignment_1

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProofScoreActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    private lateinit var btnBack: ImageButton
    private lateinit var btnRefresh: ImageButton

    private lateinit var tvProofScore: TextView
    private lateinit var tvSkillName: TextView
    private lateinit var tvProofLevel: TextView

    private lateinit var tvSkillLevelScore: TextView
    private lateinit var tvAssessmentScore: TextView
    private lateinit var tvEvidenceScore: TextView
    private lateinit var tvVerificationScore: TextView

    private lateinit var progressSkillLevel: ProgressBar
    private lateinit var progressAssessment: ProgressBar
    private lateinit var progressEvidence: ProgressBar
    private lateinit var progressVerification: ProgressBar

    private lateinit var tvRecommendation: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_proof_score)

        databaseHelper = DatabaseHelper(this)

        // -----------------------------------------
        // Find Views
        // -----------------------------------------

        btnBack =
            findViewById(R.id.btnBack)

        btnRefresh =
            findViewById(R.id.btnRefresh)

        tvProofScore =
            findViewById(R.id.tvProofScore)

        tvSkillName =
            findViewById(R.id.tvSkillName)

        tvProofLevel =
            findViewById(R.id.tvProofLevel)

        tvSkillLevelScore =
            findViewById(R.id.tvSkillLevelScore)

        tvAssessmentScore =
            findViewById(R.id.tvAssessmentScore)

        tvEvidenceScore =
            findViewById(R.id.tvEvidenceScore)

        tvVerificationScore =
            findViewById(R.id.tvVerificationScore)

        progressSkillLevel =
            findViewById(R.id.progressSkillLevel)

        progressAssessment =
            findViewById(R.id.progressAssessment)

        progressEvidence =
            findViewById(R.id.progressEvidence)

        progressVerification =
            findViewById(R.id.progressVerification)

        tvRecommendation =
            findViewById(R.id.tvRecommendation)

        // -----------------------------------------
        // Back
        // -----------------------------------------

        btnBack.setOnClickListener {

            onBackPressedDispatcher.onBackPressed()
        }

        // -----------------------------------------
        // Refresh
        // -----------------------------------------

        btnRefresh.setOnClickListener {

            calculateProofScore()
        }

        calculateProofScore()
    }

    override fun onResume() {
        super.onResume()

        calculateProofScore()
    }

    // ---------------------------------------------
    // Calculate Proof Score
    // ---------------------------------------------

    private fun calculateProofScore() {

        val skills =
            databaseHelper.getAllSkills()

        if (skills.isEmpty()) {

            showNoSkillState()

            return
        }

        // -----------------------------------------
        // Currently use the first skill
        // -----------------------------------------

        val selectedSkill =
            skills.first()

        val skillName =
            selectedSkill.name

        // -----------------------------------------
        // Skill Level
        // Weight = 30%
        // -----------------------------------------

        val skillLevelScore =
            selectedSkill.progress.coerceIn(0, 100)

        // -----------------------------------------
        // Assessment
        // Weight = 30%
        // -----------------------------------------

        val assessmentScore =
            databaseHelper
                .getAssessmentPercentageForSkill(
                    skillName
                )
                .coerceIn(0, 100)

        // -----------------------------------------
        // Evidence
        // Weight = 25%
        // -----------------------------------------

        val evidenceCount =
            databaseHelper
                .getEvidenceCountForSkill(
                    skillName
                )

        val evidenceScore =
            calculateEvidenceScore(
                evidenceCount
            )

        // -----------------------------------------
        // Verification
        // Weight = 15%
        // -----------------------------------------

        val verifiedEvidenceCount =
            databaseHelper
                .getVerifiedEvidenceCountForSkill(
                    skillName
                )

        val verificationScore =
            if (evidenceCount > 0) {

                (
                        verifiedEvidenceCount * 100
                        ) / evidenceCount

            } else {
                0
            }

        // -----------------------------------------
        // Final Score
        // -----------------------------------------

        val proofScore =
            (
                    skillLevelScore * 0.30 +
                            assessmentScore * 0.30 +
                            evidenceScore * 0.25 +
                            verificationScore * 0.15
                    ).toInt()
                .coerceIn(0, 100)

        // -----------------------------------------
        // Update UI
        // -----------------------------------------

        tvSkillName.text =
            skillName

        tvProofScore.text =
            proofScore.toString()

        tvSkillLevelScore.text =
            "$skillLevelScore / 100"

        tvAssessmentScore.text =
            "$assessmentScore / 100"

        tvEvidenceScore.text =
            "$evidenceScore / 100"

        tvVerificationScore.text =
            "$verificationScore / 100"

        progressSkillLevel.progress =
            skillLevelScore

        progressAssessment.progress =
            assessmentScore

        progressEvidence.progress =
            evidenceScore

        progressVerification.progress =
            verificationScore

        tvProofLevel.text =
            getProofLevel(proofScore)

        tvRecommendation.text =
            getRecommendation(
                skillLevelScore,
                assessmentScore,
                evidenceScore,
                verificationScore
            )
    }

    // ---------------------------------------------
    // Evidence Score
    // ---------------------------------------------

    private fun calculateEvidenceScore(
        evidenceCount: Int
    ): Int {

        return when {

            evidenceCount <= 0 ->
                0

            evidenceCount == 1 ->
                45

            evidenceCount == 2 ->
                65

            evidenceCount == 3 ->
                80

            evidenceCount == 4 ->
                90

            else ->
                100
        }
    }

    // ---------------------------------------------
    // Proof Level
    // ---------------------------------------------

    private fun getProofLevel(
        score: Int
    ): String {

        return when {

            score >= 90 ->
                "Expert Proof"

            score >= 75 ->
                "Proven Skill"

            score >= 60 ->
                "Competent"

            score >= 40 ->
                "Developing"

            else ->
                "Beginner"
        }
    }

    // ---------------------------------------------
    // Recommendation
    // ---------------------------------------------

    private fun getRecommendation(
        skillLevel: Int,
        assessment: Int,
        evidence: Int,
        verification: Int
    ): String {

        val recommendations =
            mutableListOf<String>()

        if (skillLevel < 70) {

            recommendations.add(
                "Improve your practical skill level."
            )
        }

        if (assessment < 70) {

            recommendations.add(
                "Take another assessment and improve your knowledge."
            )
        }

        if (evidence < 70) {

            recommendations.add(
                "Add more projects, certificates or other evidence."
            )
        }

        if (verification < 70) {

            recommendations.add(
                "Verify more of your submitted evidence."
            )
        }

        return if (recommendations.isEmpty()) {

            "Excellent proof! Your skill has strong evidence, assessment performance and verification."

        } else {

            recommendations.joinToString(
                separator = "\n\n"
            )
        }
    }

    // ---------------------------------------------
    // No Skill State
    // ---------------------------------------------

    private fun showNoSkillState() {

        tvSkillName.text =
            "No skill added"

        tvProofScore.text =
            "0"

        tvProofLevel.text =
            "Start Building Proof"

        tvSkillLevelScore.text =
            "0 / 100"

        tvAssessmentScore.text =
            "0 / 100"

        tvEvidenceScore.text =
            "0 / 100"

        tvVerificationScore.text =
            "0 / 100"

        progressSkillLevel.progress =
            0

        progressAssessment.progress =
            0

        progressEvidence.progress =
            0

        progressVerification.progress =
            0

        tvRecommendation.text =
            "Add a skill first, then complete an assessment and submit evidence to build your Proof Score."
    }
}