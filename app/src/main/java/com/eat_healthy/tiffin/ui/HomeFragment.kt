package com.eat_healthy.tiffin.ui
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.eat_healthy.tiffin.adapter.SelectMealAdapter
import com.eat_healthy.tiffin.databinding.FragmentHomeBinding
import com.eat_healthy.tiffin.genericFiles.ListItem
import com.eat_healthy.tiffin.models.Empty
import com.eat_healthy.tiffin.models.FoodReview
import com.eat_healthy.tiffin.models.ItemsInCart
import com.eat_healthy.tiffin.models.Meal
import com.eat_healthy.tiffin.models.MealsApiRespone
import com.eat_healthy.tiffin.utils.AppUtils.getStringValueInNextLine
import com.eat_healthy.tiffin.utils.Constants
import com.eat_healthy.tiffin.utils.Constants.HOME_PAGE
import com.eat_healthy.tiffin.utils.Constants.HOME_PAGE_SUCCESS
import com.eat_healthy.tiffin.utils.DateAndTimeUtils
import com.eat_healthy.tiffin.utils.RecyclerviewItemClicklistener
import com.eat_healthy.tiffin.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.eat_healthy.tiffin.R
import com.eat_healthy.tiffin.models.User
import com.google.android.material.dialog.MaterialAlertDialogBuilder


