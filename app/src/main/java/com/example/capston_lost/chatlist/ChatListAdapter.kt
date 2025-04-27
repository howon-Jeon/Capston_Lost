package com.example.capston_lost.chatlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.capston_lost.databinding.ItemChatroomBinding
import com.example.capston_lost.databinding.ItemUserBinding

class ChatListAdapter(private val onClick: (ChatRoomItem) -> Unit) : ListAdapter<ChatRoomItem, ChatListAdapter.ViewHolder>(differ) { // ChatListAdapter 클래스 선언

    inner class ViewHolder(private val binding: ItemChatroomBinding) : RecyclerView.ViewHolder(binding.root) { // ViewHolder 클래스 선언

        fun bind(item: ChatRoomItem) { // bind 메소드
            binding.nicknameTextView.text = item.otherUserName // 닉네임 텍스트 뷰 설정
            binding.lastMessageTextView.text = item.lastMessage // 마지막 메시지 텍스트 뷰 설정

            binding.root.setOnClickListener { // 루트 뷰 클릭 리스너 설정
                onClick(item) // 클릭 시 onClick 함수 호출
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder { // onCreateViewHolder 메소드 오버라이드
        return ViewHolder(ItemChatroomBinding.inflate( // ViewHolder 객체 반환
            LayoutInflater.from(parent.context), // 레이아웃 인플레이터 설정
            parent, // 부모 뷰그룹 설정
            false // 첨부 여부 설정
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) { // onBindViewHolder 메소드 오버라이드
        holder.bind(currentList[position]) // ViewHolder 바인딩
    }

    companion object { // companion object 선언
        val differ = object : DiffUtil.ItemCallback<ChatRoomItem>() { // DiffUtil.ItemCallback 객체 선언
            override fun areItemsTheSame(oldItem: ChatRoomItem, newItem: ChatRoomItem): Boolean { // 아이템 비교 메소드
                return oldItem.chatRoomId == newItem.chatRoomId // 아이템 ID 비교
            }

            override fun areContentsTheSame(oldItem: ChatRoomItem, newItem: ChatRoomItem): Boolean { // 콘텐츠 비교 메소드
                return oldItem == newItem // 아이템 내용 비교
            }
        }
    }
}
