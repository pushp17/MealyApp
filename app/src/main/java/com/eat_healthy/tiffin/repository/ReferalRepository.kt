package com.eat_healthy.tiffin.repository

import com.eat_healthy.tiffin.models.ReferalResponse
import com.eat_healthy.tiffin.models.RefrerReferalHistoryResponse
import com.eat_healthy.tiffin.models.User
import com.eat_healthy.tiffin.utils.BaseDataSource
import com.eat_healthy.tiffin.utils.DataState
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ActivityRetainedScoped
class ReferalRepository @Inject constructor(val apiServices: ApiServices) : BaseDataSource() {
    suspend fun singleReferalUserDataResponse(user: User): Flow<DataState<ReferalResponse?>> = flow {
        emit(DataState.Loading)
        val response = invoke { apiServices.singleReferalUserDataResponse(user) }
        when (response.statusCode) {
            200 -> emit(DataState.Success(response.data, response.statusCode))
            else -> emit(DataState.Error(401, null, response.errorMsg))
        }
    }

    suspend fun getRefrerReferalHistory(user: User): Flow<DataState<RefrerReferalHistoryResponse?>> = flow {
        emit(DataState.Loading)
        val response = invoke { apiServices.refrerReferalHistoryResponse(user) }
        when (response.statusCode) {
            200 -> emit(DataState.Success(response.data, response.statusCode))
            else -> emit(DataState.Error(401, null, response.errorMsg))
        }
    }
}
