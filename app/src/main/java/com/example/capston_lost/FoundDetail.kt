package com.example.capston_lost

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentSnapshot
import com.example.capston_lost.R

class FoundDetail : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.found_detail) // 레이아웃 파일 이름 확인

        firestore = FirebaseFirestore.getInstance()

        val documentId = intent.getStringExtra("foundItemId")
        Log.d("FoundDetail", "Document ID: $documentId") // 디버깅 메시지 추가
        if (documentId != null && documentId.isNotEmpty()) {
            loadFoundDetail(documentId)
        } else {
            showErrorAndFinish("Document ID is null or empty")
        }
    }

    private fun loadFoundDetail(documentId: String) {
        firestore.collection("found_reports").document(documentId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    populateFields(document)
                } else {
                    showErrorAndFinish("Document does not exist")
                }
            }
            .addOnFailureListener { e ->
                showErrorAndFinish(e.message ?: "Error fetching document")
            }
    }

    private fun populateFields(document: DocumentSnapshot) {
        val nickname = document.getString("nickname")
        val itemType = document.getString("itemType")
        val getDate = document.getString("getDate")
        val location = document.getString("location")
        val remarks = document.getString("remarks")
        val imageUrl = document.getString("imageUrl")

        val nameTextView: TextView = findViewById(R.id.found_detail_name)
        val itemTypeTextView: TextView = findViewById(R.id.found_detail_cat)
        val getDateTextView: TextView = findViewById(R.id.found_detail_date)
        val locationTextView: TextView = findViewById(R.id.found_detail_lostloc)
        val remarksTextView: TextView = findViewById(R.id.found_detail_comment)
        val imageView: ImageView = findViewById(R.id.found_detail_image)

        nameTextView.text = nickname ?: "N/A"
        itemTypeTextView.text = itemType ?: "N/A"
        getDateTextView.text = getDate ?: "N/A"
        locationTextView.text = location ?: "N/A"
        remarksTextView.text = remarks ?: "N/A"

        if (imageUrl != null) {
            Glide.with(this).load(imageUrl).into(imageView)
        } else {
            imageView.setImageResource(R.drawable.camera) // 기본 이미지 설정
        }
    }

    private fun showErrorAndFinish(errorMessage: String) {
        Log.e("FoundDetail", errorMessage) // 디버깅 메시지 추가
        Toast.makeText(this, "해당 데이터를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
        finish()
    }
}
