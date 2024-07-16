package com.eat_healthy.tiffin.viewHolder

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.eat_healthy.tiffin.databinding.AdapterDeliveryBinding
import com.eat_healthy.tiffin.models.DeliveryDetails
import com.eat_healthy.tiffin.models.UserAddress
import com.eat_healthy.tiffin.utils.AppUtils.rsAppendedValueWithRs

class DeliveryViewHolder(val binding: AdapterDeliveryBinding) : RecyclerView.ViewHolder(
    binding.root
) {

    fun bind(position: Int, deliveryDetails: DeliveryDetails?) {
        when (position) {
            1 -> {
                binding.strip.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#c4f9e4"))
                binding.strip.setTextColor(ColorStateList.valueOf(Color.parseColor("#2b986f")))
            }

            2 -> {
                binding.strip.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#ffeaca"))
                binding.strip.setTextColor(ColorStateList.valueOf(Color.parseColor("#da9239")))
            }

            else -> {}
        }
        val singleMealUserOrderDetail = deliveryDetails?.singleMealUserOrderDetail
        val mUserAddress = deliveryDetails?.mUserAddress
        var userAddress: UserAddress? = null
        if (mUserAddress?.userAddressList != null) {
            for (address in mUserAddress.userAddressList) {
                if (address.selected) {
                    userAddress = address
                    break
                }
            }
        }
        binding.tvName.text = singleMealUserOrderDetail?.username
        binding.tvMobile.text = singleMealUserOrderDetail?.mobileno
        val totalItems = StringBuilder()
        if (singleMealUserOrderDetail?.itemsInCart != null) {
            for ((yourMeal, price) in singleMealUserOrderDetail.itemsInCart) {
                totalItems.append(yourMeal).append("\n\n")
                binding.tvName.text =
                    singleMealUserOrderDetail.username + " - " + rsAppendedValueWithRs(price)
            }
        }
        binding.tvOrder.text = totalItems
        binding.tvArea.text = userAddress?.cityAndLocality
        binding.strip.text = singleMealUserOrderDetail?.lunchOrDinner
        if (userAddress != null && userAddress.landmark != null) {
            binding.tvLocality.text = userAddress.streetNo + " , near " + userAddress.landmark
        } else {
            binding.tvLocality.text = userAddress?.streetNo
        }
    }
}
