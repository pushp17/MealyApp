package com.eat_healthy.tiffin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eat_healthy.tiffin.R
import com.eat_healthy.tiffin.adapter.SelectMealAdapter
import com.eat_healthy.tiffin.databinding.FragmentSingleMealSelectionBinding
import com.eat_healthy.tiffin.genericFiles.ListItem
import com.eat_healthy.tiffin.models.*
import com.eat_healthy.tiffin.utils.AppUtils.getStringValueInNextLine
import com.eat_healthy.tiffin.utils.Constants
import com.eat_healthy.tiffin.utils.DateAndTimeUtils
import com.eat_healthy.tiffin.utils.RecyclerviewItemClicklistener
import com.eat_healthy.tiffin.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

//@AndroidEntryPoint
//class SingleMealSelectionFragment: BaseFragment(), RecyclerviewItemClicklistener<ListItem> {
//    private var _binding: FragmentSingleMealSelectionBinding? = null
//    private val binding get() = _binding!!
//    lateinit var selectMealAdapter:SelectMealAdapter
//    private val sharedViewModel: SharedViewModel by activityViewModels()
//
//    private var mealCategoryPrice: Int = 0
//
////    private var specialMealCategoryHeaderEnable=false
////    private var todaysMealCategoryHeaderEnable=true
////
////    //special thali variables
////    private var specialMealSelected:Boolean=false
////    private var specialMealName:String?=null
////    private var todaySpecialMealPrice:String?=null
////
////    //normal thali variables
////    private var normalMealSelected:Boolean=false
////    private var mainMealName:String?=null
////    private var sabji:String?=null
////    private var dal:String?=null
////    private var extras:String?=null
//
//    lateinit var navigationController: NavController
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentSingleMealSelectionBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//        firebaseAnalytics.logEvent(Constants.SINGLE_MEAL_SELECTION_PAGE,null)
//        navigationController=findNavController()
//        setOrderStatus()
//        mealCategoryPrice=sharedViewModel.selectedPrice.toInt()
//        val linearLayoutManager = LinearLayoutManager(requireContext())
//        binding.rvSelectMeal.layoutManager=linearLayoutManager
//        selectMealAdapter= SelectMealAdapter()
//        selectMealAdapter.setOnClickListener(this)
//        binding.rvSelectMeal.adapter=selectMealAdapter
//
//        selectMealAdapter.setItems(sharedViewModel.SelectMealMutableList)
//        if(sharedViewModel.noOfItemAddedInCart>0)  binding.flCartItem.visibility=View.VISIBLE else binding.flCartItem.visibility=View.VISIBLE
//
//        binding.flCartItem.setOnClickListener {
//            //  navigationController.navigate(R.id.action_foodSelectionFragment_to_cartListItemsBottomSheet)
//            navigationController.navigate(R.id.action_foodSelectionFragment_to_mainCourseSelectionBottomSheet)
//        }
//
//        if (sharedViewModel.noOfItemAddedInCart > 0) binding.flCartItem.visibility = View.VISIBLE else binding.flCartItem.visibility = View.VISIBLE
//        binding.flCartItem.text = sharedViewModel.noOfItemAddedInCart.toString()
//
//        binding.rvSelectMeal.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                if(newState == RecyclerView.SCROLL_STATE_IDLE){
//                    firebaseAnalytics.logEvent(Constants.SINGLE_MEAL_SELECTION_PAGE_SCROLLED,null)
//                }
//                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    firebaseAnalytics.logEvent(Constants.SINGLE_MEAL_SCROLLED_AT_END,null)
//                }
//            }
//        })
//        return root
//    }
//
//    private fun setOrderStatus() {
//        if (sharedViewModel.isServiceStarted != true) {
//            binding.tvStatus.text = getStringValueInNextLine(sharedViewModel.statusMsg)
//            return
//        }
//        sharedViewModel.apiResponse?.let {
//            when (DataAndTimeUtils.checkLunchOrDinnerTime(
//                it.lunchStartTime!!,
//                it.lunchEndTime!!,
//                it.dinnerStartTime!!,
//                it.dinnerEndTime!!
//            )) {
//                Constants.LUNCH -> {
//                    // Here Reinitializing "enableLunchTime" and "enableDinnerTime" so that update value
//                    // should be there in variable because From SharedViewmodel it initializes once only
//                    sharedViewModel.enableLunchTime = true
//                    sharedViewModel.enableDinnerTime = false
//                    binding.tvStatus.text = getStringValueInNextLine(sharedViewModel.aStartSingleMealStatusLunchTime)
//                }
//
//                Constants.DINNER -> {
//                    sharedViewModel.enableDinnerTime = true
//                    sharedViewModel.enableLunchTime = false
//                    binding.tvStatus.text = getStringValueInNextLine(sharedViewModel.afterStartSingleMealStatusDinnerTime)
//                }
//                else -> {
//                    // Time out
//                    binding.tvStatus.text =
//                        getStringValueInNextLine(sharedViewModel.afterStartOrderTimeout)
//
//                }
//            }
//        }
//    }
//
//    override fun receivedResponse(item: Any?) {
//    }
//
//    override fun onClickItem(position: Int, item: ListItem?) {
//        when (item) {
//            is SpecialMeal -> {
//             //   navigationController.navigate(R.id.action_foodSelectionFragment_to_mainCourseSelectionBottomSheet)
//            }
//            is Meal -> {
//               // navigationController.navigate(R.id.action_foodSelectionFragment_to_mainCourseSelectionBottomSheet)
//            }
//            is Button -> {
////                if (item.enable == false) return
//                firebaseAnalytics.logEvent(Constants.SINGLE_MEAL_CONTINUE_BUTTON_CLICKED,null)
//                when(position){
//                    0 -> {
//                        if (sharedViewModel.userDetail?.isUserSignedIn == true) {
//                            navigationController.navigate(R.id.action_foodSelectionFragment_to_completeAddressFragment)
//                        } else {
//                            val bundle = bundleOf(Constants.SCREEN_ENTRY_POINT to Constants.HOME_PAGE_TAB)
//                            navigationController.navigate(R.id.action_foodSelectionFragment_to_loginFragment,bundle)
//                        }
//                        return
//                    }
//                    else -> {
//                        addItemInCart(item)
//                    }
//                }
//            }
//            else -> {
//            }
//        }
//       // selectMealAdapter.notifyDataSetChanged()
//    }
//
//    private fun addItemInCart(item: Button) {
//        var numberOfItems = 0
//        if(sharedViewModel.specialMealSelected){
//            sharedViewModel.cartItemList.add(ItemsInCart(sharedViewModel.specialMealName.toString(),mealCategoryPrice.toString(),sharedViewModel.mealCategoryType.toString()))
//            numberOfItems++
//        }
//        if(sharedViewModel.normalMealSelected){
//            numberOfItems++
//            val normalMealName = StringBuilder()
//            if(!sharedViewModel.mainMealName.isNullOrEmpty())
//                normalMealName.append(sharedViewModel.mainMealName).append(" . ")
//
//            if (!sharedViewModel.sabji.isNullOrEmpty())
//                normalMealName.append(sharedViewModel.sabji).append(" . ")
//
//            if (!sharedViewModel.dal.isNullOrEmpty())
//                normalMealName.append(sharedViewModel.dal).append(" . ")
//
//            if (!sharedViewModel.extras.isNullOrEmpty())
//                normalMealName.append(sharedViewModel.extras).append(" . ")
//
//            sharedViewModel.cartItemList.add(ItemsInCart(normalMealName.toString(),mealCategoryPrice.toString(),sharedViewModel.mealCategoryType.toString()))
//        }
//        sharedViewModel.noOfItemAddedInCart = sharedViewModel.noOfItemAddedInCart+numberOfItems
//        sharedViewModel.totalPrice=sharedViewModel.totalPrice+(mealCategoryPrice*numberOfItems)
//        item.itemCount=sharedViewModel.noOfItemAddedInCart
//        item.itemPrice=sharedViewModel.totalPrice.toString()
//
//        if(item.itemCount==0) {
//            showToast("Please Add Your Food")
//            return
//        }
//        showToast("Item Added In Cart")
//        if (sharedViewModel.noOfItemAddedInCart > 0) binding.flCartItem.visibility = View.VISIBLE else binding.flCartItem.visibility = View.GONE
//        binding.flCartItem.text = sharedViewModel.noOfItemAddedInCart.toString()
//        //     refreshPage()
//    }
//
//    private fun refreshPage(){
//        selectMealAdapter.getItemsList().forEach {
//            when(it){
//                is SpecialMeal->it.selected=false
//                is MainMeal ->it.selected=false
//                is Sabji-> it.selected=false
//                is MealCategoryHeader -> it.selected=false
//            }
//            sharedViewModel.specialMealSelected=false
//            sharedViewModel.normalMealSelected=false
//            selectMealAdapter.notifyDataSetChanged()
//        }
//        showToast("Item Added In Cart")
//    }
//
//    override fun onClickItem(position: Int, item: ListItem?, id: String?) {
//    }
//}

















