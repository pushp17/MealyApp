package com.eat_healthy.tiffin.viewHolder

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eat_healthy.tiffin.databinding.AdapterSelectSabjiBinding
import com.eat_healthy.tiffin.models.Sabji

class SelectSabjiViewHolder (val binding: AdapterSelectSabjiBinding, val context: Context) : RecyclerView.ViewHolder(binding.root) {
    fun bind(position:Int,item: Sabji){
        binding.tvItemName.text=item.itemName
        if(!item.info.isNullOrEmpty()){
            binding.tcShortDesc.visibility= View.VISIBLE
            binding.tcShortDesc.text=item.info
        }else {
            binding.tcShortDesc.visibility= View.GONE
        }
        binding.position=position
        binding.item=item
        Glide.with(context).load(item.itemImage).into(binding.ivItem)
        binding.itemCheckbox.isChecked = item.selected
    }
}