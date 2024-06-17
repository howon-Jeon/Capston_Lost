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

// LostPageFragment 클래스 정의, Fragment와 LostAdapter.OnItemClickListener를 상속받아 사용
class LostPageFragment : Fragment(), LostAdapter.OnItemClickListener {

    private lateinit var firestore: FirebaseFirestore // Firestore 인스턴스 변수 선언
    private lateinit var recyclerView: RecyclerView // RecyclerView 인스턴스 변수 선언
    private lateinit var lostAdapter: LostAdapter // LostAdapter 인스턴스 변수 선언
    private val lostItems = mutableListOf<LostItem>() // LostItem 객체들을 담는 리스트 선언

    // Fragment의 뷰를 생성하는 메소드
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_lost, container, false) // fragment_lost 레이아웃을 inflate
    }

    // 뷰가 생성된 후 호출되는 메소드
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firestore = FirebaseFirestore.getInstance() // Firestore 인스턴스 초기화
        recyclerView = view.findViewById(R.id.lost_recyclerView) // RecyclerView 초기화
        recyclerView.layoutManager = LinearLayoutManager(context) // RecyclerView 레이아웃 매니저 설정

        lostAdapter = LostAdapter(lostItems, this) // LostAdapter 초기화
        recyclerView.adapter = lostAdapter // RecyclerView 어댑터 설정

        loadLostItems() // 분실물 항목을 로드하는 메소드 호출
    }

    // Firestore에서 분실물 항목을 로드하는 메소드
    private fun loadLostItems() {
        firestore.collection("lost_reports") // Firestore의 lost_reports 컬렉션 참조
            .orderBy("getDate", Query.Direction.DESCENDING) // 날짜 기준 내림차순 정렬
            .get() // 쿼리 실행
            .addOnSuccessListener { result ->
                lostItems.clear() // 기존 항목들 초기화
                for (document in result) {
                    val lostItem = document.toObject(LostItem::class.java).apply {
                        // documentId를 따로 저장
                        this.documentId = document.id // Document ID를 저장
                    }
                    lostItems.add(lostItem) // 리스트에 항목 추가
                }
                lostAdapter.notifyDataSetChanged() // 어댑터에 데이터 변경 알림
            }
            .addOnFailureListener { e ->
                // 오류 처리
            }
    }

    // 아이템 클릭 시 호출되는 메소드
    override fun onItemClick(position: Int) {
        val selectedLostItem = lostItems[position] // 선택된 항목 가져오기
        val intent = Intent(requireContext(), LostDetail::class.java) // LostDetail 액티비티를 호출하는 인텐트 생성
        intent.putExtra("lostItemId", selectedLostItem.documentId) // 인텐트에 데이터 추가
        startActivity(intent) // 액티비티 시작
    }
}

