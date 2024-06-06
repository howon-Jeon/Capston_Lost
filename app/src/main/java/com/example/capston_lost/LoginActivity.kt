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

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Firebase 인증 인스턴스 생성
        auth = Firebase.auth

        val loginBtn = findViewById<Button>(R.id.loginButton)
        loginBtn.setOnClickListener {
            attemptLogin()
        }
    }

    private fun attemptLogin() {
        val id = findViewById<EditText>(R.id.edit_id).text.toString()
        val password = findViewById<EditText>(R.id.edit_pw).text.toString()

        if (id.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "이메일 또는 패스워드가 입력되지 않았습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(id, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    val currentUser = auth.currentUser
                    updateUI(currentUser)
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(this, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        user?.let {
            Firebase.messaging.token.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    val userId = it.uid
                    val userMap = mutableMapOf<String, Any>()
                    userMap["userId"] = userId
                    userMap["username"] = it.email ?: ""
                    userMap["fcmToken"] = token ?: "" // Use the actual FCM token here

                    val databaseReference = FirebaseDatabase.getInstance().getReference(DB_USERS)
                    databaseReference.child(userId).updateChildren(userMap).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val intent = Intent(this, MainActivity2::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "데이터베이스 업데이트 실패: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                    Toast.makeText(this, "FCM 토큰을 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        } ?: run {
            Toast.makeText(this, "로그인 실패: 사용자 정보를 받아올 수 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}