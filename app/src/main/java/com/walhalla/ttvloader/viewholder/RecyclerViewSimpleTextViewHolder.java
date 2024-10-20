package com.walhalla.ttvloader.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewSimpleTextViewHolder extends RecyclerView.ViewHolder {
    private final TextView text1;

    public RecyclerViewSimpleTextViewHolder(View itemView) {
        super(itemView);
        text1 = itemView.findViewById(android.R.id.text1);
    }

    public void bind(String s) {
        text1.setText(s);
    }
}