package com.example.skill_proof_mad_assignment_1

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton

class AssessmentActivity : BaseActivity() {

    private lateinit var spinnerSkill: Spinner

    private lateinit var tvQuestion1: TextView
    private lateinit var tvQuestion2: TextView
    private lateinit var tvQuestion3: TextView
    private lateinit var tvQuestion4: TextView
    private lateinit var tvQuestion5: TextView

    private lateinit var rgQuestion1: RadioGroup
    private lateinit var rgQuestion2: RadioGroup
    private lateinit var rgQuestion3: RadioGroup
    private lateinit var rgQuestion4: RadioGroup
    private lateinit var rgQuestion5: RadioGroup

    private lateinit var rbQ1Option1: RadioButton
    private lateinit var rbQ1Option2: RadioButton
    private lateinit var rbQ1Option3: RadioButton
    private lateinit var rbQ1Option4: RadioButton

    private lateinit var rbQ2Option1: RadioButton
    private lateinit var rbQ2Option2: RadioButton
    private lateinit var rbQ2Option3: RadioButton
    private lateinit var rbQ2Option4: RadioButton

    private lateinit var rbQ3Option1: RadioButton
    private lateinit var rbQ3Option2: RadioButton
    private lateinit var rbQ3Option3: RadioButton
    private lateinit var rbQ3Option4: RadioButton

    private lateinit var rbQ4Option1: RadioButton
    private lateinit var rbQ4Option2: RadioButton
    private lateinit var rbQ4Option3: RadioButton
    private lateinit var rbQ4Option4: RadioButton

    private lateinit var rbQ5Option1: RadioButton
    private lateinit var rbQ5Option2: RadioButton
    private lateinit var rbQ5Option3: RadioButton
    private lateinit var rbQ5Option4: RadioButton

    private lateinit var btnSubmit: MaterialButton

    private lateinit var databaseHelper: DatabaseHelper

    private data class Question(
        val question: String,
        val options: List<String>,
        val correctAnswer: Int
    )

    // ------------------------------------------------
    // Available assessment skills
    // ------------------------------------------------

    private val skillList = listOf(
        "Kotlin",
        "Java",
        "Python",
        "SQL",
        "Android"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_assessment)

        databaseHelper =
            DatabaseHelper(this)

        initializeViews()

        setupSkillSpinner()

        setupListeners()

        // ------------------------------------------------
        // Determine current skill
        // ------------------------------------------------

        val intentSkill =
            intent.getStringExtra("selected_skill")

        val preferences =
            getSharedPreferences(
                "SkillProofPrefs",
                MODE_PRIVATE
            )

        val savedCurrentSkill =
            preferences.getString(
                "current_skill",
                null
            )

        /*
         * Priority:
         *
         * 1. Skill passed through Intent
         * 2. Current Skill saved in SharedPreferences
         * 3. First skill in the list
         */

        val selectedSkill =
            intentSkill
                ?: savedCurrentSkill
                ?: skillList.first()

        // ------------------------------------------------
        // Select skill in Spinner
        // ------------------------------------------------

        val skillIndex =
            skillList.indexOfFirst {

                it.equals(
                    selectedSkill,
                    ignoreCase = true
                )
            }

