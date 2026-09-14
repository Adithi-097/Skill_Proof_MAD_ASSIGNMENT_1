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

    private lateinit var tvEvidenceCount: TextView
    private lateinit var tvVerifiedCount: TextView

    private lateinit var databaseHelper: DatabaseHelper

    // ADD EVIDENCE RESULT
    private val addEvidenceLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->

            if (result.resultCode != RESULT_OK) {
                return@registerForActivityResult
            }

            val data = result.data
                ?: return@registerForActivityResult

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

            // SAVE TO SQLITE
            databaseHelper.insertEvidence(
                title,
                skill,
                type,
                link,
                status
            )

            // CREATE NEW EVIDENCE OBJECT
            val evidence =
                Evidence(
                    title,
                    skill,
                    type,
                    link,
                    status
                )

            // ADD TO RECYCLERVIEW
            evidenceAdapter.addEvidence(
                evidence
            )

            // UPDATE COUNTS
            updateCounts()
        }


    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_evidence
        )

        // DATABASE
        databaseHelper =
            DatabaseHelper(this)


        // FIND VIEWS

        val btnBack =
            findViewById<ImageButton>(
                R.id.btnBack
            )

        val btnAddEvidence =
            findViewById<MaterialButton>(
                R.id.btnAddEvidence
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


        // LOAD EVIDENCE FROM SQLITE

        evidenceList =
            databaseHelper.getAllEvidence()


        // CREATE ADAPTER

        evidenceAdapter =
            EvidenceAdapter(
                evidenceList
            ) { evidence, position ->

                showVerificationDialog(
                    evidence,
                    position
                )
            }


        // RECYCLERVIEW

        recyclerEvidence.layoutManager =
            LinearLayoutManager(this)

        recyclerEvidence.adapter =
            evidenceAdapter


        // UPDATE COUNTS

        updateCounts()


        // BACK BUTTON

        btnBack.setOnClickListener {

            finish()
        }


        // ADD EVIDENCE BUTTON

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
    }


    // UPDATE EVIDENCE COUNTS

    private fun updateCounts() {

        tvEvidenceCount.text =
            evidenceList.size.toString()


        val verifiedCount =
            evidenceList.count {

                it.status == "Verified"
            }


        tvVerifiedCount.text =
            verifiedCount.toString()
    }


    // VERIFY EVIDENCE DIALOG

    private fun showVerificationDialog(
        evidence: Evidence,
        position: Int
    ) {

        // ALREADY VERIFIED

        if (evidence.status == "Verified") {

            Toast.makeText(
                this,
                "This evidence is already verified",
                Toast.LENGTH_SHORT
            ).show()

            return
        }


        // CONFIRMATION DIALOG

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


                // UPDATE SQLITE

                val updatedRows =
                    databaseHelper.updateEvidenceStatus(
                        evidence.title,
                        "Verified"
                    )


                if (updatedRows > 0) {

                    // UPDATED EVIDENCE OBJECT

                    val updatedEvidence =
                        Evidence(
                            evidence.title,
                            evidence.skill,
                            evidence.type,
                            evidence.link,
                            "Verified"
                        )


                    // UPDATE RECYCLERVIEW

                    evidenceAdapter.updateEvidence(
                        position,
                        updatedEvidence
                    )


                    // UPDATE COUNTS

                    updateCounts()


                    // SUCCESS MESSAGE

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