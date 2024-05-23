package com.example.capston_lost

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.tabs.TabLayout

import android.widget.Button
import androidx.fragment.app.Fragment

class Searchpage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_searchpage)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        setContentView(R.layout.activity_searchpage)
//test
        val tabLayout: TabLayout = findViewById(R.id.tabLayout)
        val lostFragment: Fragment = LostFragment()
        val findFragment: Fragment = FindFragment()

        supportFragmentManager.beginTransaction().replace(R.id.main_view, findFragment).commit()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> {
                        supportFragmentManager.beginTransaction().replace(R.id.main_view, findFragment).commit()
                    }
                    1 -> {
                        supportFragmentManager.beginTransaction().replace(R.id.main_view, lostFragment).commit()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }
}