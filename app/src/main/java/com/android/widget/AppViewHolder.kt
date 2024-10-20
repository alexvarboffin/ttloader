package com.android.widget

import androidx.recyclerview.widget.RecyclerView
import com.walhalla.adapters.AppModel
import com.walhalla.ttvloader.databinding.ItemAppBinding

class AppViewHolder(@JvmField val binding: ItemAppBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: AppModel) {
        binding.icon.setImageResource(item.icon)
        binding.name.setText(item.appName)
        binding.packageName.text = item.appPackageName
        binding.icon.setBackgroundResource(item.background)
    }
}
