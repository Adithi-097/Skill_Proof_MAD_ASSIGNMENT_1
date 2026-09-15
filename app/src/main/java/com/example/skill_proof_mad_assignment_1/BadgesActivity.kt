package com.example.skill_proof_mad_assignment_1

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BadgesActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    private lateinit var btnBack: ImageButton

    private lateinit var tvUnlockedCount: TextView
    private lateinit var tvTotalCount: TextView
    private lateinit var tvBadgeMessage: TextView

    private lateinit var recyclerBadges: RecyclerView

    private lateinit var badgeAdapter: BadgeAdapter

    private val badgeList =
        mutableListOf<Badge>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_badges
        )

        databaseHelper =
            DatabaseHelper(this)

        // -----------------------------------------
        // Find Views
        // -----------------------------------------

        btnBack =
            findViewById(R.id.btnBack)

        tvUnlockedCount =
            findViewById(R.id.tvUnlockedCount)

        tvTotalCount =
            findViewById(R.id.tvTotalCount)

        tvBadgeMessage =
            findViewById(R.id.tvBadgeMessage)

        recyclerBadges =
            findViewById(R.id.recyclerBadges)

        // -----------------------------------------
        // Back
        // -----------------------------------------

        btnBack.setOnClickListener {

            onBackPressedDispatcher
                .onBackPressed()
        }

        // -----------------------------------------
        // RecyclerView
        // -----------------------------------------

        badgeAdapter =
            BadgeAdapter(badgeList)

        recyclerBadges.layoutManager =
            LinearLayoutManager(this)

        recyclerBadges.adapter =
            badgeAdapter

        recyclerBadges.setHasFixedSize(true)

        loadBadges()
    }

    override fun onResume() {
        super.onResume()

        loadBadges()
    }

    // =============================================
    // LOAD BADGES
    // =============================================

    private fun loadBadges() {

        badgeList.clear()

        val skills =
            databaseHelper.getAllSkills()

        val evidence =
            databaseHelper.getAllEvidence()

        val assessmentPercentage =
            databaseHelper.getLatestAssessmentPercentage()

        val verifiedEvidenceCount =
            evidence.count {
                it.status.equals(
                    "Verified",
                    ignoreCase = true
                )
            }

        val totalEvidenceCount =
            evidence.size

        // -----------------------------------------
        // Calculate Proof Score
        // -----------------------------------------

        val proofScore =
            calculateProofScore()

        // -----------------------------------------
        // Badge 1
        // -----------------------------------------

        badgeList.add(
            Badge(
                id = "first_skill",
                name = "First Skill",
                description =
                    "Add your first skill to SkillProof.",
                icon = "🏁",
                unlocked =
                    skills.isNotEmpty()
            )
        )

        // -----------------------------------------
        // Badge 2
        // -----------------------------------------

        badgeList.add(
            Badge(
                id = "assessment_completed",
                name = "Assessment Completed",
                description =
                    "Complete at least one skill assessment.",
                icon = "📝",
                unlocked =
                    assessmentPercentage > 0
            )
        )

        // -----------------------------------------
        // Badge 3
        // -----------------------------------------

        badgeList.add(
            Badge(
                id = "first_evidence",
                name = "First Evidence",
                description =
                    "Add your first piece of skill evidence.",
                icon = "📎",
                unlocked =
                    totalEvidenceCount >= 1
            )
        )

        // -----------------------------------------
        // Badge 4
        // -----------------------------------------

        badgeList.add(
            Badge(
                id = "verified_evidence",
                name = "Verified Evidence",
                description =
                    "Get at least one evidence item verified.",
                icon = "✅",
                unlocked =
                    verifiedEvidenceCount >= 1
            )
        )

        // -----------------------------------------
        // Badge 5
        // -----------------------------------------

        badgeList.add(
            Badge(
                id = "evidence_collector",
                name = "Evidence Collector",
                description =
                    "Collect at least three pieces of evidence.",
                icon = "📚",
                unlocked =
                    totalEvidenceCount >= 3
            )
        )

        // -----------------------------------------
        // Badge 6
        // -----------------------------------------

        badgeList.add(
            Badge(
                id = "proven_skill",
                name = "Proven Skill",
                description =
                    "Reach a Proof Score of 75 or higher.",
                icon = "🏆",
                unlocked =
                    proofScore >= 75
            )
        )

        // -----------------------------------------
        // Update UI
        // -----------------------------------------

        val unlockedCount =
            badgeList.count {
                it.unlocked
            }

        tvUnlockedCount.text =
            unlockedCount.toString()

        tvTotalCount.text =
            badgeList.size.toString()

        tvBadgeMessage.text =
            getBadgeMessage(
                unlockedCount
            )

        badgeAdapter.notifyDataSetChanged()
    }

    // =============================================
    // PROOF SCORE
    // =============================================

    private fun calculateProofScore(): Int {

        val skills =
            databaseHelper.getAllSkills()

        if (skills.isEmpty()) {
            return 0
        }

        val selectedSkill =
            skills.first()

        val skillName =
            selectedSkill.name

        // -----------------------------------------
        // Skill Level
        // -----------------------------------------

        val skillLevelScore =
            selectedSkill.progress
                .coerceIn(0, 100)

        // -----------------------------------------
        // Assessment
        // -----------------------------------------

        val assessmentScore =
            databaseHelper
                .getAssessmentPercentageForSkill(
                    skillName
                )
                .coerceIn(0, 100)

        // -----------------------------------------
        // Evidence
        // -----------------------------------------

        val evidenceCount =
            databaseHelper
                .getEvidenceCountForSkill(
                    skillName
                )

        val evidenceScore =
            calculateEvidenceScore(
                evidenceCount
            )

        // -----------------------------------------
        // Verification
        // -----------------------------------------

        val verifiedEvidenceCount =
            databaseHelper
                .getVerifiedEvidenceCountForSkill(
                    skillName
                )

        val verificationScore =
            if (evidenceCount > 0) {

                (
                        verifiedEvidenceCount * 100
                        ) / evidenceCount

            } else {
                0
            }

        // -----------------------------------------
        // Weighted Proof Score
        // -----------------------------------------

        return (
                skillLevelScore * 0.30 +
                        assessmentScore * 0.30 +
                        evidenceScore * 0.25 +
                        verificationScore * 0.15
                ).toInt()
            .coerceIn(0, 100)
    }

    // =============================================
    // EVIDENCE SCORE
    // =============================================

    private fun calculateEvidenceScore(
        evidenceCount: Int
    ): Int {

        return when {

            evidenceCount <= 0 ->
                0

            evidenceCount == 1 ->
                45

            evidenceCount == 2 ->
                65

            evidenceCount == 3 ->
                80

            evidenceCount == 4 ->
                90

            else ->
                100
        }
    }

    // =============================================
    // BADGE MESSAGE
    // =============================================

    private fun getBadgeMessage(
        unlockedCount: Int
    ): String {

        return when {

            unlockedCount == 0 ->
                "Start building your skill proof to unlock your first badge."

            unlockedCount == 1 ->
                "Great start! Keep building your proof."

            unlockedCount < 4 ->
                "Nice progress! Complete more activities to unlock more badges."

            unlockedCount < 6 ->
                "Excellent progress! You're building a strong proof profile."

            else ->
                "🏆 Amazing! You have unlocked every SkillProof badge."
        }
    }
}