package com.eat_healthy.tiffin.repository

import com.eat_healthy.tiffin.models.ApiResponse
import com.eat_healthy.tiffin.models.MonthlyUserPreferenceResponse
import com.eat_healthy.tiffin.models.SingleMealUserOrderDetail
import com.eat_healthy.tiffin.models.User
import com.eat_healthy.tiffin.utils.BaseDataSource
import com.eat_healthy.tiffin.utils.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SingleUserOrderRepository @Inject constructor(val apiServices: ApiServices) : BaseDataSource() {
    suspend fun singleUserOrder(singleMealUserOrderDetail: SingleMealUserOrderDetail): Flow<DataState<ApiResponse?>> = flow {
        emit(DataState.Loading)
        val response = invoke { apiServices.singleMealUserOrder(singleMealUserOrderDetail) }
        when (response.statusCode) {
            200 -> emit(DataState.Success(response.data, response.statusCode))
            else -> emit(DataState.Error(response.statusCode, null, response.errorMsg))
        }
    }
}