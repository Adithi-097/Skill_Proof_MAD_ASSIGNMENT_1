package com.example.skill_proof_mad_assignment_1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SkillAdapter(
    private val skillList: MutableList<Skill>
) : RecyclerView.Adapter<SkillAdapter.SkillViewHolder>() {

    class SkillViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val tvSkillName: TextView =
            itemView.findViewById(R.id.tvSkillName)

        val tvSkillLevel: TextView =
            itemView.findViewById(R.id.tvSkillLevel)

        val tvSkillProgress: TextView =
            itemView.findViewById(R.id.tvSkillProgress)

        val progressSkill: ProgressBar =
            itemView.findViewById(R.id.progressSkill)

        val tvSkillProofStatus: TextView =
            itemView.findViewById(R.id.tvSkillProofStatus)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SkillViewHolder {

        val view =
            LayoutInflater.from(parent.context)
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

        val skill =
            skillList[position]

        // Skill name
        holder.tvSkillName.text =
            skill.name

        // Skill level
        holder.tvSkillLevel.text =
            "Level: ${skill.level}"

        // Progress
        val progress =
            skill.progress.coerceIn(0, 100)

        holder.tvSkillProgress.text =
            "$progress%"

        holder.progressSkill.progress =
            progress

        // Proof status
        holder.tvSkillProofStatus.text =
            when (
                skill.proofStatus.lowercase()
            ) {

                "verified" ->
                    "✓ Verified"

                else ->
                    "● Not verified"
            }
    }

    override fun getItemCount(): Int =
        skillList.size

    fun addSkill(
        skill: Skill
    ) {

        skillList.add(skill)

        notifyItemInserted(
            skillList.lastIndex
        )
    }

    fun updateSkills(
        skills: List<Skill>
    ) {

        skillList.clear()

        skillList.addAll(
            skills
        )

        notifyDataSetChanged()
    }
}