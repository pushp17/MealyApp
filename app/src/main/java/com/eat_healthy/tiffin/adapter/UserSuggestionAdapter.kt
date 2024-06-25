package com.eat_healthy.tiffin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eat_healthy.tiffin.databinding.EditBoxQuestionTypeLayoutBinding
import com.eat_healthy.tiffin.databinding.RadioButtonTypeQuestionLayoutBinding
import com.eat_healthy.tiffin.models.SuggestionQuestionAnswer
import com.eat_healthy.tiffin.viewHolder.EditboxQuestionTypeViewHolder
import com.eat_healthy.tiffin.viewHolder.RadioQuestionTypeViewHolder
import javax.inject.Inject

class UserSuggestionAdapter @Inject constructor() : BaseRecyclerviewAdapter() {
    companion object{
        val EDIT_BOX_QUESTION_VIEW_TYPE = 0;
        val RADIO_BOX_QUESTION_VIEW_TYPE = 1;
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return when(viewType){
        EDIT_BOX_QUESTION_VIEW_TYPE -> {
            EditboxQuestionTypeViewHolder(
                EditBoxQuestionTypeLayoutBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                ), parent.context
            )
        }

        RADIO_BOX_QUESTION_VIEW_TYPE ->{
            RadioQuestionTypeViewHolder(
                RadioButtonTypeQuestionLayoutBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                ), parent.context
            )
        }
        else -> {
            EditboxQuestionTypeViewHolder(
                EditBoxQuestionTypeLayoutBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                ), parent.context
            )
        }
     }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
      when(getItemViewType(position)){
          EDIT_BOX_QUESTION_VIEW_TYPE -> {
              (holder as EditboxQuestionTypeViewHolder).bind(
                  position,
                  mutableItemList.get(position) as SuggestionQuestionAnswer
              )
          }
          RADIO_BOX_QUESTION_VIEW_TYPE -> {
              (holder as RadioQuestionTypeViewHolder).bind(
                  position,
                  mutableItemList.get(position) as SuggestionQuestionAnswer
              )
          }
          else -> {
          }
      }
    }

    override fun getItemViewType(position: Int): Int {
        return if (mutableItemList.get(position) is SuggestionQuestionAnswer) {
            if ((mutableItemList.get(position) as SuggestionQuestionAnswer).type.equals("radio_question")) {
                RADIO_BOX_QUESTION_VIEW_TYPE
            } else {
                EDIT_BOX_QUESTION_VIEW_TYPE
            }
        } else 0
    }
}