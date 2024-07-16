package com.eat_healthy.tiffin.repository

import com.eat_healthy.tiffin.models.ApiResponse
import com.eat_healthy.tiffin.models.FoodReview
import com.eat_healthy.tiffin.models.UsersSuggestionQuestions
import com.eat_healthy.tiffin.models.UsersSuggestionUpload
import com.eat_healthy.tiffin.utils.BaseDataSource
import com.eat_healthy.tiffin.utils.DataState
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ActivityRetainedScoped
class FoodReviewOrSuggestionRepository @Inject constructor(val apiServices: ApiServices) : BaseDataSource() {
    suspend fun postFoodReviewDataResponse(foodReview: FoodReview): Flow<DataState<ApiResponse?>> =
        flow {
            emit(DataState.Loading)
            val response = invoke { apiServices.postFoodReview(foodReview) }
            when (response.statusCode) {
                200 -> emit(DataState.Success(response.data, response.statusCode))
                else -> emit(DataState.Error(401, null, response.errorMsg))
            }
        }

    suspend fun getSuggestionQuestionDataResponse(): Flow<DataState<UsersSuggestionQuestions?>> =
        flow {
            emit(DataState.Loading)
            val response = invoke { apiServices.getsuggestionQuestions() }
            when (response.statusCode) {
                200 -> emit(DataState.Success(response.data, response.statusCode))
                else -> emit(DataState.Error(401, null, response.errorMsg))
            }
        }

    suspend fun postUsersSuggestionDataResponse(usersSuggestionUpload: UsersSuggestionUpload): Flow<DataState<ApiResponse?>> =
        flow {
            emit(DataState.Loading)
            val response = invoke { apiServices.postSuggestionQuestionAnswers(usersSuggestionUpload) }
            when (response.statusCode) {
                200 -> emit(DataState.Success(response.data, response.statusCode))
                else -> emit(DataState.Error(401, null, response.errorMsg))
            }
        }

    suspend fun getFoodReviewResponse(): Flow<DataState<List<FoodReview>?>> =
        flow {
            emit(DataState.Loading)
            val response = invoke { apiServices.getFoodReview() }
            when (response.statusCode) {
                200 -> emit(DataState.Success(response.data, response.statusCode))
                else -> emit(DataState.Error(401, null, response.errorMsg))
            }
        }
}