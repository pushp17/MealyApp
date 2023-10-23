package com.eat_healthy.tiffin.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.eat_healthy.tiffin.databinding.ItemHeaderBinding
import com.eat_healthy.tiffin.models.Header

class ItemHeaderViewHolder(val itemHeaderBinding: ItemHeaderBinding) : RecyclerView.ViewHolder(itemHeaderBinding.root) {
    fun bind(item:Header){
        itemHeaderBinding.tvHeader.text=item.itemHeader
    }
}