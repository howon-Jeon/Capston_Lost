package com.example.capston_lost

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.capston_lost.databinding.ActivityFindReportpageBinding

class FindReportpage : AppCompatActivity() {
    private lateinit var binding: ActivityFindReportpageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityFindReportpageBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val closeButton : Button = findViewById(R.id.closeBtn) // 뒤로 가기 버튼

        closeButton.setOnClickListener {// 뒤로 가기 버튼 ClickListener 설정 -> home으로 돌아감
            finish()
        }
    }
}