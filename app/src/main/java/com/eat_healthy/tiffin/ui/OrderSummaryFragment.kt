package com.eat_healthy.tiffin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.eat_healthy.tiffin.R
import com.eat_healthy.tiffin.databinding.FragmentOrderSummaryBinding
import com.eat_healthy.tiffin.models.ApiResponse
import com.eat_healthy.tiffin.models.MUserAddress
import com.eat_healthy.tiffin.models.SingleMealUserOrderDetail
import com.eat_healthy.tiffin.utils.AppUtils.rsAppendedValue
import com.eat_healthy.tiffin.utils.Constants
import com.eat_healthy.tiffin.utils.DataAndTimeUtils.getCurrentDate
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
//    private lateinit var checkout:Checkout
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val address = arguments?.getParcelable<MUserAddress>("address")
        binding= FragmentOrderSummaryBinding.inflate(inflater,container,false)
        navigationController=findNavController()
        viewModel.apiResponseLivedata.observe(viewLifecycleOwner,observer)
        firebaseAnalytics.logEvent(Constants.ORDER_SUMMARY_SCREEN, null)
//        Checkout.preload(requireActivity())
//        checkout = Checkout()
//        checkout.setKeyID("rzp_test_UtrJfGAmzb1T7T")
        if (sharedViewModel.monthlyUser == true) {
            monthlyUser()
        }else {
            singleMealUser()
        }

       address?.userAddressList?.forEach {
           when(it.addressType){
               Constants.LUNCH_DINNER -> {
                   if (sharedViewModel.monthlyUser == true) {
                       binding.tvAddress1Heading.text = "Lunch And Dinner Address"
                   } else {
                       binding.tvAddress1Heading.visibility = View.GONE
                   }
                   binding.tvFirstAddress.text=(it.streetNo).plus(" , ").plus(it.cityAndLocality).plus(" . ").plus(it.landmark ?:"")
                   binding.tvAddress2Heading.visibility=View.GONE
                   binding.tvSecondAddress.visibility=View.GONE
               }
               Constants.LUNCH->{
                   binding.tvFirstAddress.text=(it.streetNo).plus(" , ").plus(it.cityAndLocality).plus(" . ").plus(it.landmark ?:"")
               }
               Constants.DINNER->{
                   binding.tvSecondAddress.text=(it.streetNo).plus(" , ").plus(it.cityAndLocality).plus(" . ").plus(it.landmark ?:"")
                   binding.tvAddress2Heading.visibility=View.VISIBLE
                   binding.tvSecondAddress.visibility=View.VISIBLE
               }
               else -> {
               }
           }
       }
        binding.tvContinue.setOnClickListener {
            address?.let { it1 -> userAddressViewModel.updateUserAddress(it1) }
//            startPayment(checkout, sharedViewModel.totalPrice)
            placeOrder("")

//            if (sharedViewModel.monthlyUser == true) {
//                sharedViewModel.monthlyUserPreference?.let { it1 ->
//                    viewModel.monthlyUserOrderServices(
//                        it1
//                    )
//                }
//            } else {
//                viewModel.singleUserOrderServices(SingleMealUserOrderDetail(
//                    sharedViewModel.userDetail?.username, sharedViewModel.userDetail?.mobileno,
//                    sharedViewModel.totalPrice, viewModel.grandTotal, sharedViewModel.cartItemList
//                ))
//            }
        }
        return binding.root
    }

    fun placeOrder(orderId: String?) {
        if (sharedViewModel.monthlyUser == true) {
            firebaseAnalytics.logEvent(Constants.MONTHLY_USER_ORDER_API_CALLED, null)
            sharedViewModel.monthlyUserPreference?.let { it1 ->
                viewModel.monthlyUserOrderServices(
                    it1
                )
            }
        } else {
            firebaseAnalytics.logEvent(Constants.SINGLE_USER_ORDER_API_CALLED, null)
            viewModel.singleUserOrderServices(
                SingleMealUserOrderDetail(orderId,
                    sharedViewModel.userDetail?.username, sharedViewModel.userDetail?.mobileno,
                    sharedViewModel.totalPrice, viewModel.grandTotal, sharedViewModel.cartItemList,
                    getCurrentDate(),sharedViewModel.lunchOrDinnerTime
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

        sharedViewModel.monthlyUserPreference?.username=sharedViewModel.userDetail?.username
        sharedViewModel.monthlyUserPreference?.mobileno=sharedViewModel.userDetail?.mobileno
        if(sharedViewModel.monthlyUserPreference?.grandTotalPrice?.toDoubleOrNull()==null){
            sharedViewModel.monthlyUserPreference?.grandTotalPrice=(sharedViewModel.totalPriceForMonthlyUser+(sharedViewModel.totalPriceForMonthlyUser * 0.05)+100).toString()
        }else {
            sharedViewModel.monthlyUserPreference?.latestGrandTotalPrice=(sharedViewModel.totalPriceForMonthlyUser+(sharedViewModel.totalPriceForMonthlyUser * 0.05)+100).toString()
        }

    }

    private fun singleMealUser(){
        binding.rlItem2.visibility=View.GONE
        binding.rlItem3.visibility=View.GONE
        binding.tvMealOrMealType1.text="1. ".plus(sharedViewModel.cartItemList.getOrNull(0)?.yourMeal)
        binding.tvValue1.text=rsAppendedValue(sharedViewModel.cartItemList.getOrNull(0)?.price)
        if(sharedViewModel.cartItemList.size>1){
            binding.rlItem2.visibility=View.VISIBLE
            binding.tvMealOrMealType2.text="2. ".plus(sharedViewModel.cartItemList.getOrNull(1)?.yourMeal)
            binding.tvValue2.text=rsAppendedValue(sharedViewModel.cartItemList.getOrNull(1)?.price)
        }
        if(sharedViewModel.cartItemList.size>2){
            binding.rlItem3.visibility=View.VISIBLE
            binding.tvMealOrMealType3.text="3. ".plus(sharedViewModel.cartItemList.getOrNull(2)?.yourMeal)
            binding.tvValue3.text=rsAppendedValue(sharedViewModel.cartItemList.getOrNull(2)?.price)
        }
        if(sharedViewModel.cartItemList.size>3){
            binding.tvTotalMealCount.visibility=View.VISIBLE
            binding.tvTotalMealCount.text = "View all"
        }else {
            binding.view.visibility=View.GONE
            binding.tvTotalMealCount.visibility=View.GONE
        }
        context?.let { binding.tvTotalMealCount.setTextColor(it.getColor(com.eat_healthy.tiffin.R.color.colorPrimary)) }
        binding.tvTotalMealPrice.text = rsAppendedValue(sharedViewModel.totalPrice.toString())
        binding.tvGstPrice.text = rsAppendedValue((sharedViewModel.totalPrice * 0.05).toString())
        binding.tvDeliveryCharge.text = rsAppendedValue("3")
        viewModel.grandTotal=sharedViewModel.totalPrice+(sharedViewModel.totalPrice * 0.05)+3
        binding.tvGrandTotalPrice.text= rsAppendedValue(viewModel.grandTotal.toString())
    }

    override fun receivedResponse(item: Any?) {
        item?.let {
            if(item is ApiResponse){
                if(sharedViewModel.monthlyUser == true){
                    sharedViewModel.subscriptionExpired = false
                    sharedViewModel.userDetail?.monthlySubscriptionMoney=viewModel.grandTotal
                    sharedViewModel.userDetail?.monthlyUser=true
                    sharedPrefManager.addModelClass(Constants.USER_INFO,sharedViewModel.userDetail)
                    sharedViewModel.cartItemList.clear()
                    sharedViewModel.totalPrice=0
                    sharedViewModel.totalPriceForMonthlyUser=0
                    sharedViewModel.noOfItemAddedInCart=0
                    val alertDialog = SweetAlertDialogV2(
                        requireActivity(),
                        SweetAlertDialogV2.SUCCESS_TYPE
                    )
                    alertDialog.setCancelable(false)
                    alertDialog.titleText = "Order"
                    alertDialog.contentText = sharedViewModel.monthlySubscriptionMsg
                    alertDialog.show()
                    alertDialog.setConfirmClickListener {
                        alertDialog.dismissWithAnimation()
                        navigationController?.popBackStack(R.id.navigation_home,false)

                    }
                }else {
                    sharedViewModel.userDetail?.monthlySubscriptionMoney=viewModel.grandTotal
                    sharedViewModel.userDetail?.monthlyUser=true
                    sharedPrefManager.addModelClass(Constants.USER_INFO,sharedViewModel.userDetail)
                    sharedViewModel.cartItemList.clear()
                    sharedViewModel.totalPrice=0
                    sharedViewModel.totalPriceForMonthlyUser=0
                    sharedViewModel.noOfItemAddedInCart=0
                    val alertDialog = SweetAlertDialogV2(
                        requireActivity(),
                        SweetAlertDialogV2.SUCCESS_TYPE
                    )
                    alertDialog.setCancelable(false)
                    alertDialog.titleText = "Order"
                    alertDialog.contentText = sharedViewModel.status2
                    alertDialog.show()
                    alertDialog.setConfirmClickListener {
                        alertDialog.dismissWithAnimation()
                        navigationController?.popBackStack(R.id.navigation_home,false)

                    }
                }
            }
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