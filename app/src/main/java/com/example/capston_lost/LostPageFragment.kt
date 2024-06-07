package com.example.capston_lost

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class LostPageFragment : Fragment(), LostAdapter.OnItemClickListener {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var lostAdapter: LostAdapter
    private val lostItems = mutableListOf<LostItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_lost, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firestore = FirebaseFirestore.getInstance()
        recyclerView = view.findViewById(R.id.lost_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        lostAdapter = LostAdapter(lostItems, this)
        recyclerView.adapter = lostAdapter

        loadLostItems()
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
                lostAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                // 오류 처리
            }
    }

    override fun onItemClick(position: Int) {
        val selectedLostItem = lostItems[position]
        val intent = Intent(requireContext(), LostDetail::class.java)
        intent.putExtra("lostItemId", selectedLostItem.documentId)
        startActivity(intent)
    }
}
