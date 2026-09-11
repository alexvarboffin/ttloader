package com.walhalla.ttvloader.viewholder

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.walhalla.ttvloader.databinding.VideoItemBinding
import com.walhalla.ttvloader.models.LocalVideo
import com.walhalla.ttvloader.ui.gallery.VideoStorageAdapter

class VideoViewHolder(@JvmField val binding: VideoItemBinding) : RecyclerView.ViewHolder(
    binding.getRoot()
) {
    private var adapter: VideoStorageAdapter? = null


    fun bind(localVideo: LocalVideo, id: Int, videoStorageAdapter: VideoStorageAdapter) {
        this.adapter = videoStorageAdapter
        if (localVideo.duration != -1) {
            binding.tvDuration.text = adapter!!.secToTime(localVideo.duration)
        }
        //textView.setText(value + "");
        if (adapter!!.selectedItems.contains(id)) {
            binding.chkVideoSelected.visibility = View.VISIBLE
            binding.vCheckBackColor.setVisibility(View.VISIBLE)
            binding.frameLayout.setBackgroundColor(Color.LTGRAY)
        } else {
            binding.frameLayout.setBackgroundColor(Color.WHITE)
            binding.chkVideoSelected.setVisibility(View.GONE)
            binding.vCheckBackColor.setVisibility(View.GONE)
        }
    }
}
