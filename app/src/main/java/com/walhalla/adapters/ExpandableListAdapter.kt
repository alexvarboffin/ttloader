package com.walhalla.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import com.walhalla.ttvloader.R
import com.walhalla.ttvloader.ui.MItem

class ExpandableListAdapter : BaseExpandableListAdapter {
    private var aFloat: Float = 0f
    private lateinit var context: Context
    private lateinit var listGroup: MutableList<MItem>
    private lateinit var listItem: Map<String, MutableList<MItem>>
    constructor(context: Context, listGroup: MutableList<MItem>, listItem: Map<String, MutableList<MItem>>) : super() {
        this.context = context
        this.listGroup = listGroup
        this.listItem = listItem
        this.aFloat = context.getResources().getDisplayMetrics().density
    }
    override fun getGroupCount(): Int {
        return listGroup.size
    }
    override fun getChildrenCount(groupPosition: Int): Int {
        var obj: MItem = listGroup.`get`(groupPosition)
        var items: MutableList<MItem>? = listItem.get(obj.name)
        return if (items == null) 0 else items.size
    }
    override fun getGroup(groupPosition: Int): Any {
        var obj: MItem = listGroup.`get`(groupPosition)
        return obj
    }
    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        var obj: MItem = listGroup.`get`(groupPosition)
        var mm: MItem = listItem.get(obj.name)!![childPosition]
        return mm
    }
    override fun getGroupId(groupPosition: Int): Long {
        return (groupPosition).toLong()
    }
    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return (childPosition).toLong()
    }
    override fun hasStableIds(): Boolean {
        return false
    }
    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertViewIn: View?, parent: ViewGroup): View {
        var convertView = convertViewIn
        var item: MItem = (getGroup(groupPosition) as MItem)
        var groupName: String = item.name
        if (convertView == null) {
            var inflater: LayoutInflater = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            //convertView = inflater.inflate(android.R.layout.simple_expandable_list_item_1, null);
            convertView = inflater.inflate(R.layout.select_dialog_item, null)
        }
        var text1: TextView = convertView.findViewById(R.id.text1)
        text1.setText(groupName)
        text1.setCompoundDrawablesRelativeWithIntrinsicBounds(item.drawable, null, null, null)
        return convertView
    }
    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertViewIn: View?, parent: ViewGroup): View {
        var convertView = convertViewIn
        var item: MItem = (getChild(groupPosition, childPosition) as MItem)
        var childName: Array<String> = item.name.split("").toTypedArray()
        if (convertView == null) {
            var inflater: LayoutInflater = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            convertView = inflater.inflate(R.layout.simple_expandable_list_item_2, null)
        }
        var textView1: TextView = convertView.findViewById(R.id.packageName)
        var textView2: TextView = convertView.findViewById(R.id.componentName)
        //ImageView imageView = convertView.findViewById(R.id.icon);
        //imageView.setImageDrawable(item.icon);
        textView1.setText(childName[0])
        textView2.setText(childName[1])
        //textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_android, 0, 0, 0);
        //        textView1.setCompoundDrawablesWithIntrinsicBounds(item.drawable, null, null, null);
        //
        //        //Add margin between image and text (support various screen densities)
        //        int dp10 = (int) (10 * aFloat + 0.5f);
        //        textView1.setCompoundDrawablePadding(dp10);
        var icon: ImageView = convertView.findViewById(R.id.icon)
        icon.setImageDrawable(item.drawable)
        return convertView
    }
    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}
