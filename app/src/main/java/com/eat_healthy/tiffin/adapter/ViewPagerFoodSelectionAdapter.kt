package com.eat_healthy.tiffin.adapter

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.eat_healthy.tiffin.ui.SingleMealSelectionFragment

class ViewPagerFoodSelectionAdapter (fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }
    var vegMealSelectionFragment: SingleMealSelectionFragment? = null
    var nonVegMealSelectionFragment: SingleMealSelectionFragment? = null
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                val bundle = bundleOf("mealType" to "veg")
                vegMealSelectionFragment = SingleMealSelectionFragment()
                vegMealSelectionFragment?.arguments = bundle
                vegMealSelectionFragment!!
            }

            else -> {
                val bundle = bundleOf("mealType" to "non_veg")
                nonVegMealSelectionFragment = SingleMealSelectionFragment()
                nonVegMealSelectionFragment?.arguments = bundle
                nonVegMealSelectionFragment!!
            }
        }
    }
}
