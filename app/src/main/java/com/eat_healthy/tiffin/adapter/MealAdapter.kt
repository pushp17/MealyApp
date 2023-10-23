package com.eat_healthy.tiffin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eat_healthy.tiffin.databinding.AdapterMealPeritemBinding
import com.eat_healthy.tiffin.databinding.AdapterSpecialMealBinding
import com.eat_healthy.tiffin.databinding.ItemHeaderBinding
import com.eat_healthy.tiffin.models.Header
import com.eat_healthy.tiffin.models.MainMeal
import com.eat_healthy.tiffin.models.Sabji
import com.eat_healthy.tiffin.models.SpecialMeal
import com.eat_healthy.tiffin.utils.Constants
import com.eat_healthy.tiffin.viewHolder.ItemHeaderViewHolder
import com.eat_healthy.tiffin.viewHolder.MainMealViewHolder
import com.eat_healthy.tiffin.viewHolder.SabjiViewHolder
import com.eat_healthy.tiffin.viewHolder.SpecialMealViewHolder
import javax.inject.Inject

class MealAdapter @Inject constructor(): BaseRecyclerviewAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Constants.VIEW_SPECIAL_MEAL -> SpecialMealViewHolder(
                AdapterSpecialMealBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                ),
                parent.context
            )
            Constants.VIEW_SABJI -> SabjiViewHolder(
                AdapterMealPeritemBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                ),
                parent.context
            )
            Constants.VIEW_MAIN_MEAL -> MainMealViewHolder(
                AdapterMealPeritemBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                ),
                parent.context
            )
            else -> ItemHeaderViewHolder(
                ItemHeaderBinding.inflate(
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
                    (holder as SpecialMealViewHolder).bind(position,mutableItemList.get(position) as SpecialMeal)
                    holder.binding.itemClickListener=itemClickListener
                }
                Constants.VIEW_SABJI -> {
                    (holder as SabjiViewHolder).bind(
                        position,
                        mutableItemList.get(position) as Sabji
                    )
                    holder.binding.itemClickListener = itemClickListener
                }
                Constants.VIEW_MAIN_MEAL -> {
                    (holder as MainMealViewHolder).bind(mutableItemList.get(position) as MainMeal)
                    holder.binding.itemClickListener=itemClickListener
                }
                else -> (holder as ItemHeaderViewHolder).bind(mutableItemList.get(position) as Header)
            }
        }
    }
    override fun getItemViewType(position: Int): Int {
        return when (mutableItemList[position]) {
            is Header -> Constants.VIEW_HEADER
            is SpecialMeal -> Constants.VIEW_SPECIAL_MEAL
            is Sabji -> Constants.VIEW_SABJI
            is MainMeal -> Constants.VIEW_MAIN_MEAL
            else ->  Constants.VIEW_HEADER
        }
    }

}