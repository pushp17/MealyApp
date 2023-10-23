package com.eat_healthy.tiffin.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.eat_healthy.tiffin.R
import com.eat_healthy.tiffin.adapter.MealAdapter
import com.eat_healthy.tiffin.databinding.FragmentMealBinding
import com.eat_healthy.tiffin.genericFiles.ListItem
import com.eat_healthy.tiffin.models.Header
import com.eat_healthy.tiffin.models.SpecialMeal
import com.eat_healthy.tiffin.utils.Constants
import com.eat_healthy.tiffin.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MealFragment :ListViewFragment<MealAdapter,FragmentMealBinding>(){
    override val fragmentLayoutResId: Int
        get() = R.layout.fragment_meal
    private val sharedViewModel: SharedViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvParent.layoutManager=gridLayoutManager
        super.onViewCreated(view, savedInstanceState)
        val position:Int?=arguments?.getInt("position")
        navigationController=findNavController()
        adapter.setOnClickListener(this)
        when(position){
            0 -> adapter.setItems(sharedViewModel.normalMealMutableList)
            1->  adapter.setItems(sharedViewModel.silverMealMutableList)
            2->  adapter.setItems(sharedViewModel.goldMealMutableList)
        }
        gridLayoutManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                //define span size for this position
                //some example for your first three items
                return if (adapter.getItemsList().get(position) is Header || adapter.getItemsList().get(position) is SpecialMeal) {
                    2
                } else {
                    1 //item will take 1/2 space of row
                }
            }
        }
    }

    override fun onClickItem(position: Int, item: ListItem?) {
        if (sharedPrefManager.getStringFromPreference(Constants.USER_CITY_LOCALITY, "")
                ?.isNotEmpty() == true
        ) {
            navigationController?.navigate(R.id.action_navigation_home_to_foodSelectionFragment)
        }else{
            navigationController?.navigate(R.id.action_navigation_home_to_currentLocationMapFragment)
        }
    }
    override fun receivedResponse(item: Any?) {
    }

}