package com.eat_healthy.tiffin.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eat_healthy.tiffin.models.RefrerReferalHistoryResponse
import com.eat_healthy.tiffin.models.User
import com.eat_healthy.tiffin.repository.ReferalRepository
import com.eat_healthy.tiffin.utils.DataState
import com.eat_healthy.tiffin.utils.SharedPrefManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ReferalViewModel @Inject constructor(val referalRepository: ReferalRepository, val sharedPrefManager: SharedPrefManager): ViewModel() {

    private val _refrerReferalHistoryResponseLiveData =
        MutableLiveData<DataState<RefrerReferalHistoryResponse?>>()
    val refrerReferalHistoryResponseLiveData: LiveData<DataState<RefrerReferalHistoryResponse?>> =
        _refrerReferalHistoryResponseLiveData

    fun getRefrerReferalHistory(user: User) {
        viewModelScope.launch {
            referalRepository.getRefrerReferalHistory(user).onEach { dataState ->
                _refrerReferalHistoryResponseLiveData.value = dataState
//               if(dataState.statusCode==200)
//                   sessionSubmitMutalbleLivedata.value= DataState.Default
            }.launchIn(viewModelScope)
        }
    }
}
