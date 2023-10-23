package com.eat_healthy.tiffin.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.eat_healthy.tiffin.models.AvailableLocalityResponse
import com.eat_healthy.tiffin.repository.AvailableLocalityRepository
import com.eat_healthy.tiffin.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AvailableLocalityListViewModel @Inject constructor(
    val availableLocalityRepository: AvailableLocalityRepository
) : ViewModel() {
    private var availableLocalityMutalbleLivedata: MutableLiveData<DataState<AvailableLocalityResponse?>> = MutableLiveData()
    val availableLocalityLivedata: LiveData<DataState<AvailableLocalityResponse?>>
        get() = availableLocalityMutalbleLivedata

    fun availablelocalistList() {
        viewModelScope.launch {
            availableLocalityRepository.getAvailableLocalitiesData().onEach { dataState ->
                availableLocalityMutalbleLivedata.value = dataState

//               if(dataState.statusCode==200)
//                   sessionSubmitMutalbleLivedata.value= DataState.Default
            }.launchIn(viewModelScope)
        }
    }
}
