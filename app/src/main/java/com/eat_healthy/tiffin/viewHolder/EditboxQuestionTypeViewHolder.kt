package com.eat_healthy.tiffin.viewHolder

import android.content.Context
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.eat_healthy.tiffin.databinding.EditBoxQuestionTypeLayoutBinding
import com.eat_healthy.tiffin.models.SuggestionQuestionAnswer

class EditboxQuestionTypeViewHolder (val binding: EditBoxQuestionTypeLayoutBinding, val context: Context) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(position:Int,item: SuggestionQuestionAnswer) {
        binding.tvQuestion.text = item.question
        binding.etAnswer.addTextChangedListener {
            item.answer = it.toString()
        }
    }
}