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

class FoundPageFragment : Fragment(), FoundAdapter.OnItemClickListener { // Fragment와 OnItemClickListener 인터페이스 구현하는 FoundPageFragment 클래스 정의

    private lateinit var firestore: FirebaseFirestore // Firestore 인스턴스를 나중에 초기화하기 위해 선언
    private lateinit var recyclerView: RecyclerView // RecyclerView 인스턴스를 나중에 초기화하기 위해 선언
    private lateinit var foundAdapter: FoundAdapter // FoundAdapter 인스턴스를 나중에 초기화하기 위해 선언
    private val foundItems = mutableListOf<FoundItem>() // FoundItem 리스트 초기화

    override fun onCreateView( // Fragment의 뷰를 생성하는 메서드
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_find, container, false) // layout 파일을 inflate하여 반환
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { // Fragment의 뷰가 생성된 후 호출되는 메서드
        super.onViewCreated(view, savedInstanceState)

        firestore = FirebaseFirestore.getInstance() // Firestore 인스턴스 초기화
        recyclerView = view.findViewById(R.id.found_recyclerView) // RecyclerView를 레이아웃에서 가져오기
        recyclerView.layoutManager = LinearLayoutManager(context) // RecyclerView의 레이아웃 매니저 설정

        foundAdapter = FoundAdapter(foundItems, this) // FoundAdapter 초기화
        recyclerView.adapter = foundAdapter // RecyclerView에 어댑터 설정

        loadFoundItems() // 데이터를 불러오는 메서드 호출
    }

    private fun loadFoundItems() { // Firestore에서 데이터를 불러오는 메서드
        firestore.collection("found_reports")
            .orderBy("getDate", Query.Direction.DESCENDING) // getDate 기준 내림차순 정렬
            .get()
            .addOnSuccessListener { result -> // 데이터 불러오기 성공 시
                foundItems.clear() // 기존 데이터 클리어
                for (document in result) { // 결과에서 각 문서를 반복
                    val foundItem = document.toObject(FoundItem::class.java).apply {
                        // document를 FoundItem 객체로 변환하고 documentId 설정
                        this.documentId = document.id
                    }
                    foundItems.add(foundItem) // foundItems 리스트에 추가
                }
                foundAdapter.notifyDataSetChanged() // 어댑터에 데이터 변경 알림
            }
            .addOnFailureListener { e -> // 데이터 불러오기 실패 시
                // 오류 처리
            }
    }

    override fun onItemClick(position: Int) { // 아이템 클릭 시 호출되는 메서드
        val selectedFoundItem = foundItems[position] // 선택된 아이템 가져오기
        val intent = Intent(requireContext(), FoundDetail::class.java) // FoundDetail 액티비티로 이동하는 인텐트 생성
        intent.putExtra("foundItemId", selectedFoundItem.documentId) // 인텐트에 문서 ID 추가
        startActivity(intent) // 액티비티 시작
    }
}
