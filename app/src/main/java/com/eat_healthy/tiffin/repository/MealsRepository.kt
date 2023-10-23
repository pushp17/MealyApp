package com.eat_healthy.tiffin.repository

import com.eat_healthy.tiffin.models.MealsApiRespone
import com.eat_healthy.tiffin.utils.DataState
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ActivityRetainedScoped
class MealsRepository
    @Inject
    constructor(val mealsDataSource: MealsDataSource) {
        suspend fun getMealsData(): Flow<DataState<MealsApiRespone?>> = flow {
            emit(DataState.Loading)
            val response= mealsDataSource.getMealsApiResponse()
            when (response.statusCode) {
                200 -> emit(DataState.Success(response.data, response.statusCode))
                else -> emit(DataState.Error(response.statusCode, null,response.errorMsg))
            }
        }
    }
