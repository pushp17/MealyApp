package com.eat_healthy.tiffin.viewHolder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.eat_healthy.tiffin.databinding.RefereeHistoryAdapterBinding
import com.eat_healthy.tiffin.models.RefereeOrdersDetails
import com.eat_healthy.tiffin.utils.AppUtils

class RefereViewHolder(val binding: RefereeHistoryAdapterBinding, val context: Context) : RecyclerView.ViewHolder(binding.root) {
    fun bind(position: Int, item: RefereeOrdersDetails) {
        binding.tvDate.text = item.dateAndTime
        binding.tvUserName.text = item.refereeUserName
        binding.tvReferalAmountPerOrder.text = AppUtils.rsAppendedValue(item.refralAmountInThisOrder?.toString())
    }
}
