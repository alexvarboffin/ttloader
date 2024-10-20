package com.walhalla.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.walhalla.ttvloader.R;
import com.walhalla.ttvloader.ui.MItem;

import java.util.List;
import java.util.Map;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private final float aFloat;
    private final Context context;
    private final List<MItem> listGroup;
    private final Map<String, List<MItem>> listItem;

    public ExpandableListAdapter(Context context, List<MItem> listGroup, Map<String, List<MItem>> listItem) {
        this.context = context;
        this.listGroup = listGroup;
        this.listItem = listItem;
        this.aFloat = context.getResources().getDisplayMetrics().density;
    }

    @Override
    public int getGroupCount() {
        return listGroup.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        MItem obj = listGroup.get(groupPosition);
        List<MItem> items = listItem.get(obj.name);
        return (items == null) ? 0 : items.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        MItem obj = listGroup.get(groupPosition);
        return obj;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        MItem obj = listGroup.get(groupPosition);
        MItem mm = listItem.get(obj.name).get(childPosition);
        return mm;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        MItem item = (MItem) getGroup(groupPosition);
        String groupName = item.name;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //convertView = inflater.inflate(android.R.layout.simple_expandable_list_item_1, null);
            convertView = inflater.inflate(R.layout.select_dialog_item, null);
        }

        TextView text1 = convertView.findViewById(R.id.text1);
        text1.setText(groupName);
        text1.setCompoundDrawablesRelativeWithIntrinsicBounds(item.drawable, null, null, null);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        MItem item = (MItem) getChild(groupPosition, childPosition);
        String[] childName = item.name.split("@@");
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.simple_expandable_list_item_2, null);
        }

        TextView textView1 = convertView.findViewById(R.id.packageName);
        TextView textView2 = convertView.findViewById(R.id.componentName);
        //ImageView imageView = convertView.findViewById(R.id.icon);
        //imageView.setImageDrawable(item.icon);
        textView1.setText(childName[0]);
        textView2.setText(childName[1]);
        //textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_android, 0, 0, 0);
//        textView1.setCompoundDrawablesWithIntrinsicBounds(item.drawable, null, null, null);
//
//        //Add margin between image and text (support various screen densities)
//        int dp10 = (int) (10 * aFloat + 0.5f);
//        textView1.setCompoundDrawablePadding(dp10);

        ImageView icon = convertView.findViewById(R.id.icon);
        icon.setImageDrawable(item.drawable);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}