package com.eat_healthy.tiffin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eat_healthy.tiffin.databinding.RefereeHistoryAdapterBinding
import com.eat_healthy.tiffin.models.RefereeOrdersDetails
import com.eat_healthy.tiffin.viewHolder.RefereViewHolder
import javax.inject.Inject

class WalletAdapter @Inject constructor() : BaseRecyclerviewAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return RefereViewHolder(
            RefereeHistoryAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), parent.context
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RefereViewHolder) {
            holder.bind(position, mutableItemList.get(position) as RefereeOrdersDetails)
        }
    }
}
