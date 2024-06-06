package com.example.capston_lost

// LostDetail.kt

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.capston_lost.databinding.LostDetailBinding
import com.google.firebase.firestore.FirebaseFirestore

class LostDetail : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = LostDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Firestore 초기화
        firestore = FirebaseFirestore.getInstance()

        // 인텐트로 전달받은 lostItemId 가져오기
        val lostItemId = intent.getStringExtra("lostItemId")

        if (lostItemId != null) {
            // Firestore에서 해당 lostItemId의 데이터 가져오기
            firestore.collection("lost_items")
                .document(lostItemId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        // 데이터가 있는 경우 UI에 표시
                        val lostItem = document.toObject(LostItem::class.java)
                        if (lostItem != null) {
                            // UI에 데이터 설정
                            binding.lostDetailName.text = "닉네임" // 여기에 사용자 닉네임 설정 가능
                            binding.lostDetailCat.text = lostItem.itemType
                            binding.lostDetailDate.text = lostItem.getDate
                            binding.lostDetailLostloc.text = lostItem.location
                            binding.lostDetailComment.text = lostItem.remarks
                            // 여기에 이미지 설정 등 추가 가능
                        }
                    } else {
                        // 데이터가 없는 경우 예외 처리
                        Toast.makeText(this, "해당 데이터를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                .addOnFailureListener { e ->
                    // 데이터 가져오기 실패 시 예외 처리
                    Toast.makeText(this, "데이터를 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                    println("Error getting documents: $e")
                    finish()
                }
        } else {
            // lostItemId가 null인 경우 예외 처리
            Toast.makeText(this, "잘못된 요청입니다.", Toast.LENGTH_SHORT).show()
            finish()
        }

        // 채팅 버튼 클릭 시 동작 설정
        binding.lostDetailChat.setOnClickListener {
            // 채팅 기능 구현
            // 예시로 Toast 메시지 출력
            Toast.makeText(this, "채팅 기능 구현 예정", Toast.LENGTH_SHORT).show()
        }
    }
}