        if (skillIndex >= 0) {

            spinnerSkill.setSelection(
                skillIndex
            )

        } else {

            spinnerSkill.setSelection(0)

            loadQuestions(
                skillList[0]
            )
        }
    }

    // ====================================================
    // INITIALIZE VIEWS
    // ====================================================

    private fun initializeViews() {

        findViewById<ImageButton>(
            R.id.btnBack
        ).setOnClickListener {

            onBackPressedDispatcher
                .onBackPressed()
        }

        spinnerSkill =
            findViewById(
                R.id.spinnerSkill
            )

        tvQuestion1 =
            findViewById(
                R.id.tvQuestion1
            )

        tvQuestion2 =
            findViewById(
                R.id.tvQuestion2
            )

        tvQuestion3 =
            findViewById(
                R.id.tvQuestion3
            )

        tvQuestion4 =
            findViewById(
                R.id.tvQuestion4
            )

        tvQuestion5 =
            findViewById(
                R.id.tvQuestion5
            )

        rgQuestion1 =
            findViewById(
                R.id.rgQuestion1
            )

        rgQuestion2 =
            findViewById(
                R.id.rgQuestion2
            )

        rgQuestion3 =
            findViewById(
                R.id.rgQuestion3
            )

        rgQuestion4 =
            findViewById(
                R.id.rgQuestion4
            )

        rgQuestion5 =
            findViewById(
                R.id.rgQuestion5
            )

        rbQ1Option1 =
            findViewById(
                R.id.rbQ1Option1
            )

        rbQ1Option2 =
            findViewById(
                R.id.rbQ1Option2
            )

        rbQ1Option3 =
            findViewById(
                R.id.rbQ1Option3
            )

        rbQ1Option4 =
            findViewById(
                R.id.rbQ1Option4
            )

        rbQ2Option1 =
            findViewById(
                R.id.rbQ2Option1
            )

        rbQ2Option2 =
            findViewById(
                R.id.rbQ2Option2
            )

        rbQ2Option3 =
            findViewById(
                R.id.rbQ2Option3
            )

        rbQ2Option4 =
            findViewById(
                R.id.rbQ2Option4
            )

        rbQ3Option1 =
            findViewById(
                R.id.rbQ3Option1
            )

        rbQ3Option2 =
            findViewById(
                R.id.rbQ3Option2
            )

        rbQ3Option3 =
            findViewById(
                R.id.rbQ3Option3
            )

        rbQ3Option4 =
            findViewById(
                R.id.rbQ3Option4
            )

        rbQ4Option1 =
            findViewById(
                R.id.rbQ4Option1
            )

        rbQ4Option2 =
            findViewById(
                R.id.rbQ4Option2
            )

        rbQ4Option3 =
            findViewById(
                R.id.rbQ4Option3
            )

        rbQ4Option4 =
            findViewById(
                R.id.rbQ4Option4
            )

        rbQ5Option1 =
            findViewById(
                R.id.rbQ5Option1
            )

        rbQ5Option2 =
            findViewById(
                R.id.rbQ5Option2
            )

        rbQ5Option3 =
            findViewById(
                R.id.rbQ5Option3
            )

        rbQ5Option4 =
            findViewById(
                R.id.rbQ5Option4
            )

        btnSubmit =
            findViewById(
                R.id.btnSubmit
            )
    }

    // ====================================================
    // SKILL SPINNER
    // ====================================================

    private fun setupSkillSpinner() {

        val adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                skillList
            )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spinnerSkill.adapter =
            adapter
    }

    // ====================================================
    // LISTENERS
    // ====================================================

    private fun setupListeners() {

        spinnerSkill.setOnItemSelectedListener(

            object :
                AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: android.view.View?,
                    position: Int,
                    id: Long
                ) {

                    val selectedSkill =
                        skillList[position]

                    // Save selected assessment skill
                    getSharedPreferences(
                        "SkillProofPrefs",
                        MODE_PRIVATE
                    )
                        .edit()
                        .putString(
                            "current_skill",
                            selectedSkill
                        )
                        .apply()

                    loadQuestions(
                        selectedSkill
                    )
                }

                override fun onNothingSelected(
                    parent: AdapterView<*>?
                ) {
                    // Nothing required
                }
            }
        )

        btnSubmit.setOnClickListener {

            submitAssessment()
        }
    }

    // ====================================================
    // LOAD QUESTIONS
    // ====================================================

    private fun loadQuestions(
        skill: String
    ) {

        val questions =
            getQuestionsForSkill(
                skill
            )

        displayQuestion(
            questions[0],
            tvQuestion1,
            rbQ1Option1,
            rbQ1Option2,
            rbQ1Option3,
            rbQ1Option4
        )

        displayQuestion(
            questions[1],
            tvQuestion2,
            rbQ2Option1,
            rbQ2Option2,
            rbQ2Option3,
            rbQ2Option4
        )

        displayQuestion(
            questions[2],
            tvQuestion3,
            rbQ3Option1,
            rbQ3Option2,
            rbQ3Option3,
            rbQ3Option4
        )

        displayQuestion(
            questions[3],
            tvQuestion4,
            rbQ4Option1,
            rbQ4Option2,
            rbQ4Option3,
            rbQ4Option4
        )

        displayQuestion(
            questions[4],
            tvQuestion5,
            rbQ5Option1,
            rbQ5Option2,
            rbQ5Option3,
            rbQ5Option4
        )

        clearSelections()
    }

    // ====================================================
    // DISPLAY QUESTION
    // ====================================================

    private fun displayQuestion(
        question: Question,
        questionText: TextView,
        option1: RadioButton,
        option2: RadioButton,
        option3: RadioButton,
        option4: RadioButton
    ) {

        questionText.text =
            question.question

        option1.text =
            question.options[0]

        option2.text =
            question.options[1]

        option3.text =
            question.options[2]

        option4.text =
            question.options[3]
    }

    // ====================================================
    // CLEAR ANSWERS
    // ====================================================

    private fun clearSelections() {

        rgQuestion1.clearCheck()

        rgQuestion2.clearCheck()

        rgQuestion3.clearCheck()

        rgQuestion4.clearCheck()

        rgQuestion5.clearCheck()
    }

    // ====================================================
    // SUBMIT ASSESSMENT
    // ====================================================

    private fun submitAssessment() {

        val selectedSkill =
            spinnerSkill
                .selectedItem
                .toString()

        // ---------------------------------------------
        // Validate all questions
        // ---------------------------------------------

        if (
            rgQuestion1.checkedRadioButtonId == -1 ||
            rgQuestion2.checkedRadioButtonId == -1 ||
            rgQuestion3.checkedRadioButtonId == -1 ||
            rgQuestion4.checkedRadioButtonId == -1 ||
            rgQuestion5.checkedRadioButtonId == -1
        ) {

            Toast.makeText(
                this,
                "Please answer all 5 questions.",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        // ---------------------------------------------
        // Get Questions
        // ---------------------------------------------

        val questions =
            getQuestionsForSkill(
                selectedSkill
            )

        // ---------------------------------------------
        // Calculate Score
        // ---------------------------------------------

        var score = 0

        if (
            getSelectedOptionIndex(
                rgQuestion1
            ) == questions[0].correctAnswer
        ) {
            score++
        }

        if (
            getSelectedOptionIndex(
                rgQuestion2
            ) == questions[1].correctAnswer
        ) {
            score++
        }

        if (
            getSelectedOptionIndex(
                rgQuestion3
            ) == questions[2].correctAnswer
        ) {
            score++
        }

        if (
            getSelectedOptionIndex(
                rgQuestion4
            ) == questions[3].correctAnswer
        ) {
            score++
        }

        if (
            getSelectedOptionIndex(
                rgQuestion5
            ) == questions[4].correctAnswer
        ) {
            score++
        }

        val totalQuestions =
            questions.size

        val percentage =
            (
                    score * 100
                    ) / totalQuestions

        // ---------------------------------------------
        // Save Assessment
        // ---------------------------------------------

        databaseHelper.insertAssessment(
            selectedSkill,
            score,
            totalQuestions,
            percentage
        )

        // ---------------------------------------------
        // Open Result
        // ---------------------------------------------

        val resultIntent =
            Intent(
                this,
                AssessmentResultActivity::class.java
            )

        resultIntent.putExtra(
            "skill",
            selectedSkill
        )

        resultIntent.putExtra(
            "score",
            score
        )

        resultIntent.putExtra(
            "total",
            totalQuestions
        )

        resultIntent.putExtra(
            "percentage",
            percentage
        )

        startActivity(
            resultIntent
        )

        finish()
    }

    // ====================================================
    // GET SELECTED OPTION
    // ====================================================

    private fun getSelectedOptionIndex(
        radioGroup: RadioGroup
    ): Int {

        val checkedId =
            radioGroup.checkedRadioButtonId

        return when (checkedId) {

            -1 ->
                -1

            radioGroup.getChildAt(0).id ->
                0

            radioGroup.getChildAt(1).id ->
                1

            radioGroup.getChildAt(2).id ->
                2

            radioGroup.getChildAt(3).id ->
                3

            else ->
                -1
        }
    }

    // ====================================================
    // QUESTION BANK
    // ====================================================

    private fun getQuestionsForSkill(
        skill: String
    ): List<Question> {

        return when (
            skill.lowercase()
        ) {

            "python" ->
                getPythonQuestions()

            "java" ->
                getJavaQuestions()

            "kotlin" ->
                getKotlinQuestions()

            "sql" ->
                getSqlQuestions()

            "android" ->
                getAndroidQuestions()

            else ->
                getKotlinQuestions()
        }
    }

    // ====================================================
    // PYTHON QUESTIONS
    // ====================================================

    private fun getPythonQuestions():
            List<Question> {

        return listOf(

            Question(
                "Which keyword is used to define a function in Python?",
                listOf(
                    "function",
                    "def",
                    "fun",
                    "define"
                ),
                1
            ),

            Question(
                "Which data type is mutable in Python?",
                listOf(
                    "Tuple",
                    "String",
                    "List",
                    "Integer"
                ),
                2
            ),

            Question(
                "Which symbol is used for a comment in Python?",
                listOf(
                    "//",
                    "/*",
                    "#",
                    "<!--"
                ),
                2
            ),

            Question(
                "Which function is used to get the length of a list?",
                listOf(
                    "size()",
                    "length()",
                    "count()",
                    "len()"
                ),
                3
            ),

            Question(
                "Which keyword is used to create a class in Python?",
                listOf(
                    "class",
                    "struct",
                    "object",
                    "type"
                ),
                0
            )
        )
    }

    // ====================================================
    // JAVA QUESTIONS
    // ====================================================

    private fun getJavaQuestions():
            List<Question> {

        return listOf(

            Question(
                "Which keyword is used to create a class in Java?",
                listOf(
                    "class",
                    "struct",
                    "define",
                    "object"
                ),
                0
            ),

            Question(
                "Which method is the entry point of a Java program?",
                listOf(
                    "start()",
                    "main()",
                    "run()",
                    "execute()"
                ),
                1
            ),

            Question(
                "Which keyword is used for inheritance in Java?",
                listOf(
                    "inherits",
                    "extends",
                    "implements",
                    "super"
                ),
                1
            ),

            Question(
                "Which data type stores true or false?",
                listOf(
                    "boolean",
                    "bool",
                    "logical",
                    "bit"
                ),
                0
            ),

            Question(
                "Which keyword prevents a class from being inherited?",
                listOf(
                    "static",
                    "private",
                    "final",
                    "const"
                ),
                2
            )
        )
    }

    // ====================================================
    // KOTLIN QUESTIONS
    // ====================================================

    private fun getKotlinQuestions():
            List<Question> {

        return listOf(

            Question(
                "Which keyword declares a variable that can be reassigned?",
                listOf(
                    "val",
                    "var",
                    "let",
                    "mutable"
                ),
                1
            ),

            Question(
                "Which keyword declares an immutable variable?",
                listOf(
                    "var",
                    "let",
                    "val",
                    "const"
                ),
                2
            ),

            Question(
                "Which function is commonly used to print text in Kotlin?",
                listOf(
                    "print()",
                    "console()",
                    "write()",
                    "display()"
                ),
                0
            ),

            Question(
                "Which symbol is used for string templates?",
                listOf(
                    "@",
                    "#",
                    "$",
                    "%"
                ),
                2
            ),

            Question(
                "Which keyword is used to define a function?",
                listOf(
                    "function",
                    "def",
                    "fun",
                    "method"
                ),
                2
            )
        )
    }

    // ====================================================
    // SQL QUESTIONS
    // ====================================================

    private fun getSqlQuestions():
            List<Question> {

        return listOf(

            Question(
                "Which SQL command is used to retrieve data?",
                listOf(
                    "GET",
                    "SELECT",
                    "FETCH",
                    "READ"
                ),
                1
            ),

            Question(
                "Which command is used to add new records?",
                listOf(
                    "INSERT",
                    "ADD",
                    "CREATE",
                    "PUT"
                ),
                0
            ),

            Question(
                "Which clause is used to filter rows?",
                listOf(
                    "ORDER BY",
                    "GROUP BY",
                    "WHERE",
                    "FILTER"
                ),
                2
            ),

            Question(
                "Which command removes a table?",
                listOf(
                    "DELETE",
                    "REMOVE",
                    "DROP",
                    "CLEAR"
                ),
                2
            ),

            Question(
                "Which clause sorts query results?",
                listOf(
                    "SORT BY",
                    "ORDER BY",
                    "ARRANGE BY",
                    "GROUP BY"
                ),
                1
            )
        )
    }

    // ====================================================
    // ANDROID QUESTIONS
    // ====================================================

    private fun getAndroidQuestions():
            List<Question> {

        return listOf(

            Question(
                "Which language is officially recommended for modern Android development?",
                listOf(
                    "Kotlin",
                    "Python",
                    "PHP",
                    "Ruby"
                ),
                0
            ),

            Question(
                "Which component represents a single screen in Android?",
                listOf(
                    "Activity",
                    "Service",
                    "Broadcast",
                    "Provider"
                ),
                0
            ),

            Question(
                "Which file contains Android app permissions?",
                listOf(
                    "build.gradle",
                    "AndroidManifest.xml",
                    "settings.xml",
                    "Main.xml"
                ),
                1
            ),

            Question(
                "Which class is commonly used to move between Activities?",
                listOf(
                    "Intent",
                    "Bundle",
                    "Adapter",
                    "Fragment"
                ),
                0
            ),

            Question(
                "Which UI file format are we using for SkillProof screens?",
                listOf(
                    "XML",
                    "JSON",
                    "CSV",
                    "TXT"
                ),
                0
            )
        )
    }
}