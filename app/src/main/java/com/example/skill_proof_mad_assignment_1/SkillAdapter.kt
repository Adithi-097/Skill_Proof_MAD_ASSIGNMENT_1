package com.example.skill_proof_mad_assignment_1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SkillAdapter(
    private val skills: MutableList<Skill>
) : RecyclerView.Adapter<SkillAdapter.SkillViewHolder>() {

    class SkillViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val tvSkillName: TextView =
            itemView.findViewById(R.id.tvSkillName)

        val tvSkillLevel: TextView =
            itemView.findViewById(R.id.tvSkillLevel)

        val skillProgress: ProgressBar =
            itemView.findViewById(R.id.skillProgress)

        val tvProgressValue: TextView =
            itemView.findViewById(R.id.tvProgressValue)

        val tvProofStatus: TextView =
            itemView.findViewById(R.id.tvProofStatus)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SkillViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_skill,
                parent,
                false
            )

        return SkillViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: SkillViewHolder,
        position: Int
    ) {

        val skill = skills[position]

        holder.tvSkillName.text = skill.name
        holder.tvSkillLevel.text = skill.level

        holder.skillProgress.progress =
            skill.progress

        holder.tvProgressValue.text =
            "${skill.progress}% skill level"

        holder.tvProofStatus.text =
            "Proof status: ${skill.proofStatus}"
    }

    override fun getItemCount(): Int {
        return skills.size
    }

    fun addSkill(skill: Skill) {

        skills.add(skill)

        notifyItemInserted(skills.lastIndex)
    }
}