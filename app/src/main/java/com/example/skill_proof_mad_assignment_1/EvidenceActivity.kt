package com.example.skill_proof_mad_assignment_1

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class EvidenceActivity : AppCompatActivity() {

    // =========================================================
    // VARIABLES
    // =========================================================

    private lateinit var evidenceAdapter: EvidenceAdapter
    private lateinit var evidenceList: MutableList<Evidence>
    private lateinit var databaseHelper: DatabaseHelper

    private lateinit var tvEvidenceCount: TextView
    private lateinit var tvVerifiedCount: TextView


    // =========================================================
    // ADD NORMAL EVIDENCE RESULT
    // =========================================================

    private val addEvidenceLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->

            // Check whether AddEvidenceActivity returned successfully
            if (result.resultCode != RESULT_OK) {
                return@registerForActivityResult
            }

            val data =
                result.data
                    ?: return@registerForActivityResult


            // -----------------------------------------------------
            // Get Evidence Title
            // -----------------------------------------------------

            val title =
                data.getStringExtra("evidence_title")
                    ?: return@registerForActivityResult


            // -----------------------------------------------------
            // Get Skill
            // -----------------------------------------------------

            val skill =
                data.getStringExtra("evidence_skill")
                    ?: return@registerForActivityResult


            // -----------------------------------------------------
            // Get Evidence Type
            // -----------------------------------------------------

            val type =
                data.getStringExtra("evidence_type")
                    ?: return@registerForActivityResult


            // -----------------------------------------------------
            // Get Link
            // -----------------------------------------------------

            val link =
                data.getStringExtra("evidence_link")
                    ?: return@registerForActivityResult


            // -----------------------------------------------------
            // Get Status
            // -----------------------------------------------------

            val status =
                data.getStringExtra("evidence_status")
                    ?: "Pending"


            // -----------------------------------------------------
            // Get Attachment URI
            // -----------------------------------------------------

            val attachmentUri =
                data.getStringExtra(
                    "evidence_attachment_uri"
                ) ?: ""


            // -----------------------------------------------------
            // SAVE TO DATABASE
            // -----------------------------------------------------

            databaseHelper.insertEvidence(
                title = title,
                skill = skill,
                type = type,
                link = link,
                status = status,
                attachmentUri = attachmentUri
            )


            // -----------------------------------------------------
            // CREATE Evidence OBJECT
            // -----------------------------------------------------

            val evidence =
                Evidence(
                    title = title,
                    skill = skill,
                    type = type,
                    link = link,
                    status = status,
                    attachmentUri = attachmentUri
                )


            // -----------------------------------------------------
            // ADD TO LIST
            // -----------------------------------------------------

            evidenceList.add(evidence)


            // -----------------------------------------------------
            // UPDATE RECYCLER VIEW
            // -----------------------------------------------------

            evidenceAdapter.notifyItemInserted(
                evidenceList.lastIndex
            )


            // -----------------------------------------------------
            // UPDATE COUNTS
            // -----------------------------------------------------

            updateCounts()
        }


    // =========================================================
    // GITHUB VERIFICATION RESULT
    // =========================================================

    private val githubVerificationLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->

            // Check whether GitHub verification returned successfully
            if (result.resultCode != RESULT_OK) {
                return@registerForActivityResult
            }

            val data =
                result.data
                    ?: return@registerForActivityResult


            // -----------------------------------------------------
            // Check whether GitHub repository was verified
            // -----------------------------------------------------

            val verified =
                data.getBooleanExtra(
                    "github_verified",
                    false
                )

            if (!verified) {
                return@registerForActivityResult
            }


            // -----------------------------------------------------
            // Get Repository Information
            // -----------------------------------------------------

            val repository =
                data.getStringExtra(
                    "github_repository"
                ) ?: ""


            val owner =
                data.getStringExtra(
                    "github_owner"
                ) ?: ""


            val githubUrl =
                data.getStringExtra(
                    "github_url"
                ) ?: ""


            val language =
                data.getStringExtra(
                    "github_language"
                ) ?: "Not specified"


            // -----------------------------------------------------
            // CHECK WHETHER USER HAS A SKILL
            // -----------------------------------------------------

            val skills =
                databaseHelper.getAllSkills()

            if (skills.isEmpty()) {

                Toast.makeText(
                    this,
                    "Please add a skill before adding GitHub evidence.",
                    Toast.LENGTH_LONG
                ).show()

                return@registerForActivityResult
            }


            // -----------------------------------------------------
            // CURRENT SKILL
            // -----------------------------------------------------
            //
            // For the current MVP, the first skill is used.
            //
            // Later we can add a skill-selection dialog.
            // -----------------------------------------------------

            val currentSkill =
                skills.first()

            val skillName =
                currentSkill.name


            // -----------------------------------------------------
            // CREATE GITHUB EVIDENCE
            // -----------------------------------------------------

            val evidenceTitle =
                "GitHub: $repository"


            val evidenceType =
                "GitHub Repository"


            val evidenceLink =
                if (githubUrl.isNotEmpty()) {

                    githubUrl

                } else {

                    "https://github.com/$owner/$repository"
                }


            // -----------------------------------------------------
            // SAVE GITHUB EVIDENCE TO DATABASE
            // -----------------------------------------------------

            databaseHelper.insertEvidence(
                title = evidenceTitle,
                skill = skillName,
                type = evidenceType,
                link = evidenceLink,
                status = "Verified",
                attachmentUri = ""
            )


            // -----------------------------------------------------
            // CREATE Evidence OBJECT
            // -----------------------------------------------------

            val githubEvidence =
                Evidence(
                    title = evidenceTitle,
                    skill = skillName,
                    type = evidenceType,
                    link = evidenceLink,
                    status = "Verified",
                    attachmentUri = ""
                )


            // -----------------------------------------------------
            // ADD TO LIST
            // -----------------------------------------------------

            evidenceList.add(
                githubEvidence
            )


            // -----------------------------------------------------
            // UPDATE RECYCLER VIEW
            // -----------------------------------------------------

            evidenceAdapter.notifyItemInserted(
                evidenceList.lastIndex
            )


            // -----------------------------------------------------
            // UPDATE COUNTS
            // -----------------------------------------------------

            updateCounts()


            // -----------------------------------------------------
            // SUCCESS MESSAGE
            // -----------------------------------------------------

            Toast.makeText(
                this,
                "GitHub repository verified and added as evidence.",
                Toast.LENGTH_LONG
            ).show()
        }


    // =========================================================
    // ON CREATE
    // =========================================================

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(
            savedInstanceState
        )

        // -----------------------------------------------------
        // SET XML
        // -----------------------------------------------------

        setContentView(
            R.layout.activity_evidence
        )


        // -----------------------------------------------------
        // DATABASE
        // -----------------------------------------------------

        databaseHelper =
            DatabaseHelper(this)


        // =====================================================
        // FIND VIEWS
        // =====================================================

        val btnBack =
            findViewById<ImageButton>(
                R.id.btnBack
            )


        val btnAddEvidence =
            findViewById<MaterialButton>(
                R.id.btnAddEvidence
            )


        val btnGithubVerification =
            findViewById<MaterialButton>(
                R.id.btnGithubVerification
            )


        tvEvidenceCount =
            findViewById(
                R.id.tvEvidenceCount
            )


        tvVerifiedCount =
            findViewById(
                R.id.tvVerifiedCount
            )


        val recyclerEvidence =
            findViewById<RecyclerView>(
                R.id.recyclerEvidence
            )


        // =====================================================
        // LOAD DATABASE EVIDENCE
        // =====================================================

        evidenceList =
            databaseHelper.getAllEvidence()


        // =====================================================
        // CREATE ADAPTER
        // =====================================================

        evidenceAdapter =
            EvidenceAdapter(
                evidenceList
            ) { evidence, position ->

                showVerificationDialog(
                    evidence,
                    position
                )
            }


        // =====================================================
        // RECYCLER VIEW
        // =====================================================

        recyclerEvidence.layoutManager =
            LinearLayoutManager(this)

        recyclerEvidence.adapter =
            evidenceAdapter


        // =====================================================
        // INITIAL COUNTS
        // =====================================================

        updateCounts()


        // =====================================================
        // BACK BUTTON
        // =====================================================

        btnBack.setOnClickListener {

            finish()
        }


        // =====================================================
        // ADD NORMAL EVIDENCE
        // =====================================================

        btnAddEvidence.setOnClickListener {

            val intent =
                Intent(
                    this,
                    AddEvidenceActivity::class.java
                )

            addEvidenceLauncher.launch(
                intent
            )
        }


        // =====================================================
        // GITHUB VERIFICATION
        // =====================================================

        btnGithubVerification.setOnClickListener {

            // -------------------------------------------------
            // First check whether at least one skill exists
            // -------------------------------------------------

            val skills =
                databaseHelper.getAllSkills()


            if (skills.isEmpty()) {

                Toast.makeText(
                    this,
                    "Please add a skill first.",
                    Toast.LENGTH_LONG
                ).show()

                return@setOnClickListener
            }


            // -------------------------------------------------
            // Open GitHub Verification Activity
            // -------------------------------------------------

            val intent =
                Intent(
                    this,
                    GitHubVerificationActivity::class.java
                )


            githubVerificationLauncher.launch(
                intent
            )
        }
    }


    // =========================================================
    // ON RESUME
    // =========================================================
    //
    // This makes sure the Evidence screen gets the latest
    // database information whenever we return to it.
    // =========================================================

    override fun onResume() {

        super.onResume()


        if (
            ::databaseHelper.isInitialized &&
            ::evidenceAdapter.isInitialized
        ) {

            reloadEvidence()
        }
    }


    // =========================================================
    // RELOAD EVIDENCE
    // =========================================================

    private fun reloadEvidence() {

        val latestEvidence =
            databaseHelper.getAllEvidence()


        // Clear old data
        evidenceList.clear()


        // Add latest database data
        evidenceList.addAll(
            latestEvidence
        )


        // Refresh RecyclerView
        evidenceAdapter.notifyDataSetChanged()


        // Refresh counts
        updateCounts()
    }


    // =========================================================
    // UPDATE COUNTS
    // =========================================================

    private fun updateCounts() {

        val totalCount =
            evidenceList.size


        val verifiedCount =
            evidenceList.count {

                it.status == "Verified"
            }


        tvEvidenceCount.text =
            "Total Evidence: $totalCount"


        tvVerifiedCount.text =
            "Verified Evidence: $verifiedCount"
    }


    // =========================================================
    // MANUAL VERIFICATION DIALOG
    // =========================================================

    private fun showVerificationDialog(
        evidence: Evidence,
        position: Int
    ) {

        // -----------------------------------------------------
        // Already verified
        // -----------------------------------------------------

        if (
            evidence.status == "Verified"
        ) {

            Toast.makeText(
                this,
                "This evidence is already verified.",
                Toast.LENGTH_SHORT
            ).show()

            return
        }


        // -----------------------------------------------------
        // Confirmation dialog
        // -----------------------------------------------------

        AlertDialog.Builder(this)

            .setTitle(
                "Verify Evidence"
            )

            .setMessage(
                "Do you want to mark \"${evidence.title}\" as verified?"
            )

            .setNegativeButton(
                "Cancel",
                null
            )

            .setPositiveButton(
                "Verify"
            ) { _, _ ->


                // -------------------------------------------------
                // UPDATE DATABASE
                // -------------------------------------------------

                val updatedRows =
                    databaseHelper.updateEvidenceStatus(
                        evidence.title,
                        "Verified"
                    )


                if (
                    updatedRows > 0
                ) {

                    // -------------------------------------------------
                    // CREATE UPDATED OBJECT
                    // -------------------------------------------------

                    val updatedEvidence =
                        Evidence(
                            title = evidence.title,
                            skill = evidence.skill,
                            type = evidence.type,
                            link = evidence.link,
                            status = "Verified",
                            attachmentUri =
                                evidence.attachmentUri
                        )


                    // -------------------------------------------------
                    // UPDATE ADAPTER
                    // -------------------------------------------------

                    evidenceAdapter.updateEvidence(
                        position,
                        updatedEvidence
                    )


                    // -------------------------------------------------
                    // UPDATE LOCAL LIST
                    // -------------------------------------------------

                    if (
                        position >= 0 &&
                        position < evidenceList.size
                    ) {

                        evidenceList[position] =
                            updatedEvidence
                    }


                    // -------------------------------------------------
                    // UPDATE COUNTS
                    // -------------------------------------------------

                    updateCounts()


                    // -------------------------------------------------
                    // SUCCESS
                    // -------------------------------------------------

                    Toast.makeText(
                        this,
                        "Evidence verified successfully.",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {

                    // -------------------------------------------------
                    // FAILURE
                    // -------------------------------------------------

                    Toast.makeText(
                        this,
                        "Unable to verify evidence.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            .show()
    }
}