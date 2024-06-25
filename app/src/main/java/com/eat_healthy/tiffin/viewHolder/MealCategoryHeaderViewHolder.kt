package com.eat_healthy.tiffin.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.eat_healthy.tiffin.databinding.MediumSizeHeaderBinding
import com.eat_healthy.tiffin.models.Header

class MealCategoryHeaderViewHolder (val binding: MediumSizeHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(position:Int,item: Header){
        binding.tvMediumSizeHeader.text=item.itemHeader
    }
}