package com.example.capston_lost.chatdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.capston_lost.databinding.ItemChatBinding
import com.example.capston_lost.userlist.UserItem

class ChatAdapter : ListAdapter<ChatItem, ChatAdapter.ViewHolder>(differ) { // ChatAdapter 클래스 선언

    var otherUserItem: UserItem? = null // 상대 유저 아이템 변수 선언

    inner class ViewHolder(private val binding: ItemChatBinding) : // ViewHolder 클래스 선언
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ChatItem) { // bind 메소드
            val constraintSet = ConstraintSet() // ConstraintSet 객체 생성
            constraintSet.clone(binding.root) // ConstraintSet 클론

            if (item.userId == otherUserItem?.userId) { // 상대방 메시지인지 확인
                binding.usernameTextView.isVisible = true // 유저 이름 텍스트 뷰 보이기
                binding.usernameTextView.text = otherUserItem?.username // 유저 이름 설정
                binding.messageTextView.text = item.message // 메시지 설정

                // 상대방 메시지 레이아웃 설정
                constraintSet.connect(binding.usernameTextView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
                constraintSet.connect(binding.usernameTextView.id, ConstraintSet.END, ConstraintSet.UNSET, ConstraintSet.END, 0)
                constraintSet.connect(binding.messageTextView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
                constraintSet.connect(binding.messageTextView.id, ConstraintSet.END, ConstraintSet.UNSET, ConstraintSet.END, 0)

            } else { // 나의 메시지일 때
                binding.usernameTextView.isVisible = false // 유저 이름 텍스트 뷰 숨기기
                binding.messageTextView.text = item.message // 메시지 설정

                // 나의 메시지 레이아웃 설정
                constraintSet.connect(binding.messageTextView.id, ConstraintSet.START, ConstraintSet.UNSET, ConstraintSet.START, 0)
                constraintSet.connect(binding.messageTextView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
            }

            constraintSet.applyTo(binding.root) // ConstraintSet 적용
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder { // onCreateViewHolder 메소드
        return ViewHolder(
            ItemChatBinding.inflate( // ItemChatBinding 인플레이트
                LayoutInflater.from(parent.context), // 부모 컨텍스트에서 LayoutInflater 가져오기
                parent, // 부모 뷰 그룹
                false // attachToRoot를 false로 설정
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) { // onBindViewHolder 메소드
        holder.bind(currentList[position]) // 아이템 바인딩
    }

    companion object { // companion object 선언
        val differ = object : DiffUtil.ItemCallback<ChatItem>() { // DiffUtil.ItemCallback 객체 선언
            override fun areItemsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean { // 아이템이 동일한지 확인
                return oldItem.chatId == newItem.chatId // 채팅 아이디가 동일한지 비교
            }

            override fun areContentsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean { // 아이템의 내용이 동일한지 확인
                return oldItem == newItem // 객체 비교
            }
        }
    }
}
