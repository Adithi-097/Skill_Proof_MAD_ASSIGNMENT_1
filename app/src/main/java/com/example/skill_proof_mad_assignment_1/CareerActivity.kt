package com.example.skill_proof_mad_assignment_1

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class CareerActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    private lateinit var tvReadinessScore: TextView
    private lateinit var tvReadinessLabel: TextView
    private lateinit var tvSkillName: TextView
    private lateinit var progressReadiness: android.widget.ProgressBar
    private lateinit var tvStrongSkills: TextView
    private lateinit var tvImproveSkills: TextView
    private lateinit var tvRecommendations: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_career)

        databaseHelper = DatabaseHelper(this)

        // -----------------------------
        // Find Views
        // -----------------------------

        val btnBack =
            findViewById<ImageButton>(R.id.btnBack)

        val btnBadges =
            findViewById<MaterialButton>(R.id.btnBadges)

        tvReadinessScore =
            findViewById(R.id.tvReadinessScore)

        tvReadinessLabel =
            findViewById(R.id.tvReadinessLabel)

        tvSkillName =
            findViewById(R.id.tvSkillName)

        progressReadiness =
            findViewById(R.id.readinessProgress)

        tvStrongSkills =
            findViewById(R.id.tvStrongSkills)

        tvImproveSkills =
            findViewById(R.id.tvImproveSkills)

        tvRecommendations =
            findViewById(R.id.tvRecommendations)

        // -----------------------------
        // Back Button
        // -----------------------------

        btnBack.setOnClickListener {
            finish()
        }

        // -----------------------------
        // Skill Levels & Badges
        // -----------------------------

        btnBadges.setOnClickListener {

            val intent =
                Intent(this, BadgesActivity::class.java)

            startActivity(intent)
        }

        // -----------------------------
        // Load Career Data
        // -----------------------------

        loadCareerData()
    }

    override fun onResume() {
        super.onResume()

        if (::databaseHelper.isInitialized) {
            loadCareerData()
        }
    }

    // =========================================================
    // LOAD CAREER DATA
    // =========================================================

    private fun loadCareerData() {

        val skills =
            databaseHelper.getAllSkills()

        // -----------------------------------------------------
        // No skills added
        // -----------------------------------------------------

        if (skills.isEmpty()) {

            tvSkillName.text = "No skill added yet"

            tvReadinessScore.text = "0"

            tvReadinessLabel.text =
                "Needs Improvement"

            progressReadiness.progress = 0

            tvStrongSkills.text =
                "Add a skill to start building your career profile."

            tvImproveSkills.text =
                "No skill data available yet."

            tvRecommendations.text =
                "1. Add your first skill\n" +
                        "2. Complete an assessment\n" +
                        "3. Add supporting evidence\n" +
                        "4. Verify your evidence"

            return
        }

        // -----------------------------------------------------
        // Use the first/latest skill
        // -----------------------------------------------------

        val skill = skills.first()

        val skillName = skill.name

        val skillLevel =
            skill.progress

        // -----------------------------------------------------
        // Assessment
        // -----------------------------------------------------

        val assessmentScore =
            databaseHelper.getAssessmentPercentageForSkill(
                skillName
            )

        // -----------------------------------------------------
        // Evidence
        // -----------------------------------------------------

        val evidenceCount =
            databaseHelper.getEvidenceCountForSkill(
                skillName
            )

        val verifiedEvidenceCount =
            databaseHelper.getVerifiedEvidenceCountForSkill(
                skillName
            )

        // -----------------------------------------------------
        // Evidence Score
        // -----------------------------------------------------

        val evidenceScore = when {

            evidenceCount >= 5 -> 100

            evidenceCount == 4 -> 90

            evidenceCount == 3 -> 80

            evidenceCount == 2 -> 65

            evidenceCount == 1 -> 45

            else -> 0
        }

        // -----------------------------------------------------
        // Verification Score
        // -----------------------------------------------------

        val verificationScore =
            if (evidenceCount == 0) {

                0

            } else {

                (
                        verifiedEvidenceCount * 100
                        ) / evidenceCount
            }

        // =====================================================
        // PROOF SCORE
        // =====================================================

        val proofScore =
            (
                    skillLevel * 0.30 +
                            assessmentScore * 0.30 +
                            evidenceScore * 0.25 +
                            verificationScore * 0.15
                    ).toInt()

        // =====================================================
        // CAREER READINESS
        // =====================================================
        //
        // Proof Score       = 60%
        // Assessment       = 40%
        //
        // =====================================================

        val careerReadiness =
            (
                    proofScore * 0.60 +
                            assessmentScore * 0.40
                    ).toInt()

        // -----------------------------------------------------
        // Update UI
        // -----------------------------------------------------

        tvSkillName.text =
            "Skill: $skillName"

        tvReadinessScore.text =
            careerReadiness.toString()

        progressReadiness.progress =
            careerReadiness.coerceIn(0, 100)

        // =====================================================
        // READINESS LEVEL
        // =====================================================

        val readinessLevel = when {

            careerReadiness >= 85 ->
                "Placement Ready"

            careerReadiness >= 70 ->
                "Almost Ready"

            careerReadiness >= 50 ->
                "Developing"

            else ->
                "Needs Improvement"
        }

        tvReadinessLabel.text =
            readinessLevel

        // =====================================================
        // STRONG SKILLS
        // =====================================================

        val strongSkillMessage =
            when {

                proofScore >= 85 ->
                    "Excellent proof strength. Your $skillName skill has strong supporting evidence."

                proofScore >= 70 ->
                    "Good proof strength. Your $skillName skill is developing well."

                proofScore >= 50 ->
                    "You have a basic foundation in $skillName. Continue adding proof."

                else ->
                    "Your $skillName skill needs more development and supporting proof."
            }

        tvStrongSkills.text =
            strongSkillMessage

        // =====================================================
        // AREAS TO IMPROVE
        // =====================================================

        val improvementList =
            mutableListOf<String>()

        if (skillLevel < 70) {

            improvementList.add(
                "• Improve skill level (currently $skillLevel%)"
            )
        }

        if (assessmentScore < 70) {

            improvementList.add(
                "• Improve assessment performance (currently $assessmentScore%)"
            )
        }

        if (evidenceCount < 3) {

            improvementList.add(
                "• Add more evidence (projects, certificates or work)"
            )
        }

        if (
            evidenceCount > 0 &&
            verifiedEvidenceCount < evidenceCount
        ) {

            improvementList.add(
                "• Verify your remaining evidence"
            )
        }

        if (improvementList.isEmpty()) {

            improvementList.add(
                "• Keep maintaining and updating your skill proof"
            )
        }

        tvImproveSkills.text =
            improvementList.joinToString("\n")

        // =====================================================
        // RECOMMENDATIONS
        // =====================================================

        val recommendations =
            mutableListOf<String>()

        if (assessmentScore < 70) {

            recommendations.add(
                "1. Retake or improve your $skillName assessment."
            )
        }

        if (evidenceCount < 3) {

            recommendations.add(
                "${recommendations.size + 1}. Add more projects, certificates or other evidence."
            )
        }

        if (
            evidenceCount > 0 &&
            verifiedEvidenceCount < evidenceCount
        ) {

            recommendations.add(
                "${recommendations.size + 1}. Verify your uploaded evidence."
            )
        }

        if (skillLevel < 75) {

            recommendations.add(
                "${recommendations.size + 1}. Increase your practical skill level through projects."
            )
        }

        if (recommendations.isEmpty()) {

            recommendations.add(
                "1. Your profile is strong. Keep adding advanced projects and evidence."
            )
        }

        tvRecommendations.text =
            recommendations.joinToString("\n")
    }
}