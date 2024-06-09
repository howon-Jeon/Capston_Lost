package com.example.capston_lost

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class HomeFragment : Fragment() {

    private lateinit var profileImageView: ImageView
    private lateinit var storageRef: StorageReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve user name from SharedPreferences
        val sharedPref = requireActivity().getSharedPreferences("userDetails", Context.MODE_PRIVATE)
        val userName = sharedPref.getString("userName", "User")

        // Set user name to TextView
        val userNameTextView: TextView = view.findViewById(R.id.home_user_name)
        userNameTextView.text = userName

        profileImageView = view.findViewById(R.id.home_profile)

        // Load profile image URL from SharedPreferences
        val profileImageUrl = sharedPref.getString("profileImageUrl", null)
        if (profileImageUrl != null) {
            // Load the profile image using Glide
            Glide.with(this).load(profileImageUrl).into(profileImageView)
        } else {
            // Optionally, handle the case where there is no profile image URL
        }

        val lostButton : Button = view.findViewById(R.id.home_lost_button)
        lostButton.setOnClickListener {
            val intent = Intent(requireContext(), LostReportpage::class.java)
            startActivity(intent)
        }

        val foundButton : Button = view.findViewById(R.id.home_found_button)
        foundButton.setOnClickListener {
            val intent = Intent(requireContext(), FindReportpage::class.java)
            startActivity(intent)
        }

        val searchButton : Button = view.findViewById(R.id.chat)
        searchButton.setOnClickListener {
            val intent = Intent(requireContext(), ChatMainActivity::class.java)
            startActivity(intent)
        }
    }
}
