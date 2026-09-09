package com.walhalla.ttvloader.viewholder

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.walhalla.ttvloader.ui.gallery.VideoStorageAdapter
import com.walhalla.ttvloader.databinding.VideoItemBinding
import com.walhalla.ttvloader.models.LocalVideo

class VideoViewHolder : RecyclerView.ViewHolder {
    lateinit var binding: VideoItemBinding
    private var adapter: VideoStorageAdapter? = null
    constructor(binding: VideoItemBinding) : super(binding!!.getRoot()) {
        this.binding = binding
    }
    fun bind(localVideo: LocalVideo, id: Int, videoStorageAdapter: VideoStorageAdapter) {
        this.adapter = videoStorageAdapter
        if (localVideo.duration != 1) {
            binding!!.tvDuration.setText(adapter!!.secToTime(localVideo.duration))
        }
        //textView.setText(value + "");
        if (adapter!!.selectedItems.contains(id)) {
            binding!!.chkVideoSelected.setVisibility(View.VISIBLE)
            binding!!.vCheckBackColor.setVisibility(View.VISIBLE)
            binding!!.frameLayout.setBackgroundColor(Color.LTGRAY)
        } else {
            binding!!.frameLayout.setBackgroundColor(Color.WHITE)
            binding!!.chkVideoSelected.setVisibility(View.GONE)
            binding!!.vCheckBackColor.setVisibility(View.GONE)
        }
    }
}
