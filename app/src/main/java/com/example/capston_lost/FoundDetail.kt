package com.example.capston_lost

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.example.capston_lost.R

class FoundDetail : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.found_detail) // Ensure this is the correct layout file name

        firestore = FirebaseFirestore.getInstance()

        val documentId = intent.getStringExtra("foundItemId")
        Log.d("FoundDetail", "Document ID: $documentId")

        if (documentId != null) {
            firestore.collection("found_reports").document(documentId).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val foundItem = document.toObject(FoundItem::class.java)
                        if (foundItem != null) {
                            displayFoundItemDetails(foundItem)
                            loadUserProfileImage(foundItem.userId)
                        } else {
                            Toast.makeText(this, "아이템 데이터를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "아이템 데이터를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "데이터 불러오기 오류: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "유효한 문서 ID가 아닙니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayFoundItemDetails(foundItem: FoundItem) {
        val profileImageView: ImageView = findViewById(R.id.found_detail_profile)
        val nicknameTextView: TextView = findViewById(R.id.found_detail_name)
        val itemImageView: ImageView = findViewById(R.id.found_detail_image)
        val itemTypeTextView: TextView = findViewById(R.id.found_detail_cat)
        val getDateTextView: TextView = findViewById(R.id.found_detail_date)
        val locationTextView: TextView = findViewById(R.id.found_detail_lostloc)
        //val storageLocationTextView: TextView = findViewById(R.id.found_detail_storageloc)
        val remarksTextView: TextView = findViewById(R.id.found_detail_comment)

        nicknameTextView.text = foundItem.nickname
        itemTypeTextView.text = foundItem.itemType
        getDateTextView.text = foundItem.getDate
        locationTextView.text = foundItem.location
       // storageLocationTextView.text = foundItem.storageLocation // Assuming this field exists
        remarksTextView.text = foundItem.remarks

        val imageUrl = foundItem.imageUrl.split(",").firstOrNull()
        if (imageUrl != null && imageUrl.isNotEmpty()) {
            Glide.with(this).load(imageUrl).into(itemImageView)
        }
    }

    private fun loadUserProfileImage(userId: String) {
        val profileImageView: ImageView = findViewById(R.id.found_detail_profile)

        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val profileImageUrl = document.getString("profileImageUrl")
                    if (profileImageUrl != null && profileImageUrl.isNotEmpty()) {
                        Glide.with(this).load(profileImageUrl).into(profileImageView)
                    }
                } else {
                    Toast.makeText(this, "프로필 이미지를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "프로필 이미지 로드 오류: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
