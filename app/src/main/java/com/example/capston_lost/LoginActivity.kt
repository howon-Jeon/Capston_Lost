package com.example.capston_lost

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.capston_lost.Key.Companion.DB_URL
import com.example.capston_lost.Key.Companion.DB_USERS
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging

class LoginActivity : AppCompatActivity() { // LoginActivity 클래스 정의
    private lateinit var auth: FirebaseAuth // FirebaseAuth 인스턴스 선언

    override fun onCreate(savedInstanceState: Bundle?) { // onCreate 메소드 오버라이드
        super.onCreate(savedInstanceState) // 상위 클래스의 onCreate 호출
        setContentView(R.layout.activity_login) // activity_login 레이아웃 설정

        auth = Firebase.auth // FirebaseAuth 인스턴스 초기화

        val loginBtn = findViewById<Button>(R.id.loginButton) // 로그인 버튼 찾기
        loginBtn.setOnClickListener { // 로그인 버튼 클릭 리스너 설정
            attemptLogin() // 로그인 시도
        }
    }

    private fun attemptLogin() { // 로그인 시도 메소드
        val id = findViewById<EditText>(R.id.edit_id).text.toString() // 이메일 입력 필드에서 텍스트 가져오기
        val password = findViewById<EditText>(R.id.edit_pw).text.toString() // 패스워드 입력 필드에서 텍스트 가져오기

        if (id.isEmpty() || password.isEmpty()) { // 이메일 또는 패스워드가 비어있는지 확인
            Toast.makeText(this, "이메일 또는 패스워드가 입력되지 않았습니다.", Toast.LENGTH_SHORT).show() // 토스트 메시지 표시
            return // 메소드 종료
        }

        auth.signInWithEmailAndPassword(id, password) // 이메일과 패스워드로 로그인 시도
            .addOnCompleteListener(this) { task -> // 로그인 작업 완료 리스너
                if (task.isSuccessful) { // 로그인 성공 시
                    Log.d(TAG, "signInWithEmail:success") // 성공 로그 출력
                    val currentUser = auth.currentUser // 현재 사용자 가져오기
                    updateUI(currentUser) // UI 업데이트
                } else { // 로그인 실패 시
                    Log.w(TAG, "signInWithEmail:failure", task.exception) // 실패 로그 출력
                    Toast.makeText(this, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show() // 토스트 메시지 표시
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) { // UI 업데이트 메소드
        user?.let { // 사용자가 null이 아닌 경우
            Firebase.messaging.token.addOnCompleteListener { task -> // FCM 토큰 가져오기
                if (task.isSuccessful) { // 토큰 가져오기 성공 시
                    val token = task.result // 토큰 결과 가져오기
                    val userId = it.uid // 사용자 ID 가져오기
                    val userMap = mutableMapOf<String, Any>() // 사용자 정보 맵 생성
                    userMap["userId"] = userId // 사용자 ID 추가
                    userMap["username"] = it.email ?: "" // 사용자 이메일 추가
                    userMap["fcmToken"] = token ?: "" // FCM 토큰 추가

                    val databaseReference = FirebaseDatabase.getInstance().getReference(DB_USERS) // 데이터베이스 참조 가져오기
                    databaseReference.child(userId).updateChildren(userMap).addOnCompleteListener { task -> // 데이터베이스 업데이트
                        if (task.isSuccessful) { // 업데이트 성공 시
                            val intent = Intent(this, MainActivity2::class.java) // MainActivity2로 이동할 인텐트 생성
                            startActivity(intent) // 액티비티 시작
                            finish() // 현재 액티비티 종료
                        } else { // 업데이트 실패 시
                            Toast.makeText(this, "데이터베이스 업데이트 실패: ${task.exception?.message}", Toast.LENGTH_SHORT).show() // 토스트 메시지 표시
                        }
                    }
                } else { // 토큰 가져오기 실패 시
                    Log.w(TAG, "Fetching FCM registration token failed", task.exception) // 실패 로그 출력
                    Toast.makeText(this, "FCM 토큰을 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show() // 토스트 메시지 표시
                }
            }
        } ?: run { // 사용자가 null인 경우
            Toast.makeText(this, "로그인 실패: 사용자 정보를 받아올 수 없습니다.", Toast.LENGTH_SHORT).show() // 토스트 메시지 표시
        }
    }

    companion object { // 상수 선언을 위한 동반 객체
        private const val TAG = "LoginActivity" // 로그 태그 상수
    }
}