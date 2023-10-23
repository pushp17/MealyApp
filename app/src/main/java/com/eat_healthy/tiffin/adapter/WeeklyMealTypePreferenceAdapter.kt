package com.eat_healthy.tiffin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eat_healthy.tiffin.databinding.AdapterUserPerdayMealTypeBinding
import com.eat_healthy.tiffin.models.WeekelyMealType
import com.eat_healthy.tiffin.viewHolder.UserPerDayMealTypeViewholder
import javax.inject.Inject

class WeeklyMealTypePreferenceAdapter @Inject constructor() : BaseRecyclerviewAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return UserPerDayMealTypeViewholder(
            AdapterUserPerdayMealTypeBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is UserPerDayMealTypeViewholder) {
            holder.bind(position, mutableItemList.get(position) as WeekelyMealType)
            holder.binding.itemClickListener = itemClickListener
        }
    }
}
