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

    private val addSkillLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->

            if (result.resultCode == RESULT_OK) {

                val data = result.data ?: return@registerForActivityResult

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

                val newSkill = Skill(
                    name = skillName,
                    level = level,
                    progress = progress,
                    proofStatus = proofStatus
                )

                skillAdapter.addSkill(newSkill)

                updateSkillCount()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_skills)

        val btnBack =
            findViewById<ImageButton>(R.id.btnBack)

        val btnAddSkill =
            findViewById<MaterialButton>(R.id.btnAddSkill)

        tvSkillCount =
            findViewById(R.id.tvSkillCount)

        val recyclerSkills =
            findViewById<RecyclerView>(R.id.recyclerSkills)

        skills = mutableListOf(

            Skill(
                "Java",
                "Intermediate",
                70,
                "Not verified"
            ),

            Skill(
                "Kotlin",
                "Beginner",
                45,
                "Not verified"
            ),

            Skill(
                "Python",
                "Advanced",
                88,
                "Verified"
            )
        )

        skillAdapter =
            SkillAdapter(skills)

        recyclerSkills.layoutManager =
            LinearLayoutManager(this)

        recyclerSkills.adapter =
            skillAdapter

        updateSkillCount()

        btnBack.setOnClickListener {
            finish()
        }

        btnAddSkill.setOnClickListener {

            val intent = Intent(
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