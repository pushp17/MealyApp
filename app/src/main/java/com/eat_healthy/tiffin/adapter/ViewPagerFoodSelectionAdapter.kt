package com.eat_healthy.tiffin.adapter

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.eat_healthy.tiffin.ui.MonthlyFoodSelectionFragment
import com.eat_healthy.tiffin.ui.SingleMealSelectionFragment
import com.eat_healthy.tiffin.utils.Constants

class ViewPagerFoodSelectionAdapter (fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){

            0-> SingleMealSelectionFragment()
            else -> {
                val bundle = bundleOf(Constants.SCREEN_ENTRY_POINT to Constants.HOME_PAGE_TAB)
               val monthlyFoodSelectionFragment = MonthlyFoodSelectionFragment()
                monthlyFoodSelectionFragment.arguments=bundle
                monthlyFoodSelectionFragment
            }
        }
    }
}