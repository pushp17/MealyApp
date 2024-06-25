package com.eat_healthy.tiffin.models

import android.os.Parcelable
import androidx.databinding.ObservableInt
import com.eat_healthy.tiffin.genericFiles.ListItem
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MealsApiRespone (
    @SerializedName("statusCode")
    @Expose
    val statusCode: Long,
    @SerializedName("statusMsg")
    @Expose
    val statusMsg: String ?,
    @SerializedName("statusMsg2")
    @Expose
    val statusMsg2: String?,
    @SerializedName("monthlySubscriptionMsg")
    @Expose
    val monthlySubscriptionMsg: String?,
    @SerializedName("showRegisterPage")
    @Expose
    val showRegisterPage: Boolean?,
    @SerializedName("sabjiList")
    @Expose
    val sabjiList: List<Sabji>?,
    @SerializedName("mainMealList")
    @Expose
    val mainMealList: List<MainMeal>?,
    @SerializedName("specialMealList")
    @Expose
    val specialMealList: List<SpecialMeal>?,
    @SerializedName("mealList")
    @Expose
    val mealList: List<Meal>?,
    @SerializedName("extrasV2")
    @Expose
    val extrasV2: List<ExtrasV2>?,
    @SerializedName("lunchStartTime")
    @Expose
    val  lunchStartTime:String?="",
    @SerializedName("lunchEndTime")
    @Expose
    val lunchEndTime:String?="",
    @SerializedName("dinnerStartTime")
    @Expose
    val  dinnerStartTime:String?="",
    @SerializedName("dinnerEndTime")
    @Expose
    val  dinnerEndTime:String?="",
    @SerializedName("regularMealPrice")
    @Expose
    val  regularMealPrice:String?="",
    @SerializedName("vegSpecialPrice")
    @Expose
    val  vegSpecialPrice:String?="",
    @SerializedName("nonVegPrice")
    @Expose
    val  nonVegPrice:String?="",
    @SerializedName("appVersionName")
    @Expose
    val  appVersionName:Double=2.0,
    @SerializedName("playStoreLink")
    @Expose
    val playStoreLink: String? = "https://play.google.com/store/apps/details?id=com.application.zomato&hl=en_IN&gl=US",
    @SerializedName("baseUrl")
    @Expose
    val baseUrl: String ?,
    @SerializedName("aStart_order_timeout")
    @Expose
    val afterStartOrderTimeout: String?,
    @SerializedName("aStrt_single_meal_status_lunch_time")
    @Expose
    val aStartSingleMealStatusLunchTime: String?,
    @SerializedName("aStrt_single_meal_status_dinner_time")
    @Expose
    val afterStartSingleMealStatusDinnerTime: String?,
    @SerializedName("ar_statusMsg")
    @Expose
    val afterRegistrationStatusMsg: String?,
    @SerializedName("register_success_msg")
    @Expose
    val registerSuccessMsg: String?,
    @SerializedName("serviceStarted")
    @Expose
    val isServiceStarted: Boolean?,
    @SerializedName("contactNo")
    @Expose
    val contactNo: String?,
    @SerializedName("referalRewardMaxLimitPerUser")
    @Expose
    val referalRewardMaxLimitPerUser: Int=0,
    @SerializedName("rewardPercentagePerOrder")
    @Expose
    val rewardPercentagePerOrder: Double=0.0,
    @SerializedName("statusMsgV2")
    @Expose
    val statusMsgV2: String?,
    @SerializedName("showStatus")
    @Expose
    val showStatus: Boolean? = false,
    @SerializedName("deliveryCharge")
    @Expose
    val deliveryCharge: String?,
    @SerializedName("deliveryTimeSlot")
    @Expose
    val deliveryTimeSlot: List<String>?,
    @SerializedName("serviceArea")
    @Expose
    val serviceArea: String?,
):ListItem, Parcelable

@Parcelize
data class MainMeal (
    @SerializedName("itemId")
    @Expose
    val itemID: String?,
    @SerializedName("itemName")
    @Expose
    val itemName: String?,
    @SerializedName("itemType")
    @Expose
    val itemType: String?,
    @SerializedName("itemPrice")
    @Expose
    val itemPrice: String? = null,
    @SerializedName("itemImage")
    @Expose
    val itemImage: String?,
    @SerializedName("itemQuantity")
    @Expose
    val itemQuantity: String? = null,
    @SerializedName("info")
    @Expose
    val info: String? = null,
    @SerializedName("rating")
    @Expose
    val rating: String? = null,
    @SerializedName("mealAvailability")
    @Expose
    val mealAvailability: MealAvailability?,
    var selected: Boolean = false,
    var isLunchOrDinnerTime:String
):ListItem, Parcelable

