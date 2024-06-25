package com.eat_healthy.tiffin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eat_healthy.tiffin.databinding.AdapterSelectBaseBinding
import com.eat_healthy.tiffin.databinding.AdapterSelectExtrasCheckboxLayoutBinding
import com.eat_healthy.tiffin.databinding.AdapterSelectExtrasRadioLayoutBinding
import com.eat_healthy.tiffin.databinding.MediumSizeHeaderBinding
import com.eat_healthy.tiffin.models.ExtrasV2
import com.eat_healthy.tiffin.models.Header
import com.eat_healthy.tiffin.models.Meal
import com.eat_healthy.tiffin.utils.Constants
import com.eat_healthy.tiffin.viewHolder.MealCategoryHeaderViewHolder
import com.eat_healthy.tiffin.viewHolder.SelectBaseMealViewHolder
import com.eat_healthy.tiffin.viewHolder.SelectExtrasCheckboxViewHolder
import com.eat_healthy.tiffin.viewHolder.SelectExtrasRadioViewHolder

class SelectBaseCourseItemsAdapter : BaseRecyclerviewAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Constants.VIEW_MEAL -> SelectBaseMealViewHolder(
                AdapterSelectBaseBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                ),
                parent.context
            )
            Constants.VIEW_EXTRAS_RADIO -> SelectExtrasRadioViewHolder(
                AdapterSelectExtrasRadioLayoutBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                ),
                parent.context
            )
            Constants.VIEW_EXTRAS_CHECKBOX -> SelectExtrasCheckboxViewHolder(
                AdapterSelectExtrasCheckboxLayoutBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                ),
                parent.context
            )
            else -> MealCategoryHeaderViewHolder(
                MediumSizeHeaderBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (mutableItemList.isNotEmpty()) {
            return when (getItemViewType(position)) {
                Constants.VIEW_MEAL -> {
                    (holder as SelectBaseMealViewHolder).bind(position,
                        mutableItemList.get(position) as Meal
                    )
                    holder.binding.itemClickListener = itemClickListener
                }

                Constants.VIEW_EXTRAS_RADIO -> {
                    (holder as SelectExtrasRadioViewHolder).bind(position,
                        mutableItemList.get(position) as ExtrasV2
                    )
                    holder.binding.itemClickListener = itemClickListener
                }

                Constants.VIEW_EXTRAS_CHECKBOX -> {
                    (holder as SelectExtrasCheckboxViewHolder).bind(position,
                        mutableItemList.get(position) as ExtrasV2
                    )
                    holder.binding.itemClickListener = itemClickListener
                }

                else -> {
                    (holder as MealCategoryHeaderViewHolder).bind(position,
                        mutableItemList.get(position) as Header
                    )
                }
            }
        }
    }
    override fun getItemViewType(position: Int): Int {
        val item = mutableItemList[position]
        return when (item) {
            is Meal -> Constants.VIEW_MEAL
            is ExtrasV2 -> {
                if (item.type.equals("radio")) {
                    Constants.VIEW_EXTRAS_RADIO
                } else {
                    Constants.VIEW_EXTRAS_CHECKBOX
                }
            }
            else -> Constants.VIEW_HEADER
        }
    }
}
