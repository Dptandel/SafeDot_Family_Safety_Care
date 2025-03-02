package com.app.safedot_familysafetycare.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.safedot_familysafetycare.databinding.ItemInviteMailBinding

class InviteMailAdapter(private val context: Context, private val invitesMailsList: List<String>, private val onActionClick: OnActionClick) :
    RecyclerView.Adapter<InviteMailAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemInviteMailBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InviteMailAdapter.ViewHolder {
        val binding = ItemInviteMailBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InviteMailAdapter.ViewHolder, position: Int) {
        val inviteMail = invitesMailsList[position]
        holder.binding.email.text = inviteMail

        holder.binding.accept.setOnClickListener {
            onActionClick.onAcceptClick(inviteMail)
        }

        holder.binding.deny.setOnClickListener {
            onActionClick.onDenyClick(inviteMail)
        }
    }

    override fun getItemCount(): Int = invitesMailsList.size

    interface OnActionClick {
        fun onAcceptClick(mail: String)
        fun onDenyClick(mail: String)
    }
}