@Parcelize
data class Sabji (
    @SerializedName("sabjiId")
    @Expose
    val itemID: String?,
    @SerializedName("sabjiName")
    @Expose
    val itemName: String?,
    @SerializedName("sabjiType")
    @Expose
    val itemType: String?,
    @SerializedName("sabjiPrice")
    @Expose
    val itemPrice: String? = null,
    @SerializedName("showOffPrice")
    @Expose
    val showOffPrice: String? = null,
    @SerializedName("sabjiImage")
    @Expose
    val itemImage: String?,
    @SerializedName("info")
    @Expose
    val info: String? = null,
    @SerializedName("rating")
    @Expose
    val rating: String? = null,
    @SerializedName("mealAvailability")
    @Expose
    val mealAvailability: MealAvailability? = null,
    @SerializedName("highlight")
    @Expose
    val highlight: Boolean = false,
    var selected:Boolean=false,
    var isLunchOrDinnerTime:String
):ListItem, Parcelable


@Parcelize
data class Meal(
    @SerializedName("name")
    @Expose
    val name: String? = null,
    @SerializedName("type")
    @Expose
    val type: String? = null,
    @SerializedName("category")
    @Expose
    val category: String? = null,
    @SerializedName("price")
    @Expose
    val price: String? = null,
    @SerializedName("showAsSingleItem")
    @Expose
    val showAsSingleItem: Boolean = false,
    @SerializedName("image")
    @Expose
    val image: String? = null,
    @SerializedName("info")
    @Expose
    val info: String? = null,
    @SerializedName("mealAvailability")
    @Expose
    val mealAvailability: MealAvailability,
    @SerializedName("showOffPrice")
    @Expose
    val showOffPrice: String? = null,
    @SerializedName("highlight")
    @Expose
    val highlight: Boolean = false,
    @SerializedName("pagerPosition")
    @Expose
    val pagerPosition: Int = -1,
    @SerializedName("position")
    @Expose
    val position: Int = -1,
    @SerializedName("non_veg")
    @Expose
    val nonVeg: Boolean? = false,
    var selected: Boolean = false,
    var isLunchOrDinnerTime:String,
    var addButtonCountText: ObservableInt  = ObservableInt(0)
) : ListItem, Parcelable

@Parcelize
data class ExtrasV2(
    @SerializedName("id")
    @Expose
    val id: String?,
    @SerializedName("name")
    @Expose
    val name: String?,
    @SerializedName("type")
    @Expose
    val type: String?,
    @SerializedName("price")
    @Expose
    val price: String? = null,
    @SerializedName("showOffPrice")
    @Expose
    val showOffPrice: String? = null,
    @SerializedName("image")
    @Expose
    val image: String?,
    @SerializedName("info")
    @Expose
    val info: String? = null,
    @SerializedName("rating")
    @Expose
    val rating: String? = null,
    @SerializedName("quantity")
    @Expose
    val extraQuantity: Long?,
    @SerializedName("mealAvailability")
    @Expose
    val extraAvailability: MealAvailability? = null,
    @SerializedName("highlight")
    @Expose
    val highlight: Boolean = false,
    var selected: Boolean = false,
    var isLunchOrDinnerTime: String
) : ListItem, Parcelable



@Parcelize
data class SpecialMeal (
    @SerializedName("mealId")
    @Expose
    val itemID: String?,
    @SerializedName("mealName")
    @Expose
    val itemName: String?,
    @SerializedName("mealType")
    @Expose
    val itemType: String?,
    @SerializedName("mealPrice")
    @Expose
    val itemPrice: String? = null,
    @SerializedName("showOffPrice")
    @Expose
    val showOffPrice: String? = null,
    @SerializedName("mealImage")
    @Expose
    val itemImage: String?,
    @SerializedName("info")
    @Expose
    val info: String? = null,
    @SerializedName("rating")
    @Expose
    val rating: String? = null,
    @SerializedName("mealQuantity")
    @Expose
    val mealQuantity: Long?,
    @SerializedName("mealAvailability")
    @Expose
    val mealAvailability: MealAvailability? = null,
    @SerializedName("highlight")
    @Expose
    val highlight: Boolean = false,
    @SerializedName("fullImage")
    @Expose
    val fullImage: Boolean = false,
    @SerializedName("pagerPosition")
    @Expose
    val pagerPosition: Int = -1,
    var selected:Boolean=false,
    var isLunchOrDinnerTime:String
):ListItem, Parcelable

