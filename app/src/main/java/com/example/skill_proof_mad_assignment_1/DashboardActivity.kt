package com.example.skill_proof_mad_assignment_1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    private lateinit var tvProofScore: TextView
    private lateinit var tvSkillsDescription: TextView
    private lateinit var tvEvidenceDescription: TextView
    private lateinit var tvCareerDescription: TextView
    private lateinit var tvProofMessage: TextView
    private lateinit var tvProofHint: TextView
    private lateinit var proofProgress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_dashboard)

        databaseHelper = DatabaseHelper(this)

        // ==========================================
        // FIND VIEWS
        // ==========================================

        tvProofScore =
            findViewById(R.id.tvProofScore)

        tvSkillsDescription =
            findViewById(R.id.tvSkillsDescription)

        tvEvidenceDescription =
            findViewById(R.id.tvEvidenceDescription)

        tvCareerDescription =
            findViewById(R.id.tvCareerDescription)

        tvProofMessage =
            findViewById(R.id.tvProofMessage)

        tvProofHint =
            findViewById(R.id.tvProofHint)

        proofProgress =
            findViewById(R.id.proofProgress)

        val tvViewSkills =
            findViewById<TextView>(R.id.tvViewSkills)

        val tvViewEvidence =
            findViewById<TextView>(R.id.tvViewEvidence)

        val tvViewAssessment =
            findViewById<TextView>(R.id.tvViewAssessment)

        val tvViewCareer =
            findViewById<TextView>(R.id.tvViewCareer)

        // IMPORTANT:
        // tvProfile is a MaterialCardView in XML,
        // so use View instead of TextView.
        val tvProfile =
            findViewById<View>(R.id.tvProfile)

        val proofScoreCard =
            findViewById<View>(R.id.proofScoreCard)

        // ==========================================
        // PROOF SCORE CARD
        // ==========================================

        proofScoreCard.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    ProofScoreActivity::class.java
                )
            )
        }

        // ==========================================
        // SKILLS
        // ==========================================

        tvViewSkills.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    SkillsActivity::class.java
                )
            )
        }

        // ==========================================
        // EVIDENCE
        // ==========================================

        tvViewEvidence.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    EvidenceActivity::class.java
                )
            )
        }

        // ==========================================
        // ASSESSMENT
        // ==========================================

        tvViewAssessment.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    AssessmentActivity::class.java
                )
            )
        }

        // ==========================================
        // CAREER
        // ==========================================

        tvViewCareer.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    CareerActivity::class.java
                )
            )
        }

        // ==========================================
        // PROFILE
        // ==========================================

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
        // SKILLS COUNT
        // ==========================================

        tvSkillsDescription.text =
            "${skills.size} skill(s) added"

        // ==========================================
        // EVIDENCE COUNT
        // ==========================================

        tvEvidenceDescription.text =
            "${evidence.size} evidence item(s) added"

        // ==========================================
        // NO SKILLS
        // ==========================================

        if (skills.isEmpty()) {

            tvProofScore.text =
                "0/100"

            tvCareerDescription.text =
                "Add a skill to begin"

            tvProofMessage.text =
                "Start building your proof!"

            tvProofHint.text =
                "Add your first skill to get started →"

            proofProgress.progress = 0

            return
        }

        // ==========================================
        // CURRENT SKILL
        // ==========================================

        val skill =
            skills.first()

        val skillName =
            skill.name

        val skillLevel =
            skill.progress

        // ==========================================
        // ASSESSMENT
        // ==========================================

        val assessmentScore =
            databaseHelper.getAssessmentPercentageForSkill(
                skillName
            )

        // ==========================================
        // EVIDENCE
        // ==========================================

        val evidenceCount =
            databaseHelper.getEvidenceCountForSkill(
                skillName
            )

        val verifiedEvidenceCount =
            databaseHelper.getVerifiedEvidenceCountForSkill(
                skillName
            )

        // ==========================================
        // EVIDENCE SCORE
        // ==========================================

        val evidenceScore =
            when {

                evidenceCount >= 5 ->
                    100

                evidenceCount == 4 ->
                    90

                evidenceCount == 3 ->
                    80

                evidenceCount == 2 ->
                    65

                evidenceCount == 1 ->
                    45

                else ->
                    0
            }

        // ==========================================
        // VERIFICATION SCORE
        // ==========================================

        val verificationScore =
            if (evidenceCount == 0) {

                0

            } else {

                (verifiedEvidenceCount * 100) / evidenceCount
            }

        // ==========================================
        // PROOF SCORE
        // ==========================================

        val proofScore =
            (
                    skillLevel * 0.30 +
                            assessmentScore * 0.30 +
                            evidenceScore * 0.25 +
                            verificationScore * 0.15
                    ).toInt()

        // ==========================================
        // CAREER READINESS
        // ==========================================

        val careerReadiness =
            (
                    proofScore * 0.60 +
                            assessmentScore * 0.40
                    ).toInt()

        // ==========================================
        // DISPLAY PROOF SCORE
        // ==========================================

        tvProofScore.text =
            "$proofScore/100"

        proofProgress.progress =
            proofScore

        // ==========================================
        // PROOF MESSAGE
        // ==========================================

        tvProofMessage.text =
            when {

                proofScore >= 85 ->
                    "Excellent proof! You're highly prepared."

                proofScore >= 70 ->
                    "You're building strong proof!"

                proofScore >= 50 ->
                    "Keep improving your proof."

                else ->
                    "Start building stronger evidence."
            }

        // ==========================================
        // PROOF HINT
        // ==========================================

        tvProofHint.text =
            when {

                evidenceCount == 0 ->
                    "Add evidence to increase your score →"

                verifiedEvidenceCount < evidenceCount ->
                    "Verify your evidence to increase trust →"

                assessmentScore < 70 ->
                    "Improve your assessment score →"

                else ->
                    "Keep building your proof →"
            }

        // ==========================================
        // CAREER READINESS
        // ==========================================

        tvCareerDescription.text =
            "Career readiness: $careerReadiness/100"
    }
}