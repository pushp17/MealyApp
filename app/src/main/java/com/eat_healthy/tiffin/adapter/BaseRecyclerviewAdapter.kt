package com.eat_healthy.tiffin.adapter

import androidx.recyclerview.widget.RecyclerView
import com.eat_healthy.tiffin.genericFiles.ListItem
import com.eat_healthy.tiffin.utils.RecyclerviewItemClicklistener

abstract class BaseRecyclerviewAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var attachedToRecyclerView = false
    protected val mutableItemList = arrayListOf<ListItem>()
    protected var itemClickListener: RecyclerviewItemClicklistener<ListItem>? = null
    fun setItems(listItem: List<ListItem>) {
        mutableItemList.clear()
        mutableItemList.addAll(listItem)
        notifyDataSetChanged()
    }

    fun getItemsList():MutableList<ListItem>{
        return mutableItemList
    }

    override fun getItemCount(): Int {
      return  mutableItemList.size
    }

    fun setOnClickListener(itemClickListener: RecyclerviewItemClicklistener<ListItem>?) {
        itemClickListener?.let {
            this.itemClickListener = it
        }
    }
}