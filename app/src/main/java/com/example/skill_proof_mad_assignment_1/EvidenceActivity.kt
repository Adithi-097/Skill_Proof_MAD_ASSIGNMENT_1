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

    private lateinit var evidenceAdapter: EvidenceAdapter
    private lateinit var evidenceList: MutableList<Evidence>
    private lateinit var databaseHelper: DatabaseHelper

    private lateinit var tvEvidenceCount: TextView
    private lateinit var tvVerifiedCount: TextView

    private val addEvidenceLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->

            if (result.resultCode != RESULT_OK) {
                return@registerForActivityResult
            }

            val data = result.data ?: return@registerForActivityResult

            val title =
                data.getStringExtra("evidence_title")
                    ?: return@registerForActivityResult

            val skill =
                data.getStringExtra("evidence_skill")
                    ?: return@registerForActivityResult

            val type =
                data.getStringExtra("evidence_type")
                    ?: return@registerForActivityResult

            val link =
                data.getStringExtra("evidence_link")
                    ?: return@registerForActivityResult

            val status =
                data.getStringExtra("evidence_status")
                    ?: "Pending"

            val attachmentUri =
                data.getStringExtra("evidence_attachment_uri")
                    ?: ""

            // Save evidence in SQLite
            databaseHelper.insertEvidence(
                title = title,
                skill = skill,
                type = type,
                link = link,
                status = status,
                attachmentUri = attachmentUri
            )

            // Add new evidence to RecyclerView
            val evidence = Evidence(
                title = title,
                skill = skill,
                type = type,
                link = link,
                status = status,
                attachmentUri = attachmentUri
            )

            evidenceAdapter.addEvidence(evidence)

            updateCounts()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_evidence)

        databaseHelper = DatabaseHelper(this)

        // Views
        val btnBack =
            findViewById<ImageButton>(R.id.btnBack)

        val btnAddEvidence =
            findViewById<MaterialButton>(R.id.btnAddEvidence)

        tvEvidenceCount =
            findViewById(R.id.tvEvidenceCount)

        tvVerifiedCount =
            findViewById(R.id.tvVerifiedCount)

        val recyclerEvidence =
            findViewById<RecyclerView>(R.id.recyclerEvidence)

        // Load evidence from SQLite
        evidenceList =
            databaseHelper.getAllEvidence()

        // Adapter
        evidenceAdapter =
            EvidenceAdapter(evidenceList) { evidence, position ->
                showVerificationDialog(evidence, position)
            }

        // RecyclerView
        recyclerEvidence.layoutManager =
            LinearLayoutManager(this)

        recyclerEvidence.adapter =
            evidenceAdapter

        updateCounts()

        // Back button
        btnBack.setOnClickListener {
            finish()
        }

        // Add Evidence button
        btnAddEvidence.setOnClickListener {

            val intent =
                Intent(
                    this,
                    AddEvidenceActivity::class.java
                )

            addEvidenceLauncher.launch(intent)
        }
    }

    private fun updateCounts() {

        // Total evidence
        tvEvidenceCount.text =
            evidenceList.size.toString()

        // Verified evidence
        val verifiedCount =
            evidenceList.count {
                it.status == "Verified"
            }

        tvVerifiedCount.text =
            verifiedCount.toString()
    }

    private fun showVerificationDialog(
        evidence: Evidence,
        position: Int
    ) {

        // Already verified
        if (evidence.status == "Verified") {

            Toast.makeText(
                this,
                "This evidence is already verified",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        AlertDialog.Builder(this)
            .setTitle("Verify Evidence")
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

                val updatedRows =
                    databaseHelper.updateEvidenceStatus(
                        evidence.title,
                        "Verified"
                    )

                if (updatedRows > 0) {

                    val updatedEvidence =
                        Evidence(
                            title = evidence.title,
                            skill = evidence.skill,
                            type = evidence.type,
                            link = evidence.link,
                            status = "Verified",
                            attachmentUri = evidence.attachmentUri
                        )

                    evidenceAdapter.updateEvidence(
                        position,
                        updatedEvidence
                    )

                    updateCounts()

                    Toast.makeText(
                        this,
                        "Evidence verified successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {

                    Toast.makeText(
                        this,
                        "Unable to verify evidence",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .show()
    }
}