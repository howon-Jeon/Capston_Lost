package com.example.capston_lost

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.capston_lost.databinding.ActivityMain2Binding
import com.example.capston_lost.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMain2Binding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val currentUser = Firebase.auth.currentUser

        if(currentUser == null) {
            // 로그인이 안되어있음
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        // 탭 연결
        setContentView(R.layout.activity_main2)

        val viewPager: ViewPager2 = findViewById(R.id.viewpager)
        val tabLayout: TabLayout = findViewById(R.id.tabs)

        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "홈"
                    tab.setIcon(R.drawable.tab_home)
                }
                1 -> {
                    tab.text = "습득물"
                    tab.setIcon(R.drawable.tab_found)
                }
                2 -> {
                    tab.text = "분실물"
                    tab.setIcon(R.drawable.tab_lost)
                }
                3 -> {
                    tab.text = "마이페이지"
                    tab.setIcon(R.drawable.tab_mypage)
                }
            }
        }.attach()
    }

}