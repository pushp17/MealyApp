package com.eat_healthy.tiffin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eat_healthy.tiffin.databinding.*
import com.eat_healthy.tiffin.models.Empty
import com.eat_healthy.tiffin.viewHolder.*
import javax.inject.Inject

class SelectMealAdapter @Inject constructor() : BaseRecyclerviewAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            SelectMealViewHolder(
                AdapterItemMealDetailBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                ),
                parent.context
            )
        } else {
            EmptyViewHolder(
                EmptyLayoutBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (mutableItemList.isNotEmpty() && holder is SelectMealViewHolder) {
            holder.binding.itemClickListener=itemClickListener
            return holder.bind(position,mutableItemList.get(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(mutableItemList.getOrNull(position) is Empty) return 0 else return 1
    }
}


//class SelectMealAdapter : BaseRecyclerviewAdapter() {
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return when (viewType) {
//            Constants.VIEW_SPECIAL_MEAL -> SelectSpecialMealViewHolder(
//                AdapterSelectSpecialMealBinding.inflate(
//                    LayoutInflater.from(
//                        parent.context
//                    ), parent, false
//                ),
//                parent.context
//            )
//            Constants.VIEW_SABJI -> SelectSabjiViewHolder(
//                AdapterSelectSabjiBinding.inflate(
//                    LayoutInflater.from(
//                        parent.context
//                    ), parent, false
//                ),
//                parent.context
//            )
//            Constants.VIEW_MAIN_MEAL -> SelectMainMealViewHolder(
//                AdapterSelectMealBinding.inflate(
//                    LayoutInflater.from(
//                        parent.context
//                    ), parent, false
//                ),
//                parent.context
//            )
//            Constants.VIEW_MEAL_CATEGORY_HEADER -> MealCategoryHeaderViewHolder(
//                AdapterSelectMealHeaderBinding.inflate(
//                    LayoutInflater.from(
//                        parent.context
//                    ), parent, false
//                )
//            )
//            Constants.BUTTON -> ButtonViewHolder(
//                ButtonLayoutBinding.inflate(
//                    LayoutInflater.from(
//                        parent.context
//                    ), parent, false
//                )
//            )
//            else -> MealHeaderWithBackgroundViewHolder(
//                AdapterMealHeaderWithBackgroundBinding.inflate(
//                    LayoutInflater.from(parent.context),
//                    parent,
//                    false
//                )
//            )
//        }
//    }
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        if (mutableItemList.isNotEmpty()) {
//            return when (getItemViewType(position)) {
//                Constants.VIEW_SPECIAL_MEAL -> {
//                    (holder as SelectSpecialMealViewHolder).bind(position,mutableItemList.get(position) as SpecialMeal)
//                    holder.binding.itemClickListener=itemClickListener
//                }
//                Constants.VIEW_SABJI -> {
//                    (holder as SelectSabjiViewHolder).bind(position,mutableItemList.get(position) as Sabji)
//                    holder.binding.itemClickListener=itemClickListener
//                }
//                Constants.VIEW_MAIN_MEAL -> {
//                    (holder as SelectMainMealViewHolder).bind(position,mutableItemList.get(position) as MainMeal)
//                    holder.binding.itemClickListener=itemClickListener
//                }
//                Constants.VIEW_MEAL_CATEGORY_HEADER -> {
//                    (holder as MealCategoryHeaderViewHolder).bind(position,mutableItemList.get(position) as MealCategoryHeader)
//                    holder.binding.itemClickListener=itemClickListener
//                }
//                Constants.BUTTON -> {
//                    (holder as ButtonViewHolder).bind(position,mutableItemList.get(position) as Button)
//                    holder.binding.itemClickListener=itemClickListener
//                }
//                else ->{
//                    (holder as MealHeaderWithBackgroundViewHolder).bind(mutableItemList.get(position) as Header)
//                }
//            }
//        }
//    }
//
//}