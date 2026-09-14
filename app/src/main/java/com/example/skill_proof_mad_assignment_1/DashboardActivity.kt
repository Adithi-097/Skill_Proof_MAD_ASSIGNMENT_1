package com.example.skill_proof_mad_assignment_1

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_dashboard)

        // ------------------------------------------
        // FIND VIEWS
        // ------------------------------------------

        val tvViewSkills =
            findViewById<TextView>(R.id.tvViewSkills)

        val tvViewEvidence =
            findViewById<TextView>(R.id.tvViewEvidence)

        val tvViewAssessment =
            findViewById<TextView>(R.id.tvViewAssessment)

        val tvViewCareer =
            findViewById<TextView>(R.id.tvViewCareer)

        val tvProfile =
            findViewById<TextView>(R.id.tvProfile)

        // ------------------------------------------
        // PROOF SCORE
        // ------------------------------------------

        val proofScoreCard =
            findViewById<android.view.View>(
                R.id.proofScoreCard
            )

        proofScoreCard.setOnClickListener {

            val intent =
                Intent(
                    this,
                    ProofScoreActivity::class.java
                )

            startActivity(intent)
        }

        // ------------------------------------------
        // SKILLS
        // ------------------------------------------

        tvViewSkills.setOnClickListener {

            val intent =
                Intent(
                    this,
                    SkillsActivity::class.java
                )

            startActivity(intent)
        }

        // ------------------------------------------
        // EVIDENCE
        // ------------------------------------------

        tvViewEvidence.setOnClickListener {

            val intent =
                Intent(
                    this,
                    EvidenceActivity::class.java
                )

            startActivity(intent)
        }

        // ------------------------------------------
        // ASSESSMENT
        // ------------------------------------------

        tvViewAssessment.setOnClickListener {

            val intent =
                Intent(
                    this,
                    AssessmentActivity::class.java
                )

            startActivity(intent)
        }

        // ------------------------------------------
        // CAREER READINESS
        // ------------------------------------------

        tvViewCareer.setOnClickListener {

            val intent =
                Intent(
                    this,
                    CareerActivity::class.java
                )

            startActivity(intent)
        }

        // ------------------------------------------
        // PROFILE
        // ------------------------------------------

        tvProfile.setOnClickListener {

            val intent =
                Intent(
                    this,
                    ProfileActivity::class.java
                )

            startActivity(intent)
        }
    }
}