package com.example.capston_lost

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var profileImageView: ImageView
    private lateinit var storageRef: StorageReference
    private lateinit var recentlyTextView: TextView
    private lateinit var firestore: FirebaseFirestore
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUser = Firebase.auth.currentUser
        profileImageView = view.findViewById(R.id.home_profile)
        recentlyTextView = view.findViewById(R.id.home_recently_text)
        firestore = FirebaseFirestore.getInstance()
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)

        val userNameTextView: TextView = view.findViewById(R.id.home_user_name)

        if (currentUser != null) {
            val userDocRef = firestore.collection("users").document(currentUser.uid)
            userDocRef.get().addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val userName = document.getString("name")
                    val profileImageUrl = document.getString("profileImageUrl")

                    userNameTextView.text = userName ?: "User"
                    profileImageUrl?.let {
                        Glide.with(this).load(it).into(profileImageView)
                    }
                }
            }.addOnFailureListener {
                userNameTextView.text = "User"
            }
        }

        val lostButton: Button = view.findViewById(R.id.home_lost_button)
        lostButton.setOnClickListener {
            val intent = Intent(requireContext(), LostReportpage::class.java)
            startActivity(intent)
        }

        val foundButton: Button = view.findViewById(R.id.home_found_button)
        foundButton.setOnClickListener {
            val intent = Intent(requireContext(), FindReportpage::class.java)
            startActivity(intent)
        }

        val searchButton: Button = view.findViewById(R.id.chat)
        searchButton.setOnClickListener {
            val intent = Intent(requireContext(), ChatMainActivity::class.java)
            startActivity(intent)
        }

        swipeRefreshLayout.setOnRefreshListener {
            fetchRecentReportCount {
                swipeRefreshLayout.isRefreshing = false
            }
        }

        fetchRecentReportCount()
    }

    private fun fetchRecentReportCount(onComplete: (() -> Unit)? = null) {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -2)
        val twoDaysAgo = calendar.time

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val twoDaysAgoString = sdf.format(twoDaysAgo)

        firestore.collection("found_reports")
            .whereGreaterThanOrEqualTo("getDate", twoDaysAgoString)
            .get()
            .addOnSuccessListener { documents ->
                val count = documents.size()
                recentlyTextView.text = " $count 건"
                onComplete?.invoke()
            }
            .addOnFailureListener {
                recentlyTextView.text = "접수된 습득물 수: 오류 발생"
                onComplete?.invoke()
            }
    }
}
