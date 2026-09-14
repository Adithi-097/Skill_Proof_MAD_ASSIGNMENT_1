package com.example.skill_proof_mad_assignment_1

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CareerActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    private lateinit var tvReadinessScore: TextView
    private lateinit var tvReadinessLevel: TextView
    private lateinit var tvSkillName: TextView
    private lateinit var progressReadiness: ProgressBar

    private lateinit var tvStrongSkill: TextView
    private lateinit var tvImproveSkill: TextView
    private lateinit var tvRecommendations: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_career)

        databaseHelper = DatabaseHelper(this)

        val btnBack =
            findViewById<ImageButton>(R.id.btnBack)

        tvReadinessScore =
            findViewById(R.id.tvReadinessScore)

        tvReadinessLevel =
            findViewById(R.id.tvReadinessLabel)

        tvSkillName =
            findViewById(R.id.tvSkillName)

        progressReadiness =
            findViewById(R.id.readinessProgress)

        tvStrongSkill =
            findViewById(R.id.tvStrongSkills)

        tvImproveSkill =
            findViewById(R.id.tvImproveSkills)

        tvRecommendations =
            findViewById(R.id.tvRecommendations)

        btnBack.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        loadCareerReadiness()
    }

    private fun loadCareerReadiness() {

        val skills = databaseHelper.getAllSkills()

        // No skills available
        if (skills.isEmpty()) {

            tvSkillName.text = "No skill added"
            tvReadinessScore.text = "0"
            tvReadinessLevel.text = "Start building your skills"
            progressReadiness.progress = 0

            tvStrongSkill.text = "No strong skill yet."
            tvImproveSkill.text = "Add a skill to begin."

            tvRecommendations.text =
                "Add your first skill, complete an assessment, and upload evidence."

            return
        }

        // Get first skill
        val skill = skills[0]

        // Display skill name
        tvSkillName.text = skill.name

        // Skill progress
        val skillLevel = skill.progress

        // Assessment
        val assessmentScore =
            databaseHelper.getAssessmentPercentageForSkill(
                skill.name
            )

        // Evidence
        val evidenceCount =
            databaseHelper.getEvidenceCountForSkill(
                skill.name
            )

        val verifiedEvidenceCount =
            databaseHelper.getVerifiedEvidenceCountForSkill(
                skill.name
            )

        // Evidence score
        val evidenceScore = when {
            evidenceCount >= 5 -> 100
            evidenceCount == 4 -> 90
            evidenceCount == 3 -> 80
            evidenceCount == 2 -> 65
            evidenceCount == 1 -> 45
            else -> 0
        }

        // Verification score
        val verificationScore =
            if (evidenceCount == 0) {
                0
            } else {
                (verifiedEvidenceCount * 100) / evidenceCount
            }

        // Proof score
        val proofScore = (
                skillLevel * 0.30 +
                        assessmentScore * 0.30 +
                        evidenceScore * 0.25 +
                        verificationScore * 0.15
                ).toInt()

        // Career readiness
        val readinessScore = (
                proofScore * 0.60 +
                        assessmentScore * 0.40
                ).toInt()

        // Display readiness
        tvReadinessScore.text =
            readinessScore.toString()

        progressReadiness.progress =
            readinessScore

        tvReadinessLevel.text =
            getReadinessLevel(readinessScore)

        // Strong skill
        if (skillLevel >= 70) {

            tvStrongSkill.text =
                "${skill.name} — Strong"

        } else {

            tvStrongSkill.text =
                "${skill.name} — Developing"
        }

        // Improvement area
        if (assessmentScore < 70) {

            tvImproveSkill.text =
                "Assessment performance needs improvement."

        } else if (evidenceCount < 2) {

            tvImproveSkill.text =
                "Add more evidence for ${skill.name}."

        } else if (verifiedEvidenceCount < evidenceCount) {

            tvImproveSkill.text =
                "Verify your pending evidence."

        } else {

            tvImproveSkill.text =
                "${skill.name} is well supported."
        }

        // Recommendations
        tvRecommendations.text =
            generateRecommendations(
                skillLevel,
                assessmentScore,
                evidenceCount,
                verifiedEvidenceCount
            )
    }

    private fun getReadinessLevel(score: Int): String {

        return when {

            score >= 85 ->
                "Placement Ready"

            score >= 70 ->
                "Almost Ready"

            score >= 50 ->
                "Developing"

            else ->
                "Needs Improvement"
        }
    }

    private fun generateRecommendations(
        skillLevel: Int,
        assessmentScore: Int,
        evidenceCount: Int,
        verifiedEvidenceCount: Int
    ): String {

        val recommendations =
            mutableListOf<String>()

        if (skillLevel < 70) {

            recommendations.add(
                "• Improve your practical skill level through projects and practice."
            )
        }

        if (assessmentScore < 70) {

            recommendations.add(
                "• Retake the assessment and improve your theoretical understanding."
            )
        }

        if (evidenceCount < 2) {

            recommendations.add(
                "• Add at least one more project, certificate, or GitHub evidence."
            )
        }

        if (
            evidenceCount > 0 &&
            verifiedEvidenceCount < evidenceCount
        ) {

            recommendations.add(
                "• Verify your pending evidence to increase trust in your profile."
            )
        }

        if (recommendations.isEmpty()) {

            return "Excellent progress! Your skill has strong evidence, assessment performance, and verification."
        }

        return recommendations.joinToString(
            separator = "\n\n"
        )
    }
}
