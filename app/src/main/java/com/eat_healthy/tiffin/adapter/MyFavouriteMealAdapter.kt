package com.eat_healthy.tiffin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eat_healthy.tiffin.databinding.AdapterMyFavouriteMealBinding
import com.eat_healthy.tiffin.models.MyFavouriteMeal
import com.eat_healthy.tiffin.models.Sabji
import com.eat_healthy.tiffin.viewHolder.MyFavouriteMealViewHolder
import javax.inject.Inject

class MyFavouriteMealAdapter @Inject constructor(): BaseRecyclerviewAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyFavouriteMealViewHolder(
            AdapterMyFavouriteMealBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                ),
                parent.context
            )
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MyFavouriteMealViewHolder).bind(position,mutableItemList.get(position) as Sabji)
            holder.binding.itemClickListener = itemClickListener
    }
}
