package com.eat_healthy.tiffin.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.eat_healthy.tiffin.databinding.AdapterUserPerdayMealTypeBinding
import com.eat_healthy.tiffin.models.WeekelyMealType

class UserPerDayMealTypeViewholder (val binding:AdapterUserPerdayMealTypeBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(position: Int,weekelyMealType: WeekelyMealType){
        binding.position=position
        binding.item=weekelyMealType

        //Lunch
        if (weekelyMealType.lunchVegSelected == true) {
            binding.lunchVeg.isChecked = true
            binding.lunchVegspecial.isChecked = false
            binding.lunchNonveg.isChecked = false
        } else if (weekelyMealType.lunchVegSpecialSelected == true) {
            binding.lunchVegspecial.isChecked = true
            binding.lunchVeg.isChecked = false
            binding.lunchNonveg.isChecked = false
        } else if(weekelyMealType.lunchNonVegSelected == true){
            binding.lunchNonveg.isChecked = true
            binding.lunchVegspecial.isChecked = false
            binding.lunchVeg.isChecked = false
        }else {
            binding.lunchNonveg.isChecked = false
            binding.lunchVegspecial.isChecked = false
            binding.lunchVeg.isChecked = false
        }

        //Dinner
        if (weekelyMealType.dinnerVegSelected == true) {
            binding.dinnerVeg.isChecked = true
            binding.dinnerVegspecial.isChecked = false
            binding.dinnerNonveg.isChecked = false
        } else if(weekelyMealType.dinnerVegSpecialSelected == true) {
            binding.dinnerVegspecial.isChecked = true
            binding.dinnerVeg.isChecked = false
            binding.dinnerNonveg.isChecked = false
        } else if(weekelyMealType.dinnerNonVegSelected == true){
            binding.dinnerNonveg.isChecked = true
            binding.dinnerVegspecial.isChecked = false
            binding.dinnerVeg.isChecked = false
        }else {
            binding.dinnerNonveg.isChecked = false
            binding.dinnerVegspecial.isChecked = false
            binding.dinnerVeg.isChecked = false
        }


    }
}
