package com.eat_healthy.tiffin.viewHolder

import android.content.Context
import android.content.res.ColorStateList
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eat_healthy.tiffin.R
import com.eat_healthy.tiffin.databinding.AdapterMyFavouriteMealBinding
import com.eat_healthy.tiffin.models.Sabji

class MyFavouriteMealViewHolder (val binding: AdapterMyFavouriteMealBinding, val context: Context) : RecyclerView.ViewHolder(binding.root) {
    fun bind(position:Int,item: Sabji){
        binding.item=item
        binding.position=position
        binding.tvItemName.text=item.itemName
       // itemBinding.ivItem.strokeColor= R.color.active_green
        if(item.selected==true){
            binding.ivItem.strokeColor= ColorStateList.valueOf(context.getColor(R.color.colorPrimary))
        }else{
            binding.ivItem.strokeColor= ColorStateList.valueOf(context.getColor(R.color.transparent))
        }
        Glide.with(context).load(item.itemImage).into(binding.ivItem)
    }
}