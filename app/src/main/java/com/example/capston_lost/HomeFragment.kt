package com.example.capston_lost

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_home, container, false) // 탭의 홈 선택 시 나오는 fragment -> activity_home
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lostButton : Button = view.findViewById(R.id.home_lost_button) // 분실물 신고 버튼

        lostButton.setOnClickListener { // 습득물 신고 버튼 ClickListener 설정 -> 분실물 신고 화면 전환
            val intent = Intent(requireContext(), LostReportpage::class.java)
            startActivity(intent)
        }

        val foundButton : Button = view.findViewById(R.id.home_found_button)  // 습득물 신고 버튼

        foundButton.setOnClickListener { // 습득물 신고 버튼 ClickListener 설정 -> 습득물 신고 화면 전환
            val intent = Intent(requireContext(), LostReportpage::class.java)
            startActivity(intent)
        }

        val searchButton : Button = view.findViewById(R.id.home_search_button) // 검색 버튼

        searchButton.setOnClickListener{ // 검색 버튼 ClickListenter 설정 -> 검색 화면 전환
            val intent = Intent(requireContext(), Searchpage::class.java)
            startActivity(intent)
        }

    }

}