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

class LostDetail : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lost_detail) // Ensure this is the correct layout file name

        firestore = FirebaseFirestore.getInstance()

        val documentId = intent.getStringExtra("lostItemId")
        Log.d("LostDetail", "Document ID: $documentId")

        if (documentId != null) {
            firestore.collection("lost_reports").document(documentId).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val lostItem = document.toObject(LostItem::class.java)
                        if (lostItem != null) {
                            displayLostItemDetails(lostItem)
                            loadUserProfileImage(lostItem.userId)

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

    private fun displayLostItemDetails(lostItem: LostItem) {
        val profileImageView: ImageView = findViewById(R.id.lost_detail_profile)
        val nicknameTextView: TextView = findViewById(R.id.lost_detail_name)
        val itemImageView: ImageView = findViewById(R.id.lost_detail_image)
        val itemTypeTextView: TextView = findViewById(R.id.lost_detail_cat)
        val getDateTextView: TextView = findViewById(R.id.lost_detail_date)
        val locationTextView: TextView = findViewById(R.id.lost_detail_lostloc)
        //val storageLocationTextView: TextView = findViewById(R.id.found_detail_storageloc)
        val remarksTextView: TextView = findViewById(R.id.lost_detail_comment)

        nicknameTextView.text = lostItem.nickname
        itemTypeTextView.text = lostItem.itemType
        getDateTextView.text = lostItem.getDate
        locationTextView.text = lostItem.location
        // storageLocationTextView.text = foundItem.storageLocation // Assuming this field exists
        remarksTextView.text = lostItem.remarks

        val imageUrl = lostItem.imageUrl.split(",").firstOrNull()
        if (imageUrl != null && imageUrl.isNotEmpty()) {
            Glide.with(this).load(imageUrl).into(itemImageView)
        }
    }
    private fun loadUserProfileImage(userId: String) {
        val profileImageView: ImageView = findViewById(R.id.lost_detail_profile)

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
