package com.example.capston_lost.chatdetail

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capston_lost.Key
import com.example.capston_lost.databinding.ActivityChatdetailBinding
import com.example.capston_lost.userlist.UserItem
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatdetailBinding // 바인딩 변수 선언

    private var chatRoomId: String = "" // 채팅방 ID 변수 선언
    private var otherUserId: String = "" // 상대 유저 ID 변수 선언
    private var myUserId: String = "" // 나의 유저 ID 변수 선언
    private var myUserName: String = "" // 나의 유저 이름 변수 선언
    private var isInit = false // 초기화 여부 변수 선언

    private val chatItemList = mutableListOf<ChatItem>() // 채팅 아이템 리스트 선언
    private val chatAdapter = ChatAdapter() // 채팅 어댑터 선언
    private lateinit var linearLayoutManager: LinearLayoutManager // LinearLayoutManager 변수 선언

    override fun onCreate(savedInstanceState: Bundle?) { // onCreate 메소드 오버라이드
        super.onCreate(savedInstanceState)
        binding = ActivityChatdetailBinding.inflate(layoutInflater) // 뷰 바인딩 초기화
        setContentView(binding.root) // 레이아웃 설정

        chatRoomId = intent.getStringExtra(EXTRA_CHAT_ROOM_ID) ?: return // 인텐트에서 채팅방 ID 가져오기
        otherUserId = intent.getStringExtra(EXTRA_OTHER_USER_ID) ?: return // 인텐트에서 상대 유저 ID 가져오기
        myUserId = Firebase.auth.currentUser?.uid ?: return // 현재 유저의 ID 가져오기
        linearLayoutManager = LinearLayoutManager(applicationContext) // LinearLayoutManager 초기화

        // 사용자 정보 가져오기
        Firebase.database.reference.child(Key.DB_USERS).child(myUserId).get()
            .addOnSuccessListener { // 성공적으로 데이터를 가져왔을 때 실행
                val myUserItem = it.getValue(UserItem::class.java) // UserItem 객체로 변환
                myUserName = myUserItem?.username ?: "" // 유저 이름 설정
            }

        Firebase.database.reference.child(Key.DB_USERS).child(otherUserId).get()
            .addOnSuccessListener { // 성공적으로 데이터를 가져왔을 때 실행
                val otherUserItem = it.getValue(UserItem::class.java) // UserItem 객체로 변환
                chatAdapter.otherUserItem = otherUserItem // 상대 유저 정보 설정
            }

        // 채팅 가져오기
        Firebase.database.reference.child(Key.DB_CHATS).child(chatRoomId)
            .addChildEventListener(object : ChildEventListener { // ChildEventListener 추가
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) { // 자식이 추가되었을 때 호출
                    val chatItem = snapshot.getValue(ChatItem::class.java) ?: return // ChatItem 객체로 변환
                    chatItemList.add(chatItem) // 채팅 아이템 리스트에 추가
                    chatAdapter.submitList(chatItemList.toList()) // 새 리스트를 할당하여 업데이트
                    binding.chatRecyclerView.scrollToPosition(chatItemList.size - 1) // 마지막 아이템으로 스크롤
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {} // 자식이 변경되었을 때 호출
                override fun onChildRemoved(snapshot: DataSnapshot) {} // 자식이 제거되었을 때 호출
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {} // 자식이 이동되었을 때 호출
                override fun onCancelled(error: DatabaseError) {} // 데이터 로드가 취소되었을 때 호출
            })

        // RecyclerView 설정
        binding.chatRecyclerView.apply { // RecyclerView 설정
            layoutManager = linearLayoutManager // 레이아웃 매니저 설정
            adapter = chatAdapter // 어댑터 설정
        }

        chatAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() { // 어댑터 데이터 옵저버 등록
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) { // 아이템이 삽입되었을 때 호출
                super.onItemRangeInserted(positionStart, itemCount)

                linearLayoutManager.smoothScrollToPosition( // 스무스 스크롤
                    binding.chatRecyclerView,
                    null,
                    chatAdapter.itemCount
                )
            }
        })

        // 메시지 전송
        binding.sendButton.setOnClickListener { // 전송 버튼 클릭 리스너 설정
            val message = binding.messageEditText.text.toString() // 메시지 가져오기
            if (message.isEmpty()) { // 메시지가 비었는지 확인
                Toast.makeText(applicationContext, "빈 메시지를 전송할 수는 없습니다.", Toast.LENGTH_SHORT).show() // 토스트 메시지 출력
                return@setOnClickListener
            }

            val newChatItem = ChatItem( // 새 채팅 아이템 생성
                message = message,
                userId = myUserId
            )

            Firebase.database.reference.child(Key.DB_CHATS).child(chatRoomId).push().apply { // Firebase에 새 채팅 아이템 추가
                newChatItem.chatId = key // 채팅 아이템의 ID 설정
                setValue(newChatItem) // 값 설정
            }

            val updates: MutableMap<String, Any> = hashMapOf( // 업데이트 할 데이터 맵 생성
                "${Key.DB_CHAT_ROOMS}/$myUserId/$otherUserId/lastMessage" to message,
                "${Key.DB_CHAT_ROOMS}/$otherUserId/$myUserId/lastMessage" to message,
                "${Key.DB_CHAT_ROOMS}/$otherUserId/$myUserId/chatRoomId" to chatRoomId,
                "${Key.DB_CHAT_ROOMS}/$otherUserId/$myUserId/otherUserId" to myUserId,
                "${Key.DB_CHAT_ROOMS}/$otherUserId/$myUserId/otherUserName" to myUserName,
            )
            Firebase.database.reference.updateChildren(updates) // Firebase에 업데이트

            binding.messageEditText.text.clear() // 메시지 입력 필드 비우기
        }
    }

    companion object { // companion object 선언
        const val EXTRA_CHAT_ROOM_ID = "CHAT_ROOM_ID" // 상수 선언
        const val EXTRA_OTHER_USER_ID = "OTHER_USER_ID" // 상수 선언
    }
}
