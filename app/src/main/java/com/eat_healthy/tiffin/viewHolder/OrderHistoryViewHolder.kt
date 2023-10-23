package com.eat_healthy.tiffin.viewHolder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.eat_healthy.tiffin.databinding.AdapterOrderHistoryBinding
import com.eat_healthy.tiffin.models.SingleMealUserOrderHistory
import com.eat_healthy.tiffin.utils.AppUtils.rsAppendedValue

class OrderHistoryViewHolder (val binding: AdapterOrderHistoryBinding, val context: Context) : RecyclerView.ViewHolder(binding.root) {
    fun bind(position: Int, item: SingleMealUserOrderHistory) {
        binding.orderNo.text = item.orderId
        binding.orderDate.text = item.dateAndTime
        binding.tvAmountPaid.text = rsAppendedValue(item.grandTotalPrice)
    }
}
