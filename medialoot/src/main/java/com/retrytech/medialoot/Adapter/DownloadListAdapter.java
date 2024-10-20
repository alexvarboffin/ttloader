package com.retrytech.medialoot.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.retrytech.medialoot.databinding.DownloadListItemBinding;
import com.retrytech.medialoot.model.DownloadFile;
import com.retrytech.medialoot.R;


import java.util.ArrayList;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

public class DownloadListAdapter extends RecyclerView.Adapter<DownloadListAdapter.DownloadListViewHolder> {
    private ArrayList<DownloadFile> downloadFiles;
    private Context context;

    public DownloadListAdapter(ArrayList<DownloadFile> downloadFiles) {

        this.downloadFiles = downloadFiles;
    }

    @NonNull
    @Override
    public DownloadListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        DownloadListItemBinding binding = DownloadListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new DownloadListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadListViewHolder holder, int position) {
        DownloadFile downloadFile = downloadFiles.get(position);
        holder.binding.tvTitle.setText(downloadFile.getFilename());
        holder.binding.total.setText(downloadFile.getTotalSize());
        holder.binding.tvprogress.setText(String.valueOf(downloadFile.getProgress()));
        holder.binding.progressBar.setProgress(downloadFile.getProgress());
        if (downloadFile.getProgress() == 100) {
            holder.binding.image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play_circle_filled_black_24dp));
        } else {
            holder.binding.image.setImageDrawable(context.getResources().getDrawable(R.drawable.video));
        }
        holder.binding.progressLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "clickable", Toast.LENGTH_SHORT).show();

               /* try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(downloadFile.getPath()));
                    intent.setDataAndType(Uri.parse(downloadFile.getPath()), "video/mp4");
                    context.startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return downloadFiles.size();
    }

    class DownloadListViewHolder extends RecyclerView.ViewHolder {
        private DownloadListItemBinding binding;

        DownloadListViewHolder(@NonNull DownloadListItemBinding itemBinding) {
            super(itemBinding.getRoot());
            binding = itemBinding;
        }
    }
}
