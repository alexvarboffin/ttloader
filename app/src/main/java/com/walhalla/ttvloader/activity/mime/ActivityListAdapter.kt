package com.walhalla.ttvloader.activity.mime

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.walhalla.ttvloader.R
import java.util.ArrayList
import java.util.List

class ActivityListAdapter : RecyclerView.Adapter<ActivityListAdapter.ActivityViewHolder>() {
    private var mActivityList: MutableList<ResolveInfo> = ArrayList()
    fun setActivityList(activityList: MutableList<ResolveInfo>) {
        mActivityList = activityList
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        var view: View = LayoutInflater.from(parent.getContext()!!).inflate(R.layout.item_activity_list, parent, false)
        return ActivityViewHolder(view)
    }
    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        var resolveInfo: ResolveInfo = mActivityList.`get`(position)
        holder.bind(resolveInfo)
    }
    override fun getItemCount(): Int {
        return mActivityList.size
    }
    class ActivityViewHolder : RecyclerView.ViewHolder {
        private lateinit var mActivityName: TextView
        constructor(itemView: View) : super(itemView) {
            mActivityName = itemView.findViewById(R.id.activity_name)
        }
        fun bind(resolveInfo: ResolveInfo) {
            val activityName: String = resolveInfo.activityInfo.name
            mActivityName.setText(activityName)
            itemView.setOnClickListener(object : View.OnClickListener {
                            override fun onClick(view: View) {
                                launchActivity(view.getContext()!!, resolveInfo)
                            }
                        })
        }
        private fun launchActivity(context: Context, resolveInfo: ResolveInfo) {
            var launchIntent: Intent = Intent(Intent.ACTION_MAIN)
            launchIntent.setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name)
            context.startActivity(launchIntent)
        }
    }
}
