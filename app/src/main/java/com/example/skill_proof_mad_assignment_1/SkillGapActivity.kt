package com.example.skill_proof_mad_assignment_1

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SkillGapActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    private lateinit var btnBack: ImageButton
    private lateinit var btnRefresh: ImageButton

    private lateinit var tvSkillName: TextView
    private lateinit var tvCurrentScore: TextView
    private lateinit var tvTargetScore: TextView
    private lateinit var tvGapScore: TextView
    private lateinit var tvGapMessage: TextView

    private lateinit var tvSkillLevelScore: TextView
    private lateinit var tvAssessmentScore: TextView
    private lateinit var tvEvidenceScore: TextView
    private lateinit var tvVerificationScore: TextView

    private lateinit var progressSkillLevel: ProgressBar
    private lateinit var progressAssessment: ProgressBar
    private lateinit var progressEvidence: ProgressBar
    private lateinit var progressVerification: ProgressBar

    private lateinit var tvActionPlan: TextView

    companion object {
        private const val TARGET_SCORE = 80
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_skill_gap
        )

        databaseHelper =
            DatabaseHelper(this)

        // -----------------------------------------
        // Find Views
        // -----------------------------------------

        btnBack =
            findViewById(R.id.btnBack)

        btnRefresh =
            findViewById(R.id.btnRefresh)

        tvSkillName =
            findViewById(R.id.tvSkillName)

        tvCurrentScore =
            findViewById(R.id.tvCurrentScore)

        tvTargetScore =
            findViewById(R.id.tvTargetScore)

        tvGapScore =
            findViewById(R.id.tvGapScore)

        tvGapMessage =
            findViewById(R.id.tvGapMessage)

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

        tvActionPlan =
            findViewById(R.id.tvActionPlan)

        // -----------------------------------------
        // Back
        // -----------------------------------------

        btnBack.setOnClickListener {

            onBackPressedDispatcher
                .onBackPressed()
        }

        // -----------------------------------------
        // Refresh
        // -----------------------------------------

        btnRefresh.setOnClickListener {

            analyzeSkillGap()
        }

        analyzeSkillGap()
    }

    override fun onResume() {
        super.onResume()

        analyzeSkillGap()
    }

    // ---------------------------------------------
    // Analyze Skill Gap
    // ---------------------------------------------

    private fun analyzeSkillGap() {

        val skills =
            databaseHelper.getAllSkills()

        if (skills.isEmpty()) {

            showNoSkillState()

            return
        }

        // -----------------------------------------
        // Current Skill
        // -----------------------------------------

        val selectedSkill =
            skills.first()

        val skillName =
            selectedSkill.name

        tvSkillName.text =
            skillName

        // -----------------------------------------
        // Skill Level
        // -----------------------------------------

        val skillLevelScore =
            selectedSkill.progress
                .coerceIn(0, 100)

        // -----------------------------------------
        // Assessment
        // -----------------------------------------

        val assessmentScore =
            databaseHelper
                .getAssessmentPercentageForSkill(
                    skillName
                )
                .coerceIn(0, 100)

        // -----------------------------------------
        // Evidence
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
        // Proof Score
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
        // Gap
        // -----------------------------------------

        val gap =
            (
                    TARGET_SCORE - proofScore
                    ).coerceAtLeast(0)

        // -----------------------------------------
        // Update Score UI
        // -----------------------------------------

        tvCurrentScore.text =
            proofScore.toString()

        tvTargetScore.text =
            TARGET_SCORE.toString()

        tvGapScore.text =
            gap.toString()

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

        // -----------------------------------------
        // Gap Message
        // -----------------------------------------

        tvGapMessage.text =
            getGapMessage(
                proofScore,
                gap
            )

        // -----------------------------------------
        // Action Plan
        // -----------------------------------------

        tvActionPlan.text =
            buildActionPlan(
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
    // Gap Message
    // ---------------------------------------------

    private fun getGapMessage(
        proofScore: Int,
        gap: Int
    ): String {

        return when {

            proofScore >= TARGET_SCORE ->
                "Great job! Your current proof score has reached the target of $TARGET_SCORE."

            gap <= 10 ->
                "You're very close to the target. Focus on your weakest area to close the remaining gap."

            gap <= 25 ->
                "You're making good progress. Strengthen your evidence and assessment performance."

            else ->
                "There is a significant gap between your current proof and the target. Follow the action plan below step by step."
        }
    }

    // ---------------------------------------------
    // Build Action Plan
    // ---------------------------------------------

    private fun buildActionPlan(
        skillLevel: Int,
        assessment: Int,
        evidence: Int,
        verification: Int
    ): String {

        val actions =
            mutableListOf<String>()

        // Priority 1
        if (skillLevel < 70) {

            actions.add(
                "1. Improve practical skill level\n" +
                        "   Practice coding and build a project related to this skill."
            )
        }

        // Priority 2
        if (assessment < 70) {

            actions.add(
                "${actions.size + 1}. Improve assessment performance\n" +
                        "   Revise concepts and retake the assessment."
            )
        }

        // Priority 3
        if (evidence < 70) {

            actions.add(
                "${actions.size + 1}. Add stronger evidence\n" +
                        "   Submit projects, certificates or other relevant proof."
            )
        }

        // Priority 4
        if (verification < 70) {

            actions.add(
                "${actions.size + 1}. Verify your evidence\n" +
                        "   Use GitHub verification or other verification methods."
            )
        }

        if (actions.isEmpty()) {

            return "✓ Your skill is well supported.\n\n" +
                    "Keep your assessments and evidence updated to maintain a strong Proof Score."
        }

        return actions.joinToString(
            separator = "\n\n"
        )
    }

    // ---------------------------------------------
    // No Skill State
    // ---------------------------------------------

    private fun showNoSkillState() {

        tvSkillName.text =
            "No skill added"

        tvCurrentScore.text =
            "0"

        tvTargetScore.text =
            TARGET_SCORE.toString()

        tvGapScore.text =
            TARGET_SCORE.toString()

        tvGapMessage.text =
            "Add a skill first to analyze your skill gap."

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

        tvActionPlan.text =
            "1. Add a skill\n\n" +
                    "2. Complete an assessment\n\n" +
                    "3. Add relevant evidence\n\n" +
                    "4. Verify your evidence"
    }
}