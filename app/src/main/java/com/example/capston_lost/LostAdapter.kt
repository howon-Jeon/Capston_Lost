package com.example.capston_lost

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LostAdapter(private val dataSet: List<LostItem>, private val itemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<LostAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardTitle: TextView = view.findViewById(R.id.card_title)
        val cardCatContent: TextView = view.findViewById(R.id.card_cat_content)
        val cardDateContent: TextView = view.findViewById(R.id.card_date_content)
        val cardLocContent: TextView = view.findViewById(R.id.card_loc_content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.lost_cardview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lostItem = dataSet[position]
        holder.cardTitle.text = lostItem.title
        holder.cardCatContent.text = lostItem.itemType
        holder.cardDateContent.text = lostItem.getDate
        holder.cardLocContent.text = lostItem.location
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(position)
        }
    }

    override fun getItemCount() = dataSet.size
}
