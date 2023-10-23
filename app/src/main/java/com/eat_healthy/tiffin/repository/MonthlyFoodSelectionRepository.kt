package com.eat_healthy.tiffin.repository

import com.eat_healthy.tiffin.models.*
import com.eat_healthy.tiffin.utils.BaseDataSource
import com.eat_healthy.tiffin.utils.DataState
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MonthlyFoodSelectionRepository @Inject constructor(val apiServices: ApiServices) : BaseDataSource() {
    suspend fun fetchMonthlyUserPreference(user: User): Flow<DataState<MonthlyUserPreferenceResponse?>> = flow {
        emit(DataState.Loading)
        val response = invoke { apiServices.getMonthlyUserPreference(user) }
        when (response.statusCode) {
            200 -> emit(DataState.Success(response.data, response.statusCode))
            else -> emit(DataState.Error(response.statusCode, null, response.errorMsg))
        }
    }

    suspend fun monthlyUserOrderAndPreference(monthlyUserPreference: MonthlyUserPreference): Flow<DataState<ApiResponse?>> = flow {
        emit(DataState.Loading)
        val response = invoke { apiServices.updateMonthlyUserPreference(monthlyUserPreference) }
        when (response.statusCode) {
            200 -> emit(DataState.Success(response.data, response.statusCode))
            else -> emit(DataState.Error(response.statusCode, null, response.errorMsg))
        }
    }
}
