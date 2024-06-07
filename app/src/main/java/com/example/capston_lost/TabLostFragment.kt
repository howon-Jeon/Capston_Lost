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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class TabLostFragment : Fragment(), LostAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LostAdapter
    private lateinit var firestore: FirebaseFirestore
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val lostItems = mutableListOf<LostItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_lost, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        recyclerView = view.findViewById(R.id.lost_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = LostAdapter(lostItems, this)
        recyclerView.adapter = adapter

        firestore = FirebaseFirestore.getInstance()
        loadLostItems()

        swipeRefreshLayout.setOnRefreshListener {
            loadLostItems()
        }
    }

    private fun loadLostItems() {
        firestore.collection("lost_reports")
            .orderBy("getDate", Query.Direction.DESCENDING) // 날짜 기준 내림차순 정렬
            .get()
            .addOnSuccessListener { result ->
                lostItems.clear()
                for (document in result) {
                    val lostItem = document.toObject(LostItem::class.java).apply {
                        // documentId를 따로 저장
                        this.documentId = document.id
                    }
                    lostItems.add(lostItem)
                }
                adapter.notifyDataSetChanged()
                swipeRefreshLayout.isRefreshing = false
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "데이터를 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                swipeRefreshLayout.isRefreshing = false
            }
    }

    override fun onItemClick(position: Int) {
        val selectedLostItem = lostItems[position]
        val intent = Intent(requireContext(), LostDetail::class.java)
        intent.putExtra("lostItemId", selectedLostItem.documentId)
        startActivity(intent)
    }
}
