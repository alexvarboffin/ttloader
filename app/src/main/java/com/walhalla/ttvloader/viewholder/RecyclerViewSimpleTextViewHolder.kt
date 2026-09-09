package com.walhalla.ttvloader.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewSimpleTextViewHolder : RecyclerView.ViewHolder {
    private lateinit var text1: TextView
    constructor(itemView: View) : super(itemView) {
        text1 = itemView.findViewById(android.R.id.text1)
    }
    fun bind(s: String) {
        text1.setText(s)
    }
}
