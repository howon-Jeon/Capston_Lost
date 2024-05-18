package com.example.capston_lost

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.capston_lost.databinding.ActivityFindReportpageBinding
import com.example.capston_lost.databinding.ActivityLostReportpageBinding


class LostReportpage : AppCompatActivity() {
    private lateinit var binding: ActivityLostReportpageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLostReportpageBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val closeButton : Button = findViewById(R.id.closeBtn) // 뒤로 가기 버튼

        closeButton.setOnClickListener {// 뒤로 가기 버튼 ClickListener 설정 -> home으로 돌아감
            finish()
        }
    }
}