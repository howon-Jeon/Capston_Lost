package com.example.capston_lost

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.capston_lost.databinding.ActivityChattingBinding
import com.example.capston_lost.databinding.ActivityMain2Binding

class ChattingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChattingBinding // FoundDetail, LostDetail의 채팅버튼 선택시 나오는 채팅화면 Activity


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityChattingBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val ChatButton : Button = findViewById(R.id.chatting_back_button) // 뒤로가기 버튼

        ChatButton.setOnClickListener {// 뒤로가기 버튼 ClickListener 설정 -> 상세페이지로 전환
            finish()
        }
    }
}