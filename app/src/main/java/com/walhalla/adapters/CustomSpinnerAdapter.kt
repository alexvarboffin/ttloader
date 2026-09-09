package com.walhalla.adapters

import android.content.Context
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.walhalla.ttvloader.R

class CustomSpinnerAdapter : ArrayAdapter<String> {
    private lateinit var context: Context
    private var resource: Int = 0
    private lateinit var data: MutableList<String>
    constructor(context: Context, resource: Int, data: MutableList<String>) : super(context, resource, data.toMutableList()) {
        this.resource = resource
        this.context = context
        this.data = data
    }
    override fun getItem(position: Int): String {
        return data.`get`(position)
    }
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        // TODO Auto-generated method stub
        return getCustomView(position, convertView, parent)
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // TODO Auto-generated method stub
        return getCustomView(position, convertView, parent)
    }
    private fun getCustomView(position: Int, convertView: View?, parent: ViewGroup): View {
        var inflater: LayoutInflater = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
        //LayoutInflater inflater = (LayoutInflater) context.getLayoutInflater();
        var row: View = inflater.inflate(resource, parent, false)
        var label: TextView = row.findViewById(android.R.id.text1)
        try {
            label.setText(data.`get`(position))
        } catch (e: Exception) {
            e.printStackTrace()
            System.out.println(data.toString())
        }
        var icon: ImageView = row.findViewById(R.id.icon1)
        if (position == 0) {
        } else {
        }
        //icon.setImageResource(android.R.drawable.ic_btn_speak_now);
        //return getNothingSelectedView(parent);
        //icon.setImageResource(R.drawable.ic_sms);
        return row
    }
    fun swapData(newFolderNames: MutableList<String>) {
        this.data.clear()
        // очищаем текущий список
        this.data.addAll(newFolderNames)
        // добавляем новые данные
        notifyDataSetChanged()
    }
    // уведомляем адаптер об изменениях
    class ViewHolder {
        @JvmField var name: TextView? = null
        @JvmField var flag: ImageView? = null
    }
}
