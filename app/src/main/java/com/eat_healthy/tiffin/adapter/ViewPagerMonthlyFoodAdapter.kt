package com.eat_healthy.tiffin.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.eat_healthy.tiffin.ui.MyFoodPreferenceFragment
import com.eat_healthy.tiffin.ui.WeeklyMealTypePreferenceFragment

class ViewPagerMonthlyFoodAdapter (fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
       return when(position){
            0->WeeklyMealTypePreferenceFragment()
            else -> MyFoodPreferenceFragment()
        }
    }
}