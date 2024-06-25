package com.eat_healthy.tiffin.repository

import com.eat_healthy.tiffin.utils.BaseDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class MealsDataSource
@Inject
constructor(val apiServices: ApiServices) : BaseDataSource() {
    suspend fun getMealsApiResponse(name:String?) = invoke { apiServices.getMealsResponseV2(name) }
}