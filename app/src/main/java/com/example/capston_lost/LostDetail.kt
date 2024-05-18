package com.example.capston_lost

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

import com.example.capston_lost.databinding.FoundDetailBinding
import com.example.capston_lost.databinding.LostDetailBinding

class LostDetail : AppCompatActivity() { // TabLostFragment의 리사이클러 뷰 아이템 선택시 뜨는 분실물 상세페이지

    private lateinit var binding: LostDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = LostDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val ChatButton : Button = findViewById(R.id.lost_detail_chat) // 채팅 버튼

        ChatButton.setOnClickListener {// 채팅 버튼 ClickListener 설정 -> 채팅화면(activity_chatting) 전환
            val intent = Intent(this, ChattingActivity::class.java)
            startActivity(intent)
        }
    }
}