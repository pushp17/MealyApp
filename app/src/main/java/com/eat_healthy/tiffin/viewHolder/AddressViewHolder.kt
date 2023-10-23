package com.eat_healthy.tiffin.viewHolder

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.eat_healthy.tiffin.databinding.AdapterAddressBinding
import com.eat_healthy.tiffin.models.UserAddress
import java.util.*

class AddressViewHolder(val binding: AdapterAddressBinding):RecyclerView.ViewHolder(binding.root) {
    fun bind(position:Int,userAddress: UserAddress){
       if(position==1){
           binding.strip.backgroundTintList= ColorStateList.valueOf(Color.parseColor("#c4f9e4"))
           binding.strip.setTextColor(ColorStateList.valueOf(Color.parseColor("#2b986f")))
       }else if(position==2){
           binding.strip.backgroundTintList= ColorStateList.valueOf(Color.parseColor("#ffeaca"))
           binding.strip.setTextColor(ColorStateList.valueOf(Color.parseColor("#da9239")))
       }
        binding.position=position
        binding.item=userAddress
        binding.tvArea.text=userAddress.cityAndLocality
        binding.strip.text=userAddress.addressType
        binding.rbAddress.isChecked = userAddress.selected
        if(userAddress.landmark !=null){
            binding.tvLocality.text=userAddress.streetNo.plus(" , near ").plus(userAddress.landmark)
        }else {
            binding.tvLocality.text=userAddress.streetNo
        }
    }
}