@AndroidEntryPoint
class HomeFragment : ListViewFragment<SelectMealAdapter, FragmentHomeBinding>() ,
    RecyclerviewItemClicklistener<ListItem> {
    override val fragmentLayoutResId: Int
        get() = R.layout.fragment_home
    private var window: Window? = null
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var itemClickedPosition = 0


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        window = activity?.window
        Constants.BASEURL =
            sharedPrefManager.getStringFromPreference(Constants.BASEURL, Constants.DEFAULT_BASE_URL)
                ?: Constants.DEFAULT_BASE_URL
        if (Calendar.getInstance().timeInMillis > (sharedViewModel.returnTheLatestPassedTime ?: 0L)) {
            sharedViewModel.returnTheLatestPassedTime =
                DateAndTimeUtils.returnTheLatestPassedTime(sharedViewModel.returnTheLatestPassedTime)
            sharedViewModel.apiResponse = null
        }
        firebaseAnalytics.logEvent(HOME_PAGE,null)
        sharedViewModel.userDetail = sharedPrefManager.getModelClass(Constants.USER_INFO)
        sharedViewModel.homePageData()
        sharedViewModel.mealsLivedata.observe(viewLifecycleOwner, observer)
        binding.flCartItem.text = sharedViewModel.noOfItemAddedInCart.toString()

        binding.rlCart.setOnClickListener {
            continueButtonClickAction()
        }

        binding.tvAll.setOnClickListener {
            selectFoodTypeBackground("All")
        }

        binding.tvPureVeg.setOnClickListener {
            selectFoodTypeBackground("Pure Veg")
        }

        binding.tvVegEgg.setOnClickListener {
            selectFoodTypeBackground("Veg + Egg")
        }

        binding.tvNonVeg.setOnClickListener {
            selectFoodTypeBackground("Non Veg")
        }

        binding.flCartItem.setOnClickListener {
            navigationController?.navigate(R.id.action_navigation_home_to_cartListItemsBottomSheet)
        }

        binding.rvParent.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    firebaseAnalytics.logEvent(Constants.SINGLE_MEAL_SELECTION_PAGE_SCROLLED, null)
                }
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    firebaseAnalytics.logEvent(Constants.SINGLE_MEAL_SCROLLED_AT_END, null)
                }
            }
        })
        if (sharedViewModel.userDetail?.isUserSignedIn == true && sharedViewModel.userDetail?.hasOrdered != true)
        {
            sharedViewModel.getUserDetails(
                User(
                    sharedViewModel.userDetail?.username,
                    sharedViewModel.userDetail?.mobileno
                ), sharedPrefManager
            )
        }
    }




    override fun receivedResponse(item: Any?) {
        item?.let { response ->
            when (response) {
                is MealsApiRespone -> {
                    val baseUrl = response.baseUrl ?: Constants.DEFAULT_BASE_URL
                    sharedPrefManager.addStringToPreference(Constants.BASEURL, baseUrl)
                    Constants.BASEURL = baseUrl
                    sharedViewModel.frameDataForHomePageV2(response)
                    updateUI()
                    firebaseAnalytics.logEvent(HOME_PAGE_SUCCESS,null)
                }
            }
        }
    }

    private fun updateUI() {
        if ((getVersionName() < (sharedViewModel.apiResponse?.appVersionName
                ?: 0.0)) && sharedViewModel.apiResponse?.playStoreLink != null
        ) {
            showAppUpdateDialog(Constants.PLAYSTORE_LINK)
        }
        if (sharedViewModel.apiResponse?.enableOffer == true) {
            binding.llWalletReferal.visibility = View.VISIBLE
        }
        setStatus()
        adapter.setOnClickListener(this)
        adapter.setItems(sharedViewModel.adapterList)
        updateCartIcon()
        useCartButtonLayoutAsPerCondition()
        try {
            if (sharedPrefManager.getModelClass<FoodReview>(Constants.FOOD_REVIEW) != null && !sharedViewModel.foodReviewIsShown &&
                System.currentTimeMillis() > sharedPrefManager.getLongFromPreference(Constants.ADD_4_HOURS_TO_ORDER_TIME)
            ) {
                navigationController?.navigate(R.id.action_navigation_home_to_foodRatinReviewBottomsheet)
                sharedViewModel.foodReviewIsShown = true
            }
        } catch (e: Exception) {
        }
    }

    private fun selectFoodTypeBackground(category:String) {

        binding.tvAll.backgroundTintList = null
        binding.tvPureVeg.backgroundTintList = null
        binding.tvVegEgg.backgroundTintList = null
        binding.tvNonVeg.backgroundTintList = null

        binding.tvAll.background = ContextCompat.getDrawable(requireContext(), com.eat_healthy.tiffin.R.drawable.oval_gray_border)
        binding.tvPureVeg.background = ContextCompat.getDrawable(
            requireContext(),
            com.eat_healthy.tiffin.R.drawable.oval_gray_border
        )
        binding.tvVegEgg.background = ContextCompat.getDrawable(
            requireContext(),
            com.eat_healthy.tiffin.R.drawable.oval_gray_border
        )
        binding.tvNonVeg.background = ContextCompat.getDrawable(
            requireContext(),
            com.eat_healthy.tiffin.R.drawable.oval_gray_border
        )


        binding.tvAll.setTextColor(ContextCompat.getColor(requireContext(),R.color.black_text))
        binding.tvPureVeg.setTextColor(ContextCompat.getColor(requireContext(),R.color.black_text))
        binding.tvVegEgg.setTextColor(ContextCompat.getColor(requireContext(),R.color.black_text))
        binding.tvNonVeg.setTextColor(ContextCompat.getColor(requireContext(),R.color.black_text))
        when (category) {
            "All" -> {
                binding.tvAll.backgroundTintList = ContextCompat.getColorStateList(requireActivity(), R.color.colorPrimary)
                binding.tvAll.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
                window?.statusBarColor = ContextCompat.getColor(requireContext(),R.color.colorPrimary)
                binding.toolbar.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                adapter.setItems(sharedViewModel.adapterList)
            }

            "Pure Veg" -> {
                binding.tvPureVeg.backgroundTintList = ContextCompat.getColorStateList(requireActivity(), R.color.colorPrimary_3)
                binding.tvPureVeg.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
                window?.statusBarColor = ContextCompat.getColor(requireContext(),R.color.colorPrimary_3)
                binding.toolbar.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.colorPrimary_3))
                adapter.setItems(sharedViewModel.adapterList.filter { (it is Meal && !it.type.equals("egg") && it.nonVeg != true) || it is Empty})
            }

            "Veg + Egg" -> {
                binding.tvVegEgg.backgroundTintList = ContextCompat.getColorStateList(requireActivity(), R.color.colorPrimary_2)
                binding.tvVegEgg.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
                window?.statusBarColor = ContextCompat.getColor(requireContext(),R.color.colorPrimary_2)
                binding.toolbar.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.colorPrimary_2))
                adapter.setItems(sharedViewModel.adapterList.filter { (it is Meal && it.nonVeg != true ) || it is Empty} )
            }

            "Non Veg" -> {
                binding.tvNonVeg.backgroundTintList = ContextCompat.getColorStateList(requireActivity(), R.color.colorPrimary)
                binding.tvNonVeg.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
                window?.statusBarColor = ContextCompat.getColor(requireContext(),R.color.colorPrimary)
                binding.toolbar.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.colorPrimary))
                adapter.setItems(sharedViewModel.adapterList.filter { (it is Meal && it.nonVeg == true ) || it is Empty})
            }
        }
    }

    override fun onClickItem(position: Int, item: ListItem?, id: String?) {
        when (item) {
            is Meal -> {
                when (id) {
                    "plus" -> {
                        if (item.type.equals("extras")) {
                            itemClickedPosition = position
                            sharedViewModel.cartItemList.add(ItemsInCart(item.name!!, item.price!!,item.name))
                            updateAdapterItemAndCart(1, item.price.toInt())
                        } else {
                            openBaseItemsSelectionBottomsheet(position, item)
                        }
                    }

                    "minus" -> {
                        navigationController?.navigate(R.id.action_navigation_home_to_cartListItemsBottomSheet)
                    }

                    "addBtn" -> {
                        if (item.isLunchOrDinnerTime == Constants.LUNCH_TIMEOUT || item.isLunchOrDinnerTime == Constants.TIME_OUT) {
                            navigationController?.navigate(R.id.action_navigation_home_to_orderTimmingSlotBottomSheet)
                        } else if (item.type.equals("extras")) {
                            itemClickedPosition = position
                            sharedViewModel.cartItemList.add(
                                ItemsInCart(
                                    item.name!!,
                                    item.price!!,
                                    item.name
                                )
                            )
                            updateAdapterItemAndCart(1, item.price.toInt())
                        } else {
                            openBaseItemsSelectionBottomsheet(position, item)
                        }
                    }
                }
            }
            else -> {}
        }
        adapter.notifyDataSetChanged()
    }

    private fun openBaseItemsSelectionBottomsheet(position: Int, item: Meal) {
        itemClickedPosition = position
        val bundle = bundleOf(
            "mealType" to item.type,
            "meal" to item.name,
            "price" to item.price
        )
        navigationController?.navigate(
            R.id.action_navigation_home_to_mainCourseSelectionBottomSheet,
            bundle
        )
    }

    fun callbackFromBaseCourseSelectionBottomSheet(numberOfItems: Int? = 0, totalPrice: Int? = 0) {
      updateAdapterItemAndCart(numberOfItems,totalPrice)
    }

    fun callbackFromCartBottomSheet(ifAnyItemRemoved: Boolean?) {
        if (ifAnyItemRemoved == true) {
            adapter.notifyDataSetChanged()
            updateCartIcon()
            useCartButtonLayoutAsPerCondition()
        }
    }

    private fun updateAdapterItemAndCart(numberOfItems: Int?, price: Int?) {
        if (numberOfItems != null && numberOfItems > 0) {
            sharedViewModel.noOfItemAddedInCart = sharedViewModel.noOfItemAddedInCart.plus(numberOfItems)
            sharedViewModel.totalPrice = sharedViewModel.totalPrice.plus(price!!)
            val meal = adapter.getItemsList().getOrNull(itemClickedPosition) as Meal
            meal.addButtonCountText.set(meal.addButtonCountText.get()+numberOfItems)
            adapter.notifyItemChanged(itemClickedPosition)
        }
        updateCartIcon()
        useCartButtonLayoutAsPerCondition()
    }



    private fun updateCartIcon() {
        if (sharedViewModel.noOfItemAddedInCart > 0) binding.flCartItem.visibility =
            View.VISIBLE else binding.flCartItem.visibility = View.GONE
        binding.flCartItem.text = sharedViewModel.noOfItemAddedInCart.toString()
    }

    private fun useCartButtonLayoutAsPerCondition() {
        if (sharedViewModel.noOfItemAddedInCart > 0) {
            binding.rlCart.visibility = View.VISIBLE
            updateCartIcon()
            binding.tvItemCount.text =
                sharedViewModel.noOfItemAddedInCart.toString().plus(" ITEM").plus(" | ")
                    .plus(sharedViewModel.totalPrice).plus(" Rs")
        } else {
            binding.rlCart.visibility = View.GONE
        }
    }

    private fun continueButtonClickAction() {
        if (sharedViewModel.userDetail?.isUserSignedIn == true) {
            navigationController?.navigate(R.id.action_navigation_home_to_completeAddressFragment)
        } else {
            val bundle =
                bundleOf(Constants.SCREEN_ENTRY_POINT to Constants.HOME_PAGE_TAB)
            navigationController?.navigate(
                R.id.action_navigation_home_to_loginFragment,
                bundle
            )
        }
    }

    private fun setStatus() {
        if (sharedViewModel.lunchOrDinnerTime == Constants.LUNCH_TIMEOUT && !sharedViewModel.showStatusAtHomePage) {
            binding.llOpenStatus.visibility = View.VISIBLE
            binding.tvStatus.text = sharedViewModel.apiResponse?.statusMsgV2
        } else if (sharedViewModel.lunchOrDinnerTime == Constants.TIME_OUT && !sharedViewModel.showStatusAtHomePage) {
            binding.tvStatus.text = sharedViewModel.apiResponse?.afterStartOrderTimeout
            binding.llOpenStatus.visibility = View.VISIBLE
        } else if (sharedViewModel.showStatusAtHomePage) {
            binding.llOpenStatus.visibility = View.VISIBLE
            binding.tvStatus.text =
                getStringValueInNextLine(sharedViewModel.apiResponse?.statusMsgV2)
        }
    }

    private fun showAppUpdateDialog(playStoreLink: String) {
        val alertDialog = SweetAlertDialogV2(
            requireActivity(),
            SweetAlertDialogV2.CUSTOM_IMAGE_TYPE
        )
        alertDialog.setCancelable(false)
        alertDialog.setCustomImage(R.drawable.info_1)
        alertDialog.titleText = "Update"
        alertDialog.contentText =
            "Please Update The App To Continue."
        alertDialog.show()
        alertDialog.setConfirmClickListener {
            val uri: Uri =
                Uri.parse(playStoreLink)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }

    private fun getVersionName() = requireActivity().packageManager.getPackageInfo(
        requireActivity().packageName,
        0
    ).versionName.toDoubleOrNull() ?: 0.0

    override fun onPause() {
        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}






//@AndroidEntryPoint
//class HomeFragment : ListViewFragment<MealAdapter, FragmentHomeBinding>() {
//    override val fragmentLayoutResId: Int
//        get() = R.layout.fragment_home
//    private val sharedViewModel: SharedViewModel by activityViewModels()
//    var window: Window? = null
//    private var referrerClient: InstallReferrerClient? = null
//    var handler: Handler? = null
//    var runnable: Runnable? = null
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        //  sharedViewModel.homePageData()
//    }
//   //  window?.statusBarColor = ContextCompat.getColor(requireContext(),R.color.colorPrimary_3)
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
//        binding.rvParent.layoutManager = gridLayoutManager
//        super.onViewCreated(view, savedInstanceState)
//        window = activity?.window
//
//            firebaseAnalytics.logEvent(HOME_PAGE,null)
//            sharedViewModel.homePageData()
//            sharedViewModel.userDetail = sharedPrefManager.getModelClass(Constants.USER_INFO)
//            sharedViewModel.mealsLivedata.observe(viewLifecycleOwner, observer)
//            gridLayoutManager.spanSizeLookup =
//            object : GridLayoutManager.SpanSizeLookup() {
//                override fun getSpanSize(position: Int): Int {
//                    //define span size for this position
//                    //some example for your first three items
//                    return if (adapter.getItemsList()
//                            .get(position) is Header || adapter.getItemsList()
//                            .get(position) is SpecialMeal
//                    ) {
//                        2
//                    } else {
//                        1 //item will take 1/2 space of row
//                    }
//                }
//            }
//
//        binding.flAddItem.setOnClickListener {
//            if (sharedPrefManager.getStringFromPreference(Constants.USER_CITY_LOCALITY, "")
//                    ?.isNotEmpty() == true
//            ) {
//                navigationController?.navigate(R.id.action_navigation_home_to_foodSelectionFragment)
//            } else {
//                navigationController?.navigate(R.id.action_navigation_home_to_currentLocationMapFragment)
//            }
//        }
//
//        binding.cvReferAndEarn.setOnClickListener {
//            if (sharedViewModel.userDetail?.isUserSignedIn == true)
//                navigationController?.navigate(R.id.action_navigation_home_to_referalFragment)
//            else {
//                val bundle =
//                    bundleOf(Constants.SCREEN_ENTRY_POINT to Constants.REFER_EARN_FROM_HOME)
//                navigationController?.navigate(
//                    R.id.action_navigation_home_to_loginFragment,
//                    bundle
//                )
//            }
//        }
//
//        binding.cvWallet.setOnClickListener {
//            navigationController?.navigate(R.id.action_navigation_home_to_walletFragment)
//        }
//
//            if (sharedPrefManager.getStringFromPreference(Constants.REFERAl_CODE, "")
//            ?.isEmpty() == true
//            )
//            {
////            https://play.google.com/store/apps/details?id=com.eat_healthy.tiffin&referrer=utm_source%3Drajnikant_referal
//
//                val referrerUrl = "utm_source=RAJ-0615"
//                val referalCode = referrerUrl.substring(
//                    referrerUrl.lastIndexOf("=") + 1,
//                    referrerUrl.length
//                )
//                sharedPrefManager.addStringToPreference(Constants.REFERAl_CODE, referalCode)
////            enableReferalCode()
//            }
//
//            // Call User Details Api if user is signed in
//            if (sharedViewModel.userDetail?.isUserSignedIn == true)
//            {
//                sharedViewModel.getUserDetails(
//                    User(
//                        sharedViewModel.userDetail?.username,
//                        sharedViewModel.userDetail?.mobileno
//                    ), sharedPrefManager
//                )
//            }
//        }
//        var position_1: Int = 0
//        private fun createViewPager() {
//            val viewPager = binding.homeViewpager
//            val tabLayout = binding.tabLayout
//            val adapter = ViewPagerHomeAdapter()
//           // handler?.postDelayed(runnable, 300)
//            adapter.setItems(sharedViewModel.highlightedMutableList)
//            viewPager.adapter = adapter
//            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
//            }.attach()
//            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//                override fun onPageSelected(position: Int) {
//                    if (position > 0) {
//                        setViePagerPadding(viewPager, tabLayout, 60)
//                    } else {
//                        setViePagerPadding(viewPager, tabLayout, 30)
//                    }
//                    position_1 = position
//                }
//
//                override fun onPageScrollStateChanged(state: Int) {
//                    super.onPageScrollStateChanged(state)
//                    if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
//                        Log.d("xsdncxjksd", "dragged")
//                       // handler.removeCallbacks(runnable)
//                    }
//                }
//            })
//        }
//
//        private fun setViePagerPadding(
//            viewPager: ViewPager2,
//            tabLayout: TabLayout,
//            offsetLeftPx: Int
//        ) {
//            viewPager.apply {
//                clipToPadding = false   // allow full width shown with padding
//                clipChildren = false    // allow left/right item is not clipped
//                offscreenPageLimit = 2  // make sure left/right item is rendered
//            }
//            // increase this offset to show more of left/right
//            val offsetRightPx = 60
//            viewPager.setPadding(offsetLeftPx, 0, offsetRightPx, 0)
//            // increase this offset to increase distance between 2 items
//            val pageMarginPx = 30
//            val marginTransformer = MarginPageTransformer(pageMarginPx)
//            viewPager.setPageTransformer(marginTransformer)
//        }
//
//        override fun receivedResponse(item: Any?) {
//            item?.let { response ->
//                when (response) {
//                    is MealsApiRespone -> {
//                        sharedViewModel.referalRewardMaxLimitPerUser =
//                            response.referalRewardMaxLimitPerUser
//                        sharedViewModel.rewardPercentagePerOrder = response.rewardPercentagePerOrder
//                        updateUI(response)
////                    sharedViewModel.userDetail?.isUserSignedIn==true
//                        if (true) {
//                            // add one more condition to check if 24 hours are over or not
//                            sharedViewModel.fetchReferalData(
//                                User(
//                                    sharedViewModel.userDetail?.username,
//                                    sharedViewModel.userDetail?.mobileno
//                                ), sharedPrefManager
//                            )
//                        }
//                    }
//                }
//            }
//        }
//
//        private fun updateUI(response: MealsApiRespone) {
//
//            if (!sharedViewModel.doesShowlandingpageAsRegisterValueUpdated) {
//                sharedViewModel.doesShowlandingpageAsRegisterValueUpdated = true
//                sharedViewModel.showlandingpageAsRegister = response.showRegisterPage
//            }
//            // binding.llWalletReferal.visibility = View.VISIBLE
//            sharedViewModel.statusMsg = response.statusMsg
//            sharedViewModel.contact = response.contactNo
//            sharedViewModel.status2 = response.statusMsg2
//            sharedViewModel.monthlySubscriptionMsg = response.monthlySubscriptionMsg
//            sharedViewModel.afterStartOrderTimeout = response.afterStartOrderTimeout
//            sharedViewModel.aStartSingleMealStatusLunchTime =
//                response.aStartSingleMealStatusLunchTime
//            sharedViewModel.afterStartSingleMealStatusDinnerTime =
//                response.afterStartSingleMealStatusDinnerTime
//            sharedViewModel.afterRegistrationStatusMsg = response.afterRegistrationStatusMsg
//            sharedViewModel.registerSuccessMsg = response.registerSuccessMsg
//            sharedViewModel.isServiceStarted = response.isServiceStarted
//
//            if ((getVersionName() < response.appVersionName) && response.playStoreLink != null) {
//                showAppUpdateDialog(Constants.PLAYSTORE_LINK)
//            }
//            sharedViewModel.regularMealPrice = response.regularMealPrice ?: "59"
//            sharedViewModel.vegSpecialPrice = response.vegSpecialPrice ?: "79"
//            sharedViewModel.nonVegPrice = response.nonVegPrice ?: "89"
//            sharedViewModel.selectedPrice = sharedViewModel.regularMealPrice
//            sharedViewModel.frameDataForHomePageV2(response)
//            createViewPager()
//            setAdapter()
//        }
//
//        private fun setAdapter() {
//            val position = 0
//            navigationController = findNavController()
//            adapter.setOnClickListener(this)
//            adapter.setItems(sharedViewModel.homepageMutableList)
//        }
//
//        private fun showAppUpdateDialog(playStoreLink: String) {
//            val alertDialog = SweetAlertDialogV2(
//                requireActivity(),
//                SweetAlertDialogV2.CUSTOM_IMAGE_TYPE
//            )
//            alertDialog.setCancelable(false)
//            alertDialog.setCustomImage(R.drawable.info_1)
//            alertDialog.titleText = "Update"
//            alertDialog.contentText =
//                "We are pleased to inform you that we have started our services now so we request you to please UPDATE the app to start ordering."
//            alertDialog.show()
//            alertDialog.setConfirmClickListener {
//                val uri: Uri =
//                    Uri.parse(playStoreLink)
//                val intent = Intent(Intent.ACTION_VIEW, uri)
//                startActivity(intent)
//            }
//        }
//
//        private fun getVersionName() = requireActivity().packageManager.getPackageInfo(
//            requireActivity().packageName,
//            0
//        ).versionName.toDoubleOrNull() ?: 0.0
//
//        override fun onPause() {
//            window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//            window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
//            super.onPause()
//        }
//
//        override fun onDestroyView() {
//            referrerClient?.endConnection()
//            super.onDestroyView()
//        }
//
//
//        private fun enableReferalCode() {
//            referrerClient = InstallReferrerClient.newBuilder(requireActivity()).build()
//            referrerClient?.startConnection(object : InstallReferrerStateListener {
//
//                override fun onInstallReferrerSetupFinished(responseCode: Int) {
//                    when (responseCode) {
//                        InstallReferrerClient.InstallReferrerResponse.OK -> {
//                            // Connection established.
//                            referrerClient?.installReferrer?.let {
//                                val referrerUrl = it.installReferrer
//                                val referalCode = referrerUrl.substring(
//                                    referrerUrl.lastIndexOf("=") + 1,
//                                    referrerUrl.length
//                                )
//                                sharedPrefManager.addStringToPreference(
//                                    Constants.REFERAl_CODE,
//                                    referalCode
//                                )
//                            }
//                        }
//
//                        InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
//                            // API not available on the current Play Store app.
//                        }
//
//                        InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
//                            // Connection couldn't be established.
//                        }
//                    }
//                }
//
//                override fun onInstallReferrerServiceDisconnected() {
//                    // Try to restart the connection on the next request to
//                    // Google Play by calling the startConnection() method.
//                }
//            })
//        }
//    }
