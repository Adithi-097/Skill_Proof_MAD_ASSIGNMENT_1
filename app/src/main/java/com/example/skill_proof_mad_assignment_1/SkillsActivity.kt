package com.example.skill_proof_mad_assignment_1

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class SkillsActivity : AppCompatActivity() {

    private lateinit var skillAdapter: SkillAdapter
    private lateinit var skills: MutableList<Skill>
    private lateinit var tvSkillCount: TextView

    private lateinit var databaseHelper: DatabaseHelper

    private val addSkillLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->

            if (result.resultCode == RESULT_OK) {

                val data = result.data
                    ?: return@registerForActivityResult

                val skillName =
                    data.getStringExtra("skill_name")
                        ?: return@registerForActivityResult

                val level =
                    data.getStringExtra("skill_level")
                        ?: return@registerForActivityResult

                val progress =
                    data.getIntExtra(
                        "skill_progress",
                        40
                    )

                val proofStatus =
                    data.getStringExtra("proof_status")
                        ?: "Not verified"

                /*
                 * Save the new skill into SQLite.
                 */
                databaseHelper.insertSkill(
                    skillName,
                    level,
                    progress,
                    proofStatus
                )

                /*
                 * Add the same skill to the RecyclerView.
                 */
                val newSkill = Skill(
                    skillName,
                    level,
                    progress,
                    proofStatus
                )

                skillAdapter.addSkill(newSkill)

                updateSkillCount()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_skills)

        /*
         * Initialize SQLite database helper.
         */
        databaseHelper = DatabaseHelper(this)

        val btnBack =
            findViewById<ImageButton>(R.id.btnBack)

        val btnAddSkill =
            findViewById<MaterialButton>(R.id.btnAddSkill)

        tvSkillCount =
            findViewById(R.id.tvSkillCount)

        val recyclerSkills =
            findViewById<RecyclerView>(R.id.recyclerSkills)

        /*
         * Load skills from SQLite.
         */
        skills =
            databaseHelper.getAllSkills()

        /*
         * If database is empty, add demo data once.
         */
        if (skills.isEmpty()) {

            databaseHelper.insertSkill(
                "Java",
                "Intermediate",
                70,
                "Not verified"
            )

            databaseHelper.insertSkill(
                "Kotlin",
                "Beginner",
                45,
                "Not verified"
            )

            databaseHelper.insertSkill(
                "Python",
                "Advanced",
                88,
                "Verified"
            )

            /*
             * Load the newly inserted data.
             */
            skills =
                databaseHelper.getAllSkills()
        }

        /*
         * RecyclerView setup.
         */
        skillAdapter =
            SkillAdapter(skills)

        recyclerSkills.layoutManager =
            LinearLayoutManager(this)

        recyclerSkills.adapter =
            skillAdapter

        updateSkillCount()

        /*
         * Back button.
         */
        btnBack.setOnClickListener {
            finish()
        }

        /*
         * Add Skill button.
         */
        btnAddSkill.setOnClickListener {

            val intent =
                Intent(
                    this,
                    AddSkillActivity::class.java
                )

            addSkillLauncher.launch(intent)
        }
    }

    private fun updateSkillCount() {

        tvSkillCount.text =
            skills.size.toString()
    }
}