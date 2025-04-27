package com.example.capston_lost

import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.text.TextUtils.replace
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.capston_lost.chatlist.ChatListFragment
import com.example.capston_lost.databinding.ActivityChatMainBinding
import com.example.capston_lost.mypage.myPageFragments
import com.example.capston_lost.userlist.UserFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import android.Manifest
import com.google.firebase.ktx.Firebase

class ChatMainActivity : AppCompatActivity() { // ChatMainActivity 클래스 선언
    private lateinit var binding: ActivityChatMainBinding // ActivityChatMainBinding 변수를 선언
    private val userFragment = UserFragment() // UserFragment 인스턴스 생성
    private val chatListFragment = ChatListFragment() // ChatListFragment 인스턴스 생성
    private val myPageFragment = myPageFragments() // myPageFragments 인스턴스 생성
    private lateinit var auth: FirebaseAuth // FirebaseAuth 변수를 선언

    override fun onCreate(savedInstanceState: Bundle?) { // onCreate 메소드 재정의
        super.onCreate(savedInstanceState) // 부모 클래스의 onCreate 호출
        binding = ActivityChatMainBinding.inflate(layoutInflater) // binding 변수 초기화
        setContentView(binding.root) // 레이아웃 설정

        val currentUser = Firebase.auth.currentUser // 현재 사용자 가져오기

        if (currentUser == null) { // 사용자가 로그인되어 있지 않으면
            startActivity(Intent(this, LoginActivity::class.java)) // LoginActivity로 이동
            finish() // 현재 액티비티 종료
        }

        binding.bottomNavigationView.setOnItemSelectedListener { // bottomNavigationView 아이템 선택 리스너 설정
            when (it.itemId) { // 선택된 아이템의 ID 확인
                R.id.userList -> { // userList 아이템이 선택되면
                    replaceFragment(userFragment) // userFragment로 교체
                    supportActionBar?.title = "친구" // 액션 바 타이틀 설정
                    return@setOnItemSelectedListener true // true 반환
                }
                R.id.chatroomList -> { // chatroomList 아이템이 선택되면
                    replaceFragment(chatListFragment) // chatListFragment로 교체
                    supportActionBar?.title = "채팅" // 액션 바 타이틀 설정
                    return@setOnItemSelectedListener true // true 반환
                }
                R.id.myPage -> { // myPage 아이템이 선택되면
                    replaceFragment(myPageFragment) // myPageFragment로 교체
                    supportActionBar?.title = "마이페이지" // 액션 바 타이틀 설정
                    return@setOnItemSelectedListener true // true 반환
                }
                else -> { // 다른 경우
                    return@setOnItemSelectedListener false // false 반환
                }
            }
        }
        replaceFragment(userFragment) // 초기 화면을 userFragment로 설정
        supportActionBar?.title = "친구" // 액션 바 타이틀 설정
    }

    private fun replaceFragment(fragment: Fragment) { // 프래그먼트를 교체하는 메소드
        supportFragmentManager.beginTransaction() // 프래그먼트 트랜잭션 시작
            .apply {
                replace(R.id.frameLayout, fragment) // frameLayout을 새로운 프래그먼트로 교체
                commit() // 트랜잭션 커밋
            }
    }

    private val requestPermissionLauncher = this.registerForActivityResult( // 권한 요청 런처 초기화
        ActivityResultContracts.RequestPermission() // 권한 요청 계약 사용
    ) { isGranted: Boolean -> // 결과 콜백
        if (isGranted) { // 권한이 허용되면
            Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show() // 권한 허용 메시지 표시
        } else { // 권한이 거부되면
            Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show() // 권한 거부 메시지 표시
        }
    }

    private fun askNotificationPermission() { // 알림 권한 요청 메소드
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // 안드로이드 버전 확인
            when {
                ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS // 알림 권한 확인
                ) == PackageManager.PERMISSION_GRANTED -> { // 권한이 허용되어 있으면
                    // FCM SDK (and your app) can post notifications.
                }
                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> { // 권한 설명이 필요하면
                    showPermissionRationaleDialog() // 권한 설명 다이얼로그 표시
                }
                else -> { // 권한이 없으면
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS) // 권한 요청
                }
            }
        }
    }

    private fun showPermissionRationaleDialog() { // 권한 설명 다이얼로그 표시 메소드
        AlertDialog.Builder(this) // AlertDialog.Builder 생성
            .setMessage("알림 권한이 없으면 알림을 받을 수 없습니다.") // 메시지 설정
            .setPositiveButton("권한 허용하기") { _, _ -> // 긍정 버튼 클릭 리스너 설정
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS) // 권한 요청
            }
            .setNegativeButton("취소") { dialogInterface, _ -> dialogInterface.cancel() } // 부정 버튼 클릭 리스너 설정
            .show() // 다이얼로그 표시
    }
}


