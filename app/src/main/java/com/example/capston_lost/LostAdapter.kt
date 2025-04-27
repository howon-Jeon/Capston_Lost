package com.example.capston_lost

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

// LostAdapter 클래스 정의, RecyclerView.Adapter를 상속받아 사용
class LostAdapter(private val dataSet: List<LostItem>, private val itemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<LostAdapter.ViewHolder>() {

    // 아이템 클릭 리스너 인터페이스 정의
    interface OnItemClickListener {
        fun onItemClick(position: Int) // 아이템 클릭 시 호출되는 메소드
    }

    // ViewHolder 클래스 정의, RecyclerView.ViewHolder를 상속받아 사용
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardTitle: TextView = view.findViewById(R.id.card_title) // 카드 타이틀 텍스트뷰
        val cardCatContent: TextView = view.findViewById(R.id.card_cat_content) // 카드 카테고리 내용 텍스트뷰
        val cardDateContent: TextView = view.findViewById(R.id.card_date_content) // 카드 날짜 내용 텍스트뷰
        val cardLocContent: TextView = view.findViewById(R.id.card_loc_content) // 카드 위치 내용 텍스트뷰
        val imageView: ImageView = view.findViewById(R.id.images)  // 이미지뷰 추가
    }

    // ViewHolder 생성 메소드
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.lost_cardview, parent, false) // lost_cardview 레이아웃을 inflate
        return ViewHolder(view) // ViewHolder 객체 반환
    }

    // ViewHolder에 데이터 바인딩
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lostItem = dataSet[position]
        holder.cardTitle.text = lostItem.title // 카드 타이틀 설정
        holder.cardCatContent.text = lostItem.itemType // 카드 카테고리 설정
        holder.cardDateContent.text = lostItem.getDate // 카드 날짜 설정
        holder.cardLocContent.text = lostItem.location // 카드 위치 설정

        // 이미지 로드하여 표시
        if (lostItem.imageUrl.isNotEmpty()) {
            val imageUrl = lostItem.imageUrl.split(",").first() // 첫 번째 이미지 URL 사용
            Glide.with(holder.itemView.context)
                .load(imageUrl) // 이미지 URL 로드
                .into(holder.imageView) // 이미지뷰에 설정
        }

        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(position) // 아이템 클릭 시 리스너 호출
        }
    }

    // 데이터셋 크기 반환
    override fun getItemCount() = dataSet.size
}

