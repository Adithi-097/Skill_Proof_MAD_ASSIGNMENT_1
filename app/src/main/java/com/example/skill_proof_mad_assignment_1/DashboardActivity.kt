package com.example.skill_proof_mad_assignment_1

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView

class DashboardActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    private lateinit var tvGoodMorning: TextView
    private lateinit var tvUserName: TextView
    private lateinit var tvProofScore: TextView
    private lateinit var tvProofMessage: TextView
    private lateinit var tvProofHint: TextView
    private lateinit var proofProgress: android.widget.ProgressBar

    private lateinit var tvProgressTitle: TextView

    private lateinit var skillsCard: MaterialCardView
    private lateinit var evidenceCard: MaterialCardView
    private lateinit var assessmentCard: MaterialCardView
    private lateinit var careerCard: MaterialCardView
    private lateinit var proofScoreCard: MaterialCardView
    private lateinit var tvProfile: MaterialCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_dashboard
        )

        databaseHelper =
            DatabaseHelper(this)

        // =========================================
        // FIND VIEWS
        // =========================================

        tvGoodMorning =
            findViewById(R.id.tvGoodMorning)

        tvUserName =
            findViewById(R.id.tvUserName)

        tvProfile =
            findViewById(R.id.tvProfile)

        proofScoreCard =
            findViewById(R.id.proofScoreCard)

        tvProofScore =
            findViewById(R.id.tvProofScore)

        tvProofMessage =
            findViewById(R.id.tvProofMessage)

        proofProgress =
            findViewById(R.id.proofProgress)

        tvProofHint =
            findViewById(R.id.tvProofHint)

        tvProgressTitle =
            findViewById(R.id.tvProgressTitle)

        skillsCard =
            findViewById(R.id.skillsCard)

        evidenceCard =
            findViewById(R.id.evidenceCard)

        assessmentCard =
            findViewById(R.id.assessmentCard)

        careerCard =
            findViewById(R.id.careerCard)

        // =========================================
        // NAVIGATION
        // =========================================

        skillsCard.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    SkillsActivity::class.java
                )
            )
        }

        evidenceCard.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    EvidenceActivity::class.java
                )
            )
        }

        assessmentCard.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    AssessmentActivity::class.java
                )
            )
        }

        careerCard.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    CareerActivity::class.java
                )
            )
        }

        proofScoreCard.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    ProofScoreActivity::class.java
                )
            )
        }

        tvProfile.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    ProfileActivity::class.java
                )
            )
        }

        // =========================================
        // LOAD DASHBOARD
        // =========================================

        loadDashboard()
    }

    override fun onResume() {
        super.onResume()

        loadDashboard()
    }

    // =============================================
    // LOAD DASHBOARD DATA
    // =============================================

    private fun loadDashboard() {

        loadUserInformation()

        calculateAndDisplayProofScore()
    }

    // =============================================
    // USER INFORMATION
    // =============================================

    private fun loadUserInformation() {

        val sharedPreferences =
            getSharedPreferences(
                "SkillProofPrefs",
                MODE_PRIVATE
            )

        val name =
            sharedPreferences.getString(
                "user_name",
                "User"
            ) ?: "User"

        tvUserName.text =
            name

        tvGoodMorning.text =
            getGreeting(name)
    }

    // =============================================
    // GREETING
    // =============================================

    private fun getGreeting(
        name: String
    ): String {

        val hour =
            java.util.Calendar
                .getInstance()
                .get(java.util.Calendar.HOUR_OF_DAY)

        return when {

            hour < 12 ->
                "Good Morning,"

            hour < 17 ->
                "Good Afternoon,"

            else ->
                "Good Evening,"
        }
    }

    // =============================================
    // PROOF SCORE
    // =============================================

    private fun calculateAndDisplayProofScore() {

        val skills =
            databaseHelper.getAllSkills()

        // -----------------------------------------
        // No skill
        // -----------------------------------------

        if (skills.isEmpty()) {

            showEmptyProofState()

            return
        }

        // -----------------------------------------
        // Current skill
        // -----------------------------------------

        val selectedSkill =
            skills.first()

        val skillName =
            selectedSkill.name

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
        // Weighted Proof Score
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
        // Display
        // -----------------------------------------

        tvProofScore.text =
            proofScore.toString()

        proofProgress.progress =
            proofScore

        tvProgressTitle.text =
            "Progress for $skillName"

        tvProofMessage.text =
            getProofMessage(proofScore)

        tvProofHint.text =
            getProofHint(
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
    // PROOF MESSAGE
    // =============================================

    private fun getProofMessage(
        score: Int
    ): String {

        return when {

            score >= 90 ->
                "Excellent proof! Your skill is strongly supported."

            score >= 75 ->
                "Great work! Your skill has strong supporting proof."

            score >= 60 ->
                "Good progress. Strengthen your proof to become more credible."

            score >= 40 ->
                "You're getting started. Add more evidence and complete assessments."

            else ->
                "Start building your proof with skills, assessments and evidence."
        }
    }

    // =============================================
    // PROOF HINT
    // =============================================

    private fun getProofHint(
        skillLevel: Int,
        assessment: Int,
        evidence: Int,
        verification: Int
    ): String {

        val weakest =
            minOf(
                skillLevel,
                assessment,
                evidence,
                verification
            )

        return when {

            weakest == skillLevel ->
                "Tip: Improve your practical skill level."

            weakest == assessment ->
                "Tip: Complete or improve your assessment."

            weakest == evidence ->
                "Tip: Add more projects, certificates or evidence."

            else ->
                "Tip: Verify more of your submitted evidence."
        }
    }

    // =============================================
    // EMPTY STATE
    // =============================================

    private fun showEmptyProofState() {

        tvProofScore.text =
            "0"

        proofProgress.progress =
            0

        tvProgressTitle.text =
            "Start Your Skill Proof"

        tvProofMessage.text =
            "You haven't added a skill yet."

        tvProofHint.text =
            "Tip: Add your first skill to start building your Proof Score."
    }
}