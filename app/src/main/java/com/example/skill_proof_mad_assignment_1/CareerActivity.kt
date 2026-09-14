package com.example.skill_proof_mad_assignment_1

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CareerActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_career)

        databaseHelper = DatabaseHelper(this)

        // ------------------------------------------
        // FIND VIEWS
        // ------------------------------------------

        val btnBack =
            findViewById<ImageButton>(R.id.btnBack)

        val tvReadinessScore =
            findViewById<TextView>(R.id.tvReadinessScore)

        val tvReadinessMessage =
            findViewById<TextView>(R.id.tvReadinessMessage)

        val readinessProgress =
            findViewById<ProgressBar>(R.id.readinessProgress)

        val tvStrongSkills =
            findViewById<TextView>(R.id.tvStrongSkills)

        val tvImproveSkills =
            findViewById<TextView>(R.id.tvImproveSkills)

        val tvRecommendations =
            findViewById<TextView>(R.id.tvRecommendations)

        // ------------------------------------------
        // BACK BUTTON
        // ------------------------------------------

        btnBack.setOnClickListener {
            finish()
        }

        // ------------------------------------------
        // GET REAL DATA FROM SQLITE
        // ------------------------------------------

        val proofScore =
            calculateProofScore()

        val assessmentScore =
            databaseHelper.getLatestAssessmentPercentage()

        val skills =
            databaseHelper.getAllSkills()

        // ------------------------------------------
        // CALCULATE CAREER READINESS
        // ------------------------------------------

        val readinessScore =
            (
                    proofScore * 0.60 +
                            assessmentScore * 0.40
                    ).toInt()

        // ------------------------------------------
        // DISPLAY READINESS SCORE
        // ------------------------------------------

        tvReadinessScore.text =
            "$readinessScore%"

        readinessProgress.progress =
            readinessScore

        // ------------------------------------------
        // READINESS MESSAGE
        // ------------------------------------------

        tvReadinessMessage.text =
            when {

                readinessScore >= 85 ->
                    "You are highly prepared for your target role."

                readinessScore >= 70 ->
                    "You are on the right track. Strengthen your weak areas."

                readinessScore >= 50 ->
                    "Good start. Build more proof to improve your readiness."

                else ->
                    "Keep building skills, assessments and evidence."
            }

        // ------------------------------------------
        // STRONG AND WEAK SKILLS
        // ------------------------------------------

        val strongSkills =
            skills.filter {
                it.progress >= 70
            }

        val skillsToImprove =
            skills.filter {
                it.progress < 70
            }

        if (strongSkills.isEmpty()) {

            tvStrongSkills.text =
                "No strong skills yet.\n\nBuild your skill level to 70% or above."

        } else {

            tvStrongSkills.text =
                strongSkills.joinToString(
                    separator = "\n"
                ) {
                    "✓  ${it.name}  •  ${it.progress}%"
                }
        }

        if (skillsToImprove.isEmpty()) {

            tvImproveSkills.text =
                "No major weak areas detected."

        } else {

            tvImproveSkills.text =
                skillsToImprove.joinToString(
                    separator = "\n"
                ) {
                    "⚠  ${it.name}  •  ${it.progress}%"
                }
        }

        // ------------------------------------------
        // RECOMMENDATIONS
        // ------------------------------------------

        val recommendations =
            mutableListOf<String>()

        if (skills.isEmpty()) {

            recommendations.add(
                "Add your first skill to start building your career profile."
            )
        }

        if (skillsToImprove.isNotEmpty()) {

            recommendations.add(
                "Improve skills below 70% through practice and projects."
            )
        }

        if (assessmentScore < 70) {

            recommendations.add(
                "Take more assessments to improve your knowledge score."
            )
        }

        val evidenceScore =
            databaseHelper.getEvidenceScore()

        if (evidenceScore < 80) {

            recommendations.add(
                "Add more project, certificate or GitHub evidence."
            )
        }

        val verificationScore =
            databaseHelper.getVerificationScore()

        if (verificationScore < 50) {

            recommendations.add(
                "Verify more evidence to increase your proof credibility."
            )
        }

        if (recommendations.isEmpty()) {

            recommendations.add(
                "Excellent! Keep updating your skills and evidence regularly."
            )
        }

        tvRecommendations.text =
            recommendations.joinToString(
                separator = "\n\n"
            ) {
                "•  $it"
            }
    }

    // ------------------------------------------
    // CALCULATE PROOF SCORE
    // ------------------------------------------

    private fun calculateProofScore(): Int {

        val skillLevel =
            databaseHelper.getAverageSkillProgress()

        val assessmentScore =
            databaseHelper.getLatestAssessmentPercentage()

        val evidenceScore =
            databaseHelper.getEvidenceScore()

        val verifiedScore =
            databaseHelper.getVerificationScore()

        return (
                skillLevel * 0.30 +
                        assessmentScore * 0.30 +
                        evidenceScore * 0.25 +
                        verifiedScore * 0.15
                ).toInt()
    }
}