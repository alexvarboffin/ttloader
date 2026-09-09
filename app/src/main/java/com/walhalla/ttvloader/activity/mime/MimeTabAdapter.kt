package com.walhalla.ttvloader.activity.mime

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.content.pm.ResolveInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import com.walhalla.ttvloader.R
import java.util.List

class MimeTabAdapter : RecyclerView.Adapter<MimeTabAdapter.MimeViewHolder> {
    private lateinit var mContext: Context
    constructor(context: Context) {
        mContext = context
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MimeViewHolder {
        var view: View = LayoutInflater.from(parent.getContext()!!).inflate(R.layout.item_recycler_view, parent, false)
        return MimeViewHolder(view)
    }
    override fun onBindViewHolder(holder: MimeViewHolder, position: Int) {
        var mimeType: String = MimeTabData.MIME_TYPES.`get`(position)
        holder.bindRecyclerView(mimeType)
    }
    override fun getItemCount(): Int {
        return MimeTabData.MIME_TYPES.size
    }
    class MimeViewHolder : RecyclerView.ViewHolder {
        private lateinit var mRecyclerView: RecyclerView
        private lateinit var mAdapter: ActivityListAdapter
        constructor(itemView: View) : super(itemView) {
            mRecyclerView = itemView.findViewById(R.id.recycler_view)
            mRecyclerView.setLayoutManager(LinearLayoutManager(itemView.getContext()!!))
            mAdapter = ActivityListAdapter()
            mRecyclerView.setAdapter(mAdapter)
        }
        fun bindRecyclerView(mimeType: String) {
            var activityList: MutableList<ResolveInfo> = VideoMimeTypeHandler.getVideoHandlers(itemView.getContext()!!, mimeType)
            mAdapter.setActivityList(activityList)
        }
    }
}
