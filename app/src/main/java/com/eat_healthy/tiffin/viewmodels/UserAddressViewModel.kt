package com.eat_healthy.tiffin.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eat_healthy.tiffin.models.*
import com.eat_healthy.tiffin.repository.UserAddressRepository
import com.eat_healthy.tiffin.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserAddressViewModel @Inject constructor(private val userAddressRepository: UserAddressRepository): ViewModel() {

    private var availableLocalityMutalbleLivedata: MutableLiveData<DataState<AvailableLocalityResponse?>> = MutableLiveData()
    val availableLocalityLivedata: LiveData<DataState<AvailableLocalityResponse?>>
        get() = availableLocalityMutalbleLivedata

    private val _userAddressLiveData=MutableLiveData<DataState<UserAddressResponse?>>()
    val userAddressLiveData:LiveData<DataState<UserAddressResponse?>> = _userAddressLiveData

    private val _addressUploadLiveData=MutableLiveData<DataState<ApiResponse?>>()
    val addressUploadLiveData:LiveData<DataState<ApiResponse?>> = _addressUploadLiveData

    private var isUserAddressCalled=false

    fun availablelocalistList() {
        viewModelScope.launch {
            userAddressRepository.getAvailableLocalitiesData().onEach { dataState ->
                availableLocalityMutalbleLivedata.value = dataState

//               if(dataState.statusCode==200)
//                   sessionSubmitMutalbleLivedata.value= DataState.Default
            }.launchIn(viewModelScope)
        }
    }

    fun fetchUserAddress(user: User){
        if(!isUserAddressCalled){
            viewModelScope.launch {
                userAddressRepository.getUserAddress(user).onEach { dataState ->
                    _userAddressLiveData.value = dataState

               if(dataState.statusCode==200)
                   isUserAddressCalled=true
                }.launchIn(viewModelScope)
            }
        }
    }

    fun updateUserAddress(mUserAddress:MUserAddress){
        viewModelScope.launch {
            userAddressRepository.updateUserAddress(mUserAddress).onEach { dataState ->
                _addressUploadLiveData.value = dataState

//               if(dataState.statusCode==200)
//                   sessionSubmitMutalbleLivedata.value= DataState.Default
            }.launchIn(viewModelScope)
        }
    }
}
