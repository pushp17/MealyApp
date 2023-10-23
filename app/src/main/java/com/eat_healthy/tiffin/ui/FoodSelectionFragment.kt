package com.eat_healthy.tiffin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.eat_healthy.tiffin.adapter.ViewPagerFoodSelectionAdapter
import com.eat_healthy.tiffin.databinding.FragmentFoodSelectionBinding
import com.eat_healthy.tiffin.utils.Constants
import com.eat_healthy.tiffin.viewmodels.SharedViewModel
import com.google.android.material.tabs.TabLayoutMediator

class FoodSelectionFragment: BaseFragment() {
    lateinit var binding: FragmentFoodSelectionBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentFoodSelectionBinding.inflate(inflater,container,false)
        startViewPagerAdapter()
        return binding.root
    }
    private fun startViewPagerAdapter(){
        binding.viewpager.adapter= ViewPagerFoodSelectionAdapter(this)
        val tabTitle= arrayListOf(sharedViewModel.getSelectedMealTypeTabHeader(),"Monthly User")
        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->
            tab.text = tabTitle[position]
        }.attach()

        binding.viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 1) {
                    sharedViewModel.monthlyUser = true
                    return
                }
                sharedViewModel.monthlyUser = false
            }
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })
    }

    fun callbackFromCartListItemsBottomSheet(){
        startViewPagerAdapter()
    }
    override fun receivedResponse(item: Any?) {
    }
}