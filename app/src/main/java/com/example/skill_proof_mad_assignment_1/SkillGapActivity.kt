package com.example.skill_proof_mad_assignment_1

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView

class SkillGapActivity : BaseActivity() {

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
        private const val PREFS_NAME = "SkillProofPrefs"
        private const val CURRENT_SKILL_KEY = "current_skill"
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

        // -----------------------------------------
        // Analyze
        // -----------------------------------------

        analyzeSkillGap()
    }

    override fun onResume() {

        super.onResume()

        analyzeSkillGap()
    }

    // =================================================
    // ANALYZE CURRENT SKILL GAP
    // =================================================

    private fun analyzeSkillGap() {

        val skills =
            databaseHelper.getAllSkills()

        // -----------------------------------------
        // No skills in database
        // -----------------------------------------

        if (skills.isEmpty()) {

            showNoSkillState()

            return
        }

        // -----------------------------------------
        // Get Current Skill
        // -----------------------------------------

        val preferences =
            getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            )

        val currentSkillName =
            preferences.getString(
                CURRENT_SKILL_KEY,
                null
            )

        // -----------------------------------------
        // No Current Skill Selected
        // -----------------------------------------

        if (currentSkillName.isNullOrBlank()) {

            showNoCurrentSkillState()

            return
        }

        // -----------------------------------------
        // Find Current Skill
        // -----------------------------------------

        val selectedSkill =
            skills.firstOrNull {

                it.name.equals(
                    currentSkillName,
                    ignoreCase = true
                )
            }

        // -----------------------------------------
        // Current Skill Not Found
        // -----------------------------------------

        if (selectedSkill == null) {

            showNoCurrentSkillState()

            return
        }

        val skillName =
            selectedSkill.name

        tvSkillName.text =
            skillName

        // =================================================
        // SKILL LEVEL
        // =================================================

        val skillLevelScore =
            selectedSkill.progress
                .coerceIn(
                    0,
                    100
                )

        // =================================================
        // ASSESSMENT
        // =================================================

        val assessmentScore =
            databaseHelper
                .getAssessmentPercentageForSkill(
                    skillName
                )
                .coerceIn(
                    0,
                    100
                )

        // =================================================
        // EVIDENCE
        // =================================================

        val evidenceCount =
            databaseHelper
                .getEvidenceCountForSkill(
                    skillName
                )

        val evidenceScore =
            calculateEvidenceScore(
                evidenceCount
            )

        // =================================================
        // VERIFICATION
        // =================================================

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

        // =================================================
        // PROOF SCORE
        // =================================================

        val proofScore =
            (
                    skillLevelScore * 0.30 +
                            assessmentScore * 0.30 +
                            evidenceScore * 0.25 +
                            verificationScore * 0.15
                    )
                .toInt()
                .coerceIn(
                    0,
                    100
                )

        // =================================================
        // GAP
        // =================================================

        val gap =
            (
                    TARGET_SCORE - proofScore
                    ).coerceAtLeast(0)

        // =================================================
        // UPDATE SCORE UI
        // =================================================

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

        // =================================================
        // GAP MESSAGE
        // =================================================

        tvGapMessage.text =
            getGapMessage(
                proofScore,
                gap,
                skillName
            )

        // =================================================
        // ACTION PLAN
        // =================================================

        tvActionPlan.text =
            buildActionPlan(
                skillLevelScore,
                assessmentScore,
                evidenceScore,
                verificationScore,
                skillName
            )
    }

    // =================================================
    // EVIDENCE SCORE
    // =================================================

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

    // =================================================
    // GAP MESSAGE
    // =================================================

    private fun getGapMessage(
        proofScore: Int,
        gap: Int,
        skillName: String
    ): String {

        return when {

            proofScore >= TARGET_SCORE ->

                "Excellent! Your $skillName proof score has reached the target of $TARGET_SCORE."

            gap <= 10 ->

                "You're very close to the target. Focus on your weakest $skillName area to close the remaining gap."

            gap <= 25 ->

                "You're making good progress in $skillName. Strengthen your evidence and assessment performance."

            else ->

                "There is a significant gap in your $skillName proof. Follow the action plan below step by step."
        }
    }

    // =================================================
    // ACTION PLAN
    // =================================================

    private fun buildActionPlan(
        skillLevel: Int,
        assessment: Int,
        evidence: Int,
        verification: Int,
        skillName: String
    ): String {

        val actions =
            mutableListOf<String>()

        // -----------------------------------------
        // Skill Level
        // -----------------------------------------

        if (skillLevel < 70) {

            actions.add(
                "1. Improve $skillName practical skills\n" +
                        "   Practice coding and build a project related to $skillName."
            )
        }

        // -----------------------------------------
        // Assessment
        // -----------------------------------------

        if (assessment < 70) {

            actions.add(
                "${actions.size + 1}. Improve $skillName assessment performance\n" +
                        "   Revise concepts and retake the assessment."
            )
        }

        // -----------------------------------------
        // Evidence
        // -----------------------------------------

        if (evidence < 70) {

            actions.add(
                "${actions.size + 1}. Add stronger $skillName evidence\n" +
                        "   Submit projects, certificates or other relevant proof."
            )
        }

        // -----------------------------------------
        // Verification
        // -----------------------------------------

        if (verification < 70) {

            actions.add(
                "${actions.size + 1}. Verify your $skillName evidence\n" +
                        "   Use GitHub verification or other verification methods."
            )
        }

        // -----------------------------------------
        // Everything Good
        // -----------------------------------------

        if (actions.isEmpty()) {

            return "✓ Your $skillName skill is well supported.\n\n" +
                    "Keep your assessments and evidence updated to maintain a strong Proof Score."
        }

        return actions.joinToString(
            separator = "\n\n"
        )
    }

    // =================================================
    // NO CURRENT SKILL
    // =================================================

    private fun showNoCurrentSkillState() {

        tvSkillName.text =
            "Select a skill"

        tvCurrentScore.text =
            "0"

        tvTargetScore.text =
            TARGET_SCORE.toString()

        tvGapScore.text =
            TARGET_SCORE.toString()

        tvGapMessage.text =
            "Go to Skills and select the skill you are currently focusing on."

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
            "1. Go to Skills\n\n" +
                    "2. Select your current skill\n\n" +
                    "3. Complete an assessment\n\n" +
                    "4. Add relevant evidence\n\n" +
                    "5. Verify your evidence"
    }

    // =================================================
    // NO SKILL
    // =================================================

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
                    "2. Select it as your current skill\n\n" +
                    "3. Complete an assessment\n\n" +
                    "4. Add relevant evidence\n\n" +
                    "5. Verify your evidence"
    }
}