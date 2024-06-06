package com.example.capston_lost

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.capston_lost.databinding.ActivityMypageBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

// import com.example.myapplication.R

class Mypage : AppCompatActivity() {
    private lateinit var binding: ActivityMypageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMypageBinding.inflate(layoutInflater)

        setContentView(binding.root)

/*binding.logout.setOnClickListener {// 로그아웃 기능 구현
            Firebase.auth.signOut()
            val intent = Intent(this, MainActivity::class.java )
            startActivity(intent)  // intent 실행
        }

        binding.chat.setOnClickListener {// 채팅 기능 구현
            val intent = Intent(this, ChatMainActivity::class.java )
            startActivity(intent)  // intent 실행
        }*/

    }



}