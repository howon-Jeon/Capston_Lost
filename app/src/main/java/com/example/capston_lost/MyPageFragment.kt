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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MyPageFragment : Fragment() { // MyPageFragment 클래스 선언

    private lateinit var profileImageView: ImageView // 프로필 이미지 뷰 지연 초기화
    private lateinit var uploadProgressBar: ProgressBar // 업로드 진행 바 지연 초기화
    private lateinit var uploadStatusTextView: TextView // 업로드 상태 텍스트 뷰 지연 초기화
    private lateinit var storageRef: StorageReference // 스토리지 참조 지연 초기화
    private lateinit var db: FirebaseFirestore // Firestore 지연 초기화

    override fun onCreateView( // onCreateView 메소드 오버라이드
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_mypage, container, false) // 레이아웃 인플레이트
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { // onViewCreated 메소드 오버라이드
        super.onViewCreated(view, savedInstanceState)

        profileImageView = view.findViewById(R.id.profileImage) // 프로필 이미지 뷰 초기화
        uploadProgressBar = view.findViewById(R.id.uploadProgressBar) // 업로드 진행 바 초기화
        uploadStatusTextView = view.findViewById(R.id.uploadStatusTextView) // 업로드 상태 텍스트 뷰 초기화
        storageRef = FirebaseStorage.getInstance().reference // 스토리지 참조 초기화
        db = FirebaseFirestore.getInstance() // Firestore 초기화

        val logoutButton: Button = view.findViewById(R.id.logout) // 로그아웃 버튼 초기화
        val chatButton: Button = view.findViewById(R.id.chat) // 채팅 버튼 초기화
        val userNameTextView: TextView = view.findViewById(R.id.userName) // 유저 이름 텍스트 뷰 초기화
        val userEmailTextView: TextView = view.findViewById(R.id.userEmail) // 유저 이메일 텍스트 뷰 초기화
        val selectProfileImageButton: Button = view.findViewById(R.id.selectProfileImageButton) // 프로필 이미지 선택 버튼 초기화

        val currentUser = Firebase.auth.currentUser // 현재 유저 가져오기

        if (currentUser != null) { // 현재 유저가 존재할 경우
            userEmailTextView.text = currentUser.email // 유저 이메일 설정

            // Firestore에서 유저 이름과 프로필 이미지 URL 가져오기
            db.collection("users").document(currentUser.uid).get()
                .addOnSuccessListener { document -> // 데이터 가져오기 성공 시
                    if (document != null && document.exists()) { // 문서가 존재할 경우
                        val userName = document.getString("name") // 유저 이름 가져오기
                        val profileImageUrl = document.getString("profileImageUrl") // 프로필 이미지 URL 가져오기
                        userNameTextView.text = userName ?: "유저 이름" // 유저 이름 설정
                        profileImageUrl?.let { // 프로필 이미지 URL이 존재할 경우
                            Glide.with(this).load(it).into(profileImageView) // 프로필 이미지 로드
                        }
                    } else { // 문서가 존재하지 않을 경우
                        userNameTextView.text = "유저 이름" // 기본 유저 이름 설정
                    }
                }
                .addOnFailureListener { // 데이터 가져오기 실패 시
                    userNameTextView.text = "유저 이름" // 기본 유저 이름 설정
                }
        }

        selectProfileImageButton.setOnClickListener { // 프로필 이미지 선택 버튼 클릭 리스너 설정
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI) // 갤러리 인텐트 생성
            intent.type = "image/*" // 이미지 타입 설정
            resultLauncher.launch(intent) // 인텐트 실행
        }

        logoutButton.setOnClickListener { // 로그아웃 버튼 클릭 리스너 설정
            Firebase.auth.signOut() // Firebase 인증 로그아웃
            val intent = Intent(requireContext(), MainActivity::class.java) // MainActivity로 이동하는 인텐트 생성
            startActivity(intent) // 액티비티 시작
        }

        chatButton.setOnClickListener { // 채팅 버튼 클릭 리스너 설정
            val intent = Intent(requireContext(), ChatMainActivity::class.java) // ChatMainActivity로 이동하는 인텐트 생성
            startActivity(intent) // 액티비티 시작
        }
    }

    private fun loadProfileImage(userId: String) { // 프로필 이미지 로드 메소드
        val profileImageRef = storageRef.child("profile_images/$userId.jpg") // 프로필 이미지 참조 생성
        profileImageRef.downloadUrl.addOnSuccessListener { uri -> // 다운로드 URL 가져오기 성공 시
            Glide.with(this).load(uri).into(profileImageView) // 프로필 이미지 로드
        }.addOnFailureListener {
            // 에러 처리
        }
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result -> // 액티비티 결과 처리
        if (result.resultCode == Activity.RESULT_OK) { // 결과가 성공일 경우
            val data: Intent? = result.data // 결과 데이터 가져오기
            data?.data?.let { uri -> // 데이터가 존재할 경우
                uploadProfileImage(uri) // 프로필 이미지 업로드
            }
        }
    }

    private fun uploadProfileImage(imageUri: Uri) { // 프로필 이미지 업로드 메소드
        val currentUser = Firebase.auth.currentUser // 현재 유저 가져오기
        if (currentUser != null) { // 현재 유저가 존재할 경우
            val profileImageRef = storageRef.child("profile_images/${currentUser.uid}.jpg") // 프로필 이미지 참조 생성

            // 진행 바와 상태 텍스트 표시
            uploadProgressBar.visibility = View.VISIBLE // 진행 바 표시
            uploadStatusTextView.visibility = View.VISIBLE // 상태 텍스트 표시

            profileImageRef.putFile(imageUri).addOnSuccessListener { // 파일 업로드 성공 시
                profileImageRef.downloadUrl.addOnSuccessListener { uri -> // 다운로드 URL 가져오기 성공 시
                    Glide.with(this).load(uri).into(profileImageView) // 프로필 이미지 로드
                    // 프로필 이미지 URL을 Firestore에 저장
                    saveProfileImageUrlToFirestore(uri.toString())
                    // 진행 바와 상태 텍스트 숨기기
                    uploadProgressBar.visibility = View.GONE // 진행 바 숨기기
                    uploadStatusTextView.visibility = View.GONE // 상태 텍스트 숨기기
                }
            }.addOnFailureListener {
                // 에러 처리
                // 진행 바와 상태 텍스트 숨기기
                uploadProgressBar.visibility = View.GONE // 진행 바 숨기기
                uploadStatusTextView.visibility = View.GONE // 상태 텍스트 숨기기
            }
        }
    }

    private fun saveProfileImageUrlToFirestore(url: String) { // 프로필 이미지 URL을 Firestore에 저장하는 메소드
        val currentUser = Firebase.auth.currentUser // 현재 유저 가져오기
        if (currentUser != null) { // 현재 유저가 존재할 경우
            val userDocRef = db.collection("users").document(currentUser.uid) // 유저 문서 참조 생성
            userDocRef.update("profileImageUrl", url) // 프로필 이미지 URL 업데이트
                .addOnSuccessListener {
                    // Firestore에 프로필 이미지 URL 업데이트 성공
                }
                .addOnFailureListener {
                    // 에러 처리
                }
        }
    }
}
