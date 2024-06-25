package com.eat_healthy.tiffin.viewmodels

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import com.eat_healthy.tiffin.genericFiles.ListItem
import com.eat_healthy.tiffin.models.*
import com.eat_healthy.tiffin.repository.LoginRepository
import com.eat_healthy.tiffin.repository.MealsRepository
import com.eat_healthy.tiffin.repository.ReferalRepository
import com.eat_healthy.tiffin.utils.Constants
import com.eat_healthy.tiffin.utils.DateAndTimeUtils
import com.eat_healthy.tiffin.utils.DataState
import com.eat_healthy.tiffin.utils.SharedPrefManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel
@Inject constructor(application: Application, private val mealsRepository: MealsRepository,
                    val referalRepository: ReferalRepository,
                    val loginRepository: LoginRepository
                    ) :
    AndroidViewModel(application) {
    val mealMutableList= mutableListOf<Meal>()
    val silverMealMutableList= mutableListOf<ListItem>()
    val nonvegMealMutableList= mutableListOf<ListItem>()
    val highlightedMutableList = mutableListOf<HomeHighLightedItems>()
    val homepageMutableList = mutableListOf<ListItem>()
    val extrasMutableList = mutableListOf<ExtrasV2>()
    val adapterList = mutableListOf<ListItem>()
    var monthlyUserPreference:MonthlyUserPreference?=null
    var enableLunchTime = false
    var enableDinnerTime = false
    var monthlyUser: Boolean? = null
    var userDetail: UserDetail? = null
    var noOfItemAddedInCart = 0
    val cartItemList by lazy { mutableListOf<ItemsInCart>()}
    val deliverySlotTimming = mutableListOf<Header>()
    var totalPrice = 0
    var totalPriceForMonthlyUser=0
    var regularMealPrice="59"
    var vegSpecialPrice="79"
    var nonVegPrice="89"
    var defaultNormalSabjiImageUrl:String?=null
    var defaultVegSpecialImageUrl:String?=null
    var defaultNonVegImageUrl:String?=null
    var showlandingpageAsRegister:Boolean?=false
    var foodReviewIsShown: Boolean = false

    var statusMsg: String? = null
    var monthlySubscriptionMsg: String? = null
    var afterStartOrderTimeout: String? = null
    var aStartSingleMealStatusLunchTime: String? = null
    var afterStartSingleMealStatusDinnerTime: String? = null
    var isServiceStarted: Boolean? = false
    var lunchOrDinnerTime = Constants.TIME_OUT
    var subscriptionExpired = false
    var deliveryPrice = "3"

     var sabji:String?=null
     var contact:String?=null
    var referalApiCalled = false
    var showStatusAtHomePage = false

    var lunchStartTime = ""
    var lunchEndTime = ""
    var dinnerStartTime = ""
    var dinnerEndTime = ""

    var apiResponse:MealsApiRespone?=null

    private val _referalResponseLiveData =
        MutableLiveData<DataState<ReferalResponse?>>()
    val referalResponseLiveData: LiveData<DataState<ReferalResponse?>> =
        _referalResponseLiveData

    private val _userDetailsLiveData =
        MutableLiveData<DataState<UserDetailResponse?>>()
    val userDetailsLiveData: LiveData<DataState<UserDetailResponse?>> =
        _userDetailsLiveData

    private val _weekelyMenuLiveData =
        MutableLiveData<DataState<WeekelyMenuResponse?>>()
    val weekelyMenuLiveData: LiveData<DataState<WeekelyMenuResponse?>> =
        _weekelyMenuLiveData

    private var mealsMutalbleLivedata: MutableLiveData<DataState<MealsApiRespone?>> = MutableLiveData()
    val mealsLivedata: LiveData<DataState<MealsApiRespone?>>
        get() = mealsMutalbleLivedata

    var referalRewardMaxLimitPerUser = 0
    var rewardPercentagePerOrder = 0.0

    var lunchWorkManagerInitiated = false
    var dinnerWorkManagerInitiated = false

    var returnTheLatestPassedTime: Long? = null


   fun homePageData(){
       if (apiResponse != null) {
           mealsMutalbleLivedata.value = mealsLivedata.value
           return
       }

       val name: String? = if (this.userDetail != null) {
           userDetail?.username
       } else {
           "anonymous"
       }
       viewModelScope.launch {
           mealsRepository.getMealsData(name).onEach { dataState ->
               mealsMutalbleLivedata.value = dataState
           }.launchIn(viewModelScope)
       }
    }

    fun frameDataForHomePageV2(mealsApiRespone: MealsApiRespone) {
        if (apiResponse != null) return
        this.apiResponse = mealsApiRespone
        mealMutableList.clear()
        homepageMutableList.clear()
        extrasMutableList.clear()
        highlightedMutableList.clear()
        deliverySlotTimming.clear()
        adapterList.clear()
        lunchStartTime = mealsApiRespone.lunchStartTime ?: ""
        lunchEndTime = mealsApiRespone.lunchEndTime ?: ""
        dinnerStartTime = mealsApiRespone.dinnerStartTime ?: ""
        dinnerEndTime = mealsApiRespone.dinnerEndTime ?: ""
        regularMealPrice = mealsApiRespone.regularMealPrice ?: "73"
        vegSpecialPrice = mealsApiRespone.vegSpecialPrice ?: "92"
        nonVegPrice = mealsApiRespone.nonVegPrice ?: "101"
        deliveryPrice = mealsApiRespone.deliveryCharge ?: "3"
        mealsApiRespone.deliveryTimeSlot?.forEach {
            deliverySlotTimming.add(Header(it))
        }
        apiResponse?.let {
            showStatusAtHomePage = it.showStatus == true
            when (DateAndTimeUtils.checkLunchOrDinnerTime(
                it.lunchStartTime!!,
                it.lunchEndTime!!,
                it.dinnerStartTime!!,
                it.dinnerEndTime!!
            )) {
                Constants.LUNCH -> {
                    enableLunchTime = true
                    enableDinnerTime = false
                    lunchOrDinnerTime = Constants.LUNCH
                }
                Constants.DINNER -> {
                    enableDinnerTime = true
                    enableLunchTime = false
                    lunchOrDinnerTime = Constants.DINNER
                }
                Constants.LUNCH_TIMEOUT -> {
                    lunchOrDinnerTime = Constants.LUNCH_TIMEOUT
                }
                else -> {
                    // Time out
                    lunchOrDinnerTime = Constants.TIME_OUT
                }
            }
        }

        mealsApiRespone.mealList?.let {
//            it.filter{ it.highlight }.forEachIndexed { index, curries ->
//                highlightedMutableList.add(
//                    HomeHighLightedItems(
//                        curries.name,
//                        curries.image,
//                        curries.info,
//                        curries.price,
//                        curries.showOffPrice,
//                        false,
//                        curries.pagerPosition
//                    )
//                )
//            }

            mealMutableList.addAll(it.filter {
                it.isLunchOrDinnerTime = lunchOrDinnerTime
                it.addButtonCountText = ObservableInt(0)
                checAvailability(it)
            })
        }
//
//        highlightedMutableList.sortBy {
//            it.pagerPosition
//        }

        mealMutableList.sortBy {
            it.position
        }

        mealsApiRespone.extrasV2?.let {
            extrasMutableList.addAll(
                it.map {
                    it.isLunchOrDinnerTime = lunchOrDinnerTime
                    it
                })
        }

        adapterList.addAll(mealMutableList.filter {it.showAsSingleItem})
        adapterList.add(Empty(""))
    }

    private fun checAvailability(item:ListItem):Boolean{
     return when(item){
           is SpecialMeal->{
               if (enableLunchTime) item.mealAvailability?.lunchToday ==true
               else item.mealAvailability?.dinnerToday == true
           }
         is Meal->{
             if (enableLunchTime) item.mealAvailability?.lunchToday ==true
             else item.mealAvailability?.dinnerToday == true
         }
           is Sabji->{
               if (enableLunchTime) item.mealAvailability?.lunchToday ==true
               else item.mealAvailability?.dinnerToday == true
           }
           is MainMeal->{
               if (enableLunchTime) item.mealAvailability?.lunchToday ==true
               else item.mealAvailability?.dinnerToday == true
           }
           else -> false
       }
    }

    fun fetchReferalData(user: User, sharedPrefManager: SharedPrefManager) {
        referalApiCalled = true
        viewModelScope.launch {
            referalRepository.singleReferalUserDataResponse(user).onEach { dataState ->
                _referalResponseLiveData.value = dataState
                if (dataState.statusCode == 200) {
                    sharedPrefManager.addModelClass(
                        Constants.REFERAL_USER_DATA,
                        dataState.data?.mRefrer
                    )
                } else if (dataState.statusCode == 401) {
                    referalApiCalled = true
                }
            }.launchIn(viewModelScope)
        }
    }

    fun getUserDetails(user: User, sharedPrefManager: SharedPrefManager) {
        viewModelScope.launch {
            loginRepository.getUserDetails(user).onEach { dataState ->
                _userDetailsLiveData.value = dataState
                if (dataState.statusCode == 200) {
                    userDetail = sharedPrefManager.getModelClass<UserDetail>(Constants.USER_INFO)
                    userDetail?.referalMoney = dataState.data?.user?.referalMoney
                    userDetail?.totalReferalMoneyReceived = dataState.data?.user?.totalReferalMoneyReceived
                    sharedPrefManager.addModelClass(Constants.USER_INFO, userDetail)
                }
            }.launchIn(viewModelScope)
        }
    }

    fun getWeekelyMenu() {
        if (weekelyMenuLiveData.value is DataState.Success) {
            _weekelyMenuLiveData.value = weekelyMenuLiveData.value
            return
        }
        viewModelScope.launch {
            loginRepository.getWeekelyMenu().onEach { dataState ->
                _weekelyMenuLiveData.value = dataState
            }.launchIn(viewModelScope)
        }
    }

    fun getUserOrderHistoryDetails() {
        viewModelScope.launch {
            loginRepository.getUserOrderList().onEach { dataState ->
                if (dataState.statusCode == 200) {
                    dataState.data?.let {
                        it.forEach { perUser->
                            Log.d("OrderHistory",perUser)
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}
