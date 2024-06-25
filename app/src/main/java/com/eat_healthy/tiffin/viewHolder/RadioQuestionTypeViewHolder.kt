package com.eat_healthy.tiffin.viewHolder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.eat_healthy.tiffin.databinding.RadioButtonTypeQuestionLayoutBinding
import com.eat_healthy.tiffin.models.SuggestionQuestionAnswer

class RadioQuestionTypeViewHolder (val binding: RadioButtonTypeQuestionLayoutBinding, val context: Context) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(position:Int,item: SuggestionQuestionAnswer){
        binding.tvQuestion.text = item.question
        binding.radioBad.setOnClickListener {
            item.answer = "Bad"
            binding.radioBad.isChecked = true
            binding.radioAverage.isChecked = false
            binding.radioGood.isChecked = false
        }
        binding.radioAverage.setOnClickListener {
            item.answer = "Average"
            binding.radioBad.isChecked = false
            binding.radioAverage.isChecked = true
            binding.radioGood.isChecked = false
        }
        binding.radioGood.setOnClickListener {
            item.answer = "Good"
            binding.radioBad.isChecked = false
            binding.radioAverage.isChecked = false
            binding.radioGood.isChecked = true
        }
    }
}