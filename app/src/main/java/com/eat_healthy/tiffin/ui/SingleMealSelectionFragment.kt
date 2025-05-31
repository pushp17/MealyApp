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