package com.eat_healthy.tiffin.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eat_healthy.tiffin.databinding.AdapterAddressBinding
import com.eat_healthy.tiffin.models.UserAddress
import com.eat_healthy.tiffin.viewHolder.AddressViewHolder
import javax.inject.Inject

const val VIEW_TYPE_1 = 0
class AddressAdapter @Inject constructor():BaseRecyclerviewAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
     return AddressViewHolder(AdapterAddressBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as AddressViewHolder).bind(position,mutableItemList.get(position) as UserAddress)
        holder.binding.itemClickListener = itemClickListener
    }
}