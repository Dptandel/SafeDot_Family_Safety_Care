package com.app.safedot_familysafetycare.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.safedot_familysafetycare.databinding.ItemInviteBinding
import com.app.safedot_familysafetycare.models.Contact

class InviteAdapter(private val context: Context, private val contactsList: List<Contact>) :
    RecyclerView.Adapter<InviteAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemInviteBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InviteAdapter.ViewHolder {
        val binding = ItemInviteBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InviteAdapter.ViewHolder, position: Int) {
        val member = contactsList[position]
        holder.binding.name.text = member.name
    }

    override fun getItemCount(): Int = contactsList.size
}