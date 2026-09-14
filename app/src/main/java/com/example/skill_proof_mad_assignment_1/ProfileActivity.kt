package com.example.skill_proof_mad_assignment_1

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    private lateinit var tvUserName: TextView
    private lateinit var tvUserEmail: TextView
    private lateinit var tvSkillCount: TextView
    private lateinit var tvEvidenceCount: TextView
    private lateinit var tvProofScore: TextView
    private lateinit var tvCareerReadiness: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_profile)

        databaseHelper = DatabaseHelper(this)

        val btnBack =
            findViewById<ImageButton>(R.id.btnBack)

        tvUserName =
            findViewById(R.id.tvUserName)

        tvUserEmail =
            findViewById(R.id.tvUserEmail)

        tvSkillCount =
            findViewById(R.id.tvSkillCount)

        tvEvidenceCount =
            findViewById(R.id.tvEvidenceCount)

        tvProofScore =
            findViewById(R.id.tvProofScore)

        tvCareerReadiness =
            findViewById(R.id.tvCareerReadiness)

        btnBack.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()

        loadProfile()
    }

    private fun loadProfile() {

        // ==========================================
        // USER INFORMATION
        // ==========================================

        val preferences =
            getSharedPreferences(
                "SkillProofPrefs",
                MODE_PRIVATE
            )

        val name =
            preferences.getString(
                "user_name",
                "SkillProof User"
            )

        val email =
            preferences.getString(
                "user_email",
                "No email available"
            )

        tvUserName.text = name
        tvUserEmail.text = email

        // ==========================================
        // DATABASE DATA
        // ==========================================

        val skills =
            databaseHelper.getAllSkills()

        val evidence =
            databaseHelper.getAllEvidence()

        tvSkillCount.text =
            skills.size.toString()

        tvEvidenceCount.text =
            evidence.size.toString()

        // ==========================================
        // NO SKILLS
        // ==========================================

        if (skills.isEmpty()) {

            tvProofScore.text = "0"
            tvCareerReadiness.text = "0"

            return
        }

        // ==========================================
        // CURRENT SKILL
        // ==========================================

        val skill =
            skills.first()

        val skillName =
            skill.name

        val skillLevel =
            skill.progress

        // ==========================================
        // ASSESSMENT
        // ==========================================

        val assessmentScore =
            databaseHelper.getAssessmentPercentageForSkill(
                skillName
            )

        // ==========================================
        // EVIDENCE
        // ==========================================

        val evidenceCount =
            databaseHelper.getEvidenceCountForSkill(
                skillName
            )

        val verifiedEvidenceCount =
            databaseHelper.getVerifiedEvidenceCountForSkill(
                skillName
            )

        // ==========================================
        // EVIDENCE SCORE
        // ==========================================

        val evidenceScore =
            when {
                evidenceCount >= 5 -> 100
                evidenceCount == 4 -> 90
                evidenceCount == 3 -> 80
                evidenceCount == 2 -> 65
                evidenceCount == 1 -> 45
                else -> 0
            }

        // ==========================================
        // VERIFICATION SCORE
        // ==========================================

        val verificationScore =
            if (evidenceCount == 0) {
                0
            } else {
                (verifiedEvidenceCount * 100) / evidenceCount
            }

        // ==========================================
        // PROOF SCORE
        // ==========================================

        val proofScore =
            (
                    skillLevel * 0.30 +
                            assessmentScore * 0.30 +
                            evidenceScore * 0.25 +
                            verificationScore * 0.15
                    ).toInt()

        // ==========================================
        // CAREER READINESS
        // ==========================================

        val careerReadiness =
            (
                    proofScore * 0.60 +
                            assessmentScore * 0.40
                    ).toInt()

        // ==========================================
        // DISPLAY
        // ==========================================

        tvProofScore.text =
            proofScore.toString()

        tvCareerReadiness.text =
            careerReadiness.toString()
    }
}