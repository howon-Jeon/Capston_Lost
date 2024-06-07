package com.example.capston_lost

data class LostItem(

    val title: String = "",
    val itemType: String = "",
    val getDate: String = "",
    val location: String = "",
    val remarks: String = "",
    val userId: String = "",
    val imageUrl: String = "", // 이미지 URL 필드 추가
    var documentId: String = "" // 새로 추가된 필드
)
