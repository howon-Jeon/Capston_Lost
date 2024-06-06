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

class ChatAdapter : ListAdapter<ChatItem, ChatAdapter.ViewHolder>(differ) {

    var otherUserItem: UserItem? = null

    inner class ViewHolder(private val binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ChatItem) {
            val constraintSet = ConstraintSet()
            constraintSet.clone(binding.root)

            if (item.userId == otherUserItem?.userId) {
                // 상대방 메시지
                binding.usernameTextView.isVisible = true
                binding.usernameTextView.text = otherUserItem?.username
                binding.messageTextView.text = item.message

                // 상대방 메시지 레이아웃 설정
                constraintSet.connect(binding.usernameTextView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
                constraintSet.connect(binding.usernameTextView.id, ConstraintSet.END, ConstraintSet.UNSET, ConstraintSet.END, 0)
                constraintSet.connect(binding.messageTextView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0)
                constraintSet.connect(binding.messageTextView.id, ConstraintSet.END, ConstraintSet.UNSET, ConstraintSet.END, 0)

            } else {
                // 나의 메시지
                binding.usernameTextView.isVisible = false
                binding.messageTextView.text = item.message

                // 나의 메시지 레이아웃 설정
                constraintSet.connect(binding.messageTextView.id, ConstraintSet.START, ConstraintSet.UNSET, ConstraintSet.START, 0)
                constraintSet.connect(binding.messageTextView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0)
            }

            constraintSet.applyTo(binding.root)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemChatBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val differ = object : DiffUtil.ItemCallback<ChatItem>() {
            override fun areItemsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
                return oldItem.chatId == newItem.chatId
            }

            override fun areContentsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
