package com.eat_healthy.tiffin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eat_healthy.tiffin.databinding.AdapterDeliveryBinding
import com.eat_healthy.tiffin.models.DeliveryDetails
import com.eat_healthy.tiffin.viewHolder.DeliveryViewHolder
import javax.inject.Inject

class DeliveryAdapter @Inject constructor(): BaseRecyclerviewAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = AdapterDeliveryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return DeliveryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val deliveryDetails = mutableItemList[position] as DeliveryDetails
        (holder as DeliveryViewHolder).bind(position, deliveryDetails)
    }
}
