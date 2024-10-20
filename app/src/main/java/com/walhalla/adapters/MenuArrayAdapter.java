//package com.walhalla.adapters;
//
//import android.content.Context;
//import android.graphics.drawable.Drawable;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.core.content.ContextCompat;
//
//import com.walhalla.ttvloader.ui.MItem;
//
//import java.util.List;
//
//public class MenuArrayAdapter extends ArrayAdapter<MItem> {
//
//    private final float var0;
//
//    public MenuArrayAdapter(Context context, List<MItem> menuItems) {
//        super(context, android.R.layout.select_dialog_item, android.R.id.text1, menuItems);
//        var0 = context.getResources().getDisplayMetrics().density;
//    }
//
//    @NonNull
//    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
//        //Use super class to create the View
//        View v = super.getView(position, convertView, parent);
//        TextView name = v.findViewById(android.R.id.text1);
//        MItem item = getItem(position);
//
//        if (item != null) {
//            name.setText(item.name);
//            //Drawable drawable = ContextCompat.getDrawable(convertView.getContext(), item.drawable);
//            name.setCompoundDrawablesWithIntrinsicBounds(item.drawable, 0, 0, 0);
//        }
//
//        //Add margin between image and text (support various screen densities)
//        int dp10 = (int) (10 * var0 + 0.5f);
//        name.setCompoundDrawablePadding(dp10);
//
//        return v;
//    }
//}