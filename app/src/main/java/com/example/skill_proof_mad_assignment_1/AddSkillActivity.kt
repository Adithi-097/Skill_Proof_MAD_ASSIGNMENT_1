package com.example.skill_proof_mad_assignment_1

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class AddSkillActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_skill)

        val btnBack =
            findViewById<ImageButton>(R.id.btnBack)

        val etSkillName =
            findViewById<TextInputEditText>(R.id.etSkillName)

        val actvLevel =
            findViewById<AutoCompleteTextView>(R.id.actvLevel)

        val btnSaveSkill =
            findViewById<MaterialButton>(R.id.btnSaveSkill)

        // Skill levels
        val levels = arrayOf(
            "Beginner",
            "Intermediate",
            "Advanced"
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            levels
        )

        actvLevel.setAdapter(adapter)

        btnBack.setOnClickListener {
            finish()
        }

        btnSaveSkill.setOnClickListener {

            val skillName =
                etSkillName.text.toString().trim()

            val level =
                actvLevel.text.toString().trim()

            if (skillName.isEmpty()) {
                etSkillName.error = "Enter a skill name"
                etSkillName.requestFocus()
                return@setOnClickListener
            }

            if (level.isEmpty()) {
                actvLevel.error = "Select a skill level"
                actvLevel.requestFocus()
                return@setOnClickListener
            }

            val progress = when (level) {

                "Beginner" -> 40

                "Intermediate" -> 70

                "Advanced" -> 90

                else -> 40
            }

            val resultIntent = android.content.Intent()

            resultIntent.putExtra(
                "skill_name",
                skillName
            )

            resultIntent.putExtra(
                "skill_level",
                level
            )

            resultIntent.putExtra(
                "skill_progress",
                progress
            )

            resultIntent.putExtra(
                "proof_status",
                "Not verified"
            )

            setResult(
                RESULT_OK,
                resultIntent
            )

            finish()
        }
    }
}