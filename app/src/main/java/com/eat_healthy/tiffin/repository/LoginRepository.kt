package com.eat_healthy.tiffin.repository

import android.util.Log
import com.eat_healthy.tiffin.models.LoginResponse
import com.eat_healthy.tiffin.models.MOtp
import com.eat_healthy.tiffin.models.MealsApiRespone
import com.eat_healthy.tiffin.models.OtpVerification
import com.eat_healthy.tiffin.models.User
import com.eat_healthy.tiffin.models.UserDetailResponse
import com.eat_healthy.tiffin.models.WeekelyMenu
import com.eat_healthy.tiffin.models.WeekelyMenuResponse
import com.eat_healthy.tiffin.utils.BaseDataSource
import com.eat_healthy.tiffin.utils.Constants
import com.eat_healthy.tiffin.utils.DataState
import com.eat_healthy.tiffin.utils.SharedPrefManager
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class LoginRepository
@Inject constructor(val apiServices: ApiServices,val sharedPrefManager: SharedPrefManager) : BaseDataSource() {
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

    suspend fun getUserDetails(user:User): Flow<DataState<UserDetailResponse?>> = flow {
        emit(DataState.Loading)
        val response = invoke { apiServices.getUserDetails(user) }
        when (response.statusCode) {
            200 -> emit(DataState.Success(response.data, response.statusCode))
            else -> emit(DataState.Error(response.statusCode, null, response.errorMsg))
        }
    }

    suspend fun getUserOrderList(): Flow<DataState<List<String>?>> = flow {
        emit(DataState.Loading)
        val response = invoke { apiServices.getUserOrderList() }
        when (response.statusCode) {
            200 -> emit(DataState.Success(response.data, response.statusCode))
            else -> emit(DataState.Error(response.statusCode, null, response.errorMsg))
        }
    }

    suspend fun getWeekelyMenu(): Flow<DataState<WeekelyMenuResponse?>> = flow {
        emit(DataState.Loading)
        val response = invoke { apiServices.getWeekelyMenu() }
        when (response.statusCode) {
            200 -> {
                sharedPrefManager.addModelClass(Constants.MENU, response.data)
                emit(DataState.Success(response.data, response.statusCode))
            }
            else -> {
                emit(DataState.Error(response.statusCode, null, response.errorMsg))}
        }
    }



    suspend fun updateWeekelyMenuFromWorker(){
        val response = invoke { apiServices.getWeekelyMenu() }
        when (response.statusCode) {
            200 -> {
                sharedPrefManager.addModelClass(Constants.MENU, response.data)
            }
        }
    }
}