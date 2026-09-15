package com.example.skill_proof_mad_assignment_1

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class AssessmentHistoryActivity : BaseActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var assessmentAdapter: AssessmentHistoryAdapter

    private lateinit var btnBack: MaterialButton
    private lateinit var tvTitle: TextView
    private lateinit var tvSubtitle: TextView
    private lateinit var tvEmptyHistory: TextView
    private lateinit var recyclerHistory: RecyclerView
    private lateinit var emptyHistoryCard: MaterialCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_assessment_history)

        databaseHelper = DatabaseHelper(this)

        // =========================================
        // FIND VIEWS
        // =========================================

        btnBack =
            findViewById(R.id.btnBack)

        tvTitle =
            findViewById(R.id.tvTitle)

        tvSubtitle =
            findViewById(R.id.tvSubtitle)

        tvEmptyHistory =
            findViewById(R.id.tvEmptyHistory)

        recyclerHistory =
            findViewById(R.id.recyclerHistory)

        emptyHistoryCard =
            findViewById(R.id.emptyHistoryCard)

        // =========================================
        // BACK BUTTON
        // =========================================

        btnBack.setOnClickListener {
            finish()
        }

        // =========================================
        // RECYCLER VIEW
        // =========================================

        assessmentAdapter =
            AssessmentHistoryAdapter(
                mutableListOf()
            )

        recyclerHistory.layoutManager =
            LinearLayoutManager(this)

        recyclerHistory.adapter =
            assessmentAdapter

        recyclerHistory.setHasFixedSize(false)

        // =========================================
        // LOAD HISTORY
        // =========================================

        loadAssessmentHistory()
    }

    override fun onResume() {
        super.onResume()

        if (::databaseHelper.isInitialized) {
            loadAssessmentHistory()
        }
    }

    // =============================================
    // LOAD ASSESSMENT HISTORY
    // =============================================

    private fun loadAssessmentHistory() {

        val assessments =
            databaseHelper.getAssessmentHistory()

        if (assessments.isEmpty()) {

            // =====================================
            // SHOW EMPTY STATE
            // =====================================

            emptyHistoryCard.visibility =
                View.VISIBLE

            tvEmptyHistory.visibility =
                View.VISIBLE

            recyclerHistory.visibility =
                View.GONE

        } else {

            // =====================================
            // SHOW HISTORY
            // =====================================

            emptyHistoryCard.visibility =
                View.GONE

            tvEmptyHistory.visibility =
                View.GONE

            recyclerHistory.visibility =
                View.VISIBLE

            assessmentAdapter.updateAssessments(
                assessments
            )
        }
    }
}