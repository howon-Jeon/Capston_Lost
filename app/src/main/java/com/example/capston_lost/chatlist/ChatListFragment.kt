package com.example.capston_lost.chatlist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capston_lost.Key
import com.example.capston_lost.R
import com.example.capston_lost.chatdetail.ChatActivity
import com.example.capston_lost.databinding.FragmentChatlistBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatListFragment : Fragment(R.layout.fragment_chatlist) { // ChatListFragment 클래스 선언

    private var _binding: FragmentChatlistBinding? = null // 바인딩 변수 선언 (초기화 되지 않은 상태)
    private val binding get() = _binding!! // _binding 변수의 getter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { // onViewCreated 메소드 오버라이드
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentChatlistBinding.bind(view) // 뷰 바인딩 초기화

        val chatListAdapter = ChatListAdapter { chatRoomItem -> // 채팅 리스트 어댑터 초기화
            val intent = Intent(context, ChatActivity::class.java) // ChatActivity로 이동하는 인텐트 생성
            intent.putExtra(ChatActivity.EXTRA_OTHER_USER_ID, chatRoomItem.otherUserId) // 인텐트에 다른 유저 ID 추가
            intent.putExtra(ChatActivity.EXTRA_CHAT_ROOM_ID, chatRoomItem.chatRoomId) // 인텐트에 채팅방 ID 추가
            startActivity(intent) // 액티비티 시작
        }

        binding.chatListRecyclerView.apply { // 리사이클러 뷰 설정
            layoutManager = LinearLayoutManager(context) // 레이아웃 매니저 설정
            adapter = chatListAdapter // 어댑터 설정
        }

        val currentUserId = Firebase.auth.currentUser?.uid ?: return // 현재 유저 ID 가져오기, 없으면 리턴
        val chatRoomsDB = Firebase.database.reference.child(Key.DB_CHAT_ROOMS).child(currentUserId) // 현재 유저의 채팅방 DB 참조 생성

        chatRoomsDB.addValueEventListener(object : ValueEventListener { // 채팅방 DB에 이벤트 리스너 추가
            override fun onDataChange(snapshot: DataSnapshot) { // 데이터 변경 시 호출
                val chatRoomList = snapshot.children.mapNotNull { // 채팅방 리스트 생성
                    try {
                        it.getValue(ChatRoomItem::class.java) // 채팅방 아이템 객체로 변환
                    } catch (e: DatabaseException) { // 데이터베이스 예외 발생 시
                        Log.e("ChatListFragment", "DatabaseException: ${e.message}") // 로그 출력
                        null // null 반환
                    }
                }
                chatListAdapter.submitList(chatRoomList) // 어댑터에 채팅방 리스트 제출
            }

            override fun onCancelled(error: DatabaseError) { // 데이터 로드 취소 시 호출
                Log.e("ChatListFragment", "Database error: ${error.message}") // 로그 출력
            }
        })
    }

    override fun onDestroyView() { // onDestroyView 메소드 오버라이드
        super.onDestroyView()
        _binding = null // 바인딩 변수 null로 설정
    }
}