@Parcelize
data class MealAvailability (
    @SerializedName("lunch_today")
    @Expose
    val lunchToday: Boolean?=false,
    @SerializedName("dinner_today")
    @Expose
    val dinnerToday: Boolean?=false,
):ListItem, Parcelable

@Parcelize
data class DailyMeal (
    val lunchToday: Boolean,
    val lunchTomorrow: Boolean,
    val dinnerToday: Boolean,
    val dinnerTomorrow: Boolean
):ListItem, Parcelable

@Parcelize
data class Header (
    val itemHeader: String,
):ListItem, Parcelable

@Parcelize
data class StringText (
    val item: String,
):ListItem, Parcelable

@Parcelize
data class Button (
    val buttonText: String,
    var itemCount:Int=0,
    var itemPrice:String="",
    var enable: Boolean = true
):ListItem, Parcelable

@Parcelize
data class Empty(val empty: String) : ListItem, Parcelable

@Parcelize
data class SummarySelectedMeal (
    val summarySelectedMeal: String,
):ListItem, Parcelable

@Parcelize
data class MealCategoryHeader (
    val itemHeader: String,
    var selected:Boolean=false
):ListItem, Parcelable


@Parcelize
data class MyFavouriteMeal(
    @SerializedName("onlyRoti")
    @Expose
    var onlyRoti:Boolean?=false ,
    @SerializedName("riceAndRoti")
    @Expose
    var riceAndRoti:Boolean?=false,
    @SerializedName("preferredSabjiList")
    @Expose
    var preferredSabjiList:MutableList<Sabji> = mutableListOf()
):ListItem, Parcelable

@Parcelize
data class AvailableLocalityResponse(
    @SerializedName("statusCode")
    @Expose
    val statusCode: Long,
    @SerializedName("availableLocalityLIst")
    @Expose
    val availableLocalityLIst:List<String>?
):ListItem, Parcelable

@Parcelize
data class UserAddressResponse(
    @SerializedName("code")
    @Expose
    val statusCode: Int,
    @SerializedName("mUserAddress")
    @Expose
    val mUserAddress:MUserAddress?
):ListItem, Parcelable

@Parcelize
data class MUserAddress(
    @SerializedName("username")
    @Expose
    var username: String?=null,
    @SerializedName("mobileno")
    @Expose
    var mobileno: String?=null,
    @SerializedName("userAddressList")
    @Expose
    var userAddressList:List<UserAddress>
):ListItem,Parcelable

@Parcelize
data class UserAddress(
    @SerializedName("addressId")
    @Expose
    var addressId:String,
    @SerializedName("streetNo")
    @Expose
    var streetNo:String?=null,
    @SerializedName("landmark")
    @Expose
    var landmark:String?=null,
    @SerializedName("cityAndLocality")
    @Expose
    var cityAndLocality: String?=null,
    @SerializedName("addressType")
    @Expose
    var addressType:String?=null,
    @SerializedName("selected")
    @Expose
    var selected:Boolean=false,
):ListItem,Parcelable

@Parcelize
data class GpsLocation(
    var subLocality:String?=null,
    var locality: String?=null,
    var city:String?=null,
):ListItem,Parcelable

@Parcelize
data class ItemsInCart(
    @SerializedName("yourMeal")
    @Expose
    val yourMeal: String,
    @SerializedName("price")
    @Expose
    val price: String,
    @SerializedName("item_name")
    @Expose
    val itemName: String? = ""
) : ListItem, Parcelable

@Parcelize
data class UserWeeklyFoodPreference(
    @SerializedName("day")
    @Expose
    var day: String?=null,
    @SerializedName("dinner")
    @Expose
    var dinner: String?=null,
    @SerializedName("lunch")
    @Expose
    var lunch: String?=null
) : Parcelable

