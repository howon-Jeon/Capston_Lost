package com.example.capston_lost

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() { // RegisterActivity 클래스 정의
    private lateinit var auth: FirebaseAuth // FirebaseAuth 인스턴스 선언
    private lateinit var db: FirebaseFirestore // FirebaseFirestore 인스턴스 선언

    override fun onCreate(savedInstanceState: Bundle?) { // onCreate 메소드 오버라이드
        super.onCreate(savedInstanceState) // 상위 클래스의 onCreate 호출
        setContentView(R.layout.activity_registers) // activity_registers 레이아웃 설정

        FirebaseApp.initializeApp(this) // FirebaseApp 초기화

        auth = Firebase.auth // FirebaseAuth 인스턴스 초기화

        db = FirebaseFirestore.getInstance() // FirebaseFirestore 인스턴스 초기화

        val enrollbtn = findViewById<Button>(R.id.loginButton) // 등록 버튼 찾기
        enrollbtn.setOnClickListener { // 등록 버튼 클릭 리스너 설정
            val emailId = findViewById<EditText>(R.id.member_edit_id) // 이메일 입력 필드 찾기
            val passwordId = findViewById<EditText>(R.id.member_edit_pw) // 패스워드 입력 필드 찾기
            val checkPasswordId = findViewById<EditText>(R.id.member_edit_pw_re) // 패스워드 확인 필드 찾기
            val usersname = findViewById<EditText>(R.id.member_edit_name) // 이름 입력 필드 찾기

            val email: String = emailId.text.toString() // 이메일 텍스트 가져오기
            val password: String = passwordId.text.toString() // 패스워드 텍스트 가져오기
            val checkPassword: String = checkPasswordId.text.toString() // 패스워드 확인 텍스트 가져오기
            val names: String = usersname.text.toString() // 이름 텍스트 가져오기

            if (password != checkPassword) { // 패스워드와 패스워드 확인이 일치하지 않는 경우
                Toast.makeText(
                    baseContext,
                    "비밀번호와 비밀번호 확인이 일치하지 않습니다.",
                    Toast.LENGTH_SHORT
                ).show() // 토스트 메시지 표시

                passwordId.requestFocus() // 패스워드 입력 필드로 포커스 이동
            } else { // 패스워드와 패스워드 확인이 일치하는 경우
                auth.createUserWithEmailAndPassword(email, password) // 이메일과 패스워드로 사용자 생성
                    .addOnCompleteListener(this) { task -> // 사용자 생성 작업 완료 리스너
                        if (task.isSuccessful) { // 사용자 생성 성공 시
                            Log.d(TAG, "회원가입 성공") // 성공 로그 출력
                            Toast.makeText(
                                baseContext,
                                "회원가입 성공",
                                Toast.LENGTH_SHORT,
                            ).show() // 토스트 메시지 표시
                            val users = auth.currentUser // 현재 사용자 가져오기

                            val user = hashMapOf( // 사용자 정보 맵 생성
                                "email" to email,
                                "name" to names
                            )
                            db.collection("users").document(users!!.uid).set(user) // Firestore에 사용자 정보 저장
                                .addOnSuccessListener {
                                    Log.d(TAG, "DocumentSnapshot successfully written!") // 성공 로그 출력
                                    Toast.makeText(
                                        baseContext,
                                        "회원가입 성공",
                                        Toast.LENGTH_SHORT,
                                    ).show() // 토스트 메시지 표시
                                    updateUI(users) // UI 업데이트
                                }
                                .addOnFailureListener { e ->
                                    Log.w(TAG, "Error writing document", e) // 실패 로그 출력
                                    Toast.makeText(
                                        baseContext,
                                        "Failed to save user data.",
                                        Toast.LENGTH_SHORT
                                    ).show() // 토스트 메시지 표시
                                }

                        } else { // 사용자 생성 실패 시
                            Log.w(TAG, "createUserWithEmail:failure", task.exception) // 실패 로그 출력
                            Toast.makeText(
                                baseContext,
                                "회원가입 실패",
                                Toast.LENGTH_SHORT,
                            ).show() // 토스트 메시지 표시
                            updateUI(null) // UI 업데이트
                        }
                    }
            }
        }
    }

    private fun updateUI(user: FirebaseUser?) { // UI 업데이트 메소드
        val intent = Intent(this, MainActivity::class.java) // MainActivity로 이동할 인텐트 생성
        startActivity(intent) // 액티비티 시작
        finish() // 현재 액티비티 종료
    }

    public override fun onStart() { // onStart 메소드 오버라이드
        super.onStart() // 상위 클래스의 onStart 호출
        val currentUser = auth.currentUser // 현재 사용자 가져오기
        if (currentUser != null) { // 현재 사용자가 null이 아닌 경우
            reload() // 리로드 메소드 호출
        }
    }

    private fun reload() { // 리로드 메소드 (구현 필요)
        // 구현 필요
    }
}
