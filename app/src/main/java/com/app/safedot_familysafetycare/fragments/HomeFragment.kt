package com.app.safedot_familysafetycare.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.safedot_familysafetycare.adapters.MembersAdapter
import com.app.safedot_familysafetycare.databinding.FragmentHomeBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var membersAdapter: MembersAdapter
    private lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // For Members
        membersAdapter = MembersAdapter(mContext, mutableListOf())
        binding.rvMembers.layoutManager = LinearLayoutManager(mContext)
        binding.rvMembers.adapter = membersAdapter

        loadMembers()
    }

    private fun loadMembers() {
        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email.toString()
        Firebase.firestore.collection("users")
            .document(currentUserEmail)
            .collection("members")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val membersList = mutableListOf<Pair<String, String>>()
                for (document in querySnapshot) {
                    val name = document.getString("name") ?: "Unknown"
                    membersList.add(Pair(document.id, name))
                }
                membersAdapter.updateMembers(membersList)
            }
            .addOnFailureListener { exception ->
                Log.e("TAG", "Error loading members: $exception")
            }
    }

    fun refreshMembers() {
        loadMembers()
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}