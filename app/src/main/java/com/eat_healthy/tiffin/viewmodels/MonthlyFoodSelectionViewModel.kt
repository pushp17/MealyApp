package com.eat_healthy.tiffin.viewmodels

import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.eat_healthy.tiffin.R
import com.eat_healthy.tiffin.genericFiles.ListItem
import com.eat_healthy.tiffin.models.*
import com.eat_healthy.tiffin.repository.MonthlyFoodSelectionRepository
import com.eat_healthy.tiffin.utils.Constants
import com.eat_healthy.tiffin.utils.DataAndTimeUtils.getCurrentDate
import com.eat_healthy.tiffin.utils.DataAndTimeUtils.getDayOfTheWeek
import com.eat_healthy.tiffin.utils.DataAndTimeUtils.getNoOfDaysBetweenTwoDates
import com.eat_healthy.tiffin.utils.DataState
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MonthlyFoodSelectionViewModel @Inject constructor(
    private val monthlyFoodSelectionRepository:
    MonthlyFoodSelectionRepository, private val firebaseAnalytics: FirebaseAnalytics,
) : ViewModel() {
    val weeklyFoodPreferenceList= mutableListOf<UserWeeklyFoodPreference>()
    var screenEntryPoint:String?=null
    val weeklyMealTypeList = mutableListOf<WeekelyMealType>()
    val myFavouriteMeal = MyFavouriteMeal()
    val sabjiList = mutableListOf<Sabji>()
    var vegPrice=60
    var vegSpecialPrice=80
    var nonVegPrice=100
    var cbOnlyRoti:Boolean?=null
    var cbRiceRoti:Boolean?=null

    var totalPriceInWeek=0
    var monthlySubscriptionPrice: Int = 0
    var latestMonthlySubscriptionPrice:Int=0
    var lastMonthlySubscriptionPrice:Int=0
    var subcriptionStartDate : String? =null
    var subscriptionIntermediateDate : String? =null
    var subscriptionEndDate : String? =null

    var nofWeeksPassed = 0
    var noOfUpcomingWeeks = 0
    val daysInPastWeeks = mutableListOf<String>()
    val daysInUpcomingWeeks = mutableListOf<String>()
    var noOfDaysBetweenTodayToSubscriptionEndDate = 0

    var walletMoney=0
    var moneyToBePaid=0
    var grandTotalPrice:String?=null


    private var _monthlyUserPreferenceLivedata: MutableLiveData<DataState<MonthlyUserPreferenceResponse?>> = MutableLiveData()
    val monthlyUserPreferenceLivedata: LiveData<DataState<MonthlyUserPreferenceResponse?>>
        get() = _monthlyUserPreferenceLivedata

    private var _apiResponseLivedata: MutableLiveData<DataState<ApiResponse?>> = MutableLiveData()
    val apiResponseLivedata: LiveData<DataState<ApiResponse?>>
        get() = _apiResponseLivedata

    init {
        weeklyMealTypeList.add(
            WeekelyMealType(
                "MONDAY",
                "lunchVeg", "lunchVegSpecial",
                "lunchNonVeg",
                "dinnerVeg", "dinnerVegSpecial",
                "dinnerNonVeg"
            )
        )
        weeklyMealTypeList.add(
            WeekelyMealType("TUESDAY",
                "lunchVeg", "lunchVegSpecial",
                "lunchNonVeg",
                "dinnerVeg", "dinnerVegSpecial",
                "dinnerNonVeg"
            )
        )

        weeklyMealTypeList.add(
            WeekelyMealType("WEDNESDAY",
                "lunchVeg", "lunchVegSpecial",
                "lunchNonVeg",
                "dinnerVeg", "dinnerVegSpecial",
                "dinnerNonVeg"
            )
        )

        weeklyMealTypeList.add(
            WeekelyMealType("THURSDAY",
                "lunchVeg", "lunchVegSpecial",
                "lunchNonVeg",
                "dinnerVeg", "dinnerVegSpecial",
                "dinnerNonVeg"
            )
        )

        weeklyMealTypeList.add(
            WeekelyMealType("FRIDAY",
                "lunchVeg", "lunchVegSpecial",
                "lunchNonVeg",
                "dinnerVeg", "dinnerVegSpecial",
                "dinnerNonVeg"
            )
        )

        weeklyMealTypeList.add(
            WeekelyMealType("SATURDAY",
                "lunchVeg", "lunchVegSpecial",
                "lunchNonVeg",
                "dinnerVeg", "dinnerVegSpecial",
                "dinnerNonVeg"
            )
        )

        weeklyMealTypeList.add(
            WeekelyMealType("SUNDAY",
                "lunchVeg", "lunchVegSpecial",
                "lunchNonVeg",
                "dinnerVeg", "dinnerVegSpecial",
                "dinnerNonVeg"
            )
        )
    }

    fun getMonthlyUserPreference(user:User, sharedViewModel: SharedViewModel) {
        viewModelScope.launch {
            monthlyFoodSelectionRepository.fetchMonthlyUserPreference(user).onEach { dataState ->
               if(dataState.statusCode==200 && dataState.data?.statusCode==200L){
                   dataState.data.let {item->

                       // Weekely Mealy category update

                       noOfDaysBetweenTodayToSubscriptionEndDate = getNoOfDaysBetweenTwoDates(
                           null,
                           item.monthlyUserPreference?.subscriptionEndDate,
                           1
                       )
                       if (noOfDaysBetweenTodayToSubscriptionEndDate > 0) {
                           sharedViewModel.subscriptionExpired = false
                           walletMoney=0
                           moneyToBePaid=0
                           monthlySubscriptionPrice = item.monthlyUserPreference?.monthlySubscriptionPrice?.toIntOrNull()?:0
                           latestMonthlySubscriptionPrice = item.monthlyUserPreference?.latestMonthlySubscriptionPrice?.toIntOrNull()?:0
                           lastMonthlySubscriptionPrice = item.monthlyUserPreference?.latestMonthlySubscriptionPrice?.toIntOrNull()?:0
                           totalPriceInWeek = item.monthlyUserPreference?.totalPriceInWeek?.toIntOrNull() ?: 0
                           grandTotalPrice = item.monthlyUserPreference?.grandTotalPrice
                           subcriptionStartDate = item.monthlyUserPreference?.subscriptionStartDate
                           subscriptionIntermediateDate = item.monthlyUserPreference?.subscriptionIntermediateDate
                           subscriptionEndDate = item.monthlyUserPreference?.subscriptionEndDate

                           weeklyMealTypeList.forEachIndexed { index, weekelyMealType ->
                               updatePreference(
                                   item.monthlyUserPreference?.userWeeklyFoodPreference?.getOrNull(index)?.lunch,
                                   item.monthlyUserPreference?.userWeeklyFoodPreference?.getOrNull(index)?.dinner,
                                   weekelyMealType
                               )
                           }

                           // User Preferred sabji update
                           cbOnlyRoti =  item.monthlyUserPreference?.myFavouriteMeal?.onlyRoti
                           cbRiceRoti =  item.monthlyUserPreference?.myFavouriteMeal?.riceAndRoti
                           val sabjiHashMap=HashMap<String,Sabji>()
                           item.monthlyUserPreference?.myFavouriteMeal?.preferredSabjiList?.forEach {
                               sabjiHashMap.put(it.itemID!!,it)
                           }
                           sabjiList.forEachIndexed { index, sabji ->
                               if(sabjiHashMap.contains(sabji.itemID)){
                                   sabji.selected=true
                               }
                           }
                       } else {
                           sharedViewModel.subscriptionExpired = true
                           latestMonthlySubscriptionPrice = item.monthlyUserPreference?.latestMonthlySubscriptionPrice?.toIntOrNull()?:0
                           weeklyMealTypeList.forEachIndexed { index, weekelyMealType ->
                               updatePreference(
                                   item.monthlyUserPreference?.userWeeklyFoodPreference?.getOrNull(index)?.lunch,
                                   item.monthlyUserPreference?.userWeeklyFoodPreference?.getOrNull(index)?.dinner,
                                   weekelyMealType
                               )
                           }

                           // User Preferred sabji update
                           cbOnlyRoti =  item.monthlyUserPreference?.myFavouriteMeal?.onlyRoti
                           cbRiceRoti =  item.monthlyUserPreference?.myFavouriteMeal?.riceAndRoti
                           val sabjiHashMap=HashMap<String,Sabji>()
                           item.monthlyUserPreference?.myFavouriteMeal?.preferredSabjiList?.forEach {
                               sabjiHashMap.put(it.itemID!!,it)
                           }
                           sabjiList.forEachIndexed { index, sabji ->
                               if(sabjiHashMap.contains(sabji.itemID)){
                                   sabji.selected=true
                               }
                           }
                       }
                           }
//                   sessionSubmitMutalbleLivedata.value= DataState.Default
               }
                _monthlyUserPreferenceLivedata.value = dataState
            }.launchIn(viewModelScope)
        }
    }

    fun setPreference(id: String?,it:WeekelyMealType){
        when(id){
            "lunchVeg" -> {
                it.lunchVegSelected = !it.lunchVegSelected
                it.lunchVegSpecialSelected = false
                it.lunchNonVegSelected = false
            }
            "lunchVegSpecial" -> {
                it.lunchVegSelected = false
                it.lunchVegSpecialSelected = !it.lunchVegSpecialSelected
                it.lunchNonVegSelected = false
            }
            "lunchNonVeg" -> {
                it.lunchVegSelected = false
                it.lunchVegSpecialSelected = false
                it.lunchNonVegSelected = !it.lunchNonVegSelected
            }
            "dinnerVeg" -> {
                it.dinnerVegSelected = !it.dinnerVegSelected
                it.dinnerVegSpecialSelected = false
                it.dinnerNonVegSelected = false
            }
            "dinnerVegSpecial" -> {
                it.dinnerVegSelected = false
                it.dinnerVegSpecialSelected = !it.dinnerVegSpecialSelected
                it.dinnerNonVegSelected = false
            }
            "dinnerNonVeg" -> {
                it.dinnerVegSelected = false
                it.dinnerVegSpecialSelected = false
                it.dinnerNonVegSelected = !it.dinnerNonVegSelected
            }
        }
    }

    private fun updatePreference(lunchId: String?, dinnerId :String ?, it:WeekelyMealType){
        it.lunchVegSelected = false
        it.lunchVegSpecialSelected = false
        it.lunchNonVegSelected = false
        it.dinnerVegSelected = false
        it.dinnerVegSpecialSelected = false
        it.dinnerNonVegSelected = false

        when (lunchId) {
            "lunchVeg" -> {
                it.lunchVegSelected = true
            }
            "lunchVegSpecial" -> {
                it.lunchVegSpecialSelected = true
            }
            "lunchNonVeg" -> {
                it.lunchNonVegSelected = true
            }
        }

        when (dinnerId) {
            "dinnerVeg" -> {
                it.dinnerVegSelected = true
            }
            "dinnerVegSpecial" -> {
                it.dinnerVegSpecialSelected = true
            }
            "dinnerNonVeg" -> {
                it.dinnerNonVegSelected = true
            }
        }
    }

    fun addWeeklyPreference():List<UserWeeklyFoodPreference>?{
       // monthlySubscriptionPrice=
        weeklyFoodPreferenceList.clear()
        val weekArrayList= arrayListOf("Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday")
        weeklyMealTypeList.forEachIndexed { index, it ->
            val userWeeklyFoodPreference=UserWeeklyFoodPreference()
            userWeeklyFoodPreference.day=weekArrayList.get(index)
            userWeeklyFoodPreference.lunch="NONE"
            userWeeklyFoodPreference.dinner="NONE"
            if(it.lunchVegSelected) {
                userWeeklyFoodPreference.lunch=it.lunchVegId
            }
            if(it.lunchVegSpecialSelected) {
                userWeeklyFoodPreference.lunch= it.lunchVegSpecialId
            }
            if(it.lunchNonVegSelected) {
                userWeeklyFoodPreference.lunch= it.lunchNonVeglId
            }
            if(it.dinnerVegSelected){
                userWeeklyFoodPreference.dinner=it.dinnerVegId
            }
            if(it.dinnerVegSpecialSelected){
                userWeeklyFoodPreference.dinner=it.dinnerVegSpecialId
            }
            if(it.dinnerNonVegSelected){
                userWeeklyFoodPreference.dinner=it.dinnerNonVegId
            }
            weeklyFoodPreferenceList.add(userWeeklyFoodPreference)
        }
        return weeklyFoodPreferenceList
    }

    fun updateThePrice(list: List<ListItem>) {
        totalPriceInWeek=0
        latestMonthlySubscriptionPrice=0

        // This will give us the total price in a week
        list.forEach {
            if (it is WeekelyMealType) {
                if(it.lunchVegSelected) {
                    totalPriceInWeek += vegPrice
                }
                if(it.lunchVegSpecialSelected) {
                    totalPriceInWeek += vegSpecialPrice
                }
                if(it.lunchNonVegSelected) {
                    totalPriceInWeek += nonVegPrice
                }
                if(it.dinnerVegSelected){
                    totalPriceInWeek += vegPrice
                }
                if(it.dinnerVegSpecialSelected){
                    totalPriceInWeek += vegSpecialPrice
                }
                if(it.dinnerNonVegSelected){
                    totalPriceInWeek += nonVegPrice
                }
            }
        }

        // This will give us the price in odd number of days
        daysInUpcomingWeeks.forEach { weekDay->
            weeklyMealTypeList.forEach {
                if(weekDay.equals(it.day,true)){
                    if(it.lunchVegSelected) {
                        latestMonthlySubscriptionPrice += vegPrice
                    }
                    if(it.lunchVegSpecialSelected) {
                        latestMonthlySubscriptionPrice += vegSpecialPrice
                    }
                    if(it.lunchNonVegSelected) {
                        latestMonthlySubscriptionPrice += nonVegPrice
                    }
                    if(it.dinnerVegSelected){
                        latestMonthlySubscriptionPrice += vegPrice
                    }
                    if(it.dinnerVegSpecialSelected){
                        latestMonthlySubscriptionPrice += vegSpecialPrice
                    }
                    if(it.dinnerNonVegSelected){
                        latestMonthlySubscriptionPrice += nonVegPrice
                    }
                }
            }
        }
        latestMonthlySubscriptionPrice += noOfUpcomingWeeks * totalPriceInWeek
    }

    fun subscriptionMoneySpentInPastWeeks():Int{
        var totalPriceSpent=totalPriceInWeek*nofWeeksPassed
        daysInPastWeeks.forEach { weekDay->
            weeklyMealTypeList.forEach {
                if(weekDay.equals(it.day,true)){
                    if(it.lunchVegSelected) {
                        totalPriceSpent += vegPrice
                    }
                    if(it.lunchVegSpecialSelected) {
                        totalPriceSpent += vegSpecialPrice
                    }
                    if(it.lunchNonVegSelected) {
                        totalPriceSpent += nonVegPrice
                    }
                    if(it.dinnerVegSelected){
                        totalPriceSpent += vegPrice
                    }
                    if(it.dinnerVegSpecialSelected){
                        totalPriceSpent += vegSpecialPrice
                    }
                    if(it.dinnerNonVegSelected){
                        totalPriceSpent += nonVegPrice
                    }
                }
            }
        }
        return totalPriceSpent
    }

    // This function is used to calulate the Money expenses in odd number of day , for instance in 18 days , 4 is odd number of days.

    fun weekdaysInUpcomingAndPastWeeks(startDate:String , endDate:String){
       val noOfDaysBetweenSubscriptionDateToToday  = getNoOfDaysBetweenTwoDates(startDate,null,0)
        nofWeeksPassed = noOfDaysBetweenSubscriptionDateToToday/7
        val nofOddDaysInPassedWeeks = noOfDaysBetweenSubscriptionDateToToday % 7
        if (nofOddDaysInPassedWeeks > 0) {
            for (i in 0 until nofOddDaysInPassedWeeks) {
                daysInPastWeeks.add(
                    getDayOfTheWeek(
                        startDate,
                        noOfDaysBetweenSubscriptionDateToToday - i
                    )
                )
            }
        }

        noOfDaysBetweenTodayToSubscriptionEndDate = getNoOfDaysBetweenTwoDates(null,endDate,1)
        noOfUpcomingWeeks = noOfDaysBetweenTodayToSubscriptionEndDate /7
        val noOfOddDaysInUpcomingWeeks = noOfDaysBetweenTodayToSubscriptionEndDate % 7
        if (noOfOddDaysInUpcomingWeeks > 0) {
            for (i in 0 until noOfOddDaysInUpcomingWeeks) {
                daysInUpcomingWeeks.add(getDayOfTheWeek(endDate, -i))
            }
        }
    }

    fun reDirection(navigationController:NavController?,sharedViewModel:SharedViewModel){
        if(subcriptionStartDate == null){
            monthlySubscriptionPrice = latestMonthlySubscriptionPrice
        } else {
            subscriptionIntermediateDate = getCurrentDate()
        }
        val monthlyUserPreference = MonthlyUserPreference(
            username = null,
            mobileno = null,
            monthlySubscriptionPrice = monthlySubscriptionPrice.toString(),
            latestMonthlySubscriptionPrice = latestMonthlySubscriptionPrice.toString(),
            grandTotalPrice = grandTotalPrice,
            latestGrandTotalPrice = null,
            totalPriceInWeek = totalPriceInWeek.toString(),
            subscriptionStartDate = subcriptionStartDate,
            subscriptionIntermediateDate = subscriptionIntermediateDate,
            subscriptionEndDate = subscriptionEndDate,
            myFavouriteMeal = myFavouriteMeal,
            userWeeklyFoodPreference = weeklyFoodPreferenceList
        )
        sharedViewModel.monthlyUserPreference=monthlyUserPreference

        if (screenEntryPoint?.equals(Constants.HOME_PAGE_TAB) == true) {
            firebaseAnalytics.logEvent(Constants.MONTHLY_FOOD_SELECTION_PAGE_FROM_HOME,null)
            if (sharedViewModel.userDetail?.isUserSignedIn == true) {
                navigationController?.navigate(R.id.action_foodSelectionFragment_to_completeAddressFragment)
            } else {
                val bundle = bundleOf(Constants.SCREEN_ENTRY_POINT to screenEntryPoint)
                navigationController?.navigate(R.id.action_foodSelectionFragment_to_loginFragment, bundle
                )
            }
        } else {
            firebaseAnalytics.logEvent(Constants.MONTHLY_FOOD_SELECTION_PAGE,null)
            if (sharedViewModel.userDetail?.isUserSignedIn == true) {
                navigationController?.navigate(R.id.action_monthlyFoodSelectionFragment_to_completeAddressFragment)
            } else {
                val bundle = bundleOf(Constants.SCREEN_ENTRY_POINT to screenEntryPoint)
                navigationController?.navigate(R.id.action_monthlyFoodSelectionFragment_to_loginFragment, bundle
                )
            }
        }
    }
}