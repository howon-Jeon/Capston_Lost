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

// LostDetail 액티비티 클래스 정의
class LostDetail : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore // Firestore 인스턴스 변수 선언

    // 액티비티 생성 시 호출되는 메소드
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lost_detail) // 레이아웃 설정, 파일 이름이 맞는지 확인 필요

        firestore = FirebaseFirestore.getInstance() // Firestore 인스턴스 초기화

        val documentId = intent.getStringExtra("lostItemId") // 인텐트에서 문서 ID 가져오기
        Log.d("LostDetail", "Document ID: $documentId") // 문서 ID 로그 출력

        if (documentId != null) {
            firestore.collection("lost_reports").document(documentId).get() // Firestore에서 문서 가져오기
                .addOnSuccessListener { document -> // 성공 시
                    if (document != null && document.exists()) {
                        val lostItem = document.toObject(LostItem::class.java) // 문서를 LostItem 객체로 변환
                        if (lostItem != null) {
                            displayLostItemDetails(lostItem) // LostItem 정보 표시
                            loadUserProfileImage(lostItem.userId) // 사용자 프로필 이미지 로드

                        } else {
                            Toast.makeText(this, "아이템 데이터를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show() // 오류 메시지 표시
                        }
                    } else {
                        Toast.makeText(this, "아이템 데이터를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show() // 오류 메시지 표시
                    }
                }
                .addOnFailureListener { e -> // 실패 시
                    Toast.makeText(this, "데이터 불러오기 오류: ${e.message}", Toast.LENGTH_SHORT).show() // 오류 메시지 표시
                }
        } else {
            Toast.makeText(this, "유효한 문서 ID가 아닙니다.", Toast.LENGTH_SHORT).show() // 오류 메시지 표시
        }
    }

    // LostItem 정보 표시 메소드
    private fun displayLostItemDetails(lostItem: LostItem) {
        val profileImageView: ImageView = findViewById(R.id.lost_detail_profile) // 프로필 이미지뷰
        val nicknameTextView: TextView = findViewById(R.id.lost_detail_name) // 닉네임 텍스트뷰
        val itemImageView: ImageView = findViewById(R.id.lost_detail_image) // 아이템 이미지뷰
        val itemTypeTextView: TextView = findViewById(R.id.lost_detail_cat) // 아이템 종류 텍스트뷰
        val getDateTextView: TextView = findViewById(R.id.lost_detail_date) // 날짜 텍스트뷰
        val locationTextView: TextView = findViewById(R.id.lost_detail_lostloc) // 위치 텍스트뷰
        // val storageLocationTextView: TextView = findViewById(R.id.found_detail_storageloc) // 저장 위치 텍스트뷰 (주석 처리됨)
        val remarksTextView: TextView = findViewById(R.id.lost_detail_comment) // 비고 텍스트뷰

        nicknameTextView.text = lostItem.nickname // 닉네임 설정
        itemTypeTextView.text = lostItem.itemType // 아이템 종류 설정
        getDateTextView.text = lostItem.getDate // 날짜 설정
        locationTextView.text = lostItem.location // 위치 설정
        // storageLocationTextView.text = foundItem.storageLocation // Assuming this field exists // (주석 처리됨)
        remarksTextView.text = lostItem.remarks // 비고 설정

        val imageUrl = lostItem.imageUrl.split(",").firstOrNull() // 첫 번째 이미지 URL 사용
        if (imageUrl != null && imageUrl.isNotEmpty()) {
            Glide.with(this).load(imageUrl).into(itemImageView) // 이미지뷰에 이미지 설정
        }
    }

    // 사용자 프로필 이미지 로드 메소드
    private fun loadUserProfileImage(userId: String) {
        val profileImageView: ImageView = findViewById(R.id.lost_detail_profile) // 프로필 이미지뷰

        firestore.collection("users").document(userId).get() // Firestore에서 사용자 문서 가져오기
            .addOnSuccessListener { document -> // 성공 시
                if (document != null && document.exists()) {
                    val profileImageUrl = document.getString("profileImageUrl") // 프로필 이미지 URL 가져오기
                    if (profileImageUrl != null && profileImageUrl.isNotEmpty()) {
                        Glide.with(this).load(profileImageUrl).into(profileImageView) // 이미지뷰에 프로필 이미지 설정
                    }
                } else {
                    Toast.makeText(this, "프로필 이미지를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show() // 오류 메시지 표시
                }
            }
            .addOnFailureListener { e -> // 실패 시
                Toast.makeText(this, "프로필 이미지 로드 오류: ${e.message}", Toast.LENGTH_SHORT).show() // 오류 메시지 표시
            }
    }
}
