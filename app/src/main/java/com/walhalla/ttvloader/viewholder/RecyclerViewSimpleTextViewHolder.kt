package com.walhalla.ttvloader.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewSimpleTextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val text1: TextView = itemView.findViewById(android.R.id.text1)

    fun bind(s: String?) {
        text1.text = s
    }
}