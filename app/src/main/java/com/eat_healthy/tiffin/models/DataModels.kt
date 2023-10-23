package com.eat_healthy.tiffin.models

import android.os.Parcelable
import com.eat_healthy.tiffin.genericFiles.ListItem
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import javax.sql.StatementEvent

@Parcelize
data class MealsApiRespone (
    @SerializedName("statusCode")
    @Expose
    val statusCode: Long,
    @SerializedName("statusMsg")
    @Expose
    val statusMsg: String?,
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
    var selected:Boolean=false,
    var isLunchOrDinnerTime:String
):ListItem, Parcelable

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
data class Button (
    val buttonText: String,
    var itemCount:Int=0,
    var itemPrice:String="",
    var enable: Boolean = true
):ListItem, Parcelable

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
    @SerializedName("itemType")
    @Expose
    val itemType: String
) : ListItem,Parcelable

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
    var mobileno: String?,
):Parcelable

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
    var monthlySubscriptionMoney:Double
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
):Parcelable

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
    val itemsInCart: List<ItemsInCart>
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
