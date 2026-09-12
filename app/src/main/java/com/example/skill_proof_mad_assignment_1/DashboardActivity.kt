package com.example.skill_proof_mad_assignment_1

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_dashboard)

        val tvViewSkills =
            findViewById<TextView>(R.id.tvViewSkills)

        val tvViewEvidence =
            findViewById<TextView>(R.id.tvViewEvidence)

        val tvViewAssessment =
            findViewById<TextView>(R.id.tvViewAssessment)

        val tvViewCareer =
            findViewById<TextView>(R.id.tvViewCareer)


        tvViewSkills.setOnClickListener {

            val intent = android.content.Intent(
                this,
                SkillsActivity::class.java
            )

            startActivity(intent)
        }


        tvViewEvidence.setOnClickListener {

            val intent = Intent(
                this,
                EvidenceActivity::class.java
            )

            startActivity(intent)
        }


        tvViewAssessment.setOnClickListener {

            Toast.makeText(
                this,
                "Assessment module coming next",
                Toast.LENGTH_SHORT
            ).show()
        }


        tvViewCareer.setOnClickListener {

            Toast.makeText(
                this,
                "Career Readiness module coming next",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
