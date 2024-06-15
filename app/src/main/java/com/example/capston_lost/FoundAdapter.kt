package com.example.capston_lost

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class FoundAdapter(private val dataSet: List<FoundItem>, private val itemClickListener: OnItemClickListener) : // FoundAdapter 클래스 정의
    RecyclerView.Adapter<FoundAdapter.ViewHolder>() { // RecyclerView.Adapter를 상속

    interface OnItemClickListener { // 아이템 클릭 리스너 인터페이스 정의
        fun onItemClick(position: Int) // 클릭 시 호출되는 메서드 정의
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) { // ViewHolder 클래스 정의
        val cardTitle: TextView = view.findViewById(R.id.card_title) // 카드 제목 텍스트 뷰를 레이아웃에서 가져오기
        val cardCatContent: TextView = view.findViewById(R.id.card_cat_content) // 카드 카테고리 내용 텍스트 뷰를 레이아웃에서 가져오기
        val cardDateContent: TextView = view.findViewById(R.id.card_date_content) // 카드 날짜 내용 텍스트 뷰를 레이아웃에서 가져오기
        val cardLocContent: TextView = view.findViewById(R.id.card_loc_content) // 카드 위치 내용 텍스트 뷰를 레이아웃에서 가져오기
        val imageView: ImageView = view.findViewById(R.id.images)  // 이미지 뷰를 레이아웃에서 가져오기
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder { // ViewHolder를 생성하는 메서드
        val view = LayoutInflater.from(parent.context) // 레이아웃 인플레이터를 통해
            .inflate(R.layout.found_cardview, parent, false) // 카드뷰 레이아웃을 인플레이트
        return ViewHolder(view) // ViewHolder 생성 및 반환
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) { // ViewHolder에 데이터를 바인딩하는 메서드
        val foundItem = dataSet[position] // 현재 아이템 가져오기
        holder.cardTitle.text = foundItem.title // 제목 설정
        holder.cardCatContent.text = foundItem.itemType // 아이템 종류 설정
        holder.cardDateContent.text = foundItem.getDate // 날짜 설정
        holder.cardLocContent.text = foundItem.location // 위치 설정

        if (foundItem.imageUrl.isNotEmpty()) { // 이미지 URL이 비어있지 않은 경우
            val imageUrl = foundItem.imageUrl.split(",").first() // 첫 번째 이미지 URL 사용
            Glide.with(holder.itemView.context) // Glide를 통해
                .load(imageUrl) // 이미지를 로드하고
                .into(holder.imageView) // 이미지 뷰에 표시
        }

        holder.itemView.setOnClickListener { // 아이템 클릭 리스너 설정
            itemClickListener.onItemClick(position) // 클릭 시 호출
        }
    }

    override fun getItemCount() = dataSet.size // 아이템 개수 반환
}
