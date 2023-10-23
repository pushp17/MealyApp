package com.eat_healthy.tiffin.viewHolder

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eat_healthy.tiffin.databinding.AdapterSelectMealBinding
import com.eat_healthy.tiffin.models.MainMeal

class SelectMainMealViewHolder (val binding: AdapterSelectMealBinding, val context: Context) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(position:Int,item: MainMeal){
        binding.tvItemName.text=item.itemName
        if(!item.info.isNullOrEmpty()){
            binding.tcShortDesc.visibility=View.VISIBLE
            binding.tcShortDesc.text=item.info
        }else {
            binding.tcShortDesc.visibility=View.GONE
        }
        binding.position=position
        binding.item=item
        binding.itemCheckbox.isChecked=item.selected
        Glide.with(context).load(item.itemImage).into(binding.ivItem)
    }
}