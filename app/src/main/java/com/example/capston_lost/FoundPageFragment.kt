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

class FoundPageFragment : Fragment(), FoundAdapter.OnItemClickListener {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var foundAdapter: FoundAdapter
    private val foundItems = mutableListOf<FoundItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_find, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firestore = FirebaseFirestore.getInstance()
        recyclerView = view.findViewById(R.id.found_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        foundAdapter = FoundAdapter(foundItems, this)
        recyclerView.adapter = foundAdapter

        loadFoundItems()
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
                foundAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                // 오류 처리
            }
    }

    override fun onItemClick(position: Int) {
        val selectedFoundItem = foundItems[position]
        val intent = Intent(requireContext(), FoundDetail::class.java)
        intent.putExtra("foundItemId", selectedFoundItem.documentId)
        startActivity(intent)
    }
}
