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

class EvidenceActivity : AppCompatActivity() {

    private lateinit var evidenceAdapter: EvidenceAdapter
    private lateinit var evidenceList: MutableList<Evidence>

    private lateinit var tvEvidenceCount: TextView
    private lateinit var tvVerifiedCount: TextView

    private val addEvidenceLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->

            if (result.resultCode != RESULT_OK) return@registerForActivityResult

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

            val evidence = Evidence(
                title = title,
                skill = skill,
                type = type,
                link = link,
                status = status
            )

            evidenceAdapter.addEvidence(evidence)

            updateCounts()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_evidence)

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

        evidenceList = mutableListOf()

        evidenceAdapter =
            EvidenceAdapter(evidenceList)

        recyclerEvidence.layoutManager =
            LinearLayoutManager(this)

        recyclerEvidence.adapter =
            evidenceAdapter

        updateCounts()

        btnBack.setOnClickListener {
            finish()
        }

        btnAddEvidence.setOnClickListener {

            val intent = Intent(
                this,
                AddEvidenceActivity::class.java
            )

            addEvidenceLauncher.launch(intent)
        }
    }

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
}