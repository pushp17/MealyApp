package com.eat_healthy.tiffin.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.eat_healthy.tiffin.databinding.AdapterInfoBinding

class InfoViewHolder (val binding: AdapterInfoBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: String, position: Int){
        binding.tvInfo.text=position.toString().plus(". ").plus(item)
    }
}