package com.eat_healthy.tiffin.repository

import com.eat_healthy.tiffin.models.*
import com.eat_healthy.tiffin.utils.BaseDataSource
import com.eat_healthy.tiffin.utils.DataState
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ActivityRetainedScoped
class UserAddressRepository
@Inject constructor(val apiServices: ApiServices) : BaseDataSource() {

    suspend fun getAvailableLocalitiesData(): Flow<DataState<AvailableLocalityResponse?>> = flow {
        emit(DataState.Loading)
        val response = invoke { apiServices.getAvailableLocalityResponse() }
        when (response.statusCode) {
            200 -> emit(DataState.Success(response.data, response.statusCode))
            else -> emit(DataState.Error(response.statusCode, null, response.errorMsg))
        }
    }

    suspend fun getUserAddress(user: User): Flow<DataState<UserAddressResponse?>> = flow {
        emit(DataState.Loading)
        val response = invoke { apiServices.getUserAddresses(user) }
        when (response.statusCode) {
            200 -> emit(DataState.Success(response.data, response.statusCode))
            else -> emit(DataState.Error(response.statusCode, null, response.errorMsg))
        }
    }

    suspend fun updateUserAddress(mUserAddress: MUserAddress): Flow<DataState<ApiResponse?>> = flow {
        emit(DataState.Loading)
        val response = invoke { apiServices.updateUserAddress(mUserAddress) }
        when (response.statusCode) {
            200 -> emit(DataState.Success(response.data, response.statusCode))
            else -> emit(DataState.Error(response.statusCode, null, response.errorMsg))
        }
    }
}
