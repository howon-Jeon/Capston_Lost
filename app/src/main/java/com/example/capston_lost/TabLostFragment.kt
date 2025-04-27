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

class TabLostFragment : Fragment(), LostAdapter.OnItemClickListener { // TabLostFragment 클래스 정의, Fragment 및 LostAdapter.OnItemClickListener 상속

    private lateinit var recyclerView: RecyclerView // RecyclerView 타입의 지연 초기화 변수 선언
    private lateinit var adapter: LostAdapter // LostAdapter 타입의 지연 초기화 변수 선언
    private lateinit var firestore: FirebaseFirestore // FirebaseFirestore 타입의 지연 초기화 변수 선언
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout // SwipeRefreshLayout 타입의 지연 초기화 변수 선언
    private val lostItems = mutableListOf<LostItem>() // LostItem 리스트 초기화

    override fun onCreateView( // onCreateView 메소드 오버라이드
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_lost, container, false) // 레이아웃 인플레이트
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { // onViewCreated 메소드 오버라이드
        super.onViewCreated(view, savedInstanceState)

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout) // swipeRefreshLayout 초기화
        recyclerView = view.findViewById(R.id.lost_recyclerView) // recyclerView 초기화
        recyclerView.layoutManager = LinearLayoutManager(requireContext()) // RecyclerView 레이아웃 매니저 설정

        adapter = LostAdapter(lostItems, this) // 어댑터 초기화
        recyclerView.adapter = adapter // RecyclerView에 어댑터 설정

        firestore = FirebaseFirestore.getInstance() // Firestore 인스턴스 초기화
        loadLostItems() // 데이터 로드

        swipeRefreshLayout.setOnRefreshListener { // 스와이프 새로고침 리스너 설정
            loadLostItems() // 데이터 로드
        }
    }

    private fun loadLostItems() { // 데이터 로드 메소드
        firestore.collection("lost_reports") // "lost_reports" 컬렉션 참조
            .orderBy("getDate", Query.Direction.DESCENDING) // 날짜 기준 내림차순 정렬
            .get() // 데이터 가져오기
            .addOnSuccessListener { result -> // 데이터 가져오기 성공 시
                lostItems.clear() // 리스트 초기화
                for (document in result) { // 결과의 각 문서에 대해
                    val lostItem = document.toObject(LostItem::class.java).apply { // LostItem 객체 생성 및 초기화
                        this.documentId = document.id // documentId 저장
                    }
                    lostItems.add(lostItem) // 리스트에 아이템 추가
                }
                adapter.notifyDataSetChanged() // 어댑터 데이터 변경 알림
                swipeRefreshLayout.isRefreshing = false // 새로고침 상태 해제
            }
            .addOnFailureListener { e -> // 데이터 가져오기 실패 시
                Toast.makeText(requireContext(), "데이터를 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show() // 토스트 메시지 표시
                swipeRefreshLayout.isRefreshing = false // 새로고침 상태 해제
            }
    }

    override fun onItemClick(position: Int) { // 아이템 클릭 시 동작 정의
        val selectedLostItem = lostItems[position] // 선택된 아이템 가져오기
        val intent = Intent(requireContext(), LostDetail::class.java) // LostDetail 액티비티로 이동하는 인텐트 생성
        intent.putExtra("lostItemId", selectedLostItem.documentId) // 인텐트에 데이터 추가
        startActivity(intent) // 액티비티 시작
    }
}
