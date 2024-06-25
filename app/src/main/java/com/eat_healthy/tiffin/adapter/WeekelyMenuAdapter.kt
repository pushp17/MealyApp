package com.eat_healthy.tiffin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eat_healthy.tiffin.databinding.WeekelyMenuAdapterItemBinding
import com.eat_healthy.tiffin.models.WeekelyMenu
import javax.inject.Inject

class WeekelyMenuAdapter @Inject constructor(): BaseRecyclerviewAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return WeekelyMenuViewHolder(
            WeekelyMenuAdapterItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            ),
            parent.context
        )
    }

    override fun onBindViewHolder(viewholder: RecyclerView.ViewHolder, position: Int) {
        (viewholder as WeekelyMenuViewHolder).bind(position,mutableItemList.get(position) as WeekelyMenu)
        viewholder.binding.itemClickListener=itemClickListener
    }

    class WeekelyMenuViewHolder(val binding: WeekelyMenuAdapterItemBinding, val context: Context):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int,item: WeekelyMenu) {
            binding.itemName.text = item.item
            binding.desc.text = item.desc
            Glide.with(context).load(item.image).into(binding.itemImage)
            binding.item = item
            binding.position = position
            if(item.day?.contains("lunch") == true){
                binding.tvNotifyLunch.visibility = View.VISIBLE
            }else {
                binding.tvNotifyLunch.visibility = View.GONE
            }
            if(item.day?.contains("dinner") == true){
                binding.tvNotifyDinner.visibility = View.VISIBLE
            }else {
                binding.tvNotifyDinner.visibility = View.GONE
            }
            binding.notifyLunch = "notifyLunch"
            binding.notifyDinner = "notifyDinner"
        }
    }
}