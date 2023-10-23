package com.eat_healthy.tiffin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eat_healthy.tiffin.databinding.*
import com.eat_healthy.tiffin.models.*
import com.eat_healthy.tiffin.utils.Constants
import com.eat_healthy.tiffin.viewHolder.*


class SelectMealAdapter : BaseRecyclerviewAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Constants.VIEW_SPECIAL_MEAL -> SelectSpecialMealViewHolder(
                AdapterSelectSpecialMealBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                ),
                parent.context
            )
            Constants.VIEW_SABJI -> SelectSabjiViewHolder(
                AdapterSelectSabjiBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                ),
                parent.context
            )
            Constants.VIEW_MAIN_MEAL -> SelectMainMealViewHolder(
                AdapterSelectMealBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                ),
                parent.context
            )
            Constants.VIEW_MEAL_CATEGORY_HEADER -> MealCategoryHeaderViewHolder(
                AdapterSelectMealHeaderBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
            Constants.BUTTON -> ButtonViewHolder(
                ButtonLayoutBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
            else -> MealHeaderWithBackgroundViewHolder(
                AdapterMealHeaderWithBackgroundBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (mutableItemList.isNotEmpty()) {
            return when (getItemViewType(position)) {
                Constants.VIEW_SPECIAL_MEAL -> {
                    (holder as SelectSpecialMealViewHolder).bind(position,mutableItemList.get(position) as SpecialMeal)
                    holder.binding.itemClickListener=itemClickListener
                }
                Constants.VIEW_SABJI -> {
                    (holder as SelectSabjiViewHolder).bind(position,mutableItemList.get(position) as Sabji)
                    holder.binding.itemClickListener=itemClickListener
                }
                Constants.VIEW_MAIN_MEAL -> {
                    (holder as SelectMainMealViewHolder).bind(position,mutableItemList.get(position) as MainMeal)
                    holder.binding.itemClickListener=itemClickListener
                }
                Constants.VIEW_MEAL_CATEGORY_HEADER -> {
                    (holder as MealCategoryHeaderViewHolder).bind(position,mutableItemList.get(position) as MealCategoryHeader)
                    holder.binding.itemClickListener=itemClickListener
                }
                Constants.BUTTON -> {
                    (holder as ButtonViewHolder).bind(position,mutableItemList.get(position) as Button)
                    holder.binding.itemClickListener=itemClickListener
                }
                else ->{
                    (holder as MealHeaderWithBackgroundViewHolder).bind(mutableItemList.get(position) as Header)
                }
            }
        }
    }
    override fun getItemViewType(position: Int): Int {
        return when (mutableItemList[position]) {
            is Header -> Constants.VIEW_HEADER
            is MealCategoryHeader -> Constants.VIEW_MEAL_CATEGORY_HEADER
            is SpecialMeal -> Constants.VIEW_SPECIAL_MEAL
            is Sabji -> Constants.VIEW_SABJI
            is MainMeal -> Constants.VIEW_MAIN_MEAL
            is Button -> Constants.BUTTON
            else -> Constants.VIEW_HEADER
        }
    }

}