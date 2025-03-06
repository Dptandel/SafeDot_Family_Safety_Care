package com.app.safedot_familysafetycare.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.safedot_familysafetycare.databinding.ItemMemberBinding
import com.app.safedot_familysafetycare.models.Member

class MembersAdapter(private val context: Context, private val membersList: MutableList<Pair<String,String>>) :
    RecyclerView.Adapter<MembersAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemMemberBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MembersAdapter.ViewHolder {
        val binding = ItemMemberBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MembersAdapter.ViewHolder, position: Int) {
        val member = membersList[position]
        holder.binding.name.text = member.second
    }

    override fun getItemCount(): Int = membersList.size

    fun updateMembers(newLit: List<Pair<String,String>>) {
        membersList.clear()
        membersList.addAll(newLit)
        notifyDataSetChanged()
    }
}