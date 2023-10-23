package com.eat_healthy.tiffin.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.eat_healthy.tiffin.R
import com.eat_healthy.tiffin.adapter.MyFavouriteMealAdapter
import com.eat_healthy.tiffin.databinding.FragmentMyFoodPreferenceBinding
import com.eat_healthy.tiffin.genericFiles.ListItem
import com.eat_healthy.tiffin.models.Sabji
import com.eat_healthy.tiffin.viewmodels.MonthlyFoodSelectionViewModel
import com.eat_healthy.tiffin.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyFoodPreferenceFragment: ListViewFragment<MyFavouriteMealAdapter, FragmentMyFoodPreferenceBinding>() {
    override val fragmentLayoutResId: Int
        get() = R.layout.fragment_my_food_preference
    private val viewModel: MonthlyFoodSelectionViewModel by viewModels(
        ownerProducer = { requireParentFragment() })
    private val sharedViewModel: SharedViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val gridLayoutManager = GridLayoutManager(requireContext(), 3)
        recyclerView?.layoutManager=gridLayoutManager
        super.onViewCreated(view, savedInstanceState)
        viewModel.apiResponseLivedata.observe(viewLifecycleOwner,observer)
        adapter.setOnClickListener(this)
        binding.cbOnlyRoti.isChecked=viewModel.cbOnlyRoti ?: false
        binding.cbRiceRoti.isChecked=viewModel.cbRiceRoti ?: false
        adapter.setItems(viewModel.sabjiList)
        btContinueClick()
    }

    private fun btContinueClick(){
        binding.tvContinue.setOnClickListener {
            viewModel.addWeeklyPreference()
            sharedViewModel.totalPriceForMonthlyUser=viewModel.moneyToBePaid
            if(binding.cbOnlyRoti.isChecked){
                viewModel.myFavouriteMeal.onlyRoti=true
            }
            if(binding.cbRiceRoti.isChecked){
                viewModel.myFavouriteMeal.riceAndRoti=true
            }
            viewModel.sabjiList.forEach {
                if(it.selected){
                    viewModel.myFavouriteMeal.preferredSabjiList.add(it)
                }
            }
            if (viewModel.latestMonthlySubscriptionPrice > 0) {
                viewModel.reDirection(navigationController, sharedViewModel)
            } else {
                (parentFragment as MonthlyFoodSelectionFragment)?.changeViewpagerPosition(0)
                showToast("Please Select Food For This Week")
            }
        }
    }

    override fun receivedResponse(item: Any?) {

    }

    override fun onClickItem(position: Int, item: ListItem?) {
        item?.let {
            if(item is Sabji)  {
                item.selected=!item.selected
                adapter.notifyItemChanged(position)
            }
        }
    }
}
