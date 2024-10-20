package com.walhalla.ttvloader.activity.mime;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.walhalla.ttvloader.R;

import java.util.ArrayList;
import java.util.List;

public class ActivityListAdapter extends RecyclerView.Adapter<ActivityListAdapter.ActivityViewHolder> {

    private List<ResolveInfo> mActivityList = new ArrayList<>();

    public void setActivityList(List<ResolveInfo> activityList) {
        mActivityList = activityList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity_list, parent, false);
        return new ActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        ResolveInfo resolveInfo = mActivityList.get(position);
        holder.bind(resolveInfo);
    }

    @Override
    public int getItemCount() {
        return mActivityList.size();
    }

    static class ActivityViewHolder extends RecyclerView.ViewHolder {

        private final TextView mActivityName;

        public ActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            mActivityName = itemView.findViewById(R.id.activity_name);
        }

        public void bind(final ResolveInfo resolveInfo) {
            final String activityName = resolveInfo.activityInfo.name;
            mActivityName.setText(activityName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    launchActivity(view.getContext(), resolveInfo);
                }
            });
        }

        private void launchActivity(@NonNull Context context, ResolveInfo resolveInfo) {
            Intent launchIntent = new Intent(Intent.ACTION_MAIN);
            launchIntent.setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
            context.startActivity(launchIntent);
        }
    }
}
