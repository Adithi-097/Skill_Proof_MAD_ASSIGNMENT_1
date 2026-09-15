package com.example.skill_proof_mad_assignment_1

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class ProfileActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    private lateinit var btnBack: ImageButton
    private lateinit var btnLogout: MaterialButton

    private lateinit var tvProfileName: TextView
    private lateinit var tvProfileEmail: TextView
    private lateinit var tvSkillCount: TextView
    private lateinit var tvEvidenceCount: TextView
    private lateinit var tvVerifiedCount: TextView
    private lateinit var tvAssessmentCount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_profile)

        databaseHelper = DatabaseHelper(this)

        btnBack = findViewById(R.id.btnBack)
        btnLogout = findViewById(R.id.btnLogout)

        tvProfileName = findViewById(R.id.tvProfileName)
        tvProfileEmail = findViewById(R.id.tvProfileEmail)
        tvSkillCount = findViewById(R.id.tvSkillCount)
        tvEvidenceCount = findViewById(R.id.tvEvidenceCount)
        tvVerifiedCount = findViewById(R.id.tvVerifiedCount)
        tvAssessmentCount = findViewById(R.id.tvAssessmentCount)

        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnLogout.setOnClickListener {
            logoutUser()
        }

        loadProfile()
    }

    override fun onResume() {
        super.onResume()
        if (::databaseHelper.isInitialized) {
            loadProfile()
        }
    }

    private fun loadProfile() {

        val preferences =
            getSharedPreferences(
                "SkillProofPrefs",
                MODE_PRIVATE
            )

        val name =
            preferences.getString(
                "user_name",
                "SkillProof User"
            ) ?: "SkillProof User"

        val email =
            preferences.getString(
                "user_email",
                "No email available"
            ) ?: "No email available"

        tvProfileName.text = name
        tvProfileEmail.text = email

        val skills =
            databaseHelper.getAllSkills()

        val evidence =
            databaseHelper.getAllEvidence()

        val verifiedEvidence =
            evidence.count {
                it.status.equals(
                    "Verified",
                    ignoreCase = true
                )
            }

        val assessmentCount =
            skills.count { skill ->
                databaseHelper
                    .getAssessmentPercentageForSkill(
                        skill.name
                    ) > 0
            }

        tvSkillCount.text =
            skills.size.toString()

        tvEvidenceCount.text =
            evidence.size.toString()

        tvVerifiedCount.text =
            verifiedEvidence.toString()

        tvAssessmentCount.text =
            assessmentCount.toString()
    }

    private fun logoutUser() {

        val preferences =
            getSharedPreferences(
                "SkillProofPrefs",
                MODE_PRIVATE
            )

        preferences
            .edit()
            .putBoolean(
                "is_logged_in",
                false
            )
            .apply()

        Toast.makeText(
            this,
            "Logged out successfully.",
            Toast.LENGTH_SHORT
        ).show()

        val intent =
            Intent(
                this,
                LoginActivity::class.java
            )

        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)

        finish()
    }
}