@Parcelize
data class WeekelyMealType(
    val day:String,
    val lunchVegId:String,
    val lunchVegSpecialId:String,
    val lunchNonVeglId:String,
    val dinnerVegId:String,
    val dinnerVegSpecialId:String,
    val dinnerNonVegId:String,
    var lunchVegSelected:Boolean=false,
    var lunchVegSpecialSelected:Boolean=false,
    var lunchNonVegSelected:Boolean=false,
    var dinnerVegSelected:Boolean=false,
    var dinnerVegSpecialSelected:Boolean=false,
    var dinnerNonVegSelected:Boolean=false
):ListItem,Parcelable

@Parcelize
data class Address(
    val subLocality: String?,
    val locality: String?,
    val state: String?
):Parcelable

// You can keep adding the new fields based on the requirement of Clicklistener arguments
data class ListItemClickRequiredArguments(
    var id:String?
)

@Parcelize
data class User(
    @SerializedName("username")
    @Expose
    var username: String?,
    @SerializedName("mobileno")
    @Expose
    var mobileno: String?
) : Parcelable

@Parcelize
data class MonthlyUserPreferenceResponse(
    @SerializedName("code")
    @Expose
    val statusCode: Long,
    @SerializedName("monthlyUserPreference")
    @Expose
    var monthlyUserPreference: MonthlyUserPreference?,
) : Parcelable

@Parcelize
data class MonthlyUserPreference(
    @SerializedName("username")
    @Expose
    var username: String?,
    @SerializedName("mobileno")
    @Expose
    var mobileno: String?,
    @SerializedName("monthlySubscriptionPrice")
    @Expose
    val monthlySubscriptionPrice: String?,
    @SerializedName("latestMonthlySubscriptionPrice")
    @Expose
    val latestMonthlySubscriptionPrice: String?,
    @SerializedName("grandTotalPrice")
    @Expose
    var grandTotalPrice: String?,
    @SerializedName("latestGrandTotalPrice")
    @Expose
    var latestGrandTotalPrice: String?,
    @SerializedName("totalPriceInWeek")
    @Expose
    var totalPriceInWeek : String?,
    @SerializedName("subscriptionStartDate")
    @Expose
    val subscriptionStartDate: String?,
    @SerializedName("subscriptionIntermediateDate")
    @Expose
    val subscriptionIntermediateDate: String?,
    @SerializedName("subscriptionEndDate")
    @Expose
    val subscriptionEndDate: String?,
    @SerializedName("myFavouriteMeal")
    @Expose
    val myFavouriteMeal: MyFavouriteMeal?=null,
    @SerializedName("userWeeklyFoodPreference")
    @Expose
    var userWeeklyFoodPreference: List<UserWeeklyFoodPreference>? = null
) : ListItem,Parcelable

@Parcelize
data class UserDetail(
    var username: String?,
    var mobileno: String?,
    var isUserSignedIn:Boolean?,
    var monthlyUser:Boolean?,
    var monthlySubscriptionMoney:Double,
    @SerializedName("referalMoney")
    @Expose
    var referalMoney: Double? = 0.0,
    @SerializedName("totalReferalMoneyReceived")
    @Expose
    var totalReferalMoneyReceived: Double? = 0.0

):Parcelable

@Parcelize
data class ApiResponse(
    @SerializedName("code")
    @Expose
    val statusCode:Int,
    @SerializedName("msg")
    @Expose
    val msg:String?
):ListItem, Parcelable

@Parcelize
data class LoginResponse(
    @SerializedName("code")
    @Expose
    val statusCode:Int,
    @SerializedName("msg")
    @Expose
    val msg:String?
):ListItem, Parcelable

@Parcelize
data class UserDetailResponse(
    @SerializedName("code")
    @Expose
    val statusCode:Int,
    @SerializedName("mUser")
    @Expose
    val user:UserDetail?
):ListItem, Parcelable

@Parcelize
data class MOtp(
    @SerializedName("username")
    @Expose
    var username: String? = null,
    @SerializedName("mobileno")
    @Expose
    var mobileno: String? = null,
    @SerializedName("otp")
    @Expose
    var otp: String? = null
):Parcelable

@Parcelize
data class OtpVerification(
    @SerializedName("code")
    @Expose
    val statusCode:Int,
    @SerializedName("msg")
    @Expose
    val msg:String?,
    @SerializedName("firstTimeUser")
    @Expose
    val firstTimeUser:Boolean?
):Parcelable

