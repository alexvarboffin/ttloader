package com.walhalla.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.ImageView;

import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.walhalla.ttvloader.R;

import java.util.List;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final int resource;
    private final List<String> data;

    public CustomSpinnerAdapter(@NonNull Context context, @LayoutRes int resource,
                                List<String> data) {
        super(context, resource, data);
        this.resource = resource;
        this.context = context;
        this.data = data;
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return data.get(position);
    }


    @Override
    public View getDropDownView(int position, View convertView,
                                @NonNull ViewGroup parent) {
        // TODO Auto-generated method stub
        return getCustomView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // TODO Auto-generated method stub
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //LayoutInflater inflater = (LayoutInflater) context.getLayoutInflater();
        View row = inflater.inflate(resource, parent, false);

        TextView label = row.findViewById(android.R.id.text1);
        try{
            label.setText(data.get(position));
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(data.toString());
        }

        ImageView icon = row.findViewById(R.id.icon1);

        if (position == 0) {
            //icon.setImageResource(android.R.drawable.ic_btn_speak_now);
            //return getNothingSelectedView(parent);
        } else {
            //icon.setImageResource(R.drawable.ic_sms);
        }

        return row;
    }

    public void swapData(List<String> newFolderNames) {
        this.data.clear();  // очищаем текущий список
        this.data.addAll(newFolderNames);  // добавляем новые данные
        notifyDataSetChanged();  // уведомляем адаптер об изменениях
    }

    public static class ViewHolder {
        public TextView name;
        public ImageView flag;
    }
}

