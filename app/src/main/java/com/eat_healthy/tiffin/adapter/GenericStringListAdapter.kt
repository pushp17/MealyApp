package com.eat_healthy.tiffin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eat_healthy.tiffin.databinding.GenericStringListAdapterBinding
import com.eat_healthy.tiffin.models.StringText

class GenericStringListAdapter : BaseRecyclerviewAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return GenericStringViewHolder(
            GenericStringListAdapterBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(viewholder: RecyclerView.ViewHolder, position: Int) {
        (viewholder as GenericStringViewHolder).bind(mutableItemList.get(position) as StringText)
    }

    class GenericStringViewHolder(val binding: GenericStringListAdapterBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: StringText) {
            binding.tvItem.text = item.item
        }
    }
}