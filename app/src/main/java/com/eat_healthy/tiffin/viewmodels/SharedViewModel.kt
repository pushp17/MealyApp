package com.eat_healthy.tiffin.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.eat_healthy.tiffin.R
import com.eat_healthy.tiffin.genericFiles.ListItem
import com.eat_healthy.tiffin.models.*
import com.eat_healthy.tiffin.repository.MealsRepository
import com.eat_healthy.tiffin.utils.Constants
import com.eat_healthy.tiffin.utils.DataAndTimeUtils
import com.eat_healthy.tiffin.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel
@Inject constructor(application: Application, private val mealsRepository: MealsRepository) :
    AndroidViewModel(application) {
    private val viewModelContext=application
    val selectedMealCategoryMutableList = mutableListOf<ListItem>()
    val normalMealMutableList= mutableListOf<ListItem>()
    val silverMealMutableList= mutableListOf<ListItem>()
    val goldMealMutableList= mutableListOf<ListItem>()
    var monthlyUserPreference:MonthlyUserPreference?=null
    var enableLunchTime=false
    var enableDinnerTime=false
    var mealCategoryTabPosition=0
    var monthlyUser:Boolean?=null
    var userDetail:UserDetail?=null
    var noOfItemAddedInCart=0
    val cartItemList by lazy { mutableListOf<ItemsInCart>()}
    var totalPrice = 0
    var totalPriceForMonthlyUser=0
    var regularMealPrice="59"
    var vegSpecialPrice="79"
    var nonVegPrice="89"
    var selectedPrice="59"
    var noticeBoardShown=false
    var defaultNormalSabjiImageUrl:String?=null
    var defaultVegSpecialImageUrl:String?=null
    var defaultNonVegImageUrl:String?=null
    var showlandingpageAsRegister:Boolean?=false
    var doesShowlandingpageAsRegisterValueUpdated=false

    var statusMsg: String? = null
    var status2: String? = null
    var monthlySubscriptionMsg: String? = null
    var afterStartOrderTimeout: String? = null
    var aStartSingleMealStatusLunchTime: String? = null
    var afterStartSingleMealStatusDinnerTime: String? = null
    var afterRegistrationStatusMsg: String? = null
    var registerSuccessMsg: String? = null
    var isServiceStarted: Boolean? = false
    var lunchOrDinnerTime = Constants.TIME_OUT
    var subscriptionExpired = false

     var specialMealCategoryHeaderEnable=false
     var todaysMealCategoryHeaderEnable=true

    //special thali variables
     var specialMealSelected:Boolean=false
     var specialMealName:String?=null
     var todaySpecialMealPrice:String?=null
     var mealCategoryType:String?=null

    //normal thali variables
     var normalMealSelected:Boolean=false
     var mainMealName:String?=null
     var sabji:String?=null
     var dal:String?=null
     var extras:String?=null
     var contact:String?=null

    var apiResponse:MealsApiRespone?=null
    private var mealsMutalbleLivedata: MutableLiveData<DataState<MealsApiRespone?>> = MutableLiveData()
    val mealsLivedata: LiveData<DataState<MealsApiRespone?>>
        get() = mealsMutalbleLivedata
   fun homePageData(){
       if(apiResponse != null){
           mealsMutalbleLivedata.value = mealsLivedata.value
           return
       }
       viewModelScope.launch {
           mealsRepository.getMealsData().onEach { dataState ->
               mealsMutalbleLivedata.value = dataState
           }.launchIn(viewModelScope)
       }
    }
    fun frameDataForHomePage(mealsApiRespone: MealsApiRespone) {
        this.apiResponse=mealsApiRespone
        normalMealMutableList.clear()
        silverMealMutableList.clear()
        goldMealMutableList.clear()
        apiResponse?.let {
            when (DataAndTimeUtils.checkLunchOrDinnerTime(
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
                else -> {
                    // Time out
                    lunchOrDinnerTime = Constants.TIME_OUT
                }
            }
        }

        // This defaul image url is used to store default image of all category to use across the app
        if(defaultNormalSabjiImageUrl == null){
            run breaking@ {
                mealsApiRespone.sabjiList?.forEach {
                    when (it.itemType) {
                        "normal" -> {
                            defaultNormalSabjiImageUrl=it.itemImage
                        }
                        "silver" -> {
                            if(defaultVegSpecialImageUrl != null)return@forEach
                            defaultVegSpecialImageUrl=it.itemImage
                        }
                        "gold" -> {
                            defaultNonVegImageUrl=it.itemImage
                            return@breaking
                        }
                    }
                }
            }
        }
        // Normal Meal
        normalMealMutableList.add(Header(viewModelContext.getString(R.string.special_meal)))
        mealsApiRespone.specialMealList?.let {
            normalMealMutableList.addAll(it.filter {
                it.isLunchOrDinnerTime = lunchOrDinnerTime
                it.itemType.equals("normal")
            })
        }
        normalMealMutableList.add(Header(viewModelContext.getString(R.string.sabji)))
        mealsApiRespone.sabjiList?.let {
            normalMealMutableList.addAll(it.filter {
                it.isLunchOrDinnerTime = lunchOrDinnerTime
                it.itemType.equals("normal")
            })
        //    defaultNormalSabjiImageUrl =(normalMealMutableList.getOrNull(1) as Sabji).itemImage
        }
        normalMealMutableList.add(Header(viewModelContext.getString(R.string.main_meal)))
        mealsApiRespone.mainMealList?.let {
            normalMealMutableList.addAll(it.filter {
                it.isLunchOrDinnerTime = lunchOrDinnerTime
                it.itemType.equals("dal") ||  it.itemType.equals("rice_roti") ||  it.itemType.equals("extras")
            })
        }

        // Silver Meal
        silverMealMutableList.add(Header(viewModelContext.getString(R.string.special_meal)))
        mealsApiRespone.specialMealList?.let {
            silverMealMutableList.addAll(it.filter {
                it.itemType.equals("silver")
            })
        }
        silverMealMutableList.add(Header(viewModelContext.getString(R.string.sabji)))
        mealsApiRespone.sabjiList?.let {
            silverMealMutableList.addAll(it.filter {
                it.itemType.equals("silver")
            })
          //  defaultVegSpecialImageUrl =(silverMealMutableList.getOrNull(0) as Sabji).itemImage
        }

        silverMealMutableList.add(Header(viewModelContext.getString(R.string.main_meal)))
        mealsApiRespone.mainMealList?.let {
            silverMealMutableList.addAll(it.filter {
                it.itemType.equals("dal") ||  it.itemType.equals("rice_roti") ||  it.itemType.equals("extras")
            })
        }

        // Gold Meal
        goldMealMutableList.add(Header(viewModelContext.getString(R.string.special_meal)))
        mealsApiRespone.specialMealList?.let {
            goldMealMutableList.addAll(it.filter {
                it.itemType.equals("gold")
            })
        }
        goldMealMutableList.add(Header(viewModelContext.getString(R.string.sabji)))
        mealsApiRespone.sabjiList?.let {
            goldMealMutableList.addAll(it.filter {
                it.itemType.equals("gold")
            })
          //  defaultNonVegImageUrl =(goldMealMutableList.getOrNull(0) as Sabji).itemImage
        }
        goldMealMutableList.add(Header(viewModelContext.getString(R.string.main_meal)))
        mealsApiRespone.mainMealList?.let {
            goldMealMutableList.addAll(it.filter {
                it.itemType.equals("dal") ||  it.itemType.equals("rice_roti") ||
                        it.itemType.equals("extras")
            })
        }
    }

    fun frameFoodForSelectedCategory(): List<ListItem> {
        selectedMealCategoryMutableList.clear()
        selectedMealCategoryMutableList.add(
            MealCategoryHeader(
                viewModelContext.getString(R.string.special_meal),
                specialMealCategoryHeaderEnable
            )
        )
        apiResponse?.specialMealList?.let { it ->
            selectedMealCategoryMutableList.addAll(it.filter { specialMeal ->
                when (mealCategoryTabPosition) {
                    0 -> {
                        if(specialMeal.itemType.equals(Constants.normalMealCategory)){
                            specialMealName=specialMeal.itemName
                            mealCategoryType=Constants.normalMealCategory
                        }
                        specialMeal.itemType.equals(Constants.normalMealCategory) && checAvailability(
                            specialMeal
                        )
                    }
                    1 -> {
                        if(specialMeal.itemType.equals(Constants.silverMealCategory)){
                            specialMealName=specialMeal.itemName
                            mealCategoryType=Constants.silverMealCategory
                        }
                        specialMeal.itemType.equals(Constants.silverMealCategory) && checAvailability(
                            specialMeal
                        )
                    }
                    2 -> {
                        if(specialMeal.itemType.equals(Constants.goldMealCategory)){
                            specialMealName=specialMeal.itemName
                            mealCategoryType=Constants.goldMealCategory
                        }
                        specialMeal.itemType.equals(Constants.goldMealCategory) && checAvailability(
                            specialMeal
                        )
                    }
                    else -> {
                        specialMeal.itemType.equals(Constants.normalMealCategory) && checAvailability(
                            specialMeal
                        )
                    }
                }
            })
            if (selectedMealCategoryMutableList.size == 1) {
                selectedMealCategoryMutableList.clear()
            }
        }
        selectedMealCategoryMutableList.add(MealCategoryHeader(viewModelContext.getString(R.string.regular_meal),true))
        apiResponse?.sabjiList?.let {
            selectedMealCategoryMutableList.add(Header(viewModelContext.getString(R.string.sabji)))
            selectedMealCategoryMutableList.addAll(it.filter { sabji ->
                when (mealCategoryTabPosition) {
                    0 -> {
                        sabji.itemType.equals(Constants.normalMealCategory) && checAvailability(
                            sabji
                        )
                    }
                    1 -> {
                        sabji.itemType.equals(Constants.silverMealCategory) && checAvailability(
                            sabji
                        )
                    }
                    2 -> {
                        sabji.itemType.equals(Constants.goldMealCategory) && checAvailability(sabji)
                    }
                    else -> {
                        sabji.itemType.equals(Constants.normalMealCategory) && checAvailability(
                            sabji
                        )
                    }
                }
            })
        }

        apiResponse?.mainMealList?.let {
            // Dal
            val dalList = mutableListOf<ListItem>()
            dalList.addAll(it.filter {
                it.itemType.equals("dal") && checAvailability(it)
            })
            if (dalList.isNotEmpty()) {
                selectedMealCategoryMutableList.add(Header(viewModelContext.getString(R.string.dal)))
                selectedMealCategoryMutableList.addAll(dalList)
            }

            // Rice Roti
            val riceRotiList = mutableListOf<ListItem>()
            riceRotiList.addAll(it.filter {
                it.itemType.equals("rice_roti") && checAvailability(it)
            })
            if (riceRotiList.isNotEmpty()) {
                selectedMealCategoryMutableList.add(Header(viewModelContext.getString(R.string.rice_roti)))
                selectedMealCategoryMutableList.addAll(riceRotiList)
            }

            // Extras
            selectedMealCategoryMutableList.add(Header(viewModelContext.getString(R.string.extras)))
            selectedMealCategoryMutableList.addAll(it.filter {
                it.itemType.equals("extras") && checAvailability(it)
            })
        }
        if (lunchOrDinnerTime.equals(Constants.TIME_OUT)) {
            selectedMealCategoryMutableList.add(
                Button(
                    "",
                    noOfItemAddedInCart,
                    totalPrice.toString(),
                    enable = false
                )
            )
        } else {
            selectedMealCategoryMutableList.add(
                Button(
                    "",
                    noOfItemAddedInCart,
                    totalPrice.toString(),
                    enable = true
                )
            )
        }

        return selectedMealCategoryMutableList
    }

    private fun checAvailability(item:ListItem):Boolean{
     return when(item){
           is SpecialMeal->{
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
    fun getSelectedMealTypeTabHeader():String{
       return when(selectedPrice){
            regularMealPrice-> "VEG- ₹".plus(regularMealPrice)
            vegSpecialPrice-> "VEG- ₹".plus(vegSpecialPrice)
            nonVegPrice-> "VEG- ₹".plus(nonVegPrice)
           else -> "VEG- ₹".plus(regularMealPrice)
       }
    }
}