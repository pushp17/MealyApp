package com.eat_healthy.tiffin.ui

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.eat_healthy.tiffin.R
import com.eat_healthy.tiffin.databinding.FragmentOrderSummaryBinding
import com.eat_healthy.tiffin.models.ApiResponse
import com.eat_healthy.tiffin.models.FoodReview
import com.eat_healthy.tiffin.models.MUserAddress
import com.eat_healthy.tiffin.models.RefereePerOrderDetail
import com.eat_healthy.tiffin.models.SingleMealUserOrderDetail
import com.eat_healthy.tiffin.utils.AppUtils.rsAppendedValue
import com.eat_healthy.tiffin.utils.Constants
import com.eat_healthy.tiffin.utils.DateAndTimeUtils.getCurrentDate
import com.eat_healthy.tiffin.viewmodels.OrderSummaryViewModel
import com.eat_healthy.tiffin.viewmodels.SharedViewModel
import com.eat_healthy.tiffin.viewmodels.UserAddressViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OrderSummaryFragment:BaseFragment() {
    lateinit var binding: FragmentOrderSummaryBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val userAddressViewModel: UserAddressViewModel by viewModels()
    private val viewModel: OrderSummaryViewModel by viewModels()
    private var navigationController: NavController?=null
    var referalCodeOrderCount = 0
    var refralAmountInThisOrder= 0.0
    var orderRedemeedPrice = 0.0
//    private lateinit var checkout:Checkout
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val address = arguments?.getParcelable<MUserAddress>("address")
        binding= FragmentOrderSummaryBinding.inflate(inflater,container,false)
        navigationController=findNavController()
//        referalCodeOrderCount = sharedPrefManager.getIntFromPreference(Constants.REFERAL_CODE_ORDER_COUNT)
//        refralAmountInThisOrder = ((sharedViewModel.totalPrice / sharedViewModel.cartItemList.size) * (sharedViewModel.rewardPercentagePerOrder / 100))
//
//       sharedViewModel.userDetail?.referalMoney?.let { referalAmountInWallet ->
//        if (referalAmountInWallet > sharedViewModel.totalPrice.toDouble()) {
//            orderRedemeedPrice = referalAmountInWallet - sharedViewModel.totalPrice
//        } else {
//            orderRedemeedPrice = referalAmountInWallet
//        }
//      }
        viewModel.apiResponseLivedata.observe(viewLifecycleOwner,observer)
        firebaseAnalytics.logEvent(Constants.ORDER_SUMMARY_SCREEN, null)
     if (sharedViewModel.monthlyUser == true) {
        monthlyUser()
     } else {
        singleMealUser()
     }

    binding.llTotalPrice.setOnClickListener {
        navigationController?.navigate(R.id.action_orderSummaryFragment_to_cartListItemsBottomSheet)
    }

       address?.userAddressList?.forEach {
           binding.tvFirstAddress.text=(it.streetNo).plus(" , ").plus(it.cityAndLocality).plus(" . ").plus(it.landmark ?:"")

           // Use the below commented code after implementing the monthly services

//           when(it.addressType){
//               Constants.LUNCH_DINNER -> {
//                   if (sharedViewModel.monthlyUser == true) {
//                       binding.tvAddress1Heading.text = "Lunch And Dinner Address"
//                   } else {
//                       binding.tvAddress1Heading.visibility = View.GONE
//                   }
//                   binding.tvFirstAddress.text=(it.streetNo).plus(" , ").plus(it.cityAndLocality).plus(" . ").plus(it.landmark ?:"")
//                   binding.tvAddress2Heading.visibility=View.GONE
//                   binding.tvSecondAddress.visibility=View.GONE
//               }
//               Constants.LUNCH->{
//                   binding.tvFirstAddress.text=(it.streetNo).plus(" , ").plus(it.cityAndLocality).plus(" . ").plus(it.landmark ?:"")
//               }
//               Constants.DINNER->{
//                   binding.tvSecondAddress.text=(it.streetNo).plus(" , ").plus(it.cityAndLocality).plus(" . ").plus(it.landmark ?:"")
//                   binding.tvAddress2Heading.visibility=View.VISIBLE
//                   binding.tvSecondAddress.visibility=View.VISIBLE
//               }
//               else -> {
//               }
//           }
       }
        binding.tvContinue.setOnClickListener {
            address?.let { it1 -> userAddressViewModel.updateUserAddress(it1) }
            placeOrder("")
        }
        return binding.root
    }

    private fun placeOrder(orderId: String?) {
        if (sharedViewModel.monthlyUser == true) {
            firebaseAnalytics.logEvent(Constants.MONTHLY_USER_ORDER_API_CALLED, null)
            sharedViewModel.monthlyUserPreference?.let { it1 ->
                viewModel.monthlyUserOrderServices(
                    it1
                )
            }
        } else {
            firebaseAnalytics.logEvent(Constants.SINGLE_USER_ORDER_API_CALLED, null)
            val referalCode = sharedPrefManager.getStringFromPreference(Constants.REFERAl_CODE,"")
            var refereePerOrderDetail: RefereePerOrderDetail? = null
//            sharedViewModel.referalRewardMaxLimitPerUser
            if(!referalCode.isNullOrEmpty() && referalCodeOrderCount < 30){
                referalCodeOrderCount += 1
                refereePerOrderDetail = RefereePerOrderDetail(
                    referalCode,
                    referalCodeOrderCount,
                    refralAmountInThisOrder
                )
            }
            viewModel.singleUserOrderServices(
                SingleMealUserOrderDetail(orderId,
                    sharedViewModel.userDetail?.username, sharedViewModel.userDetail?.mobileno,
                    sharedViewModel.totalPrice, viewModel.grandTotal, sharedViewModel.cartItemList,
                    getCurrentDate(),sharedViewModel.lunchOrDinnerTime,refereePerOrderDetail,orderRedemeedPrice
                )
            )
        }
    }

    private fun monthlyUser() {
        binding.tvMealPlan.visibility = View.VISIBLE
        viewModel.calulateMealCount(sharedViewModel)
        binding.tvMealOrMealType1.text="Regular Veg- ".plus(rsAppendedValue(sharedViewModel.regularMealPrice))
        binding.tvMealOrMealType2.text="Veg Special- ".plus(rsAppendedValue(sharedViewModel.vegSpecialPrice))
        binding.tvMealOrMealType3.text="Non Veg- ".plus(rsAppendedValue(sharedViewModel.nonVegPrice))

        binding.tvValue1.text=viewModel.vegCount.toString()
        binding.tvValue2.text=viewModel.vegSpecialCount.toString()
        binding.tvValue3.text=viewModel.nonVegCount.toString()

        binding.tvTotalMealCount.text = "Total Meal In One Week : ".plus(viewModel.totalMealCount.toString())

        binding.tvTotalMealPrice.text = rsAppendedValue(sharedViewModel.totalPriceForMonthlyUser.toString())
        binding.tvGstPrice.text = rsAppendedValue(String.format("%.2f",(sharedViewModel.totalPriceForMonthlyUser * 0.05)))
        var deliveryCharge = 0
        if (sharedViewModel.subscriptionExpired) {
            deliveryCharge = 100
            binding.tvDeliveryCharge.text = rsAppendedValue("100")
        } else {
            binding.tvDeliveryCharge.text = rsAppendedValue("0")
        }
        viewModel.grandTotal=sharedViewModel.totalPriceForMonthlyUser+(sharedViewModel.totalPriceForMonthlyUser * 0.05)+deliveryCharge
        binding.tvGrandTotalPrice.text= rsAppendedValue(viewModel.grandTotal.toString())
        binding.tvTotalGrandPrice.text = rsAppendedValue(String.format("%.1f", viewModel.grandTotal))

        sharedViewModel.monthlyUserPreference?.username=sharedViewModel.userDetail?.username
        sharedViewModel.monthlyUserPreference?.mobileno=sharedViewModel.userDetail?.mobileno
        if (sharedViewModel.monthlyUserPreference?.grandTotalPrice?.toDoubleOrNull() == null) {
            sharedViewModel.monthlyUserPreference?.grandTotalPrice =
                (sharedViewModel.totalPriceForMonthlyUser + (sharedViewModel.totalPriceForMonthlyUser * 0.05) + 100).toString()
        } else {
            sharedViewModel.monthlyUserPreference?.latestGrandTotalPrice =
                (sharedViewModel.totalPriceForMonthlyUser + (sharedViewModel.totalPriceForMonthlyUser * 0.05) + 100).toString()
        }
    }

    private fun singleMealUser() {
        binding.tvMealPlan.visibility = View.VISIBLE
        binding.tvMealPlan.text = "Your Meal"
        binding.rlItem2.visibility=View.GONE
        binding.rlItem3.visibility=View.GONE
        if (orderRedemeedPrice > 0.0) {
            binding.llDiscount.visibility = View.VISIBLE
            binding.llReferalDiscount.visibility = View.VISIBLE
            binding.tvReferalDiscount.text = "- "+rsAppendedValue(orderRedemeedPrice.toString())
            binding.tvDiscount.text = rsAppendedValue(orderRedemeedPrice.toString()).plus(" saved!")
        } else {
            binding.llDiscount.visibility = View.GONE
            binding.llReferalDiscount.visibility = View.GONE
        }
        binding.tvMealOrMealType1.text=(sharedViewModel.cartItemList.getOrNull(0)?.yourMeal)
        binding.tvValue1.text=rsAppendedValue(sharedViewModel.cartItemList.getOrNull(0)?.price)
        if(sharedViewModel.cartItemList.size>1) {
            binding.rlItem2.visibility=View.VISIBLE
            binding.tvMealOrMealType2.text=(sharedViewModel.cartItemList.getOrNull(1)?.yourMeal)
            binding.tvValue2.text=rsAppendedValue(sharedViewModel.cartItemList.getOrNull(1)?.price)
        }

        if(sharedViewModel.cartItemList.size>2) {
            binding.rlItem3.visibility=View.VISIBLE
            binding.tvMealOrMealType3.text=(sharedViewModel.cartItemList.getOrNull(2)?.yourMeal)
            binding.tvValue3.text=rsAppendedValue(sharedViewModel.cartItemList.getOrNull(2)?.price)
        }
        if (sharedViewModel.cartItemList.size > 3) {
            binding.tvTotalMealCount.visibility=View.VISIBLE
            binding.tvTotalMealCount.text = "View all"
            binding.tvTotalMealCount.setOnClickListener {
                navigationController?.navigate(R.id.action_orderSummaryFragment_to_cartListItemsBottomSheet)
            }
        }else {
            binding.view.visibility=View.GONE
            binding.tvTotalMealCount.visibility=View.GONE
        }
        context?.let { binding.tvTotalMealCount.setTextColor(it.getColor(R.color.colorPrimary)) }
        binding.tvTotalMealPrice.text = rsAppendedValue(sharedViewModel.totalPrice.toString())
        binding.tvGstPrice.text = rsAppendedValue(String.format("%.1f",(sharedViewModel.totalPrice * 0.05)))
        binding.tvDeliveryCharge.text = rsAppendedValue(sharedViewModel.deliveryPrice)
        viewModel.grandTotal = sharedViewModel.totalPrice + (sharedViewModel.totalPrice * 0.05) + sharedViewModel.deliveryPrice.toInt() - orderRedemeedPrice

//        val totalPriceWithoutDiscount = sharedViewModel.totalPrice + (sharedViewModel.totalPrice * 0.05) + 3
//        if (orderRedemeedPrice > 0.0) {
//            binding.tvTotalPrice.text =
//                rsAppendedValue(String.format("%.1f", totalPriceWithoutDiscount))
//            binding.tvTotalPrice.setPaintFlags(binding.tvTotalPrice.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
//            binding.tvTotalPrice.visibility = View.VISIBLE
//        }

        binding.tvGrandTotalPrice.text = rsAppendedValue(String.format("%.1f", viewModel.grandTotal))
        binding.tvTotalGrandPrice.text = rsAppendedValue(String.format("%.1f", viewModel.grandTotal))
        binding.llTotalPrice.setOnClickListener {
        }
    }

    override fun receivedResponse(item: Any?) {
        item?.let {
            if (item is ApiResponse) {
                if (sharedViewModel.monthlyUser == true) {
                    sharedViewModel.subscriptionExpired = false
                    sharedViewModel.userDetail?.monthlySubscriptionMoney = viewModel.grandTotal
                    sharedViewModel.userDetail?.monthlyUser = true
                    sharedPrefManager.addModelClass(Constants.USER_INFO, sharedViewModel.userDetail)
                    sharedViewModel.cartItemList.clear()
                    sharedViewModel.totalPrice = 0
                    sharedViewModel.totalPriceForMonthlyUser = 0
                    sharedViewModel.noOfItemAddedInCart = 0
                    val alertDialog = SweetAlertDialogV2(
                        requireActivity(),
                        SweetAlertDialogV2.SUCCESS_TYPE
                    )
                    alertDialog.setCancelable(false)
                    alertDialog.titleText = "Order"

                    alertDialog.contentText = sharedViewModel.apiResponse?.monthlySubscriptionMsg
                    alertDialog.show()
                    alertDialog.setConfirmClickListener {
                        alertDialog.dismissWithAnimation()
                        navigationController?.popBackStack(R.id.navigation_home, false)

                    }
                } else {
                    sharedPrefManager.addIntToPreference(
                        Constants.REFERAL_CODE_ORDER_COUNT,
                        referalCodeOrderCount
                    )
                    val foodReview = FoodReview(
                        "",
                        "",
                        getCurrentDate(),
                        sharedViewModel.lunchOrDinnerTime,
                        "",
                        sharedViewModel.userDetail?.username!!,
                        sharedViewModel.cartItemList.map { it.yourMeal }.toList()
                    )
                    sharedPrefManager.addModelClass(Constants.FOOD_REVIEW, foodReview)
                    sharedViewModel.userDetail?.monthlySubscriptionMoney = viewModel.grandTotal
                    sharedViewModel.userDetail?.monthlyUser = true
                    sharedPrefManager.addModelClass(Constants.USER_INFO, sharedViewModel.userDetail)
                    sharedViewModel.cartItemList.clear()
                    sharedViewModel.totalPrice = 0
                    sharedViewModel.totalPriceForMonthlyUser = 0
                    sharedViewModel.noOfItemAddedInCart = 0
                    navigationController?.navigate(
                        R.id.action_orderSummaryFragment_to_orderSuccessFragment,
                        bundleOf("estimatedDeliveryTime" to item.msg)
                    )
                }
            }
        }
    }

    fun callbackFromCartListItemsBottomSheet(){
        singleMealUser()
        binding.llTotalPrice.setOnClickListener {
            navigationController?.navigate(R.id.action_orderSummaryFragment_to_cartListItemsBottomSheet)
        }
    }
