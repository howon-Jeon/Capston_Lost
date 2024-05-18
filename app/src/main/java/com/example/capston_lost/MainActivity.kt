package com.example.capston_lost

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.capston_lost.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.memberButton.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java )
            startActivity(intent)  // intent 실행
        }
        binding.loginButton.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java )
            startActivity(intent)  // intent 실행
        }
    }

}