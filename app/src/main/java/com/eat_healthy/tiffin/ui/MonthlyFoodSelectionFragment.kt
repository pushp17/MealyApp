package com.eat_healthy.tiffin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.eat_healthy.tiffin.adapter.ViewPagerMonthlyFoodAdapter
import com.eat_healthy.tiffin.databinding.FragmentMonthlyFoodSelectionBinding
import com.eat_healthy.tiffin.models.User
import com.eat_healthy.tiffin.utils.Constants
import com.eat_healthy.tiffin.viewmodels.MonthlyFoodSelectionViewModel
import com.eat_healthy.tiffin.viewmodels.SharedViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MonthlyFoodSelectionFragment:BaseFragment() {
    lateinit var binding:FragmentMonthlyFoodSelectionBinding
    private val viewModel: MonthlyFoodSelectionViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var apiCalled=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!sharedViewModel.apiResponse?.sabjiList.isNullOrEmpty()) {
            viewModel.sabjiList.addAll(sharedViewModel.apiResponse?.sabjiList!!)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentMonthlyFoodSelectionBinding.inflate(inflater,container,false)
        if (sharedViewModel.userDetail?.isUserSignedIn == true && !apiCalled) {
            viewModel.getMonthlyUserPreference(
                User(
                    sharedViewModel.userDetail?.username,
                    sharedViewModel.userDetail?.mobileno
                ),
                sharedViewModel
            )
        }else {
            startViewPagerAdapter()
        }
        viewModel.monthlyUserPreferenceLivedata.observe(viewLifecycleOwner,observer)
        sharedViewModel.monthlyUser=true
        viewModel.screenEntryPoint = arguments?.getString(Constants.SCREEN_ENTRY_POINT)
//        if (sharedViewModel.userDetail?.isUserSignedIn != true) {
//            startViewPagerAdapter()
//        }
        return binding.root
    }
    private fun startViewPagerAdapter(){
        binding.viewpager.adapter= ViewPagerMonthlyFoodAdapter(this)
        val tabTitle= arrayListOf("Weekly Food","My Favourite")
        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->
            tab.text = tabTitle[position]
        }.attach()
    }

    fun changeViewpagerPosition(position:Int){
        binding.viewpager.currentItem=position
    }
    override fun receivedResponse(item: Any?) {
        apiCalled=true
        startViewPagerAdapter()
    }
}