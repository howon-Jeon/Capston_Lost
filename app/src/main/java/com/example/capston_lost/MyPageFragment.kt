package com.example.capston_lost

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MyPageFragment : Fragment() {

    private lateinit var profileImageView: ImageView
    private lateinit var uploadProgressBar: ProgressBar
    private lateinit var uploadStatusTextView: TextView
    private lateinit var storageRef: StorageReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_mypage, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileImageView = view.findViewById(R.id.profileImage)
        uploadProgressBar = view.findViewById(R.id.uploadProgressBar)
        uploadStatusTextView = view.findViewById(R.id.uploadStatusTextView)
        storageRef = FirebaseStorage.getInstance().reference

        val logoutButton: Button = view.findViewById(R.id.logout)
        val chatButton: Button = view.findViewById(R.id.chat)
        val userNameTextView: TextView = view.findViewById(R.id.userName)
        val userEmailTextView: TextView = view.findViewById(R.id.userEmail)
        val selectProfileImageButton: Button = view.findViewById(R.id.selectProfileImageButton)

        val currentUser = Firebase.auth.currentUser

        if (currentUser != null) {
            userEmailTextView.text = currentUser.email

            // Retrieve user name from SharedPreferences
            val sharedPref = requireActivity().getSharedPreferences("userDetails", Context.MODE_PRIVATE)
            val userName = sharedPref.getString("userName", "유저 이름")
            userNameTextView.text = userName

            // Load the current profile image from Firebase Storage
            loadProfileImage(currentUser.uid)
        }

        selectProfileImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            resultLauncher.launch(intent)
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

    private fun loadProfileImage(userId: String) {
        val profileImageRef = storageRef.child("profile_images/$userId.jpg")
        profileImageRef.downloadUrl.addOnSuccessListener { uri ->
            Glide.with(this).load(uri).into(profileImageView)
        }.addOnFailureListener {
            // Handle the error
        }
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            data?.data?.let { uri ->
                uploadProfileImage(uri)
            }
        }
    }

    private fun uploadProfileImage(imageUri: Uri) {
        val currentUser = Firebase.auth.currentUser
        if (currentUser != null) {
            val profileImageRef = storageRef.child("profile_images/${currentUser.uid}.jpg")

            // Show progress bar and status text
            uploadProgressBar.visibility = View.VISIBLE
            uploadStatusTextView.visibility = View.VISIBLE

            profileImageRef.putFile(imageUri).addOnSuccessListener {
                profileImageRef.downloadUrl.addOnSuccessListener { uri ->
                    Glide.with(this).load(uri).into(profileImageView)
                    // Save profile image URL to SharedPreferences
                    saveProfileImageUrl(uri.toString())
                    // Hide progress bar and status text
                    uploadProgressBar.visibility = View.GONE
                    uploadStatusTextView.visibility = View.GONE
                }
            }.addOnFailureListener {
                // Handle the error
                // Hide progress bar and status text
                uploadProgressBar.visibility = View.GONE
                uploadStatusTextView.visibility = View.GONE
            }
        }
    }

    private fun saveProfileImageUrl(url: String) {
        val sharedPref = requireActivity().getSharedPreferences("userDetails", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("profileImageUrl", url)
            apply()
        }
    }
}
