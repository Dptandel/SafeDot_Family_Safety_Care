package com.app.safedot_familysafetycare.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.safedot_familysafetycare.databinding.ItemInviteNameBinding

class InvitesAdapter(
    private val context: Context,
    private val invitesList: List<Pair<String, String>>,
    private val onActionClick: OnActionClick
) :
    RecyclerView.Adapter<InvitesAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemInviteNameBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InvitesAdapter.ViewHolder {
        val binding = ItemInviteNameBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InvitesAdapter.ViewHolder, position: Int) {
        val invitePair = invitesList[position]
        val inviteEmail = invitePair.first
        val inviteName = invitePair.second

        holder.binding.name.text = inviteName // Display the name

        holder.binding.accept.setOnClickListener {
            onActionClick.onAcceptClick(inviteEmail)
        }

        holder.binding.deny.setOnClickListener {
            onActionClick.onDenyClick(inviteEmail)
        }
    }

    override fun getItemCount(): Int = invitesList.size

    interface OnActionClick {
        fun onAcceptClick(mail: String)
        fun onDenyClick(mail: String)
    }
}