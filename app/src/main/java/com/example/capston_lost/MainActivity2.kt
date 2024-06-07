package com.example.capston_lost

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.viewpager2.widget.ViewPager2
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.capston_lost.databinding.ActivityMain2Binding
import com.google.android.material.tabs.TabLayout
import com.example.capston_lost.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import android.graphics.Typeface
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.view.ViewGroup

class MainActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMain2Binding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val currentUser = Firebase.auth.currentUser

        if (currentUser == null) {
            // 로그인이 안되어있음
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return // 반드시 return하여 이후 코드를 실행하지 않도록 합니다.
        }

        // Get the user name from the intent
        val userName = intent.getStringExtra("USER_NAME")

        // 탭 연결
        val viewPager: ViewPager2 = binding.viewpager
        val tabLayout: TabLayout = binding.tabs

        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        val typeface: Typeface? = ResourcesCompat.getFont(this, R.font.gmarketsansttfmedium)

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

        // Apply the typeface to all tab items after setup
        for (i in 0 until tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(i)
            if (tab != null) {
                applyFontToTab(tab, typeface)
            }
        }

        // Add a listener to apply the typeface when a tab is selected
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    applyFontToTab(tab, typeface)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    applyFontToTab(tab, typeface)
                }
            }
        })
    }

    private fun applyFontToTab(tab: TabLayout.Tab, typeface: Typeface?) {
        val tabViewGroup = (tab.view as ViewGroup)
        val tabViewChildCount = tabViewGroup.childCount
        for (i in 0 until tabViewChildCount) {
            val tabViewChild = tabViewGroup.getChildAt(i)
            if (tabViewChild is TextView) {
                tabViewChild.typeface = typeface
            }
        }
    }
}
