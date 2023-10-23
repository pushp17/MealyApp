package com.eat_healthy.tiffin.viewHolder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.eat_healthy.tiffin.databinding.AdapterCartItemBinding
import com.eat_healthy.tiffin.models.ItemsInCart
import com.eat_healthy.tiffin.utils.AppUtils

class CartItemViewHolder(val binding: AdapterCartItemBinding, val context: Context) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(position: Int, item: ItemsInCart) {
        binding.item = item
        binding.position = position
        binding.tvItemName.text = (position + 1).toString().plus(" . ").plus(item.yourMeal)
        binding.tvValue2.text = AppUtils.rsAppendedValue(item.price)
    }
}
