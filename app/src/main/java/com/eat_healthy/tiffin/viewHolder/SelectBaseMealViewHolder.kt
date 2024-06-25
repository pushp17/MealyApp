package com.eat_healthy.tiffin.viewHolder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.eat_healthy.tiffin.databinding.AdapterSelectBaseBinding
import com.eat_healthy.tiffin.models.Meal

class SelectBaseMealViewHolder (val binding: AdapterSelectBaseBinding, val context: Context) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(position:Int,item: Meal){
        binding.tvItemName.text=item.name
        binding.position=position
        binding.item=item
        binding.itemRadiobtn.isChecked = item.selected
    }
}