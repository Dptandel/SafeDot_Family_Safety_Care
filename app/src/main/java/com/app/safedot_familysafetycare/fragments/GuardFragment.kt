package com.app.safedot_familysafetycare.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.safedot_familysafetycare.adapters.InvitesAdapter
import com.app.safedot_familysafetycare.databinding.FragmentGuardBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class GuardFragment : Fragment(), InvitesAdapter.OnActionClick {

    private lateinit var binding: FragmentGuardBinding

    private lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGuardBinding.inflate(inflater, container, false)

        binding.sendInvite.setOnClickListener {
            sendInvite()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getInvites()
    }

    private fun getInvites() {
        val firestore = Firebase.firestore

        firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.email.toString())
            .collection("invites").get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val list: ArrayList<String> = ArrayList()
                    for (item in it.result) {
                        if (item.get("invite_status") == 0L) {
                            list.add(item.id)
                        }
                    }

                    Log.d("TAG", "getInvites: $list")

                    val adapter = InvitesAdapter(mContext, list, this)
                    binding.rvYourInvites.layoutManager = LinearLayoutManager(mContext)
                    binding.rvYourInvites.adapter = adapter
                }
            }
    }

    private fun sendInvite() {
        val mail = binding.inviteEmail.text.toString()

        Log.d("TAG", "sendInvite: $mail")

        val firestore = Firebase.firestore

        val data = hashMapOf(
            "invite_status" to 0
        )

        val senderMail = FirebaseAuth.getInstance().currentUser?.email.toString()

        firestore.collection("users")
            .document(mail)
            .collection("invites")
            .document(senderMail).set(data)
            .addOnSuccessListener {

            }
            .addOnFailureListener {

            }
    }

    companion object {

        @JvmStatic
        fun newInstance() = GuardFragment()
    }

    override fun onAcceptClick(mail: String) {
        Log.d("TAG", "onAcceptClick: $mail")

        val firestore = Firebase.firestore

        val data = hashMapOf(
            "invite_status" to 1
        )

        val senderMail = FirebaseAuth.getInstance().currentUser?.email.toString()

        firestore.collection("users")
            .document(senderMail)
            .collection("invites")
            .document(mail).set(data)
            .addOnSuccessListener {

            }
            .addOnFailureListener {

            }
    }

    override fun onDenyClick(mail: String) {
        Log.d("TAG", "onDenyClick: $mail")

        val firestore = Firebase.firestore

        val data = hashMapOf(
            "invite_status" to -1
        )

        val senderMail = FirebaseAuth.getInstance().currentUser?.email.toString()

        firestore.collection("users")
            .document(senderMail)
            .collection("invites")
            .document(mail).set(data)
            .addOnSuccessListener {

            }
            .addOnFailureListener {

            }
    }
}