package com.eat_healthy.tiffin.viewHolder

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eat_healthy.tiffin.R
import com.eat_healthy.tiffin.databinding.AdapterMealPeritemBinding
import com.eat_healthy.tiffin.models.MainMeal
import com.eat_healthy.tiffin.utils.Constants

class MainMealViewHolder(val binding: AdapterMealPeritemBinding, val context: Context) :RecyclerView.ViewHolder(binding.root) {
    fun bind(item:MainMeal){
        binding.itemName.text=item.itemName
        binding.position=0
        when (item.isLunchOrDinnerTime) {
            Constants.LUNCH -> {
                if (item.mealAvailability?.lunchToday == true) {
                    binding.isAvailable.text = "Availabe"
                    binding.indicator.backgroundTintList =
                        ColorStateList.valueOf(context.getColor(R.color.greenNight))
                } else {
                    binding.isAvailable.text = "Not Availabe In Lunch"
                    binding.indicator.backgroundTintList =
                        ColorStateList.valueOf(context.getColor(R.color.colorPrimary))
                }
            }

            Constants.DINNER -> {
                if (item.mealAvailability?.dinnerToday == true) {
                    binding.isAvailable.text = "Availabe"
                    binding.indicator.backgroundTintList =
                        ColorStateList.valueOf(context.getColor(R.color.greenNight))
                } else {
                    binding.isAvailable.text = "Not Availabe Today"
                    binding.indicator.backgroundTintList =
                        ColorStateList.valueOf(context.getColor(R.color.colorPrimary))
                }
            }

            else -> {
                // Time out
                binding.isAvailable.visibility = View.GONE
                binding.indicator.visibility = View.GONE
            }
        }
        Glide.with(context).load(item.itemImage).into(binding.itemImage)
    }
}