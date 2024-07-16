package com.eat_healthy.tiffin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eat_healthy.tiffin.databinding.ItemHeaderBinding
import com.eat_healthy.tiffin.databinding.UserReviewAdapterLayoutBinding
import com.eat_healthy.tiffin.models.FoodReview
import com.eat_healthy.tiffin.models.Header
import com.eat_healthy.tiffin.viewHolder.ItemHeaderViewHolder
import javax.inject.Inject

class UserReviewAdapter @Inject constructor() : BaseRecyclerviewAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                ItemHeaderViewHolder(
                    ItemHeaderBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            1 -> {
                UserReviewViewHolder(
                    UserReviewAdapterLayoutBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    ),
                    parent.context
                )
            }

            else -> {
                ItemHeaderViewHolder(
                    ItemHeaderBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is UserReviewViewHolder) {
            (holder as UserReviewViewHolder).bind(position, mutableItemList.get(position) as FoodReview)
        }else {
            (holder as ItemHeaderViewHolder).bind(mutableItemList.get(position) as Header)
        }
    }

    class UserReviewViewHolder(val binding: UserReviewAdapterLayoutBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int, item: FoodReview) {
            binding.dateAndFoodTime.text ="Order :".plus(item.foodOrderDate).plus(",  ").plus(item.lunchOrDinner).plus(",  ").plus(item.foodReviewUserName)
            binding.foodOrderList.text = (item.foodOrderList.getOrNull(0))
            if (item.foodOrderList.size > 1) {
                binding.foodOrderList.text = (item.foodOrderList.getOrNull(0))?.plus("\n\n")
                    .plus((item.foodOrderList.getOrNull(1)))
            }
            if (item.foodReview.isNotEmpty()) {
                binding.foodReview.text = item.foodReview
                binding.foodReview.visibility = View.VISIBLE
            } else {
                binding.foodReview.visibility = View.GONE
            }
            binding.rating.text = "Rating :".plus(item.foodRating)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(mutableItemList.getOrNull(position) is Header) return 0 else return 1
    }
}
