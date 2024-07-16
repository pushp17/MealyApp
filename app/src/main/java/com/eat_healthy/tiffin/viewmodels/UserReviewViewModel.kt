package com.eat_healthy.tiffin.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eat_healthy.tiffin.genericFiles.ListItem
import com.eat_healthy.tiffin.models.FoodReview
import com.eat_healthy.tiffin.models.Header
import com.eat_healthy.tiffin.repository.FoodReviewOrSuggestionRepository
import com.eat_healthy.tiffin.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserReviewViewModel @Inject constructor(private val foodReviewOrSuggestionRepository: FoodReviewOrSuggestionRepository): ViewModel() {

    private var _userReviewLivedata: MutableLiveData<DataState<List<FoodReview>?>> = MutableLiveData()
    val userReviewLivedata : LiveData<DataState<List<FoodReview>?>>
        get() = _userReviewLivedata

   val usersFoodReviewForTracking = mutableListOf<ListItem>()

    fun callUserReview() {
        viewModelScope.launch {
            foodReviewOrSuggestionRepository.getFoodReviewResponse().onEach { dataState ->
                _userReviewLivedata.value = dataState
                if (dataState is DataState.Success<*>) {
                    dataState.data?.groupBy { it.foodReviewDate }?.map { (key, result) ->
                        usersFoodReviewForTracking.add(Header(key))
                        usersFoodReviewForTracking.addAll(result)
                    }
                    usersFoodReviewForTracking.reverse()
                    _userReviewLivedata.value = dataState
                }
            }.launchIn(viewModelScope)
        }
    }
}