package com.app.safedot_familysafetycare.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.app.safedot_familysafetycare.LoginActivity
import com.app.safedot_familysafetycare.R
import com.app.safedot_familysafetycare.databinding.FragmentProfileBinding
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this.requireContext(), gso)

        val auth = Firebase.auth
        val user = auth.currentUser

        if (user != null) {
            val userName = user.displayName
            val email = user.email
            val photoUrl = user.photoUrl
            binding.username.text = userName
            binding.email.text = email
            Glide.with(this.requireContext()).load(photoUrl).placeholder(R.drawable.profile).into(binding.profilePic)
        } else {
            // Handle the case where the user is not signed in
        }

        binding.btnLogout.setOnClickListener {
            signOutAndLogin()
        }
    }

    private fun signOutAndLogin() {
        mAuth.signOut()

        mGoogleSignInClient.signOut().addOnCompleteListener {
            Toast.makeText(this.requireContext(), "Logout Successfully!!!", Toast.LENGTH_SHORT)
                .show()

            startActivity(Intent(this.requireContext(), LoginActivity::class.java))
            activity?.finish()
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = ProfileFragment()
    }
}