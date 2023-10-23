package com.eat_healthy.tiffin.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eat_healthy.tiffin.models.SingleMealUserOrderDetail
import com.eat_healthy.tiffin.models.SingleMealUserOrderHistoryResponse
import com.eat_healthy.tiffin.models.User
import com.eat_healthy.tiffin.repository.OrderHistoryRepository
import com.eat_healthy.tiffin.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderHistoryViewmodel @Inject constructor(val orderHistoryRepository: OrderHistoryRepository) :
    ViewModel() {
    private val _orderHistoryResponseLiveData =
        MutableLiveData<DataState<SingleMealUserOrderHistoryResponse?>>()
    val orderHistoryResponseLiveData: LiveData<DataState<SingleMealUserOrderHistoryResponse?>> =
        _orderHistoryResponseLiveData

    fun getSingleUserOrdersHistory(user: User) {
        viewModelScope.launch {
            orderHistoryRepository.singleUserOrdersHistory(user).onEach { dataState ->
                _orderHistoryResponseLiveData.value = dataState
            }.launchIn(viewModelScope)
        }
    }
}
