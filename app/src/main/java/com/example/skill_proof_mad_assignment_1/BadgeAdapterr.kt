package com.example.skill_proof_mad_assignment_1

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BadgeAdapter(
    private val badgeList: List<Badge>
) : RecyclerView.Adapter<BadgeAdapter.BadgeViewHolder>() {

    class BadgeViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val tvBadgeIcon: TextView =
            itemView.findViewById(R.id.tvBadgeIcon)

        val tvBadgeName: TextView =
            itemView.findViewById(R.id.tvBadgeName)

        val tvBadgeDescription: TextView =
            itemView.findViewById(R.id.tvBadgeDescription)

        val tvBadgeStatus: TextView =
            itemView.findViewById(R.id.tvBadgeStatus)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BadgeViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_badge, parent, false)

        return BadgeViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: BadgeViewHolder,
        position: Int
    ) {

        val badge = badgeList[position]

        holder.tvBadgeIcon.text = badge.icon
        holder.tvBadgeName.text = badge.name
        holder.tvBadgeDescription.text = badge.description

        if (badge.unlocked) {

            holder.tvBadgeStatus.text = "UNLOCKED ✓"
            holder.tvBadgeStatus.setTextColor(
                Color.parseColor("#2E7D32")
            )

            holder.itemView.alpha = 1.0f

        } else {

            holder.tvBadgeStatus.text = "LOCKED 🔒"
            holder.tvBadgeStatus.setTextColor(
                Color.parseColor("#757575")
            )

            holder.itemView.alpha = 0.55f
        }
    }

    override fun getItemCount(): Int {
        return badgeList.size
    }
}