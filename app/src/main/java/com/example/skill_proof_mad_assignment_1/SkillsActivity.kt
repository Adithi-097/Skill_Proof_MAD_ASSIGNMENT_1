package com.example.skill_proof_mad_assignment_1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class SkillsActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var skillAdapter: SkillAdapter

    private val skillList = mutableListOf<Skill>()

    private lateinit var recyclerSkills: RecyclerView
    private lateinit var tvEmptySkills: TextView
    private lateinit var tvSkillCount: TextView
    private lateinit var btnAddSkill: MaterialButton
    private lateinit var btnBack: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_skills)

        // -----------------------------------------
        // Database
        // -----------------------------------------

        databaseHelper = DatabaseHelper(this)

        // -----------------------------------------
        // Find Views
        // -----------------------------------------

        btnBack = findViewById(R.id.btnBack)
        btnAddSkill = findViewById(R.id.btnAddSkill)

        recyclerSkills = findViewById(R.id.recyclerSkills)
        tvEmptySkills = findViewById(R.id.tvEmptySkills)
        tvSkillCount = findViewById(R.id.tvSkillCount)

        // -----------------------------------------
        // Back Button
        // -----------------------------------------

        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // -----------------------------------------
        // RecyclerView
        // -----------------------------------------

        skillAdapter = SkillAdapter(skillList)

        recyclerSkills.layoutManager =
            LinearLayoutManager(this)

        recyclerSkills.adapter = skillAdapter

        recyclerSkills.setHasFixedSize(true)

        // -----------------------------------------
        // Add Skill
        // -----------------------------------------

        btnAddSkill.setOnClickListener {

            val intent =
                Intent(
                    this,
                    AddSkillActivity::class.java
                )

            startActivity(intent)
        }

        // -----------------------------------------
        // Load Skills
        // -----------------------------------------

        loadSkills()
    }

    // ---------------------------------------------
    // Reload whenever screen becomes visible again
    // ---------------------------------------------

    override fun onResume() {
        super.onResume()

        loadSkills()
    }

    // ---------------------------------------------
    // Load Skills From Database
    // ---------------------------------------------

    private fun loadSkills() {

        skillList.clear()

        val databaseSkills =
            databaseHelper.getAllSkills()

        // -----------------------------------------
        // Add demo skills only if database is empty
        // -----------------------------------------

        if (databaseSkills.isEmpty()) {

            databaseHelper.insertSkill(
                Skill(
                    name = "Java",
                    level = "Intermediate",
                    progress = 70,
                    proofStatus = "Not verified"
                )
            )

            databaseHelper.insertSkill(
                Skill(
                    name = "Kotlin",
                    level = "Beginner",
                    progress = 45,
                    proofStatus = "Not verified"
                )
            )

            databaseHelper.insertSkill(
                Skill(
                    name = "Python",
                    level = "Advanced",
                    progress = 88,
                    proofStatus = "Verified"
                )
            )

            // Load again after inserting demo data
            skillList.addAll(
                databaseHelper.getAllSkills()
            )

        } else {

            skillList.addAll(databaseSkills)
        }

        // -----------------------------------------
        // Update UI
        // -----------------------------------------

        updateSkillUI()
    }

    // ---------------------------------------------
    // Update Skill Screen UI
    // ---------------------------------------------

    private fun updateSkillUI() {

        val count = skillList.size

        tvSkillCount.text =
            if (count == 1) {
                "1 skill added"
            } else {
                "$count skills added"
            }

        if (skillList.isEmpty()) {

            recyclerSkills.visibility =
                View.GONE

            tvEmptySkills.visibility =
                View.VISIBLE

        } else {

            recyclerSkills.visibility =
                View.VISIBLE

            tvEmptySkills.visibility =
                View.GONE
        }

        skillAdapter.notifyDataSetChanged()
    }
}