//    fun startPayment(checkout: Checkout, amount:Int){
//        sharedViewModel.selectedMealCategoryMutableList.forEach {
//            if (it is Sabji) {
//                it.selected = false
//            } else if (it is MainMeal) {
//                it.selected = false
//            }
//        }
//        try {
//            CoroutineScope(Dispatchers.Main).launch {
//                val order_id = withContext(Dispatchers.IO) {
//                    val razorpay =
//                        RazorpayClient("rzp_test_UtrJfGAmzb1T7T", "BiiTuB0GwoiyJFyQLUztBZtd")
//                    val orderRequest = JSONObject()
//                    orderRequest.put("amount", amount*100) // amount in the smallest currency unit
//                    orderRequest.put("currency", "INR");
//                    orderRequest.put("receipt", "order_rcptid_11")
//                    val orderx = razorpay.orders.create(orderRequest)
//                    JSONObject(orderx.toString()).getString("id")
//                }
//                Log.d("scdksbcnksdc","order_outside  "+ order_id)
//                val options = JSONObject()
//                options.put("name", "Mealy")
//                options.put("description", "Demoing Charges")
//                //You can omit the image option to fetch the image from the dashboard
//                options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg")
//                options.put("theme.color", "#3399cc")
//                options.put("currency", "INR")
//                options.put("order_id", order_id)
//                options.put("amount", amount*100)//pass amount in currency subunits
//
//                val retryObj = JSONObject();
//                retryObj.put("enabled", true)
//                retryObj.put("max_count", 4)
//                options.put("retry", retryObj)
//
//                val prefill = JSONObject()
//                prefill.put("email", "gaurav.kumar@example.com")
//                prefill.put("contact", "9113584599")
//
//                options.put("prefill", prefill)
//                checkout.open(activity, options)
//            }
//        } catch (e: Exception) {
//            // Handle Exception
//        } catch (e: RazorpayException) {
//            // Handle Exception
//        }
//    }
}