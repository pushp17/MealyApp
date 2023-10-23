package com.eat_healthy.tiffin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eat_healthy.tiffin.databinding.AdapterCartItemBinding
import com.eat_healthy.tiffin.models.ItemsInCart
import com.eat_healthy.tiffin.viewHolder.CartItemViewHolder
import javax.inject.Inject

class CartItemListAdapter @Inject constructor() : BaseRecyclerviewAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CartItemViewHolder(
            AdapterCartItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            ),
            parent.context
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CartItemViewHolder).bind(position, mutableItemList.get(position) as ItemsInCart)
        holder.binding.itemClickListener = itemClickListener
    }
}
