package com.example.skill_proof_mad_assignment_1

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SkillGapActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    private lateinit var tvSkillName: TextView
    private lateinit var tvCurrentScore: TextView
    private lateinit var tvTargetScore: TextView
    private lateinit var tvGapScore: TextView

    private lateinit var tvSkillLevelScore: TextView
    private lateinit var tvAssessmentScore: TextView
    private lateinit var tvEvidenceScore: TextView
    private lateinit var tvVerificationScore: TextView

    private lateinit var progressSkillLevel: ProgressBar
    private lateinit var progressAssessment: ProgressBar
    private lateinit var progressEvidence: ProgressBar
    private lateinit var progressVerification: ProgressBar

    private lateinit var tvGapMessage: TextView
    private lateinit var tvActionPlan: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_skill_gap)

        databaseHelper = DatabaseHelper(this)

        // --------------------------------------------------
        // Find Views
        // --------------------------------------------------

        val btnBack =
            findViewById<ImageButton>(R.id.btnBack)

        tvSkillName =
            findViewById(R.id.tvSkillName)

        tvCurrentScore =
            findViewById(R.id.tvCurrentScore)

        tvTargetScore =
            findViewById(R.id.tvTargetScore)

        tvGapScore =
            findViewById(R.id.tvGapScore)

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

        tvGapMessage =
            findViewById(R.id.tvGapMessage)

        tvActionPlan =
            findViewById(R.id.tvActionPlan)

        // --------------------------------------------------
        // Back
        // --------------------------------------------------

        btnBack.setOnClickListener {
            finish()
        }

        // --------------------------------------------------
        // Load Data
        // --------------------------------------------------

        loadSkillGap()
    }

    override fun onResume() {
        super.onResume()

        if (::databaseHelper.isInitialized) {
            loadSkillGap()
        }
    }

    // ======================================================
    // LOAD SKILL GAP
    // ======================================================

    private fun loadSkillGap() {

        val skills =
            databaseHelper.getAllSkills()

        // --------------------------------------------------
        // No skill available
        // --------------------------------------------------

        if (skills.isEmpty()) {

            tvSkillName.text =
                "No skill added"

            tvCurrentScore.text =
                "0"

            tvTargetScore.text =
                "80"

            tvGapScore.text =
                "80"

            tvSkillLevelScore.text =
                "0%"

            tvAssessmentScore.text =
                "0%"

            tvEvidenceScore.text =
                "0%"

            tvVerificationScore.text =
                "0%"

            progressSkillLevel.progress = 0
            progressAssessment.progress = 0
            progressEvidence.progress = 0
            progressVerification.progress = 0

            tvGapMessage.text =
                "Add a skill to start analyzing your skill gap."

            tvActionPlan.text =
                "1. Add your first skill\n" +
                        "2. Complete a skill assessment\n" +
                        "3. Add supporting evidence\n" +
                        "4. Verify your evidence"

            return
        }

        // --------------------------------------------------
        // Current Skill
        // --------------------------------------------------

        val skill =
            skills.first()

        val skillName =
            skill.name

        val skillLevel =
            skill.progress

        // --------------------------------------------------
        // Assessment
        // --------------------------------------------------

        val assessmentScore =
            databaseHelper.getAssessmentPercentageForSkill(
                skillName
            )

        // --------------------------------------------------
        // Evidence
        // --------------------------------------------------

        val evidenceCount =
            databaseHelper.getEvidenceCountForSkill(
                skillName
            )

        val verifiedEvidenceCount =
            databaseHelper.getVerifiedEvidenceCountForSkill(
                skillName
            )

        // --------------------------------------------------
        // Evidence Score
        // --------------------------------------------------

        val evidenceScore = when {

            evidenceCount >= 5 -> 100

            evidenceCount == 4 -> 90

            evidenceCount == 3 -> 80

            evidenceCount == 2 -> 65

            evidenceCount == 1 -> 45

            else -> 0
        }

        // --------------------------------------------------
        // Verification Score
        // --------------------------------------------------

        val verificationScore =
            if (evidenceCount == 0) {

                0

            } else {

                (
                        verifiedEvidenceCount * 100
                        ) / evidenceCount
            }

        // ==================================================
        // PROOF SCORE
        // ==================================================

        val proofScore =
            (
                    skillLevel * 0.30 +
                            assessmentScore * 0.30 +
                            evidenceScore * 0.25 +
                            verificationScore * 0.15
                    ).toInt()

        // ==================================================
        // TARGET
        // ==================================================

        val targetScore = 80

        val gap =
            (targetScore - proofScore)
                .coerceAtLeast(0)

        // --------------------------------------------------
        // Display
        // --------------------------------------------------

        tvSkillName.text =
            "Skill: $skillName"

        tvCurrentScore.text =
            proofScore.toString()

        tvTargetScore.text =
            targetScore.toString()

        tvGapScore.text =
            gap.toString()

        // --------------------------------------------------
        // Component Scores
        // --------------------------------------------------

        tvSkillLevelScore.text =
            "$skillLevel%"

        tvAssessmentScore.text =
            "$assessmentScore%"

        tvEvidenceScore.text =
            "$evidenceScore%"

        tvVerificationScore.text =
            "$verificationScore%"

        progressSkillLevel.progress =
            skillLevel.coerceIn(0, 100)

        progressAssessment.progress =
            assessmentScore.coerceIn(0, 100)

        progressEvidence.progress =
            evidenceScore.coerceIn(0, 100)

        progressVerification.progress =
            verificationScore.coerceIn(0, 100)

        // ==================================================
        // GAP MESSAGE
        // ==================================================

        tvGapMessage.text =
            when {

                proofScore >= 80 ->
                    "Excellent! Your $skillName skill has reached the target readiness level."

                proofScore >= 70 ->
                    "You are close to the target. A few improvements can make your $skillName skill placement-ready."

                proofScore >= 50 ->
                    "Your $skillName skill has a moderate gap. Focus on assessment, evidence and practical work."

                else ->
                    "Your $skillName skill has a significant gap. Start by building stronger evidence and improving your assessment score."
            }

        // ==================================================
        // ACTION PLAN
        // ==================================================

        val actions =
            mutableListOf<String>()

        if (skillLevel < 80) {

            actions.add(
                "Improve practical skill level from $skillLevel% to 80%."
            )
        }

        if (assessmentScore < 70) {

            actions.add(
                "Improve your assessment score from $assessmentScore% to at least 70%."
            )
        }

        if (evidenceCount < 3) {

            actions.add(
                "Add at least ${3 - evidenceCount} more evidence item(s)."
            )
        }

        if (
            evidenceCount > 0 &&
            verifiedEvidenceCount < evidenceCount
        ) {

            actions.add(
                "Verify your remaining evidence."
            )
        }

        if (actions.isEmpty()) {

            actions.add(
                "Your skill proof is strong. Continue adding advanced projects and certifications."
            )
        }

        val formattedActions =
            actions.mapIndexed { index, action ->
                "${index + 1}. $action"
            }

        tvActionPlan.text =
            formattedActions.joinToString("\n")
    }
}