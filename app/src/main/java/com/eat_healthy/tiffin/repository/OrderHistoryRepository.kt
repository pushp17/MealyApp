package com.eat_healthy.tiffin.repository

import com.eat_healthy.tiffin.models.SingleMealUserOrderHistoryResponse
import com.eat_healthy.tiffin.models.User
import com.eat_healthy.tiffin.utils.BaseDataSource
import com.eat_healthy.tiffin.utils.DataState
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ActivityRetainedScoped
class OrderHistoryRepository @Inject constructor(val apiServices: ApiServices) : BaseDataSource() {
    suspend fun singleUserOrdersHistory(user: User): Flow<DataState<SingleMealUserOrderHistoryResponse?>> = flow {
        emit(DataState.Loading)
        val response = invoke { apiServices.singleUserOrdersHistory(user) }
        when (response.statusCode) {
            200 -> emit(DataState.Success(response.data, response.statusCode))
            else -> emit(DataState.Error(response.statusCode, null, response.errorMsg))
        }
    }
}