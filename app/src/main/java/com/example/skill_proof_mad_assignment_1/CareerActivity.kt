package com.example.skill_proof_mad_assignment_1

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import com.google.android.material.button.MaterialButton

class CareerActivity : BaseActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    private lateinit var btnBack: ImageButton
    private lateinit var btnBadges: MaterialButton
    private lateinit var btnSkillGap: MaterialButton

    private lateinit var tvSkillName: TextView
    private lateinit var tvCareerScore: TextView
    private lateinit var tvCareerStatus: TextView
    private lateinit var tvCareerMessage: TextView

    private lateinit var tvProofScore: TextView
    private lateinit var tvAssessmentScore: TextView

    private lateinit var progressCareer: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_career)

        databaseHelper =
            DatabaseHelper(this)

        // -----------------------------------------
        // Find Views
        // -----------------------------------------

        btnBack =
            findViewById(R.id.btnBack)

        btnBadges =
            findViewById(R.id.btnBadges)

        btnSkillGap =
            findViewById(R.id.btnSkillGap)

        tvSkillName =
            findViewById(R.id.tvSkillName)

        tvCareerScore =
            findViewById(R.id.tvCareerScore)

        tvCareerStatus =
            findViewById(R.id.tvCareerStatus)

        tvCareerMessage =
            findViewById(R.id.tvCareerMessage)

        tvProofScore =
            findViewById(R.id.tvProofScore)

        tvAssessmentScore =
            findViewById(R.id.tvAssessmentScore)

        progressCareer =
            findViewById(R.id.progressCareer)

        // -----------------------------------------
        // Back
        // -----------------------------------------

        btnBack.setOnClickListener {

            onBackPressedDispatcher
                .onBackPressed()
        }

        // -----------------------------------------
        // Badges
        // -----------------------------------------

        btnBadges.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    BadgesActivity::class.java
                )
            )
        }

        // -----------------------------------------
        // Skill Gap
        // -----------------------------------------

        btnSkillGap.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    SkillGapActivity::class.java
                )
            )
        }

        calculateCareerReadiness()
    }

    override fun onResume() {

        super.onResume()

        calculateCareerReadiness()
    }

    // ---------------------------------------------
    // Calculate Career Readiness
    // ---------------------------------------------

    private fun calculateCareerReadiness() {

        val skills =
            databaseHelper.getAllSkills()

        // -----------------------------------------
        // No skills
        // -----------------------------------------

        if (skills.isEmpty()) {

            showNoSkillState()

            return
        }

        // -----------------------------------------
        // Get Current Skill
        // -----------------------------------------

        val preferences =
            getSharedPreferences(
                "SkillProofPrefs",
                MODE_PRIVATE
            )

        val currentSkillName =
            preferences.getString(
                "current_skill",
                null
            )

        // -----------------------------------------
        // No Current Skill Selected
        // -----------------------------------------

        if (currentSkillName.isNullOrBlank()) {

            showNoCurrentSkillState()

            return
        }

        // -----------------------------------------
        // Find Selected Skill
        // -----------------------------------------

        val selectedSkill =
            skills.firstOrNull {

                it.name.equals(
                    currentSkillName,
                    ignoreCase = true
                )
            }

        // -----------------------------------------
        // Selected Skill Not Found
        // -----------------------------------------

        if (selectedSkill == null) {

            showNoCurrentSkillState()

            return
        }

        val skillName =
            selectedSkill.name

        tvSkillName.text =
            skillName

        // -----------------------------------------
        // Skill Level
        // -----------------------------------------

        val skillLevelScore =
            selectedSkill.progress
                .coerceIn(
                    0,
                    100
                )

        // -----------------------------------------
        // Assessment
        // -----------------------------------------

        val assessmentScore =
            databaseHelper
                .getAssessmentPercentageForSkill(
                    skillName
                )
                .coerceIn(
                    0,
                    100
                )

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
        // Proof Score
        // -----------------------------------------

        val proofScore =
            (
                    skillLevelScore * 0.30 +
                            assessmentScore * 0.30 +
                            evidenceScore * 0.25 +
                            verificationScore * 0.15
                    )
                .toInt()
                .coerceIn(
                    0,
                    100
                )

        // -----------------------------------------
        // Career Readiness
        //
        // Proof Score = 60%
        // Assessment = 40%
        // -----------------------------------------

        val careerReadiness =
            (
                    proofScore * 0.60 +
                            assessmentScore * 0.40
                    )
                .toInt()
                .coerceIn(
                    0,
                    100
                )

        // -----------------------------------------
        // Update UI
        // -----------------------------------------

        tvCareerScore.text =
            careerReadiness.toString()

        tvProofScore.text =
            "$proofScore / 100"

        tvAssessmentScore.text =
            "$assessmentScore / 100"

        progressCareer.progress =
            careerReadiness

        tvCareerStatus.text =
            getCareerStatus(
                careerReadiness
            )

        tvCareerMessage.text =
            getCareerMessage(
                careerReadiness
            )
    }

    // ---------------------------------------------
    // Evidence Score
    // ---------------------------------------------

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

    // ---------------------------------------------
    // Career Status
    // ---------------------------------------------

    private fun getCareerStatus(
        score: Int
    ): String {

        return when {

            score >= 85 ->
                "Career Ready"

            score >= 70 ->
                "Almost Ready"

            score >= 50 ->
                "Developing"

            else ->
                "Needs Improvement"
        }
    }

    // ---------------------------------------------
    // Career Message
    // ---------------------------------------------

    private fun getCareerMessage(
        score: Int
    ): String {

        return when {

            score >= 85 ->

                "Excellent! Your skill has strong proof and you are showing good career readiness. Keep your evidence updated."

            score >= 70 ->

                "You're close to being career ready. Strengthen your weaker areas and add more verified evidence."

            score >= 50 ->

                "You're making progress. Complete more assessments, improve your skill level and build stronger evidence."

            else ->

                "Start building your proof by improving your skill, completing assessments and adding verified evidence."
        }
    }

    // ---------------------------------------------
    // No Current Skill Selected
    // ---------------------------------------------

    private fun showNoCurrentSkillState() {

        tvSkillName.text =
            "Select a skill"

        tvCareerScore.text =
            "0"

        tvCareerStatus.text =
            "Select Current Skill"

        tvCareerMessage.text =
            "Go to Skills and select the skill you are currently focusing on."

        tvProofScore.text =
            "0 / 100"

        tvAssessmentScore.text =
            "0 / 100"

        progressCareer.progress =
            0
    }

    // ---------------------------------------------
    // No Skills
    // ---------------------------------------------

    private fun showNoSkillState() {

        tvSkillName.text =
            "No skill added"

        tvCareerScore.text =
            "0"

        tvCareerStatus.text =
            "Start Building"

        tvCareerMessage.text =
            "Add a skill first. Then select it as your current skill to calculate career readiness."

        tvProofScore.text =
            "0 / 100"

        tvAssessmentScore.text =
            "0 / 100"

        progressCareer.progress =
            0
    }
}