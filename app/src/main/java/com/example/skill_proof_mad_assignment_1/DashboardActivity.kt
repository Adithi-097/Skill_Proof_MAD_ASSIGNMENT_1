package com.example.skill_proof_mad_assignment_1

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    private lateinit var tvSkillCount: TextView
    private lateinit var tvEvidenceCount: TextView
    private lateinit var tvProofScore: TextView
    private lateinit var tvCareerScore: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_dashboard)

        databaseHelper = DatabaseHelper(this)

        // ==========================================
        // FIND VIEWS
        // ==========================================

        tvSkillCount =
            findViewById(R.id.tvSkillCount)

        tvEvidenceCount =
            findViewById(R.id.tvEvidenceCount)

        tvProofScore =
            findViewById(R.id.tvProofScore)

        tvCareerScore =
            findViewById(R.id.tvCareerScore)

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

        val proofScoreCard =
            findViewById<android.view.View>(
                R.id.proofScoreCard
            )

        // ==========================================
        // NAVIGATION
        // ==========================================

        proofScoreCard.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    ProofScoreActivity::class.java
                )
            )
        }

        tvViewSkills.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    SkillsActivity::class.java
                )
            )
        }

        tvViewEvidence.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    EvidenceActivity::class.java
                )
            )
        }

        tvViewAssessment.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    AssessmentActivity::class.java
                )
            )
        }

        tvViewCareer.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    CareerActivity::class.java
                )
            )
        }

        tvProfile.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    ProfileActivity::class.java
                )
            )
        }
    }

    override fun onResume() {
        super.onResume()

        loadDashboardData()
    }

    // ==========================================
    // LOAD DASHBOARD DATA
    // ==========================================

    private fun loadDashboardData() {

        val skills =
            databaseHelper.getAllSkills()

        val evidence =
            databaseHelper.getAllEvidence()

        // ==========================================
        // SKILL COUNT
        // ==========================================

        tvSkillCount.text =
            skills.size.toString()

        // ==========================================
        // EVIDENCE COUNT
        // ==========================================

        tvEvidenceCount.text =
            evidence.size.toString()

        // ==========================================
        // PROOF SCORE
        // ==========================================

        if (skills.isEmpty()) {

            tvProofScore.text =
                "0"

            tvCareerScore.text =
                "0"

            return
        }

        val selectedSkill =
            skills.first()

        val skillLevel =
            selectedSkill.progress

        val assessmentScore =
            databaseHelper.getAssessmentPercentageForSkill(
                selectedSkill.name
            )

        val evidenceCount =
            databaseHelper.getEvidenceCountForSkill(
                selectedSkill.name
            )

        val verifiedEvidenceCount =
            databaseHelper.getVerifiedEvidenceCountForSkill(
                selectedSkill.name
            )

        val evidenceScore =
            when {

                evidenceCount >= 5 -> 100
                evidenceCount == 4 -> 90
                evidenceCount == 3 -> 80
                evidenceCount == 2 -> 65
                evidenceCount == 1 -> 45
                else -> 0
            }

        val verificationScore =
            if (evidenceCount == 0) {
                0
            } else {
                (verifiedEvidenceCount * 100) /
                        evidenceCount
            }

        val proofScore =
            (
                    skillLevel * 0.30 +
                            assessmentScore * 0.30 +
                            evidenceScore * 0.25 +
                            verificationScore * 0.15
                    ).toInt()

        tvProofScore.text =
            proofScore.toString()

        // ==========================================
        // CAREER READINESS
        // ==========================================

        val careerReadiness =
            (
                    proofScore * 0.60 +
                            assessmentScore * 0.40
                    ).toInt()

        tvCareerScore.text =
            careerReadiness.toString()
    }
}