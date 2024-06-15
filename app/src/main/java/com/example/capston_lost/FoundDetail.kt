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

class FoundDetail : AppCompatActivity() { // FoundDetail 액티비티 클래스 정의

    private lateinit var firestore: FirebaseFirestore // Firestore 인스턴스를 나중에 초기화하기 위해 선언

    override fun onCreate(savedInstanceState: Bundle?) { // 액티비티가 생성될 때 호출되는 onCreate 메서드
        super.onCreate(savedInstanceState) // 슈퍼클래스의 onCreate 메서드 호출
        setContentView(R.layout.found_detail) // 레이아웃 설정 (레이아웃 파일이름이 맞는지 확인 필요)

        firestore = FirebaseFirestore.getInstance() // Firestore 인스턴스 초기화

        val documentId = intent.getStringExtra("foundItemId") // 인텐트로부터 문서 ID 가져오기
        Log.d("FoundDetail", "Document ID: $documentId") // 문서 ID를 로그에 출력

        if (documentId != null) { // 문서 ID가 유효한 경우
            firestore.collection("found_reports").document(documentId).get() // Firestore에서 문서 가져오기
                .addOnSuccessListener { document -> // 성공적으로 가져온 경우
                    if (document != null && document.exists()) { // 문서가 존재하는 경우
                        val foundItem = document.toObject(FoundItem::class.java) // 문서를 FoundItem 객체로 변환
                        if (foundItem != null) { // foundItem 객체가 유효한 경우
                            displayFoundItemDetails(foundItem) // 아이템 세부 정보를 표시하는 메서드 호출
                            loadUserProfileImage(foundItem.userId) // 사용자 프로필 이미지를 로드하는 메서드 호출
                        } else {
                            Toast.makeText(this, "아이템 데이터를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show() // 오류 메시지 표시
                        }
                    } else {
                        Toast.makeText(this, "아이템 데이터를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show() // 오류 메시지 표시
                    }
                }
                .addOnFailureListener { e -> // 실패한 경우
                    Toast.makeText(this, "데이터 불러오기 오류: ${e.message}", Toast.LENGTH_SHORT).show() // 오류 메시지 표시
                }
        } else {
            Toast.makeText(this, "유효한 문서 ID가 아닙니다.", Toast.LENGTH_SHORT).show() // 유효하지 않은 문서 ID 오류 메시지 표시
        }
    }


    private fun displayFoundItemDetails(foundItem: FoundItem) { // 아이템 세부 정보를 표시하는 메서드
        val profileImageView: ImageView = findViewById(R.id.found_detail_profile) // 프로필 이미지뷰를 레이아웃에서 가져오기
        val nicknameTextView: TextView = findViewById(R.id.found_detail_name) // 닉네임 텍스트뷰를 레이아웃에서 가져오기
        val itemImageView: ImageView = findViewById(R.id.found_detail_image) // 아이템 이미지뷰를 레이아웃에서 가져오기
        val itemTypeTextView: TextView = findViewById(R.id.found_detail_cat) // 아이템 타입 텍스트뷰를 레이아웃에서 가져오기
        val getDateTextView: TextView = findViewById(R.id.found_detail_date) // 획득 날짜 텍스트뷰를 레이아웃에서 가져오기
        val locationTextView: TextView = findViewById(R.id.found_detail_lostloc) // 위치 텍스트뷰를 레이아웃에서 가져오기
        val remarksTextView: TextView = findViewById(R.id.found_detail_comment) // 비고 텍스트뷰를 레이아웃에서 가져오기

        nicknameTextView.text = foundItem.nickname // 닉네임을 텍스트뷰에 설정
        itemTypeTextView.text = foundItem.itemType // 아이템 타입을 텍스트뷰에 설정
        getDateTextView.text = foundItem.getDate // 획득 날짜를 텍스트뷰에 설정
        locationTextView.text = foundItem.location // 위치를 텍스트뷰에 설정
        remarksTextView.text = foundItem.remarks // 비고를 텍스트뷰에 설정

        val imageUrl = foundItem.imageUrl.split(",").firstOrNull() // 이미지 URL을 콤마로 나눠서 첫 번째 항목 가져오기
        if (imageUrl != null && imageUrl.isNotEmpty()) { // 이미지 URL이 유효한 경우
            Glide.with(this).load(imageUrl).into(itemImageView) // Glide를 사용하여 이미지뷰에 이미지 로드
        }
    }

    private fun loadUserProfileImage(userId: String) { // 사용자 프로필 이미지를 로드하는 메서드
        val profileImageView: ImageView = findViewById(R.id.found_detail_profile) // 프로필 이미지뷰를 레이아웃에서 가져오기

        firestore.collection("users").document(userId).get() // Firestore에서 사용자 문서 가져오기
            .addOnSuccessListener { document -> // 성공적으로 가져온 경우
                if (document != null && document.exists()) { // 문서가 존재하는 경우
                    val profileImageUrl = document.getString("profileImageUrl") // 프로필 이미지 URL 가져오기
                    if (profileImageUrl != null && profileImageUrl.isNotEmpty()) { // 프로필 이미지 URL이 유효한 경우
                        Glide.with(this).load(profileImageUrl).into(profileImageView) // Glide를 사용하여 프로필 이미지뷰에 이미지 로드
                    }
                } else {
                    Toast.makeText(this, "프로필 이미지를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show() // 오류 메시지 표시
                }
            }
            .addOnFailureListener { e -> // 실패한 경우
                Toast.makeText(this, "프로필 이미지 로드 오류: ${e.message}", Toast.LENGTH_SHORT).show() // 오류 메시지 표시
            }
    }
}
