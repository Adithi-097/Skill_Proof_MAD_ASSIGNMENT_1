package com.example.skill_proof_mad_assignment_1

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class EvidenceActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var evidenceAdapter: EvidenceAdapter

    private val evidenceList =
        mutableListOf<Evidence>()

    private lateinit var btnBack: ImageButton
    private lateinit var btnAddEvidence: MaterialButton
    private lateinit var btnGithubVerification: MaterialButton

    private lateinit var recyclerEvidence: RecyclerView

    private lateinit var tvEmptyEvidence: TextView
    private lateinit var tvEvidenceCount: TextView
    private lateinit var tvVerifiedCount: TextView

    private var selectedGithubSkill: String = ""

    // ------------------------------------------------
    // GitHub Verification Result
    // ------------------------------------------------

    private val githubVerificationLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->

            if (
                result.resultCode ==
                RESULT_OK &&
                result.data != null
            ) {

                val data =
                    result.data!!

                val isVerified =
                    data.getBooleanExtra(
                        "github_verified",
                        false
                    )

                if (!isVerified) {
                    return@registerForActivityResult
                }

                val repository =
                    data.getStringExtra(
                        "github_repository"
                    ) ?: "GitHub Repository"

                val owner =
                    data.getStringExtra(
                        "github_owner"
                    ) ?: ""

                val language =
                    data.getStringExtra(
                        "github_language"
                    ) ?: "Not specified"

                val githubUrl =
                    data.getStringExtra(
                        "github_url"
                    ) ?: ""

                if (githubUrl.isBlank()) {
                    Toast.makeText(
                        this,
                        "GitHub URL is missing.",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@registerForActivityResult
                }

                val skill =
                    if (
                        selectedGithubSkill.isNotBlank()
                    ) {
                        selectedGithubSkill
                    } else {
                        "General"
                    }

                val evidence =
                    Evidence(
                        title =
                            "$repository GitHub Repository",

                        skill =
                            skill,

                        type =
                            "GitHub Project",

                        link =
                            githubUrl,

                        status =
                            "Verified",

                        attachmentUri =
                            ""
                    )

                databaseHelper.insertEvidence(
                    evidence
                )

                Toast.makeText(
                    this,
                    "GitHub project added as verified evidence.",
                    Toast.LENGTH_LONG
                ).show()

                loadEvidence()
            }
        }

    // ------------------------------------------------
    // onCreate
    // ------------------------------------------------

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(
            savedInstanceState
        )

        setContentView(
            R.layout.activity_evidence
        )

        databaseHelper =
            DatabaseHelper(this)

        btnBack =
            findViewById(
                R.id.btnBack
            )

        btnAddEvidence =
            findViewById(
                R.id.btnAddEvidence
            )

        btnGithubVerification =
            findViewById(
                R.id.btnGithubVerification
            )

        recyclerEvidence =
            findViewById(
                R.id.recyclerEvidence
            )

        tvEmptyEvidence =
            findViewById(
                R.id.tvEmptyEvidence
            )

        tvEvidenceCount =
            findViewById(
                R.id.tvEvidenceCount
            )

        tvVerifiedCount =
            findViewById(
                R.id.tvVerifiedCount
            )

        // ------------------------------------------------
        // RecyclerView
        // ------------------------------------------------

        evidenceAdapter =
            EvidenceAdapter(
                evidenceList
            ) { evidence, position ->

                showEvidenceOptions(
                    evidence,
                    position
                )
            }

        recyclerEvidence.layoutManager =
            LinearLayoutManager(this)

        recyclerEvidence.adapter =
            evidenceAdapter

        recyclerEvidence.setHasFixedSize(false)

        recyclerEvidence.isNestedScrollingEnabled =
            false

        // ------------------------------------------------
        // Back
        // ------------------------------------------------

        btnBack.setOnClickListener {

            onBackPressedDispatcher
                .onBackPressed()
        }

        // ------------------------------------------------
        // Add Evidence
        // ------------------------------------------------

        btnAddEvidence.setOnClickListener {

            val intent =
                Intent(
                    this,
                    AddEvidenceActivity::class.java
                )

            startActivity(intent)
        }

        // ------------------------------------------------
        // GitHub Verification
        // ------------------------------------------------

        btnGithubVerification.setOnClickListener {

            chooseSkillForGithub()
        }

        loadEvidence()
    }

    // ------------------------------------------------
    // Reload when returning to screen
    // ------------------------------------------------

    override fun onResume() {
        super.onResume()

        if (
            ::databaseHelper.isInitialized
        ) {
            loadEvidence()
        }
    }

    // ------------------------------------------------
    // Load Evidence
    // ------------------------------------------------

    private fun loadEvidence() {

        evidenceList.clear()

        val databaseEvidence =
            databaseHelper.getAllEvidence()

        evidenceList.addAll(
            databaseEvidence
        )

        updateEvidenceUI()
    }

    // ------------------------------------------------
    // Update UI
    // ------------------------------------------------

    private fun updateEvidenceUI() {

        val total =
            evidenceList.size

        val verified =
            evidenceList.count {
                it.status.equals(
                    "Verified",
                    ignoreCase = true
                )
            }

        tvEvidenceCount.text =
            total.toString()

        tvVerifiedCount.text =
            verified.toString()

        if (evidenceList.isEmpty()) {

            recyclerEvidence.visibility =
                View.GONE

            tvEmptyEvidence.visibility =
                View.VISIBLE

        } else {

            recyclerEvidence.visibility =
                View.VISIBLE

            tvEmptyEvidence.visibility =
                View.GONE
        }

        evidenceAdapter.notifyDataSetChanged()
    }

    // ------------------------------------------------
    // Choose Skill for GitHub Verification
    // ------------------------------------------------

    private fun chooseSkillForGithub() {

        val skills =
            databaseHelper.getAllSkills()

        if (skills.isEmpty()) {

            Toast.makeText(
                this,
                "Please add a skill before verifying a GitHub project.",
                Toast.LENGTH_LONG
            ).show()

            return
        }

        if (skills.size == 1) {

            selectedGithubSkill =
                skills[0].name

            openGithubVerification()

            return
        }

        val skillNames =
            skills.map {
                it.name
            }.toTypedArray()

        var selectedIndex =
            0

        AlertDialog.Builder(this)
            .setTitle(
                "Select Skill"
            )
            .setSingleChoiceItems(
                skillNames,
                0
            ) { _, which ->

                selectedIndex =
                    which
            }
            .setPositiveButton(
                "Continue"
            ) { dialog, _ ->

                selectedGithubSkill =
                    skillNames[selectedIndex]

                dialog.dismiss()

                openGithubVerification()
            }
            .setNegativeButton(
                "Cancel",
                null
            )
            .show()
    }

    // ------------------------------------------------
    // Open GitHub Verification
    // ------------------------------------------------

    private fun openGithubVerification() {

        val intent =
            Intent(
                this,
                GitHubVerificationActivity::class.java
            )

        intent.putExtra(
            "selected_skill",
            selectedGithubSkill
        )

        githubVerificationLauncher.launch(
            intent
        )
    }

    // ------------------------------------------------
    // Evidence Options
    // ------------------------------------------------

    private fun showEvidenceOptions(
        evidence: Evidence,
        position: Int
    ) {

        val options =
            if (
                evidence.status.equals(
                    "Verified",
                    ignoreCase = true
                )
            ) {
                arrayOf(
                    "Open Evidence Link"
                )
            } else {
                arrayOf(
                    "Mark as Verified",
                    "Open Evidence Link"
                )
            }

        AlertDialog.Builder(this)
            .setTitle(
                evidence.title
            )
            .setItems(
                options
            ) { _, which ->

                if (
                    evidence.status.equals(
                        "Verified",
                        ignoreCase = true
                    )
                ) {

                    if (which == 0) {
                        openEvidenceLink(
                            evidence
                        )
                    }

                } else {

                    when (which) {

                        0 -> {
                            verifyEvidence(
                                evidence,
                                position
                            )
                        }

                        1 -> {
                            openEvidenceLink(
                                evidence
                            )
                        }
                    }
                }
            }
            .show()
    }

    // ------------------------------------------------
    // Mark Evidence as Verified
    // ------------------------------------------------

    private fun verifyEvidence(
        evidence: Evidence,
        position: Int
    ) {

        databaseHelper.updateEvidenceStatus(
            evidence.link,
            "Verified"
        )

        val updatedEvidence =
            evidence.copy(
                status = "Verified"
            )

        evidenceAdapter.updateEvidence(
            position,
            updatedEvidence
        )

        Toast.makeText(
            this,
            "Evidence marked as verified.",
            Toast.LENGTH_SHORT
        ).show()

        loadEvidence()
    }

    // ------------------------------------------------
    // Open Evidence Link
    // ------------------------------------------------

    private fun openEvidenceLink(
        evidence: Evidence
    ) {

        var url =
            evidence.link.trim()

        if (
            !url.startsWith("http://") &&
            !url.startsWith("https://")
        ) {
            url =
                "https://$url"
        }

        try {

            val intent =
                Intent(
                    Intent.ACTION_VIEW,
                    android.net.Uri.parse(url)
                )

            startActivity(intent)

        } catch (e: Exception) {

            Toast.makeText(
                this,
                "Unable to open evidence link.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}