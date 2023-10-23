package com.eat_healthy.tiffin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eat_healthy.tiffin.databinding.AdapterInfoBinding
import com.eat_healthy.tiffin.viewHolder.InfoViewHolder

class InfoAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var attachedToRecyclerView = false
    private val infoList = mutableListOf<String>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return InfoViewHolder(
            AdapterInfoBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        return  (holder as InfoViewHolder).bind(infoList.get(position) as String,position)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        attachedToRecyclerView = true
    }

    override fun getItemCount(): Int {
        return infoList.size
    }

    fun setItems(listItem: List<String>) {
        infoList.clear()
        infoList.addAll(listItem)
        if (attachedToRecyclerView) {
            notifyDataSetChanged()
        }
    }
    fun getItemsList():MutableList<String>{
        return infoList
    }

}