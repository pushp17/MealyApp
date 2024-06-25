package com.eat_healthy.tiffin.viewHolder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eat_healthy.tiffin.databinding.HomePageViewpagerType1Binding
import com.eat_healthy.tiffin.models.HomeHighLightedItems

class HomePageViewpagerHolderType1 (val binding: HomePageViewpagerType1Binding, val context: Context) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(position: Int, item: HomeHighLightedItems) {
        Glide.with(context).load(item.itemImage).into(binding.ivItem)
    }
}
