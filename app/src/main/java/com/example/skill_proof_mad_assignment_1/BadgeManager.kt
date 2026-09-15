package com.example.skill_proof_mad_assignment_1

class BadgeManager(
    private val databaseHelper: DatabaseHelper
) {

    fun getBadges(): List<Badge> {

        val skills = databaseHelper.getAllSkills()
        val evidence = databaseHelper.getAllEvidence()

        val badges = mutableListOf<Badge>()

        // 1. First Skill Badge
        badges.add(
            Badge(
                id = "first_skill",
                name = "First Skill",
                description = "Add your first skill to SkillProof",
                icon = "🌱",
                unlocked = skills.isNotEmpty()
            )
        )

        // 2. Assessment Completed Badge
        val assessmentCompleted =
            databaseHelper.getLatestAssessmentPercentage() > 0

        badges.add(
            Badge(
                id = "assessment_completed",
                name = "Assessment Completed",
                description = "Complete your first skill assessment",
                icon = "📝",
                unlocked = assessmentCompleted
            )
        )

        // 3. First Evidence Badge
        badges.add(
            Badge(
                id = "first_evidence",
                name = "First Evidence",
                description = "Add your first piece of skill evidence",
                icon = "📁",
                unlocked = evidence.isNotEmpty()
            )
        )

        // 4. Verified Evidence Badge
        val verifiedEvidence =
            evidence.count { it.status == "Verified" }

        badges.add(
            Badge(
                id = "verified_evidence",
                name = "Verified Evidence",
                description = "Get your first evidence verified",
                icon = "✅",
                unlocked = verifiedEvidence > 0
            )
        )

        // 5. Evidence Collector Badge
        badges.add(
            Badge(
                id = "evidence_collector",
                name = "Evidence Collector",
                description = "Add at least 3 pieces of evidence",
                icon = "📚",
                unlocked = evidence.size >= 3
            )
        )

        // 6. Proven Skill Badge
        val skillsWithGoodProgress =
            skills.count { it.progress >= 75 }

        badges.add(
            Badge(
                id = "proven_skill",
                name = "Proven Skill",
                description = "Reach 75% or higher skill progress",
                icon = "🏆",
                unlocked = skillsWithGoodProgress > 0
            )
        )

        return badges
    }

    fun getUnlockedBadges(): List<Badge> {
        return getBadges().filter { it.unlocked }
    }

    fun getLockedBadges(): List<Badge> {
        return getBadges().filter { !it.unlocked }
    }
}