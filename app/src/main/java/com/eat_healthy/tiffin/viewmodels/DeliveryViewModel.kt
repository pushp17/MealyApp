package com.eat_healthy.tiffin.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eat_healthy.tiffin.models.DeliveryDetailsResponse
import com.eat_healthy.tiffin.repository.DeliveryRepository
import com.eat_healthy.tiffin.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeliveryViewModel @Inject constructor(val deliveryRepository: DeliveryRepository): ViewModel() {
    private val _deliveryResponseLiveData = MutableLiveData<DataState<DeliveryDetailsResponse?>>()
    val deliveryResponseLiveData: LiveData<DataState<DeliveryDetailsResponse?>> =
        _deliveryResponseLiveData


    fun getTodaysDeliveryDetailsList(dateAndTime: String) {
        viewModelScope.launch {
            deliveryRepository.getTodaysDeliveryData(dateAndTime).onEach { dataState ->
                _deliveryResponseLiveData.value = dataState
            }.launchIn(viewModelScope)
        }
    }
}