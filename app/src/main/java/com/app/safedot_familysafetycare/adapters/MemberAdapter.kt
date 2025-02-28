package com.app.safedot_familysafetycare.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.safedot_familysafetycare.databinding.ItemMemberBinding
import com.app.safedot_familysafetycare.models.Member

class MemberAdapter(private val context: Context, private val membersList: List<Member>) :
    RecyclerView.Adapter<MemberAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemMemberBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberAdapter.ViewHolder {
        val binding = ItemMemberBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MemberAdapter.ViewHolder, position: Int) {
        val member = membersList[position]
        holder.binding.name.text = member.name
    }

    override fun getItemCount(): Int = membersList.size
}