package com.walhalla.ttvloader.activity.mime;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.walhalla.ttvloader.R;

import java.util.List;

public class MimeTabAdapter extends RecyclerView.Adapter<MimeTabAdapter.MimeViewHolder> {

    private final Context mContext;

    public MimeTabAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public MimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view, parent, false);
        return new MimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MimeViewHolder holder, int position) {
        String mimeType = MimeTabData.MIME_TYPES.get(position);
        holder.bindRecyclerView(mimeType);
    }

    @Override
    public int getItemCount() {
        return MimeTabData.MIME_TYPES.size();
    }

    static class MimeViewHolder extends RecyclerView.ViewHolder {

        private final RecyclerView mRecyclerView;
        private final ActivityListAdapter mAdapter;

        public MimeViewHolder(@NonNull View itemView) {
            super(itemView);
            mRecyclerView = itemView.findViewById(R.id.recycler_view);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            mAdapter = new ActivityListAdapter();
            mRecyclerView.setAdapter(mAdapter);
        }

        public void bindRecyclerView(String mimeType) {
            List<ResolveInfo> activityList = VideoMimeTypeHandler.getVideoHandlers(itemView.getContext(), mimeType);
            mAdapter.setActivityList(activityList);
        }
    }
}
