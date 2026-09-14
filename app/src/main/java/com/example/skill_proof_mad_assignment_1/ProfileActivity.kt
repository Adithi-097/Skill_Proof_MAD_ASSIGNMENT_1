package com.example.skill_proof_mad_assignment_1

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_profile)

        databaseHelper = DatabaseHelper(this)

        // ------------------------------------------
        // FIND VIEWS
        // ------------------------------------------

        val btnBack =
            findViewById<ImageButton>(R.id.btnBack)

        val tvUserName =
            findViewById<TextView>(R.id.tvUserName)

        val tvUserEmail =
            findViewById<TextView>(R.id.tvUserEmail)

        val tvSkillCount =
            findViewById<TextView>(R.id.tvSkillCount)

        val tvEvidenceCount =
            findViewById<TextView>(R.id.tvEvidenceCount)

        val tvProofScore =
            findViewById<TextView>(R.id.tvProofScore)

        val tvCareerReadiness =
            findViewById<TextView>(R.id.tvCareerReadiness)

        // ------------------------------------------
        // BACK BUTTON
        // ------------------------------------------

        btnBack.setOnClickListener {
            finish()
        }

        // ------------------------------------------
        // USER INFORMATION
        // ------------------------------------------

        val sharedPreferences =
            getSharedPreferences(
                "SkillProofPrefs",
                MODE_PRIVATE
            )

        val userName =
            sharedPreferences.getString(
                "user_name",
                "SkillProof User"
            )

        val userEmail =
            sharedPreferences.getString(
                "user_email",
                "user@example.com"
            )

        tvUserName.text =
            userName

        tvUserEmail.text =
            userEmail

        // ------------------------------------------
        // GET DATABASE DATA
        // ------------------------------------------

        val skills =
            databaseHelper.getAllSkills()

        val evidence =
            databaseHelper.getAllEvidence()

        val skillCount =
            skills.size

        val evidenceCount =
            evidence.size

        val proofScore =
            calculateProofScore()

        val assessmentScore =
            databaseHelper.getLatestAssessmentPercentage()

        val careerReadiness =
            (
                    proofScore * 0.60 +
                            assessmentScore * 0.40
                    ).toInt()

        // ------------------------------------------
        // DISPLAY SUMMARY
        // ------------------------------------------

        tvSkillCount.text =
            skillCount.toString()

        tvEvidenceCount.text =
            evidenceCount.toString()

        tvProofScore.text =
            proofScore.toString()

        tvCareerReadiness.text =
            "$careerReadiness%"
    }

    // ------------------------------------------
    // CALCULATE PROOF SCORE
    // ------------------------------------------

    private fun calculateProofScore(): Int {

        val skillLevel =
            databaseHelper.getAverageSkillProgress()

        val assessmentScore =
            databaseHelper.getLatestAssessmentPercentage()

        val evidenceScore =
            databaseHelper.getEvidenceScore()

        val verifiedScore =
            databaseHelper.getVerificationScore()

        return (
                skillLevel * 0.30 +
                        assessmentScore * 0.30 +
                        evidenceScore * 0.25 +
                        verifiedScore * 0.15
                ).toInt()
    }
}