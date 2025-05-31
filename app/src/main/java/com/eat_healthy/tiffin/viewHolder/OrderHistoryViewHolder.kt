package com.eat_healthy.tiffin.viewHolder

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.eat_healthy.tiffin.databinding.AdapterOrderHistoryBinding
import com.eat_healthy.tiffin.models.SingleMealUserOrderHistory
import com.eat_healthy.tiffin.utils.AppUtils.rsAppendedValue

class OrderHistoryViewHolder (val binding: AdapterOrderHistoryBinding, val context: Context) : RecyclerView.ViewHolder(binding.root) {
    fun bind(position: Int, item: SingleMealUserOrderHistory) {
        binding.item = item
        binding.position = position
        binding.orderDetail = "orderDetail"
        binding.deliverySlot = "deliverySlot"
        binding.orderNo.text = item.orderId
        binding.orderDate.text = item.dateAndTime
        if (item.orderTime != null) {
            binding.tvOrderTime.text = item.orderTime
            binding.llTime.visibility = View.VISIBLE
        } else {
            binding.llTime.visibility = View.GONE
        }
        if (item.estimatedDeliveryTime != null) {
            binding.estimatedDeliveryTime.text = item.estimatedDeliveryTime
            binding.llEstimatedDeliveryTime.visibility = View.VISIBLE
        } else {
            binding.llEstimatedDeliveryTime.visibility = View.GONE
        }
        if (item.paymentSuccessDone == true) {
            binding.tvAmountPaid.text = rsAppendedValue(item.grandTotalPrice)
        } else {
            binding.tvAmountPaid.text = rsAppendedValue(item.grandTotalPrice.plus("( COD )"))
        }
    }
}
