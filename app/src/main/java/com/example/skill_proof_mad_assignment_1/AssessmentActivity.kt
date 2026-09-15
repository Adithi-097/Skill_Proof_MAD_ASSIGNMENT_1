package com.example.skill_proof_mad_assignment_1

import android.os.Bundle
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class AssessmentActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    private lateinit var btnBack: ImageButton
    private lateinit var btnSubmit: MaterialButton

    private lateinit var spinnerSkill: Spinner

    private lateinit var rgQuestion1: RadioGroup
    private lateinit var rgQuestion2: RadioGroup
    private lateinit var rgQuestion3: RadioGroup
    private lateinit var rgQuestion4: RadioGroup
    private lateinit var rgQuestion5: RadioGroup

    private lateinit var tvResultTitle: TextView
    private lateinit var tvResultScore: TextView
    private lateinit var tvResultMessage: TextView

    private val skills = arrayOf(
        "Kotlin",
        "Java",
        "Python",
        "SQL",
        "Android"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_assessment)

        databaseHelper = DatabaseHelper(this)

        // -----------------------------------------
        // Find Views
        // -----------------------------------------

        btnBack =
            findViewById(R.id.btnBack)

        btnSubmit =
            findViewById(R.id.btnSubmit)

        spinnerSkill =
            findViewById(R.id.spinnerSkill)

        rgQuestion1 =
            findViewById(R.id.rgQuestion1)

        rgQuestion2 =
            findViewById(R.id.rgQuestion2)

        rgQuestion3 =
            findViewById(R.id.rgQuestion3)

        rgQuestion4 =
            findViewById(R.id.rgQuestion4)

        rgQuestion5 =
            findViewById(R.id.rgQuestion5)

        tvResultTitle =
            findViewById(R.id.tvResultTitle)

        tvResultScore =
            findViewById(R.id.tvResultScore)

        tvResultMessage =
            findViewById(R.id.tvResultMessage)

        // -----------------------------------------
        // Initial Result State
        // -----------------------------------------

        tvResultTitle.visibility =
            TextView.GONE

        tvResultScore.visibility =
            TextView.GONE

        tvResultMessage.visibility =
            TextView.GONE

        // -----------------------------------------
        // Skill Spinner
        // -----------------------------------------

        val skillAdapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                skills
            )

        skillAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spinnerSkill.adapter =
            skillAdapter

        // -----------------------------------------
        // Back Button
        // -----------------------------------------

        btnBack.setOnClickListener {

            onBackPressedDispatcher.onBackPressed()
        }

        // -----------------------------------------
        // Submit
        // -----------------------------------------

        btnSubmit.setOnClickListener {

            submitAssessment()
        }
    }

    // ---------------------------------------------
    // Submit Assessment
    // ---------------------------------------------

    private fun submitAssessment() {

        // -----------------------------------------
        // Check Questions
        // -----------------------------------------

        if (!isQuestionAnswered(rgQuestion1)) {
            Toast.makeText(
                this,
                "Please answer Question 1.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (!isQuestionAnswered(rgQuestion2)) {
            Toast.makeText(
                this,
                "Please answer Question 2.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (!isQuestionAnswered(rgQuestion3)) {
            Toast.makeText(
                this,
                "Please answer Question 3.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (!isQuestionAnswered(rgQuestion4)) {
            Toast.makeText(
                this,
                "Please answer Question 4.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (!isQuestionAnswered(rgQuestion5)) {
            Toast.makeText(
                this,
                "Please answer Question 5.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // -----------------------------------------
        // Calculate Score
        // -----------------------------------------

        var score = 0

        if (isCorrect(rgQuestion1, R.id.q1Option2)) {
            score++
        }

        if (isCorrect(rgQuestion2, R.id.q2Option1)) {
            score++
        }

        if (isCorrect(rgQuestion3, R.id.q3Option3)) {
            score++
        }

        if (isCorrect(rgQuestion4, R.id.q4Option2)) {
            score++
        }

        if (isCorrect(rgQuestion5, R.id.q5Option1)) {
            score++
        }

        val totalQuestions = 5

        val percentage =
            (score * 100) / totalQuestions

        val selectedSkill =
            spinnerSkill.selectedItem
                .toString()

        // -----------------------------------------
        // Save Assessment
        // -----------------------------------------

        databaseHelper.insertAssessment(
            selectedSkill,
            score,
            totalQuestions,
            percentage
        )

        // -----------------------------------------
        // Show Result
        // -----------------------------------------

        showResult(
            score,
            totalQuestions,
            percentage
        )

        Toast.makeText(
            this,
            "Assessment saved successfully!",
            Toast.LENGTH_SHORT
        ).show()
    }

    // ---------------------------------------------
    // Check Answered
    // ---------------------------------------------

    private fun isQuestionAnswered(
        group: RadioGroup
    ): Boolean {

        return group.checkedRadioButtonId != -1
    }

    // ---------------------------------------------
    // Check Correct Answer
    // ---------------------------------------------

    private fun isCorrect(
        group: RadioGroup,
        correctId: Int
    ): Boolean {

        return group.checkedRadioButtonId == correctId
    }

    // ---------------------------------------------
    // Show Result
    // ---------------------------------------------

    private fun showResult(
        score: Int,
        total: Int,
        percentage: Int
    ) {

        tvResultTitle.visibility =
            TextView.VISIBLE

        tvResultScore.visibility =
            TextView.VISIBLE

        tvResultMessage.visibility =
            TextView.VISIBLE

        tvResultScore.text =
            "$score / $total  •  $percentage%"

        tvResultMessage.text =
            when {
                percentage >= 80 ->
                    "Excellent! Your knowledge is strongly demonstrated."

                percentage >= 60 ->
                    "Good performance. Keep practicing to strengthen your skill."

                percentage >= 40 ->
                    "You're developing this skill. More practice will improve your proof."

                else ->
                    "Keep learning and practicing. Your next assessment can improve your score."
            }
    }
}