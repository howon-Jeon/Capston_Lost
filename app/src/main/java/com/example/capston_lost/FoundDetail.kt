package com.example.capston_lost

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.capston_lost.databinding.ActivityMain2Binding
import com.example.capston_lost.databinding.FoundDetailBinding

class FoundDetail : AppCompatActivity() { // TabFoundFragment의 리사이클러 뷰 아이템 선택시 뜨는 습득물 상세페이지

    private lateinit var binding: FoundDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = FoundDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val ChatButton : Button = findViewById(R.id.found_detail_chat) // 채팅 버튼

        ChatButton.setOnClickListener {// 채팅 버튼 ClickListener 설정 -> 채팅화면(activity_chatting) 전환
            val intent = Intent(this, ChattingActivity::class.java)
            startActivity(intent)
        }
    }
}