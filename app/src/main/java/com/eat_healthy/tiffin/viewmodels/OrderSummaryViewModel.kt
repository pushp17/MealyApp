package com.eat_healthy.tiffin.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eat_healthy.tiffin.models.ApiResponse
import com.eat_healthy.tiffin.models.MonthlyUserPreference
import com.eat_healthy.tiffin.models.SingleMealUserOrderDetail
import com.eat_healthy.tiffin.repository.MonthlyFoodSelectionRepository
import com.eat_healthy.tiffin.repository.SingleUserOrderRepository
import com.eat_healthy.tiffin.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderSummaryViewModel @Inject constructor(
    private val monthlyFoodSelectionRepository: MonthlyFoodSelectionRepository,
    private val singleUserOrderRepository: SingleUserOrderRepository
) : ViewModel() {
    var vegCount=0
    var vegSpecialCount=0
    var nonVegCount=0
    var totalMealCount=0
    var grandTotal = 0.0
    var submitButtonAlreadyClicked = false

    private var _apiResponseLivedata: MutableLiveData<DataState<ApiResponse?>> = MutableLiveData()
    val apiResponseLivedata: LiveData<DataState<ApiResponse?>>
        get() = _apiResponseLivedata

    fun calulateMealCount(sharedViewModel:SharedViewModel){
        sharedViewModel.monthlyUserPreference?.userWeeklyFoodPreference?.forEach {
            when (it.lunch) {
                "lunchVeg" -> {
                    vegCount++
                }
                "lunchVegSpecial" -> {
                    vegSpecialCount++
                }
                "lunchNonVeg" -> {
                    nonVegCount++
                }
            }

            when (it.dinner) {
                "dinnerVeg" -> {
                    vegCount++
                }
                "dinnerVegSpecial" -> {
                    vegSpecialCount++
                }
                "dinnerNonVeg" -> {
                    nonVegCount++
                }
            }
        }
        totalMealCount=vegCount+vegSpecialCount+nonVegCount
    }

    fun singleUserOrderServices(singleMealUserOrderDetail: SingleMealUserOrderDetail){
        if (!submitButtonAlreadyClicked) {
            submitButtonAlreadyClicked = true
            viewModelScope.launch {
                singleUserOrderRepository.singleUserOrder(singleMealUserOrderDetail)
                    .onEach { dataState ->
                        _apiResponseLivedata.value = dataState
                        if (dataState is DataState.Error<*>) {
                            submitButtonAlreadyClicked = false
                        }
                    }.launchIn(viewModelScope)
            }
        }
    }


    fun monthlyUserOrderServices(monthlyUserPreference: MonthlyUserPreference){
        viewModelScope.launch {
            monthlyFoodSelectionRepository.monthlyUserOrderAndPreference(monthlyUserPreference).onEach { dataState ->
                _apiResponseLivedata.value = dataState
//               if(dataState.statusCode==200)
//                   sessionSubmitMutalbleLivedata.value= DataState.Default
            }.launchIn(viewModelScope)
        }
    }


}