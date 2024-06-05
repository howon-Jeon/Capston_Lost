package com.example.capston_lost.chatlist

data class ChatRoomItem (  //파이어베이스 DB 들어갈 것하고 연관
    val chatRoomId: String? = null,
    val otherUserName: String? = null,
    val lastMessage: String? = null,
    val otherUserId: String? = null,
)
