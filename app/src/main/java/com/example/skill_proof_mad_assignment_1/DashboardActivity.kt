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

        // Proof Score card
        val proofScoreCard =
            findViewById<android.view.View>(R.id.proofScoreCard)

        proofScoreCard.setOnClickListener {

            val intent = Intent(
                this,
                ProofScoreActivity::class.java
            )

            startActivity(intent)
        }

        // Skills
        tvViewSkills.setOnClickListener {

            val intent = Intent(
                this,
                SkillsActivity::class.java
            )

            startActivity(intent)
        }

        // Evidence
        tvViewEvidence.setOnClickListener {

            val intent = Intent(
                this,
                EvidenceActivity::class.java
            )

            startActivity(intent)
        }

        // Assessment
        tvViewAssessment.setOnClickListener {

            val intent = Intent(
                this,
                AssessmentActivity::class.java
            )

            startActivity(intent)
        }

        // Career Readiness
        tvViewCareer.setOnClickListener {

            Toast.makeText(
                this,
                "Career Readiness module coming next",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}