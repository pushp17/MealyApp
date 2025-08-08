package com.eat_healthy.tiffin.repository

import com.eat_healthy.tiffin.models.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiServices {
    @GET("/tiffin/homepage")
    suspend fun getMealsApiResponse(): Response<MealsApiRespone>

    //"https://c47d6e09-cd65-45ac-aff4-bc4142274e39.mock.pstmn.io/mealyhomepage"
    @GET("tiffin/homepageV2")
    suspend fun getMealsResponseV2(@Query("username") user: String? = null): Response<MealsApiRespone>

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
    suspend fun singleMealUserOrder(@Body singleMealUserOrderDetail:SingleMealUserOrderDetail): Response<OrderPlaceResponse>

    @POST("/tiffin/updateAddress")
    suspend fun updateUserAddress(@Body mUserAddress: MUserAddress): Response<ApiResponse>

    @POST("/tiffin/singleUserOrdersHistory")
    suspend fun singleUserOrdersHistory(@Body user:User): Response<SingleMealUserOrderHistoryResponse>

    @POST("/tiffin/referalCode")
    suspend fun singleReferalUserDataResponse(@Body user:User): Response<ReferalResponse>

    @POST("/tiffin/userDetails")
    suspend fun getUserDetails(@Body user:User): Response<UserDetailResponse>

    @POST("/tiffin/refrerRefralHistory")
    suspend fun refrerReferalHistoryResponse(@Body user:User): Response<RefrerReferalHistoryResponse>

    @GET("/tiffin/userOrderList")
    suspend fun getUserOrderList(): Response<List<String>>

    @POST("/tiffin/foodReview")
     suspend fun postFoodReview(@Body foodReview: FoodReview): Response<ApiResponse>

  @GET("/tiffin/feedbackquestion")
  suspend fun getsuggestionQuestions(): Response<UsersSuggestionQuestions>

  @POST("/tiffin/postSuggestionQuestionsAnswers")
  suspend fun postSuggestionQuestionAnswers(@Body usersSuggestionUpload: UsersSuggestionUpload): Response<ApiResponse>

   // "/tiffin/weekelyMenu"
    @GET("/tiffin/weekelyMenu")
    suspend fun getWeekelyMenu(): Response<WeekelyMenuResponse>

    @GET("/tiffin/getFoodReview")
    suspend fun getFoodReview(): Response<List<FoodReview>>

    @POST("/tiffin/todayOrder")
    suspend fun serviceforTodaysOrderList(@Body dateAndTime:String): Response<DeliveryDetailsResponse>

    @POST("/tiffin/updateOrderStatus")
    suspend fun updateOrderStatus(@Body orderId: String): Response<Unit>
}