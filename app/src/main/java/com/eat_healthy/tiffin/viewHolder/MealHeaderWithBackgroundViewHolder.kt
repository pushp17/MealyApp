package com.eat_healthy.tiffin.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.eat_healthy.tiffin.databinding.AdapterMealHeaderWithBackgroundBinding
import com.eat_healthy.tiffin.models.Header

class MealHeaderWithBackgroundViewHolder (val binding: AdapterMealHeaderWithBackgroundBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Header){
        binding.tvHeader.text=item.itemHeader
    }
}