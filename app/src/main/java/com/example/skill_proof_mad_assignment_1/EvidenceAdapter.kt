package com.example.skill_proof_mad_assignment_1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EvidenceAdapter(
    private val evidenceList: MutableList<Evidence>
) : RecyclerView.Adapter<EvidenceAdapter.EvidenceViewHolder>() {

    class EvidenceViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val tvEvidenceTitle: TextView =
            itemView.findViewById(R.id.tvEvidenceTitle)

        val tvEvidenceStatus: TextView =
            itemView.findViewById(R.id.tvEvidenceStatus)

        val tvEvidenceType: TextView =
            itemView.findViewById(R.id.tvEvidenceType)

        val tvEvidenceSkill: TextView =
            itemView.findViewById(R.id.tvEvidenceSkill)

        val tvEvidenceLink: TextView =
            itemView.findViewById(R.id.tvEvidenceLink)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EvidenceViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_evidence,
                parent,
                false
            )

        return EvidenceViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: EvidenceViewHolder,
        position: Int
    ) {

        val evidence = evidenceList[position]

        holder.tvEvidenceTitle.text =
            evidence.title

        holder.tvEvidenceStatus.text =
            evidence.status

        holder.tvEvidenceType.text =
            evidence.type

        holder.tvEvidenceSkill.text =
            "Skill: ${evidence.skill}"

        holder.tvEvidenceLink.text =
            evidence.link
    }

    override fun getItemCount(): Int {
        return evidenceList.size
    }

    fun addEvidence(evidence: Evidence) {

        evidenceList.add(evidence)

        notifyItemInserted(evidenceList.lastIndex)
    }
}