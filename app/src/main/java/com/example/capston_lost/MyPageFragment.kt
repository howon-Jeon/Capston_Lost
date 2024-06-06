package com.example.capston_lost

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.capston_lost.userlist.UserItem
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MyPageFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_mypage, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val logoutButton: Button = view.findViewById(R.id.logout)
        val chatButton: Button = view.findViewById(R.id.chat)
        val userNameTextView: TextView = view.findViewById(R.id.userName)
        val userEmailTextView: TextView = view.findViewById(R.id.userEmail)

        val currentUser = Firebase.auth.currentUser

        if (currentUser != null) {
            userEmailTextView.text = currentUser.email

            // Retrieve user name from SharedPreferences
            val sharedPref = requireActivity().getSharedPreferences("userDetails", Context.MODE_PRIVATE)
            val userName = sharedPref.getString("userName", "유저 이름")

            userNameTextView.text = userName
        }

        logoutButton.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }

        chatButton.setOnClickListener {
            val intent = Intent(requireContext(), ChatMainActivity::class.java)
            startActivity(intent)
        }
    }
}