@AndroidEntryPoint
class SingleMealSelectionFragment: BaseFragment(), RecyclerviewItemClicklistener<ListItem> {
    private var _binding: FragmentSingleMealSelectionBinding? = null
    private val binding get() = _binding!!
    lateinit var selectMealAdapter: SelectMealAdapter
    private val sharedViewModel: SharedViewModel by activityViewModels()
    var itemClickedPosition = 0
    val adapterList = mutableListOf<ListItem>()

    lateinit var navigationController: NavController
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSingleMealSelectionBinding.inflate(inflater, container, false)
        val root: View = binding.root
        adapterList.clear()
        if(arguments?.getString("mealType").equals("veg")){
            adapterList.addAll(sharedViewModel.mealMutableList.filter { it.showAsSingleItem && (it.nonVeg == null || it.nonVeg == false) })
        }else {
            adapterList.addAll(sharedViewModel.mealMutableList.filter { it.showAsSingleItem && (it.nonVeg == true) })
        }
        adapterList.add(Empty(""))
        firebaseAnalytics.logEvent(Constants.SINGLE_MEAL_SELECTION_PAGE, null)
        navigationController = findNavController()
        setOrderStatus()
        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.rvSelectMeal.layoutManager = linearLayoutManager
        selectMealAdapter = SelectMealAdapter()
        selectMealAdapter.setOnClickListener(this)
        binding.rvSelectMeal.adapter = selectMealAdapter
        selectMealAdapter.setItems(adapterList)
        updateCartIcon()
        useCartButtonLayoutAsPerCondition()
        binding.flCartItem.text = sharedViewModel.noOfItemAddedInCart.toString()

