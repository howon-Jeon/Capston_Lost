package com.example.capston_lost

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class TabLostFragment : Fragment(),  LostAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_lost, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.lost_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val dataSet = arrayOf("Item 1") // 카드뷰에 표시할 데이터

        adapter = LostAdapter(dataSet,this )
        recyclerView.adapter = adapter
    }

    override fun onItemClick(position: Int) { // 카드뷰 선택시 상세페이지 전환
        val intent = Intent(requireContext(), LostDetail::class.java)
        startActivity(intent)
    }
}

