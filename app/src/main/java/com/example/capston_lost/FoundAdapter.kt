package com.example.capston_lost

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FoundAdapter(private val dataSet: Array<String>, private val itemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<FoundAdapter.ViewHolder>() { // 습득물 목록 화면(activity_found)의 리사이클러뷰 아이템을 위한 Adapter

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardTitle: TextView = view.findViewById(R.id.card_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.found_cardview, parent, false)

        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cardTitle.text = dataSet[position]
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(position)
        }
    }

    override fun getItemCount() = dataSet.size
}