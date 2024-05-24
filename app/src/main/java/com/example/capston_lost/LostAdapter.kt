package com.example.capston_lost

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LostAdapter(private val dataSet: Array<String>, private val itemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<LostAdapter.ViewHolder>() { // 분실물 목록 화면(activity_lost)의 리사이클러뷰 아이템을 위한 Adapter


    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardTitle: TextView = view.findViewById(R.id.card_title)
        // 카드뷰의 다른 TextView 등도 여기에 추가할 수 있습니다.
    }

    //xml파일을 inflate하여 뷰홀더 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.lost_cardview, parent, false)

        return ViewHolder(view)
    }

    //onCreateViewHolder에서 만든 뷰와 실제 데이터 연결
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cardTitle.text = dataSet[position]
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(position)
        }
    }

    //리사이클러뷰의 아이템 총 개수 반환
    override fun getItemCount() = dataSet.size
    //return

}