@Parcelize
data class SingleMealUserOrderDetail(
    @SerializedName("orderId")
    @Expose
    var orderId: String?,
    @SerializedName("username")
    @Expose
    var username: String?,
    @SerializedName("mobileno")
    @Expose
    var mobileno: String?,
    @SerializedName("subTotalPrice")
    @Expose
    val subTotalPrice:Int,
    @SerializedName("grandTotalPrice")
    @Expose
    val grandTotalPrice:Double,
    @SerializedName("itemsInCart")
    @Expose
    val itemsInCart:List<ItemsInCart>,
    @SerializedName("dateAndTime")
    @Expose
    var dateAndTime: String?,
    @SerializedName("lunchOrDinner")
    @Expose
    var lunchOrDinner: String?,
    @SerializedName("refereePerOrderDetail")
    @Expose
    var refereePerOrderDetail: RefereePerOrderDetail?,
    @SerializedName("orderRedemeedPrice")
    @Expose
    var orderRedemeedPrice: Double?,
    @SerializedName("estimatedDeliveryTime")
    @Expose
    var estimatedDeliveryTime: String?=null,
    @SerializedName("orderTime")
    @Expose
    var orderTime: String?=null
):Parcelable

@Parcelize
data class RefereePerOrderDetail(
    @SerializedName("referalCode")
    @Expose
    val referalCode: String,
    @SerializedName("referalCodeOrderCount")
    @Expose
    val referalCodeOrderCount: Int,
    @SerializedName("refralAmountInThisOrder")
    @Expose
    val refralAmountInThisOrder: Double?,
) : Parcelable


@Parcelize
data class SingleMealUserOrderHistoryResponse(
    @SerializedName("statusCode")
    @Expose
    val statusCode:Int,
    @SerializedName("singleMealUserOrderDetail")
    @Expose
    val itemsInCart: List<SingleMealUserOrderDetail>?,
) : Parcelable

@Parcelize
data class SingleMealUserOrderHistory(
    val orderId: String,
    val dateAndTime: String,
    val subTotalPrice: Int,
    val grandTotalPrice: String,
    val itemsInCart: List<ItemsInCart>,
    val orderTime:String?,
    val estimatedDeliveryTime: String?
) : ListItem, Parcelable

@Parcelize
data class MonthlyUserOrderDetail(
    @SerializedName("monthlyUserOrderDetail")
    @Expose
    val monthlyUserOrderDetailsAndPreference:MonthlyUserPreference
):Parcelable

@Parcelize
data class DeliveryDetailsResponse(
    @SerializedName("statusCode")
    @Expose
    val statusCode: Long,
    @SerializedName("deliveryDetailsList")
    @Expose
    val deliveryDetailsList: List<DeliveryDetails>
) : ListItem, Parcelable

@Parcelize
data class DeliveryDetails(
    @SerializedName("mUserAddress")
    @Expose
    var mUserAddress: MUserAddress? = null,
    @SerializedName("singleMealUserOrderDetail")
    @Expose
    var singleMealUserOrderDetail: SingleMealUserOrderDetail? = null,
) : ListItem, Parcelable

@Parcelize
data class ReferalResponse(
    @SerializedName("statusCode")
    @Expose
    var statusCode: Int? = null,
    @SerializedName("mRefrer")
    @Expose
    var mRefrer: MRefrer? = null,
) : ListItem, Parcelable

@Parcelize
data class MRefrer(
    @SerializedName("mobileno")
    @Expose
    var mobileno: String,
    @SerializedName("username")
    @Expose
    var username: String,
    @SerializedName("referalCode")
    @Expose
    var referalCode: String,
    @SerializedName("isReferalCodeActive")
    @Expose
    var isReferalCodeActive: Boolean,
    @SerializedName("referalCodecCreatedDate")
    @Expose
    var referalCodecCreatedDate: String,
    @SerializedName("rewardAmount")
    @Expose
    var rewardAmount: Int,
    @SerializedName("referalMsg")
    @Expose
    var referalMsg: String,
    @SerializedName("referalSharedMsg")
    @Expose
    var referalSharedMsg: String,
) : ListItem, Parcelable

@Parcelize
data class RefrerReferalHistoryResponse(
    @SerializedName("code")
    @Expose
    var statusCode: Int,
    @SerializedName("refrerRefralHistory")
    @Expose
    var refrerReferalHistory: RefrerReferalHistory,
) : ListItem, Parcelable

@Parcelize
data class RefrerReferalHistory(
    @SerializedName("refererMobileno")
    @Expose
    var refererMobileno: String?,
    @SerializedName("referalCode")
    @Expose
    var refereeTotalAmount: String?,
    @SerializedName("refrerRefreeHistoryList")
    @Expose
    var refereeOrdersDetails: List<RefereeOrdersDetails>,
) : ListItem ,Parcelable

