package com.eat_healthy.tiffin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eat_healthy.tiffin.databinding.DeliverySlotAdapterItemBinding
import com.eat_healthy.tiffin.models.Header

class DeliverySlotAdapter : BaseRecyclerviewAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return DeliverySlotViewHolder(
            DeliverySlotAdapterItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(viewholder: RecyclerView.ViewHolder, position: Int) {
        (viewholder as DeliverySlotViewHolder).bind(mutableItemList.get(position) as Header)
    }

    class DeliverySlotViewHolder(val binding: DeliverySlotAdapterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Header) {
            binding.tvDeliverySlot.text = item.itemHeader

        }
    }
}