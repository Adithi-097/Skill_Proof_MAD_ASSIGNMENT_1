package com.example.skill_proof_mad_assignment_1

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BadgesActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var badgeManager: BadgeManager
    private lateinit var badgeAdapter: BadgeAdapter

    private lateinit var tvLevel: TextView
    private lateinit var tvProofScore: TextView
    private lateinit var recyclerBadges: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_badges)

        // Initialize database
        databaseHelper = DatabaseHelper(this)

        // Initialize badge manager
        badgeManager = BadgeManager(databaseHelper)

        // Find views
        val btnBack = findViewById<ImageButton>(R.id.btnBack)

        tvLevel = findViewById(R.id.tvLevel)
        tvProofScore = findViewById(R.id.tvProofScore)
        recyclerBadges = findViewById(R.id.recyclerBadges)

        // Back button
        btnBack.setOnClickListener {
            finish()
        }

        // RecyclerView setup
        recyclerBadges.layoutManager =
            LinearLayoutManager(this)

        loadBadges()
    }

    override fun onResume() {
        super.onResume()

        // Refresh badges whenever the screen becomes visible
        if (::badgeManager.isInitialized) {
            loadBadges()
        }
    }

    private fun loadBadges() {

        val skills = databaseHelper.getAllSkills()

        // If user has no skills yet
        if (skills.isEmpty()) {

            tvLevel.text = "Beginner"
            tvProofScore.text = "0 / 100"

        } else {

            // Use the first/current skill
            val skill = skills.first()

            val skillName = skill.name

            val skillLevel = skill.progress

            val assessmentScore =
                databaseHelper.getAssessmentPercentageForSkill(skillName)

            val evidenceCount =
                databaseHelper.getEvidenceCountForSkill(skillName)

            val verifiedEvidenceCount =
                databaseHelper.getVerifiedEvidenceCountForSkill(skillName)

            // Calculate evidence score
            val evidenceScore = when {
                evidenceCount >= 5 -> 100
                evidenceCount == 4 -> 90
                evidenceCount == 3 -> 80
                evidenceCount == 2 -> 65
                evidenceCount == 1 -> 45
                else -> 0
            }

            // Calculate verification score
            val verificationScore =
                if (evidenceCount == 0) {
                    0
                } else {
                    (verifiedEvidenceCount * 100) / evidenceCount
                }

            // Same Proof Score formula used in ProofScoreActivity
            val proofScore =
                (
                        skillLevel * 0.30 +
                                assessmentScore * 0.30 +
                                evidenceScore * 0.25 +
                                verificationScore * 0.15
                        ).toInt()

            tvProofScore.text = "$proofScore / 100"

            // Determine level
            tvLevel.text = getSkillLevel(proofScore)
        }

        // Load badges
        val badges = badgeManager.getBadges()

        badgeAdapter = BadgeAdapter(badges)

        recyclerBadges.adapter = badgeAdapter
    }

    private fun getSkillLevel(score: Int): String {

        return when {
            score >= 90 -> "Expert"
            score >= 75 -> "Proven Skill"
            score >= 60 -> "Competent"
            score >= 40 -> "Developing"
            else -> "Beginner"
        }
    }
}