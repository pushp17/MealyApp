package com.eat_healthy.tiffin.ui

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieAnimationView
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
import com.eat_healthy.tiffin.utils.hideLoading
import com.eat_healthy.tiffin.utils.showLoading
import com.eat_healthy.tiffin.viewmodels.OrderSummaryViewModel
import com.eat_healthy.tiffin.viewmodels.SharedViewModel
import com.eat_healthy.tiffin.viewmodels.UserAddressViewModel
import com.razorpay.Checkout
import com.razorpay.RazorpayClient
import com.razorpay.RazorpayException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import org.json.JSONObject


@AndroidEntryPoint
class OrderSummaryFragment:BaseFragment()  {
    lateinit var binding: FragmentOrderSummaryBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val userAddressViewModel: UserAddressViewModel by viewModels()
    private val viewModel: OrderSummaryViewModel by viewModels()
    private var navigationController: NavController?=null
    var referalCodeOrderCount = 0
    var refralAmountInThisOrder= 0.0
    var orderRedemeedPrice = 0.0
    private lateinit var checkout:Checkout
    var isPaymentSuccessful = false
    var orderSuccess = false
    var estimatedDeliveryTime:String? = ""
    var tenPercentDiscountedPrice = 0.0
    var fiftyPercentDiscountedPrice = 0.0
    var totalDiscountedPrice = 0.0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val address = arguments?.getParcelable<MUserAddress>("address")
        binding = FragmentOrderSummaryBinding.inflate(inflater,container,false)
        navigationController=findNavController()
        checkout = Checkout()
        checkout.setKeyID("rzp_live_ddvz84A1tN5Ipr")
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
        if (sharedViewModel.apiResponse?.enableOffer == true) {
            if (sharedViewModel.cartItemList.size > 1) {
                tenPercentDiscountedPrice = sharedViewModel.totalPrice * 0.1
            }
            if (sharedViewModel.userDetail?.hasOrdered != true) {
                fiftyPercentDiscountedPrice = sharedViewModel.totalPrice * 0.5
                if (fiftyPercentDiscountedPrice > 100.0) {
                    fiftyPercentDiscountedPrice = 100.0
                }
            }
            totalDiscountedPrice = tenPercentDiscountedPrice + fiftyPercentDiscountedPrice
            if (totalDiscountedPrice > 0.0) {
                showAnimatedDialog(requireContext(), totalDiscountedPrice)
            }
        }
     if (sharedViewModel.monthlyUser == true) {
        monthlyUser()
     } else {
        singleMealUser()
     }
        address?.let { it1 -> userAddressViewModel.updateUserAddress(it1) }
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
                startPayment(checkout,viewModel.grandTotal)
                placeOrder(null,null,true)
        }
        return binding.root
    }

    private fun showAnimatedDialog(context: Context, savings: Double) {
        val dialog = Dialog(context)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_animated, null)

        dialog.setContentView(view)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)

        val animationView = view.findViewById<LottieAnimationView>(R.id.lottieAnimation)
        val textView = view.findViewById<TextView>(R.id.savingsText)
        val closeButton = view.findViewById<Button>(R.id.closeButton)

        animationView.setAnimation(R.raw.anim_1)  // Add your confetti JSON file in res/raw/
        animationView.playAnimation()

        textView.text = "You saved ₹$savings on this order."

        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
     fun invokePlaceOrder(orderId: String?, paymentId:String?) {
             CoroutineScope(Dispatchers.Main).launch {
                 delay(300L)
                 if(orderSuccess) {
                     //call the api here for sending success message
                     navigationController?.navigate(
                         R.id.action_orderSummaryFragment_to_orderSuccessFragment,
                         bundleOf(
                             "estimatedDeliveryTime" to estimatedDeliveryTime,
                             "isPaymentSuccessful" to true
                         )
                     )
                 } else {
                     isPaymentSuccessful = true
                     viewModel.submitButtonAlreadyClicked = false
                     placeOrder(orderId,paymentId , true)
                 }
             }
     }

      private fun placeOrder(orderId: String?, paymentId:String?, isPaymentSuccess:Boolean) {
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
                SingleMealUserOrderDetail(orderId,paymentId,isPaymentSuccess,
                    sharedViewModel.userDetail?.username, sharedViewModel.userDetail?.mobileno,
                    sharedViewModel.totalPrice, viewModel.grandTotal, sharedViewModel.cartItemList,
                    getCurrentDate(),sharedViewModel.lunchOrDinnerTime,refereePerOrderDetail,orderRedemeedPrice
                )
            )
        }
    }

    override fun receivedResponse(item: Any?) {
        item?.let {
            if (item is ApiResponse) {
                orderSuccess = true
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
                    estimatedDeliveryTime = item.msg
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
                    sharedViewModel.userDetail?.hasOrdered = true
                    sharedPrefManager.addModelClass(Constants.USER_INFO, sharedViewModel.userDetail)
                    sharedViewModel.cartItemList.clear()
                    sharedViewModel.totalPrice = 0
                    sharedViewModel.totalPriceForMonthlyUser = 0
                    sharedViewModel.noOfItemAddedInCart = 0
                    if (isPaymentSuccessful) {
                        navigationController?.navigate(
                            R.id.action_orderSummaryFragment_to_orderSuccessFragment,
                            bundleOf(
                                "estimatedDeliveryTime" to item.msg,
                                "isPaymentSuccessful" to true
                            )
                        )
                    }
                }
            }
        }
    }

    private fun startPayment(checkout: Checkout, amount:Double) {
        showLoading()
        try {
            CoroutineScope(Dispatchers.Main).launch {
                val order_id = (Dispatchers.IO) {
                    val razorpay =
                        RazorpayClient("", "")
                    val orderRequest = JSONObject()
                    orderRequest.put("amount", amount*100) // amount in the smallest currency unit
                    orderRequest.put("currency", "INR")
                    orderRequest.put("receipt", "order_rcptid_11")
                    val orderx = razorpay.orders.create(orderRequest)
                    JSONObject(orderx.toString()).getString("id")
                }
                val options = JSONObject()
                options.put("name", "Mealy")
                options.put("description", "Food Charges")
                //You can omit the image option to fetch the image from the dashboard
                options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg")
                options.put("theme.color", "#DF4F4F")
                options.put("currency", "INR")
                options.put("order_id", order_id)
                options.put("amount", amount*100)

                val retryObj = JSONObject();
                retryObj.put("enabled", true)
                retryObj.put("max_count", 4)
                options.put("retry", retryObj)

                val prefill = JSONObject()
                prefill.put("contact", sharedViewModel.userDetail?.mobileno)

                options.put("prefill", prefill)
                checkout.open(activity, options)
            }
        } catch (e: Exception) {
            hideLoading()
            showToast("Something went wrong , Please try again")

        } catch (e: RazorpayException) {
            // Handle Exception
            hideLoading()
            showToast("Something went wrong , Please try again")
        }
    }


    fun callbackFromCartListItemsBottomSheet(){
        singleMealUser()
        binding.llTotalPrice.setOnClickListener {
            navigationController?.navigate(R.id.action_orderSummaryFragment_to_cartListItemsBottomSheet)
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
        } else {
            binding.view.visibility=View.GONE
            binding.tvTotalMealCount.visibility=View.GONE
        }
        context?.let { binding.tvTotalMealCount.setTextColor(it.getColor(R.color.colorPrimary)) }
        binding.tvTotalMealPrice.text = rsAppendedValue(sharedViewModel.totalPrice.toString())
        binding.tvGstPrice.text = rsAppendedValue(String.format("%.1f",(sharedViewModel.totalPrice * 0.05)))
        binding.tvDeliveryCharge.text = rsAppendedValue(sharedViewModel.deliveryPrice)
        viewModel.grandTotal = sharedViewModel.totalPrice + (sharedViewModel.totalPrice * 0.05) + sharedViewModel.deliveryPrice.toInt() - orderRedemeedPrice - totalDiscountedPrice

//        val totalPriceWithoutDiscount = sharedViewModel.totalPrice + (sharedViewModel.totalPrice * 0.05) + 3
//        if (orderRedemeedPrice > 0.0) {
//            binding.tvTotalPrice.text =
//                rsAppendedValue(String.format("%.1f", totalPriceWithoutDiscount))
//            binding.tvTotalPrice.setPaintFlags(binding.tvTotalPrice.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
//            binding.tvTotalPrice.visibility = View.VISIBLE
//        }
        val totalPriceWithoutDiscount = sharedViewModel.totalPrice + (sharedViewModel.totalPrice * 0.05) + sharedViewModel.deliveryPrice.toInt()
        if (totalDiscountedPrice > 0.0) {
            binding.tvTotalPrice.text =
                rsAppendedValue(String.format("%.1f", totalPriceWithoutDiscount))
            binding.tvTotalPrice.setPaintFlags(binding.tvTotalPrice.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
            binding.tvTotalPrice.visibility = View.VISIBLE

            binding.tvTotalPriceWithoutDiscount.text =
                rsAppendedValue(String.format("%.1f", totalPriceWithoutDiscount))
            binding.tvTotalPriceWithoutDiscount.setPaintFlags(binding.tvTotalPrice.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
            binding.tvTotalPriceWithoutDiscount.visibility = View.VISIBLE
        }
        binding.tvGrandTotalPrice.text = rsAppendedValue(String.format("%.1f", viewModel.grandTotal))
        binding.tvTotalGrandPrice.text = rsAppendedValue(String.format("%.1f", viewModel.grandTotal))
        binding.llTotalPrice.setOnClickListener {
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
}