        binding.rvSelectMeal.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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

        binding.rlCart.setOnClickListener {
            continueButtonClickAction()
        }

        // use this code to show message when order time is over

//        if (false) {
//            binding.rlCart.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.colorPrimaryLight))
//            binding.llNext.visibility = View.GONE
//            binding.tvItemCount.text="Today's Order Time is Over"
//        }

        return root
    }

    private fun continueButtonClickAction() {
        if (sharedViewModel.userDetail?.isUserSignedIn == true) {
//            navigationController.navigate(R.id.action_foodSelectionFragment_to_completeAddressFragment)
        } else {
            val bundle =
                bundleOf(Constants.SCREEN_ENTRY_POINT to Constants.HOME_PAGE_TAB)
            navigationController.navigate(
                R.id.action_foodSelectionFragment_to_loginFragment,
                bundle
            )
        }
    }

    private fun setOrderStatus() {
        if (sharedViewModel.isServiceStarted != true) {
            binding.tvStatus.text = getStringValueInNextLine(sharedViewModel.statusMsg)
            return
        }
        sharedViewModel.apiResponse?.let {
            when (DateAndTimeUtils.checkLunchOrDinnerTime(
                it.lunchStartTime!!,
                it.lunchEndTime!!,
                it.dinnerStartTime!!,
                it.dinnerEndTime!!
            )) {
                Constants.LUNCH -> {
                    // Here Reinitializing "enableLunchTime" and "enableDinnerTime" so that update value
                    // should be there in variable because From SharedViewmodel it initializes once only
                    sharedViewModel.enableLunchTime = true
                    sharedViewModel.enableDinnerTime = false
                    binding.tvStatus.text =
                        getStringValueInNextLine(sharedViewModel.aStartSingleMealStatusLunchTime)
                }

                Constants.DINNER -> {
                    sharedViewModel.enableDinnerTime = true
                    sharedViewModel.enableLunchTime = false
                    binding.tvStatus.text =
                        getStringValueInNextLine(sharedViewModel.afterStartSingleMealStatusDinnerTime)
                }

                else -> {
                    // Time out
                    binding.tvStatus.text =
                        getStringValueInNextLine(sharedViewModel.afterStartOrderTimeout)

                }
            }
        }
    }


    override fun onClickItem(position: Int, item: ListItem?, id: String?) {
    }

    fun updateCart(numberOfItems: Int?, price: Int?) {
        if (numberOfItems != null && numberOfItems > 0) {
            sharedViewModel.noOfItemAddedInCart =
                sharedViewModel.noOfItemAddedInCart.plus(numberOfItems)
            sharedViewModel.totalPrice = sharedViewModel.totalPrice.plus(
                price!!
            )
            (selectMealAdapter.getItemsList()
                .getOrNull(itemClickedPosition) as Meal).addButtonCountText.set(numberOfItems)
            selectMealAdapter.notifyItemChanged(itemClickedPosition)
        }
        updateCartIcon()
        useCartButtonLayoutAsPerCondition()
    }

    private fun useCartButtonLayoutAsPerCondition() {
        if (sharedViewModel.noOfItemAddedInCart > 0) {
            binding.rlCart.visibility = View.VISIBLE
            updateCartIcon()
            binding.tvItemCount.text =
                sharedViewModel.noOfItemAddedInCart.toString().plus(" ITEM").plus(" | ")
                    .plus(sharedViewModel.totalPrice).plus(" Rs")
        } else if (false) {
            // use this condition when Mealy is close
            binding.rlCart.visibility = View.VISIBLE
        } else {
            binding.rlCart.visibility = View.GONE
        }
    }

    private fun updateCartIcon() {
        if (sharedViewModel.noOfItemAddedInCart > 0) binding.flCartItem.visibility =
            View.VISIBLE else binding.flCartItem.visibility = View.GONE
        binding.flCartItem.text = sharedViewModel.noOfItemAddedInCart.toString()
    }

    fun removeItemCallback(position: Int, numberOfItems: Int) {

    }

    override fun receivedResponse(item: Any?) {
    }

    override fun onClickItem(position: Int, item: ListItem?) {
    }
}