@Parcelize
data class RefereeOrdersDetails(
    @SerializedName("refereeUserName")
    @Expose
    var refereeUserName: String?,
    @SerializedName("refereeMobileNo")
    @Expose
    var refereeMobileNo: String?,
    @SerializedName("referalCodeOrderCount")
    @Expose
    var referalCodeOrderCount: Int? = 0,
    @SerializedName("refralAmountInThisOrder")
    @Expose
    var refralAmountInThisOrder: Double? = 0.0,
    @SerializedName("dateAndTime")
    @Expose
    var dateAndTime: String?
) : ListItem, Parcelable

@Parcelize
data class HomeHighLightedItems(
    val itemName: String?,
    val itemImage: String?,
    val desc: String?,
    val itemPrice: String?,
    val showOffPrice: String?,
    val fullImage:Boolean=false,
    val pagerPosition:Int=-1
) : ListItem, Parcelable

data class FoodReview(
    @SerializedName("foodReview")
    @Expose
    var foodReview: String,
    @SerializedName("foodRating")
    @Expose
    var foodRating: String,
    @SerializedName("foodOrderDate")
    @Expose
    val foodOrderDate: String,
    @SerializedName("lunchOrDinner")
    @Expose
    val lunchOrDinner: String,
    @SerializedName("foodReviewDate")
    @Expose
    var foodReviewDate: String,
    @SerializedName("foodReviewUserName")
    @Expose
    val foodReviewUserName: String,
    @SerializedName("foodOrderList")
    @Expose
    val foodOrderList: List<String>
)

@Parcelize
data class UsersSuggestionUpload(
    @SerializedName("userName")
    @Expose
    val userName: String?,
    @SerializedName("mobileno")
    @Expose
    val mobileno: String?,
    @SerializedName("listOfSuggestionQuestionAnswer")
    @Expose
    val listOfSuggestionQuestionAnswer: List<SuggestionQuestionAnswer>?,
) : ListItem, Parcelable

@Parcelize
data class UsersSuggestionQuestions(
    @SerializedName("code")
    @Expose
    val statusCode: Long,
    @SerializedName("header")
    @Expose
    val header: String?,
    @SerializedName("listOfSuggestionQuestionAnswer")
    @Expose
    val listOfSuggestionQuestionAnswer: List<SuggestionQuestionAnswer>?,
) : ListItem, Parcelable

@Parcelize
data class SuggestionQuestionAnswer(
    @SerializedName("question")
    @Expose
    val question: String,
    @SerializedName("type")
    @Expose
    val type: String,
    @SerializedName("answer")
    @Expose
    var answer: String?,
    @SerializedName("position")
    @Expose
    var position: Int?=0,
) : ListItem, Parcelable

@Parcelize
data class WeekelyMenuResponse(
    @SerializedName("code")
    @Expose
    val code: Long,
    @SerializedName("lunch_alarm_hour")
    @Expose
    val lunch_alarm_hour: Int ?,
    @SerializedName("lunch_alarm_min")
    @Expose
    val lunch_alarm_min: Int ?,
    @SerializedName("dinner_alarm_hour")
    @Expose
    val dinner_alarm_hour: Int ?,
    @SerializedName("dinner_alarm_min")
    @Expose
    val dinner_alarm_min: Int ?,
    @SerializedName("weekelyMenuList")
    @Expose
    var weekelyMenuList: List<WeekelyMenu>?,
) : ListItem, Parcelable

@Parcelize
data class WeekelyMenu(
    @SerializedName("item")
    @Expose
    val item: String,
    @SerializedName("info")
    @Expose
    val desc: String,
    @SerializedName("image")
    @Expose
    val image: String,
    @SerializedName("day")
    @Expose
    var day: String?,

) : ListItem, Parcelable

@Parcelize
data class UsersFoodReminder(
    val savedMenuList: MutableList<WeekelyMenu>,
) : ListItem, Parcelable

enum class Type(val value: String) {
    Gold("gold"),
    Normal("normal"),
    Silver("silver");

    companion object {
        public fun fromValue(value: String): Type = when (value) {
            "gold"   -> Gold
            "normal" -> Normal
            "silver" -> Silver
            else     -> throw IllegalArgumentException()
        }
    }
}
