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

class TabFoundFragment : Fragment(), FoundAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FoundAdapter
    private lateinit var firestore: FirebaseFirestore
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val foundItems = mutableListOf<FoundItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_found, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        recyclerView = view.findViewById(R.id.found_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = FoundAdapter(foundItems, this)
        recyclerView.adapter = adapter

        firestore = FirebaseFirestore.getInstance()
        loadFoundItems()

        swipeRefreshLayout.setOnRefreshListener {
            loadFoundItems()
        }
    }

    private fun loadFoundItems() {
        firestore.collection("found_reports")
            .orderBy("getDate", Query.Direction.DESCENDING) // 날짜 기준 내림차순 정렬
            .get()
            .addOnSuccessListener { result ->
                foundItems.clear()
                for (document in result) {
                    val foundItem = document.toObject(FoundItem::class.java).apply {
                        // documentId를 따로 저장
                        this.documentId = document.id
                    }
                    foundItems.add(foundItem)
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
        val selectedFoundItem = foundItems[position]
        val intent = Intent(requireContext(), FoundDetail::class.java)
        intent.putExtra("foundItemId", selectedFoundItem.documentId)
        startActivity(intent)
    }
}
