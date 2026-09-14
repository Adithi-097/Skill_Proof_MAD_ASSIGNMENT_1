package com.example.skill_proof_mad_assignment_1

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class EvidenceAdapter(
    private val evidenceList: MutableList<Evidence>,
    private val onEvidenceClick: (Evidence, Int) -> Unit
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

        val view =
            LayoutInflater.from(parent.context)
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

        val evidence =
            evidenceList[position]

        // ------------------------------------------
        // DISPLAY DATA
        // ------------------------------------------

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

        // ------------------------------------------
        // OPEN LINK
        // ------------------------------------------

        holder.tvEvidenceLink.setOnClickListener {

            var url =
                evidence.link.trim()

            if (
                !url.startsWith("http://") &&
                !url.startsWith("https://")
            ) {
                url =
                    "https://$url"
            }

            try {

                val intent =
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(url)
                    )

                holder.itemView.context
                    .startActivity(intent)

            } catch (e: Exception) {

                Toast.makeText(
                    holder.itemView.context,
                    "Unable to open this link",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // ------------------------------------------
        // CLICK ENTIRE EVIDENCE CARD
        // ------------------------------------------

        holder.itemView.setOnClickListener {

            onEvidenceClick(
                evidence,
                holder.bindingAdapterPosition
            )
        }
    }

    override fun getItemCount(): Int =
        evidenceList.size

    // ------------------------------------------
    // ADD EVIDENCE
    // ------------------------------------------

    fun addEvidence(
        evidence: Evidence
    ) {

        evidenceList.add(evidence)

        notifyItemInserted(
            evidenceList.lastIndex
        )
    }

    // ------------------------------------------
    // UPDATE EVIDENCE
    // ------------------------------------------

    fun updateEvidence(
        position: Int,
        evidence: Evidence
    ) {

        if (
            position < 0 ||
            position >= evidenceList.size
        ) {
            return
        }

        evidenceList[position] =
            evidence

        notifyItemChanged(position)
    }
}