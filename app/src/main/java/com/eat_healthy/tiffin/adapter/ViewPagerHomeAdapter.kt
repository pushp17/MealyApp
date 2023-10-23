package com.eat_healthy.tiffin.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.eat_healthy.tiffin.ui.MealFragment

class ViewPagerHomeAdapter(fa: Fragment) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return 3
    }
    override fun createFragment(position: Int): Fragment {
        val bundle=Bundle()
        bundle.putInt("position",position)
        val mealFragment=MealFragment()
        mealFragment.arguments=bundle
        return mealFragment
    }
}