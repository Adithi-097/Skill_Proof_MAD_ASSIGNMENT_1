package com.example.skill_proof_mad_assignment_1

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class AddEvidenceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_evidence)

        val btnBack =
            findViewById<ImageButton>(R.id.btnBack)

        val etTitle =
            findViewById<TextInputEditText>(R.id.etTitle)

        val actvSkill =
            findViewById<AutoCompleteTextView>(R.id.actvSkill)

        val actvType =
            findViewById<AutoCompleteTextView>(R.id.actvType)

        val etLink =
            findViewById<TextInputEditText>(R.id.etLink)

        val btnSaveEvidence =
            findViewById<MaterialButton>(R.id.btnSaveEvidence)

        val skills = arrayOf(
            "Java",
            "Kotlin",
            "Python",
            "Machine Learning"
        )

        val types = arrayOf(
            "Project",
            "Certificate",
            "GitHub Repository",
            "Other"
        )

        actvSkill.setAdapter(
            ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line,
                skills
            )
        )

        actvType.setAdapter(
            ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line,
                types
            )
        )

        btnBack.setOnClickListener {
            finish()
        }

        btnSaveEvidence.setOnClickListener {

            val title =
                etTitle.text.toString().trim()

            val skill =
                actvSkill.text.toString().trim()

            val type =
                actvType.text.toString().trim()

            val link =
                etLink.text.toString().trim()

            if (title.isEmpty()) {
                etTitle.error = "Enter evidence title"
                etTitle.requestFocus()
                return@setOnClickListener
            }

            if (skill.isEmpty()) {
                actvSkill.error = "Select a skill"
                actvSkill.requestFocus()
                return@setOnClickListener
            }

            if (type.isEmpty()) {
                actvType.error = "Select evidence type"
                actvType.requestFocus()
                return@setOnClickListener
            }

            if (link.isEmpty()) {
                etLink.error = "Enter a link"
                etLink.requestFocus()
                return@setOnClickListener
            }

            val resultIntent = Intent()

            resultIntent.putExtra(
                "evidence_title",
                title
            )

            resultIntent.putExtra(
                "evidence_skill",
                skill
            )

            resultIntent.putExtra(
                "evidence_type",
                type
            )

            resultIntent.putExtra(
                "evidence_link",
                link
            )

            resultIntent.putExtra(
                "evidence_status",
                "Pending"
            )

            setResult(
                RESULT_OK,
                resultIntent
            )

            finish()
        }
    }
}