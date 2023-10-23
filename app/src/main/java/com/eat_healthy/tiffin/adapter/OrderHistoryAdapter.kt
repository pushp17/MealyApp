package com.eat_healthy.tiffin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eat_healthy.tiffin.databinding.AdapterOrderHistoryBinding
import com.eat_healthy.tiffin.models.SingleMealUserOrderHistory
import com.eat_healthy.tiffin.viewHolder.OrderHistoryViewHolder
import javax.inject.Inject

class OrderHistoryAdapter @Inject constructor() : BaseRecyclerviewAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return OrderHistoryViewHolder(
            AdapterOrderHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), parent.context
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as OrderHistoryViewHolder).bind(
            position,
            mutableItemList.get(position) as SingleMealUserOrderHistory
        )
    }
}
