package com.eat_healthy.tiffin.viewHolder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eat_healthy.tiffin.databinding.AdapterSelectSpecialMealBinding
import com.eat_healthy.tiffin.models.SpecialMeal

class SelectSpecialMealViewHolder (
    val binding: AdapterSelectSpecialMealBinding,
    val context: Context
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(position:Int,item: SpecialMeal) {
        binding.tvItemName.text = item.itemName
        binding.position=position
        binding.item=item
        Glide.with(context).load(item.itemImage).into(binding.ivItem)
    }
}