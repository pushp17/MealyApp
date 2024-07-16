package com.eat_healthy.tiffin.repository

import com.eat_healthy.tiffin.models.DeliveryDetailsResponse
import com.eat_healthy.tiffin.utils.BaseDataSource
import com.eat_healthy.tiffin.utils.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeliveryRepository @Inject constructor(val apiServices: ApiServices) : BaseDataSource() {
    suspend fun getTodaysDeliveryData(string: String): Flow<DataState<DeliveryDetailsResponse?>> =
        flow {
            emit(DataState.Loading)
            val response = invoke { apiServices.serviceforTodaysOrderList(string) }
            when (response.statusCode) {
                200 -> emit(DataState.Success(response.data, response.statusCode))
                else -> emit(DataState.Error(response.statusCode, null, response.errorMsg))
            }
        }
}
