package com.eat_healthy.tiffin.viewHolder

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.eat_healthy.tiffin.R
import com.eat_healthy.tiffin.databinding.ButtonLayoutBinding
import com.eat_healthy.tiffin.models.Button

class ButtonViewHolder (val binding: ButtonLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(position:Int,item: Button){
        binding.position=position
        binding.item=item
        //item.enable != true
        if (false) {
            binding.llAddInCart.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.colorPrimaryLight))
            binding.llNext.visibility = View.GONE
            binding.tvAddInCart.text="Today's Order Time is Over"
        }
        binding.tvItemCount.text=item.itemCount.toString().plus(" ITEM").plus(" | ").plus(item.itemPrice).plus(" Rs")
        if (item.itemCount > 0) binding.llNext.visibility =
            View.VISIBLE else binding.llNext.visibility = View.GONE
    }
}