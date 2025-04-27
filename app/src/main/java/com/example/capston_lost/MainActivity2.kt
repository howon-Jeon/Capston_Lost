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

class MainActivity2 : AppCompatActivity() { // MainActivity2 클래스 정의, AppCompatActivity 상속

    private lateinit var binding: ActivityMain2Binding // ActivityMain2Binding 타입의 지연 초기화 변수 선언

    override fun onCreate(savedInstanceState: Bundle?) { // onCreate 메소드 오버라이드
        binding = ActivityMain2Binding.inflate(layoutInflater) // binding 변수 초기화
        super.onCreate(savedInstanceState) // 상위 클래스의 onCreate 호출
        setContentView(binding.root) // 레이아웃 설정
        val currentUser = Firebase.auth.currentUser // 현재 로그인한 사용자 가져오기

        if (currentUser == null) { // 사용자가 로그인되어 있지 않으면
            startActivity(Intent(this, MainActivity::class.java)) // MainActivity로 이동
            finish() // 현재 액티비티 종료
            return // 이후 코드 실행 방지
        }

        val userName = intent.getStringExtra("USER_NAME") // 인텐트에서 사용자 이름 가져오기

        val viewPager: ViewPager2 = binding.viewpager // ViewPager2 객체 초기화
        val tabLayout: TabLayout = binding.tabs // TabLayout 객체 초기화

        val adapter = ViewPagerAdapter(this) // ViewPagerAdapter 객체 생성
        viewPager.adapter = adapter // ViewPager의 어댑터 설정

        val typeface: Typeface? = ResourcesCompat.getFont(this, R.font.gmarketsansttfmedium) // 커스텀 폰트 로드

        TabLayoutMediator(tabLayout, viewPager) { tab, position -> // TabLayoutMediator 설정
            when (position) { // 포지션에 따라 탭 설정
                0 -> {
                    tab.text = "홈" // 첫 번째 탭 텍스트 설정
                    tab.setIcon(R.drawable.tab_home) // 첫 번째 탭 아이콘 설정
                }
                1 -> {
                    tab.text = "습득물" // 두 번째 탭 텍스트 설정
                    tab.setIcon(R.drawable.tab_found) // 두 번째 탭 아이콘 설정
                }
                2 -> {
                    tab.text = "분실물" // 세 번째 탭 텍스트 설정
                    tab.setIcon(R.drawable.tab_lost) // 세 번째 탭 아이콘 설정
                }
                3 -> {
                    tab.text = "마이페이지" // 네 번째 탭 텍스트 설정
                    tab.setIcon(R.drawable.tab_mypage) // 네 번째 탭 아이콘 설정
                }
            }
        }.attach() // TabLayoutMediator 적용

        for (i in 0 until tabLayout.tabCount) { // 모든 탭에 대해 폰트 적용
            val tab = tabLayout.getTabAt(i) // 각 탭 가져오기
            if (tab != null) {
                applyFontToTab(tab, typeface) // 폰트 적용
            }
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener { // 탭 선택 리스너 추가
            override fun onTabSelected(tab: TabLayout.Tab?) { // 탭 선택 시
                if (tab != null) {
                    applyFontToTab(tab, typeface) // 폰트 적용
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {} // 탭 선택 해제 시 (구현 필요 없음)

            override fun onTabReselected(tab: TabLayout.Tab?) { // 탭 다시 선택 시
                if (tab != null) {
                    applyFontToTab(tab, typeface) // 폰트 적용
                }
            }
        })
    }

    private fun applyFontToTab(tab: TabLayout.Tab, typeface: Typeface?) { // 탭에 폰트 적용 메소드
        val tabViewGroup = (tab.view as ViewGroup) // 탭의 뷰 그룹 가져오기
        val tabViewChildCount = tabViewGroup.childCount // 뷰 그룹의 자식 수
        for (i in 0 until tabViewChildCount) { // 모든 자식 뷰에 대해
            val tabViewChild = tabViewGroup.getChildAt(i) // 각 자식 뷰 가져오기
            if (tabViewChild is TextView) { // 자식 뷰가 TextView일 경우
                tabViewChild.typeface = typeface // 폰트 적용
            }
        }
    }
}
