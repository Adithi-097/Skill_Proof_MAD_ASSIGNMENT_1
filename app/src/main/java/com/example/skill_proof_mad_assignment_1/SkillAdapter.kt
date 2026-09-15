package com.example.skill_proof_mad_assignment_1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class SkillAdapter(
    private val skillList: MutableList<Skill>,
    private val onSkillSelected: (Skill) -> Unit
) : RecyclerView.Adapter<SkillAdapter.SkillViewHolder>() {

    private var currentSkillName: String? = null

    // ---------------------------------------------
    // ViewHolder
    // ---------------------------------------------

    class SkillViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val tvSkillName: TextView =
            itemView.findViewById(
                R.id.tvSkillName
            )

        val tvSkillLevel: TextView =
            itemView.findViewById(
                R.id.tvSkillLevel
            )

        val tvSkillProgress: TextView =
            itemView.findViewById(
                R.id.tvSkillProgress
            )

        val progressSkill: ProgressBar =
            itemView.findViewById(
                R.id.progressSkill
            )

        val tvSkillProofStatus: TextView =
            itemView.findViewById(
                R.id.tvSkillProofStatus
            )

        val btnSelectSkill: MaterialButton =
            itemView.findViewById(
                R.id.btnSelectSkill
            )
    }

    // ---------------------------------------------
    // Create ViewHolder
    // ---------------------------------------------

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SkillViewHolder {

        val view =
            LayoutInflater.from(
                parent.context
            ).inflate(
                R.layout.item_skill,
                parent,
                false
            )

        return SkillViewHolder(
            view
        )
    }

    // ---------------------------------------------
    // Bind Data
    // ---------------------------------------------

    override fun onBindViewHolder(
        holder: SkillViewHolder,
        position: Int
    ) {

        val skill =
            skillList[position]

        // -----------------------------------------
        // Skill Name
        // -----------------------------------------

        holder.tvSkillName.text =
            skill.name

        // -----------------------------------------
        // Skill Level
        // -----------------------------------------

        holder.tvSkillLevel.text =
            "Level: ${skill.level}"

        // -----------------------------------------
        // Progress
        // -----------------------------------------

        val progress =
            skill.progress.coerceIn(
                0,
                100
            )

        holder.tvSkillProgress.text =
            "$progress%"

        holder.progressSkill.progress =
            progress

        // -----------------------------------------
        // Proof Status
        // -----------------------------------------

        holder.tvSkillProofStatus.text =
            when (
                skill.proofStatus.lowercase()
            ) {

                "verified" ->
                    "✓ Verified"

                else ->
                    "● Not verified"
            }

        // -----------------------------------------
        // Current Skill
        // -----------------------------------------

        val isCurrent =
            skill.name.equals(
                currentSkillName,
                ignoreCase = true
            )

        if (isCurrent) {

            holder.btnSelectSkill.text =
                "✓ Current Skill"

        } else {

            holder.btnSelectSkill.text =
                "Set as Current"
        }

        // -----------------------------------------
        // Select Skill
        // -----------------------------------------

        holder.btnSelectSkill.setOnClickListener {

            onSkillSelected(
                skill
            )
        }
    }

    // ---------------------------------------------
    // Item Count
    // ---------------------------------------------

    override fun getItemCount(): Int {

        return skillList.size
    }

    // ---------------------------------------------
    // Add Skill
    // ---------------------------------------------

    fun addSkill(
        skill: Skill
    ) {

        skillList.add(
            skill
        )

        notifyItemInserted(
            skillList.lastIndex
        )
    }

    // ---------------------------------------------
    // Update Skills
    // ---------------------------------------------

    fun updateSkills(
        skills: List<Skill>
    ) {

        skillList.clear()

        skillList.addAll(
            skills
        )

        notifyDataSetChanged()
    }

    // ---------------------------------------------
    // Set Current Skill
    // ---------------------------------------------

    fun setCurrentSkill(
        skillName: String?
    ) {

        currentSkillName =
            skillName
    }
}