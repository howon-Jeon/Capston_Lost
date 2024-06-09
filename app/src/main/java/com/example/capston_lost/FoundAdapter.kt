package com.example.capston_lost

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class FoundAdapter(private val dataSet: List<FoundItem>, private val itemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<FoundAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardTitle: TextView = view.findViewById(R.id.card_title)
        val cardCatContent: TextView = view.findViewById(R.id.card_cat_content)
        val cardDateContent: TextView = view.findViewById(R.id.card_date_content)
        val cardLocContent: TextView = view.findViewById(R.id.card_loc_content)
        val imageView: ImageView = view.findViewById(R.id.images)  // 이미지 뷰 추가
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.found_cardview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val foundItem = dataSet[position]
        holder.cardTitle.text = foundItem.title
        holder.cardCatContent.text = foundItem.itemType
        holder.cardDateContent.text = foundItem.getDate
        holder.cardLocContent.text = foundItem.location

        // 이미지 로드하여 표시
        if (foundItem.imageUrl.isNotEmpty()) {
            val imageUrl = foundItem.imageUrl.split(",").first() // 첫 번째 이미지 URL 사용
            Glide.with(holder.itemView.context)
                .load(imageUrl)
                .into(holder.imageView)
        }

        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(position)
        }
    }

    override fun getItemCount() = dataSet.size
}
