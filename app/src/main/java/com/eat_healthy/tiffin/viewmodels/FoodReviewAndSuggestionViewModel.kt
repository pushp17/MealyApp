package com.eat_healthy.tiffin.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eat_healthy.tiffin.models.ApiResponse
import com.eat_healthy.tiffin.models.FoodReview
import com.eat_healthy.tiffin.models.UsersSuggestionQuestions
import com.eat_healthy.tiffin.models.UsersSuggestionUpload
import com.eat_healthy.tiffin.repository.ApiServices
import com.eat_healthy.tiffin.repository.FoodReviewOrSuggestionRepository
import com.eat_healthy.tiffin.utils.DataState
import com.eat_healthy.tiffin.utils.SharedPrefManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodReviewAndSuggestionViewModel @Inject constructor(val repository: FoodReviewOrSuggestionRepository, val sharedPrefManager: SharedPrefManager): ViewModel() {

    private val _foodReviewResponseLiveData =
        MutableLiveData<DataState<ApiResponse?>>()
    val foodReviewResponseLiveData: LiveData<DataState<ApiResponse?>> =
        _foodReviewResponseLiveData

    private val _suggestionQuestionResponseLiveData =
        MutableLiveData<DataState<UsersSuggestionQuestions?>>()
    val suggestionQuestionResponseLiveData: LiveData<DataState<UsersSuggestionQuestions?>> =
        _suggestionQuestionResponseLiveData
    private var usersSuggestionsQuestionsfetched = false

    private val _usersSuggestionUploadResponseLiveData =
        MutableLiveData<DataState<ApiResponse?>>()
    val usersSuggestionUploadResponseLiveData: LiveData<DataState<ApiResponse?>> =
        _usersSuggestionUploadResponseLiveData

    fun postFoodReview(foodReview: FoodReview) {
        viewModelScope.launch {
            repository.postFoodReviewDataResponse(foodReview).onEach { dataState ->
            }.launchIn(viewModelScope)
        }
    }

    fun getUserSuggestionQuestions() {
        if(usersSuggestionsQuestionsfetched) {
            _suggestionQuestionResponseLiveData.value = suggestionQuestionResponseLiveData.value
            return
        }
        viewModelScope.launch {
            repository.getSuggestionQuestionDataResponse().onEach { dataState ->
                _suggestionQuestionResponseLiveData.value = dataState
                if(dataState.statusCode==200) {
                    usersSuggestionsQuestionsfetched = true
                }
            }.launchIn(viewModelScope)
        }
    }

    fun uploadUserSuggestion(usersSuggestionUpload: UsersSuggestionUpload) {
        viewModelScope.launch {
            repository.postUsersSuggestionDataResponse(usersSuggestionUpload).onEach { dataState ->
                _usersSuggestionUploadResponseLiveData.value = dataState
            }.launchIn(viewModelScope)
        }
    }
}
