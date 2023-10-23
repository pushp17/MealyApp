package com.eat_healthy.tiffin.repository

import com.eat_healthy.tiffin.models.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiServices {
    @GET("/tiffin/homepage")
    suspend fun getMealsApiResponse(): Response<MealsApiRespone>

    @GET("/tiffin/availableLocalityList")
    suspend fun getAvailableLocalityResponse(): Response<AvailableLocalityResponse>

    @POST("/tiffin/userAddress")
    suspend fun getUserAddresses(@Body user:User): Response<UserAddressResponse>

    @POST("/tiffin/login")
    suspend fun login(@Body login:User): Response<LoginResponse>

    @POST("/tiffin/OtpVerification")
    suspend fun otpVerification(@Body otp:MOtp): Response<OtpVerification>

    @POST("/tiffin/getMonthlyUserPreference")
    suspend fun getMonthlyUserPreference(@Body user:User): Response<MonthlyUserPreferenceResponse>

    @POST("/tiffin/updateMonthlyUserPreference")
    suspend fun updateMonthlyUserPreference(@Body monthlyUserPreference:MonthlyUserPreference): Response<ApiResponse>

    @POST("/tiffin/singleMealUserOrder")
    suspend fun singleMealUserOrder(@Body singleMealUserOrderDetail:SingleMealUserOrderDetail): Response<ApiResponse>

    @POST("/tiffin/updateAddress")
    suspend fun updateUserAddress(@Body mUserAddress: MUserAddress): Response<ApiResponse>

    @POST("/tiffin/singleUserOrdersHistory")
    suspend fun singleUserOrdersHistory(@Body user:User): Response<SingleMealUserOrderHistoryResponse>

    @POST("/tiffin/todaysOrder")
    suspend fun singleDeliveryDetailsResponse(@Body dateAndTime:String): Response<DeliveryDetailsResponse>
}