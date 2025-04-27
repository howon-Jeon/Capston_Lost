package com.example.capston_lost

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 4 // Number of tabs
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment() // Replace with actual fragment class
            1 -> TabFoundFragment() // Replace with actual fragment class
            2 -> TabLostFragment() // This is the fragment you provided
            3 -> MyPageFragment() // Replace with actual fragment class
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}
