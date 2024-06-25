package com.eat_healthy.tiffin.viewHolder

import android.content.Context
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eat_healthy.tiffin.databinding.HomePageViewpagerType2Binding
import com.eat_healthy.tiffin.models.HomeHighLightedItems
import com.eat_healthy.tiffin.utils.AppUtils

class HomePageViewpagerHolderType2 (val binding: HomePageViewpagerType2Binding, val context: Context) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(position: Int, item: HomeHighLightedItems) {
        Glide.with(context).load(item.itemImage).into(binding.ivItem)
        binding.tvItemName.text=item.itemName
    }
}
