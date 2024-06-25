package com.eat_healthy.tiffin.viewHolder

import android.content.Context
import android.graphics.Paint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eat_healthy.tiffin.R
import com.eat_healthy.tiffin.databinding.AdapterItemMealDetailBinding
import com.eat_healthy.tiffin.genericFiles.ListItem
import com.eat_healthy.tiffin.models.Meal
import com.eat_healthy.tiffin.models.SpecialMeal
import com.eat_healthy.tiffin.utils.AppUtils
import com.eat_healthy.tiffin.utils.Constants

class SelectMealViewHolder(val binding: AdapterItemMealDetailBinding, val context: Context) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(position: Int, item: ListItem) {
        binding.plus="plus"
        binding.minus="minus"
        binding.addBtn="addBtn"
        if (item is Meal) {
            binding.itemName.text = item.name
            if (!item.info.isNullOrEmpty()) {
                binding.desc.visibility = View.VISIBLE
                binding.desc.text = item.info
            } else {
                binding.desc.visibility = View.GONE
            }
            binding.price.text = AppUtils.rsAppendedValue(item.price)
            binding.shoffPrice.text = AppUtils.rsAppendedValue(item.showOffPrice)
            binding.shoffPrice.paintFlags =
                binding.shoffPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            binding.position = position
            binding.item = item
            Glide.with(context).load(item.image).into(binding.itemImage)

            if (item.addButtonCountText.get() > 0) {
                binding.llPlusMinus.visibility = View.VISIBLE
                binding.llAddItem.visibility = View.GONE
            } else {
                binding.llPlusMinus.visibility = View.GONE
                binding.llAddItem.visibility = View.VISIBLE
            }

            binding.tvCount.text = item.addButtonCountText.get().toString()
            if (item.isLunchOrDinnerTime == Constants.LUNCH_TIMEOUT || item.isLunchOrDinnerTime == Constants.TIME_OUT) {
                binding.tvAdd.text = context.getString(R.string.see_timming)
                binding.tvAdd.textSize = 13.0f
            }

            if(item.highlight){
                binding.specialMealLogo.visibility = View.VISIBLE
            }else {
                binding.specialMealLogo.visibility = View.GONE
            }

        }
//        else if (item is SpecialMeal) {
//            binding.itemName.text = item.itemName
//            if (!item.info.isNullOrEmpty()) {
//                binding.desc.visibility = View.VISIBLE
//                binding.desc.text = item.info
//            } else {
//                binding.desc.visibility = View.GONE
//            }
//            binding.price.text = AppUtils.rsAppendedValue(item.itemPrice)
//            binding.shoffPrice.text = AppUtils.rsAppendedValue(item.showOffPrice)
//            binding.shoffPrice.paintFlags =
//                binding.shoffPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
////            binding.position = position
////            binding.item = item
//            Glide.with(context).load(item.itemImage).into(binding.itemImage)
//        }
    }
}