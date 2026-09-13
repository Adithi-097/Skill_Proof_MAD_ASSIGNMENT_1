package com.example.skill_proof_mad_assignment_1

import android.os.Bundle
import android.widget.AutoCompleteTextView
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout

class AssessmentActivity : AppCompatActivity() {

    private lateinit var actvSkill: AutoCompleteTextView
    private lateinit var questionNumber: TextView
    private lateinit var questionText: TextView
    private lateinit var optionsGroup: RadioGroup
    private lateinit var btnNext: MaterialButton
    private lateinit var tvResult: TextView

    private var currentQuestion = 0
    private var score = 0
    private var assessmentStarted = false

    private val questions = listOf(
        Question(
            "Which keyword is used to declare a variable whose value cannot be changed in Kotlin?",
            listOf("var", "val", "const", "let"),
            1
        ),
        Question(
            "Which collection does not allow duplicate elements?",
            listOf("List", "ArrayList", "Set", "Array"),
            2
        ),
        Question(
            "Which function is the entry point of a Kotlin program?",
            listOf("start()", "run()", "main()", "init()"),
            2
        ),
        Question(
            "Which keyword is used to create a class in Kotlin?",
            listOf("object", "class", "struct", "type"),
            1
        ),
        Question(
            "Which Android component is normally used to represent a screen?",
            listOf("Service", "Activity", "BroadcastReceiver", "Provider"),
            1
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_assessment)

        val btnBack =
            findViewById<ImageButton>(R.id.btnBack)

        actvSkill =
            findViewById(R.id.actvSkill)

        questionNumber =
            findViewById(R.id.tvQuestionNumber)

        questionText =
            findViewById(R.id.tvQuestion)

        optionsGroup =
            findViewById(R.id.radioOptions)

        btnNext =
            findViewById(R.id.btnNext)

        tvResult =
            findViewById(R.id.tvResult)

        val skillLayout =
            findViewById<TextInputLayout>(R.id.skillLayout)

        val skills = arrayOf(
            "Kotlin",
            "Java",
            "Python",
            "SQL",
            "Android"
        )

        val adapter = android.widget.ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            skills
        )

        actvSkill.setAdapter(adapter)

        btnBack.setOnClickListener {
            finish()
        }

        btnNext.setOnClickListener {

            if (!assessmentStarted) {

                val selectedSkill =
                    actvSkill.text.toString().trim()

                if (selectedSkill.isEmpty()) {
                    skillLayout.error =
                        "Select a skill to begin"
                    actvSkill.requestFocus()
                    return@setOnClickListener
                }

                skillLayout.error = null

                assessmentStarted = true
                currentQuestion = 0
                score = 0

                showQuestion()

                return@setOnClickListener
            }

            checkAnswer()
        }
    }

    private fun showQuestion() {

        if (currentQuestion >= questions.size) {
            showResult()
            return
        }

        val question =
            questions[currentQuestion]

        questionNumber.text =
            "Question ${currentQuestion + 1} of ${questions.size}"

        questionText.text =
            question.text

        optionsGroup.removeAllViews()

        question.options.forEachIndexed { index, option ->

            val radioButton =
                RadioButton(this)

            radioButton.text = option
            radioButton.textSize = 14f

            radioButton.setPadding(
                8,
                18,
                8,
                18
            )

            radioButton.id =
                1000 + index

            optionsGroup.addView(radioButton)
        }

        btnNext.text =
            if (currentQuestion == questions.lastIndex) {
                "SUBMIT ASSESSMENT"
            } else {
                "NEXT QUESTION"
            }
    }

    private fun checkAnswer() {

        val selectedId =
            optionsGroup.checkedRadioButtonId

        if (selectedId == -1) {

            Toast.makeText(
                this,
                "Please select an answer",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val selectedIndex =
            selectedId - 1000

        val correctAnswer =
            questions[currentQuestion].correctAnswerIndex

        if (selectedIndex == correctAnswer) {
            score++
        }

        currentQuestion++

        showQuestion()
    }

    private fun showResult() {

        questionNumber.text = "Assessment Complete"

        questionText.text = ""

        optionsGroup.removeAllViews()

        val percentage =
            (score * 100) / questions.size

        tvResult.text =
            "Your Score\n$score/${questions.size}  •  $percentage%"

        tvResult.visibility =
            TextView.VISIBLE

        btnNext.text = "RETAKE ASSESSMENT"

        assessmentStarted = false
        currentQuestion = 0
        score = 0
    }
}

data class Question(
    val text: String,
    val options: List<String>,
    val correctAnswerIndex: Int
)