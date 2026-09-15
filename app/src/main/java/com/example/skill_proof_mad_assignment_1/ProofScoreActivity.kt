package com.example.skill_proof_mad_assignment_1

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView

class ProofScoreActivity : BaseActivity() {

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

        // =========================================
        // FIND VIEWS
        // =========================================

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

        // =========================================
        // BACK
        // =========================================

        btnBack.setOnClickListener {

            onBackPressedDispatcher.onBackPressed()
        }

        // =========================================
        // REFRESH
        // =========================================

        btnRefresh.setOnClickListener {

            calculateProofScore()
        }

        // =========================================
        // INITIAL LOAD
        // =========================================

        calculateProofScore()
    }

    override fun onResume() {
        super.onResume()

        calculateProofScore()
    }

    // =============================================
    // CALCULATE PROOF SCORE
    // =============================================

    private fun calculateProofScore() {

        val skills =
            databaseHelper.getAllSkills()

        // =========================================
        // NO SKILLS
        // =========================================

        if (skills.isEmpty()) {

            showNoSkillState()

            return
        }

        // =========================================
        // GET CURRENT SKILL
        // =========================================

        val sharedPreferences =
            getSharedPreferences(
                "SkillProofPrefs",
                MODE_PRIVATE
            )

        val currentSkillName =
            sharedPreferences.getString(
                "current_skill",
                null
            )

        // =========================================
        // FIND CURRENT SKILL
        // =========================================

        val selectedSkill =
            if (!currentSkillName.isNullOrBlank()) {

                skills.firstOrNull {

                    it.name.equals(
                        currentSkillName,
                        ignoreCase = true
                    )
                }

            } else {
                null
            }

        // =========================================
        // NO CURRENT SKILL SELECTED
        // =========================================

        if (selectedSkill == null) {

            showNoCurrentSkillState()

            return
        }

        val skillName =
            selectedSkill.name

        // =========================================
        // SKILL LEVEL
        // Weight = 30%
        // =========================================

        val skillLevelScore =
            selectedSkill.progress
                .coerceIn(0, 100)

        // =========================================
        // ASSESSMENT
        // Weight = 30%
        // =========================================

        val assessmentScore =
            databaseHelper
                .getAssessmentPercentageForSkill(
                    skillName
                )
                .coerceIn(0, 100)

        // =========================================
        // EVIDENCE
        // Weight = 25%
        // =========================================

        val evidenceCount =
            databaseHelper
                .getEvidenceCountForSkill(
                    skillName
                )

        val evidenceScore =
            calculateEvidenceScore(
                evidenceCount
            )

        // =========================================
        // VERIFICATION
        // Weight = 15%
        // =========================================

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

        // =========================================
        // FINAL PROOF SCORE
        // =========================================

        val proofScore =
            (
                    skillLevelScore * 0.30 +
                            assessmentScore * 0.30 +
                            evidenceScore * 0.25 +
                            verificationScore * 0.15
                    ).toInt()
                .coerceIn(0, 100)

        // =========================================
        // UPDATE UI
        // =========================================

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

    // =============================================
    // EVIDENCE SCORE
    // =============================================

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

    // =============================================
    // PROOF LEVEL
    // =============================================

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

    // =============================================
    // RECOMMENDATION
    // =============================================

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

    // =============================================
    // NO CURRENT SKILL STATE
    // =============================================

    private fun showNoCurrentSkillState() {

        tvSkillName.text =
            "Select a skill"

        tvProofScore.text =
            "0"

        tvProofLevel.text =
            "Select Current Skill"

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
            "Open Skills and select a skill as your Current Skill to view its Proof Score."
    }

    // =============================================
    // NO SKILL STATE
    // =============================================

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