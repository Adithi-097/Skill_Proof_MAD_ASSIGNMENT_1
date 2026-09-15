package com.example.skill_proof_mad_assignment_1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AssessmentHistoryAdapter(
    private val assessments: MutableList<Assessment>
) : RecyclerView.Adapter<AssessmentHistoryAdapter.AssessmentViewHolder>() {

    class AssessmentViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val tvSkill: TextView =
            itemView.findViewById(R.id.tvSkill)

        val tvScore: TextView =
            itemView.findViewById(R.id.tvScore)

        val tvPercentage: TextView =
            itemView.findViewById(R.id.tvPercentage)

        val tvPerformance: TextView =
            itemView.findViewById(R.id.tvPerformance)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AssessmentViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_assessment_history,
                parent,
                false
            )

        return AssessmentViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: AssessmentViewHolder,
        position: Int
    ) {

        val assessment = assessments[position]

        holder.tvSkill.text = assessment.skill

        holder.tvScore.text =
            "${assessment.score} / ${assessment.total} Correct"

        holder.tvPercentage.text =
            "${assessment.percentage}%"

        holder.tvPerformance.text =
            getPerformance(assessment.percentage)
    }

    override fun getItemCount(): Int {
        return assessments.size
    }

    fun updateAssessments(
        newAssessments: List<Assessment>
    ) {

        assessments.clear()
        assessments.addAll(newAssessments)

        notifyDataSetChanged()
    }

    private fun getPerformance(
        percentage: Int
    ): String {

        return when {
            percentage >= 90 -> "Excellent"
            percentage >= 75 -> "Very Good"
            percentage >= 60 -> "Good"
            percentage >= 40 -> "Needs Improvement"
            else -> "Beginner"
        }
    }
}