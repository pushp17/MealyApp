package com.eat_healthy.tiffin.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.eat_healthy.tiffin.databinding.AdapterSelectMealHeaderBinding
import com.eat_healthy.tiffin.models.Header
import com.eat_healthy.tiffin.models.MealCategoryHeader

class MealCategoryHeaderViewHolder (val binding: AdapterSelectMealHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(position:Int,item: MealCategoryHeader){
        binding.position=position
        binding.item=item
        binding.tvMealCategoryHeader.text=item.itemHeader

    }
}