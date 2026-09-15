package com.example.skill_proof_mad_assignment_1

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class AddSkillActivity :  BaseActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    private lateinit var btnBack: ImageButton
    private lateinit var btnSaveSkill: MaterialButton

    private lateinit var etSkillName: TextInputEditText
    private lateinit var etSkillLevel: TextInputEditText
    private lateinit var etSkillProgress: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_skill)

        // -----------------------------------------
        // Database
        // -----------------------------------------

        databaseHelper = DatabaseHelper(this)

        // -----------------------------------------
        // Find Views
        // -----------------------------------------

        btnBack =
            findViewById(R.id.btnBack)

        btnSaveSkill =
            findViewById(R.id.btnSaveSkill)

        etSkillName =
            findViewById(R.id.etSkillName)

        etSkillLevel =
            findViewById(R.id.etSkillLevel)

        etSkillProgress =
            findViewById(R.id.etSkillProgress)

        // -----------------------------------------
        // Back Button
        // -----------------------------------------

        btnBack.setOnClickListener {

            onBackPressedDispatcher.onBackPressed()
        }

        // -----------------------------------------
        // Save Skill
        // -----------------------------------------

        btnSaveSkill.setOnClickListener {

            saveSkill()
        }
    }

    // ---------------------------------------------
    // Save Skill To Database
    // ---------------------------------------------

    private fun saveSkill() {

        val skillName =
            etSkillName.text
                .toString()
                .trim()

        val skillLevel =
            etSkillLevel.text
                .toString()
                .trim()

        val progressText =
            etSkillProgress.text
                .toString()
                .trim()

        // -----------------------------------------
        // Skill Name Validation
        // -----------------------------------------

        if (skillName.isEmpty()) {

            etSkillName.error =
                "Enter a skill name"

            etSkillName.requestFocus()

            return
        }

        // -----------------------------------------
        // Skill Level Validation
        // -----------------------------------------

        if (skillLevel.isEmpty()) {

            etSkillLevel.error =
                "Enter your skill level"

            etSkillLevel.requestFocus()

            return
        }

        // -----------------------------------------
        // Progress Validation
        // -----------------------------------------

        if (progressText.isEmpty()) {

            etSkillProgress.error =
                "Enter your skill progress"

            etSkillProgress.requestFocus()

            return
        }

        val progress =
            progressText.toIntOrNull()

        if (progress == null) {

            etSkillProgress.error =
                "Enter a valid number"

            etSkillProgress.requestFocus()

            return
        }

        if (progress < 0 || progress > 100) {

            etSkillProgress.error =
                "Progress must be between 0 and 100"

            etSkillProgress.requestFocus()

            return
        }

        // -----------------------------------------
        // Create Skill
        // -----------------------------------------

        val skill =
            Skill(
                name = skillName,
                level = skillLevel,
                progress = progress,
                proofStatus = "Not verified"
            )

        // -----------------------------------------
        // Insert Into Database
        // -----------------------------------------

        databaseHelper.insertSkill(skill)

        // -----------------------------------------
        // Success Message
        // -----------------------------------------

        Toast.makeText(
            this,
            "Skill added successfully!",
            Toast.LENGTH_SHORT
        ).show()

        // -----------------------------------------
        // Return To Skills Screen
        // -----------------------------------------

        finish()
    }
}