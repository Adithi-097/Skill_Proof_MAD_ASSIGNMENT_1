package com.example.skill_proof_mad_assignment_1

class BadgeManager(
    private val databaseHelper: DatabaseHelper
) {

    fun getBadges(): List<Badge> {

        val skills =
            databaseHelper.getAllSkills()

        val evidence =
            databaseHelper.getAllEvidence()

        val totalEvidence =
            evidence.size

        val verifiedEvidence =
            evidence.count {
                it.status.equals(
                    "Verified",
                    ignoreCase = true
                )
            }

        val assessments =
            skills.any { skill ->

                databaseHelper
                    .getAssessmentPercentageForSkill(
                        skill.name
                    ) > 0
            }

        val hasProvenSkill =
            skills.any { skill ->

                val assessmentScore =
                    databaseHelper
                        .getAssessmentPercentageForSkill(
                            skill.name
                        )

                val evidenceCount =
                    databaseHelper
                        .getEvidenceCountForSkill(
                            skill.name
                        )

                val verifiedCount =
                    databaseHelper
                        .getVerifiedEvidenceCountForSkill(
                            skill.name
                        )

                val evidenceScore =
                    calculateEvidenceScore(
                        evidenceCount
                    )

                val verificationScore =
                    if (evidenceCount > 0) {
                        (verifiedCount * 100) /
                                evidenceCount
                    } else {
                        0
                    }

                val proofScore =
                    (
                            skill.progress * 0.30 +
                                    assessmentScore * 0.30 +
                                    evidenceScore * 0.25 +
                                    verificationScore * 0.15
                            ).toInt()

                proofScore >= 75
            }

        return listOf(

            Badge(
                id = "first_skill",
                name = "First Skill",
                description =
                    "Add your first skill to SkillProof.",
                icon = "🎯",
                unlocked =
                    skills.isNotEmpty()
            ),

            Badge(
                id = "assessment_completed",
                name = "Assessment Completed",
                description =
                    "Complete at least one skill assessment.",
                icon = "📝",
                unlocked =
                    assessments
            ),

            Badge(
                id = "first_evidence",
                name = "First Evidence",
                description =
                    "Add your first piece of skill evidence.",
                icon = "📁",
                unlocked =
                    totalEvidence >= 1
            ),

            Badge(
                id = "verified_evidence",
                name = "Verified Evidence",
                description =
                    "Get at least one evidence item verified.",
                icon = "✓",
                unlocked =
                    verifiedEvidence >= 1
            ),

            Badge(
                id = "evidence_collector",
                name = "Evidence Collector",
                description =
                    "Collect at least three evidence items.",
                icon = "🏆",
                unlocked =
                    totalEvidence >= 3
            ),

            Badge(
                id = "proven_skill",
                name = "Proven Skill",
                description =
                    "Achieve a Proof Score of 75 or above.",
                icon = "⭐",
                unlocked =
                    hasProvenSkill
            )
        )
    }

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

    fun getUnlockedBadgeCount(): Int {

        return getBadges()
            .count { it.unlocked }
    }

    fun getTotalBadgeCount(): Int {

        return getBadges().size
    }

    fun getBadgeProgress(): Int {

        val total =
            getTotalBadgeCount()

        if (total == 0) {
            return 0
        }

        val unlocked =
            getUnlockedBadgeCount()

        return (
                unlocked * 100
                ) / total
    }
}