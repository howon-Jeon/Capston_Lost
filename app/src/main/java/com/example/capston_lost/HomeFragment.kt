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

class HomeFragment : Fragment() { // HomeFragment 클래스 정의

    private lateinit var profileImageView: ImageView // 프로필 이미지뷰를 나중에 초기화하기 위해 선언
    private lateinit var storageRef: StorageReference // Storage 참조를 나중에 초기화하기 위해 선언
    private lateinit var recentlyTextView: TextView // 최근 텍스트뷰를 나중에 초기화하기 위해 선언
    private lateinit var firestore: FirebaseFirestore // Firestore 인스턴스를 나중에 초기화하기 위해 선언
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout // SwipeRefreshLayout을 나중에 초기화하기 위해 선언

    override fun onCreateView( // Fragment의 뷰를 생성하는 메서드
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // layout 파일을 inflate하여 반환
        return inflater.inflate(R.layout.activity_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { // Fragment의 뷰가 생성된 후 호출되는 메서드
        super.onViewCreated(view, savedInstanceState)

        val currentUser = Firebase.auth.currentUser // 현재 사용자 가져오기
        profileImageView = view.findViewById(R.id.home_profile) // 프로필 이미지뷰를 레이아웃에서 가져오기
        recentlyTextView = view.findViewById(R.id.home_recently_text) // 최근 텍스트뷰를 레이아웃에서 가져오기
        firestore = FirebaseFirestore.getInstance() // Firestore 인스턴스 초기화
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout) // SwipeRefreshLayout을 레이아웃에서 가져오기

        val userNameTextView: TextView = view.findViewById(R.id.home_user_name) // 사용자 이름 텍스트뷰를 레이아웃에서 가져오기

        if (currentUser != null) { // 현재 사용자가 존재하는 경우
            val userDocRef = firestore.collection("users").document(currentUser.uid) // 사용자 문서 참조 가져오기
            userDocRef.get().addOnSuccessListener { document -> // 문서 가져오기 성공 시
                if (document != null && document.exists()) { // 문서가 존재하는 경우
                    val userName = document.getString("name") // 사용자 이름 가져오기
                    val profileImageUrl = document.getString("profileImageUrl") // 프로필 이미지 URL 가져오기

                    userNameTextView.text = userName ?: "User" // 사용자 이름 텍스트뷰에 설정, 기본값은 "User"
                    profileImageUrl?.let {
                        // 프로필 이미지 URL이 유효한 경우 Glide를 사용하여 이미지 로드
                        Glide.with(this).load(it).into(profileImageView)
                    }
                }
            }.addOnFailureListener {
                userNameTextView.text = "User" // 실패 시 기본값 "User" 설정
            }
        }

        val lostButton: Button = view.findViewById(R.id.home_lost_button) // 분실물 신고 버튼을 레이아웃에서 가져오기
        lostButton.setOnClickListener {
            val intent = Intent(requireContext(), LostReportpage::class.java) // LostReportpage 액티비티로 이동하는 인텐트 생성
            startActivity(intent) // 액티비티 시작
        }

        val foundButton: Button = view.findViewById(R.id.home_found_button) // 습득물 신고 버튼을 레이아웃에서 가져오기
        foundButton.setOnClickListener {
            val intent = Intent(requireContext(), FindReportpage::class.java) // FindReportpage 액티비티로 이동하는 인텐트 생성
            startActivity(intent) // 액티비티 시작
        }

        val searchButton: Button = view.findViewById(R.id.chat) // 채팅 버튼을 레이아웃에서 가져오기
        searchButton.setOnClickListener {
            val intent = Intent(requireContext(), ChatMainActivity::class.java) // ChatMainActivity 액티비티로 이동하는 인텐트 생성
            startActivity(intent) // 액티비티 시작
        }

        swipeRefreshLayout.setOnRefreshListener {
            fetchRecentReportCount {
                swipeRefreshLayout.isRefreshing = false // 새로고침 완료 후 스와이프 새로고침 레이아웃 해제
            }
        }

        fetchRecentReportCount() // 최근 신고 수를 가져오는 메서드 호출
    }

    private fun fetchRecentReportCount(onComplete: (() -> Unit)? = null) {
        val calendar = Calendar.getInstance() // 현재 날짜와 시간을 가져오기 위해 Calendar 인스턴스 생성
        calendar.add(Calendar.DAY_OF_YEAR, -2)
        val twoDaysAgo = calendar.time //  날짜를 가져오기

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // 날짜 형식을 지정하기 위한 SimpleDateFormat 인스턴스 생성
        val twoDaysAgoString = sdf.format(twoDaysAgo) //  날짜를 문자열로 변환

        firestore.collection("found_reports")
            .whereGreaterThanOrEqualTo("getDate", twoDaysAgoString) // getDate가 2일 전 날짜 이후인 문서들 필터링
            .get()
            .addOnSuccessListener { documents -> // 데이터 가져오기 성공 시
                val count = documents.size() // 문서 수를 계산
                recentlyTextView.text = " $count 건" // 최근 신고 수를 텍스트뷰에 설정
                onComplete?.invoke() // 콜백 함수 호출
            }
            .addOnFailureListener {
                recentlyTextView.text = "접수된 습득물 수: 오류 발생" // 오류 발생 시 텍스트뷰에 메시지 설정
                onComplete?.invoke() // 콜백 함수 호출
            }
    }
}