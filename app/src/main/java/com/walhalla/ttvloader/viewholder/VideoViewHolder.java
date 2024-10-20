package com.walhalla.ttvloader.viewholder;

import android.graphics.Color;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

import com.walhalla.ttvloader.ui.gallery.VideoStorageAdapter;
import com.walhalla.ttvloader.databinding.VideoItemBinding;
import com.walhalla.ttvloader.models.LocalVideo;

public class VideoViewHolder extends RecyclerView.ViewHolder {

    public final VideoItemBinding binding;

    private VideoStorageAdapter adapter;

    public VideoViewHolder(VideoItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }


    public void bind(final LocalVideo localVideo, final int id, VideoStorageAdapter videoStorageAdapter) {
        this.adapter = videoStorageAdapter;
        if (localVideo.duration != -1) {
            binding.tvDuration.setText(adapter.secToTime(localVideo.duration));
        }
        //textView.setText(value + "");
        if (adapter.selectedItems.contains(id)) {
            binding.chkVideoSelected.setVisibility(View.VISIBLE);
            binding.vCheckBackColor.setVisibility(View.VISIBLE);
            binding.frameLayout.setBackgroundColor(Color.LTGRAY);
        } else {
            binding.frameLayout.setBackgroundColor(Color.WHITE);
            binding.chkVideoSelected.setVisibility(View.GONE);
            binding.vCheckBackColor.setVisibility(View.GONE);
        }
    }
}
