package com.eat_healthy.tiffin.repository

import com.eat_healthy.tiffin.models.LoginResponse
import com.eat_healthy.tiffin.models.MOtp
import com.eat_healthy.tiffin.models.OtpVerification
import com.eat_healthy.tiffin.models.User
import com.eat_healthy.tiffin.utils.BaseDataSource
import com.eat_healthy.tiffin.utils.DataState
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ActivityRetainedScoped
class LoginRepository
@Inject constructor(val apiServices: ApiServices) : BaseDataSource() {
    suspend fun login(user:User): Flow<DataState<LoginResponse?>> = flow {
        emit(DataState.Loading)
        val response = invoke { apiServices.login(user) }
        when (response.statusCode) {
            200 -> emit(DataState.Success(response.data, response.statusCode))
            else -> emit(DataState.Error(response.statusCode, null, response.errorMsg))
        }
    }

    suspend fun otpVerification(otp:MOtp): Flow<DataState<OtpVerification?>> = flow {
        emit(DataState.Loading)
        val response = invoke { apiServices.otpVerification(otp) }
        when (response.statusCode) {
            200 -> emit(DataState.Success(response.data, response.statusCode))
            else -> emit(DataState.Error(response.statusCode, null, response.errorMsg))
        }
    }
}