package com.eat_healthy.tiffin.viewHolder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.eat_healthy.tiffin.databinding.AdapterSelectExtrasRadioLayoutBinding
import com.eat_healthy.tiffin.models.ExtrasV2
import com.eat_healthy.tiffin.utils.AppUtils

class SelectExtrasRadioViewHolder (val binding: AdapterSelectExtrasRadioLayoutBinding, val context: Context) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(position:Int,item: ExtrasV2){
        binding.tvItemName.text=item.name
        binding.position=position
        binding.item=item
        if(item.price?.toInt() != null &&  item.price.toInt() > 0){
            binding.price.text = AppUtils.rsAppendedValue(item.price)
        }
        binding.itemRadiobtn.isChecked = item.selected
    }
}