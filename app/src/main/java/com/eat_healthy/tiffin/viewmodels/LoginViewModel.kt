package com.eat_healthy.tiffin.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eat_healthy.tiffin.models.*
import com.eat_healthy.tiffin.repository.LoginRepository
import com.eat_healthy.tiffin.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(val loginRepository: LoginRepository):ViewModel() {
    private val _userLoginResponseLiveData= MutableLiveData<DataState<LoginResponse?>>()
    val userLoginResponseLiveData: LiveData<DataState<LoginResponse?>> = _userLoginResponseLiveData

    private val _otpVerificationLiveData= MutableLiveData<DataState<OtpVerification?>>()
    val otpVerificationLiveData: LiveData<DataState<OtpVerification?>> = _otpVerificationLiveData

    fun login(user:User){
        viewModelScope.launch {
            loginRepository.login(user).onEach { dataState ->
                _userLoginResponseLiveData.value = dataState
//               if(dataState.statusCode==200)
//                   sessionSubmitMutalbleLivedata.value= DataState.Default
            }.launchIn(viewModelScope)
        }
    }

    fun verifyOtp(otp: MOtp){
        viewModelScope.launch {
            loginRepository.otpVerification(otp).onEach { dataState ->
                _otpVerificationLiveData.value = dataState
//               if(dataState.statusCode==200)
//                   sessionSubmitMutalbleLivedata.value= DataState.Default
            }.launchIn(viewModelScope)
        }
    }
}
