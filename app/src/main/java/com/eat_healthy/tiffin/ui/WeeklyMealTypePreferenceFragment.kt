package com.eat_healthy.tiffin.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.eat_healthy.tiffin.R
import com.eat_healthy.tiffin.adapter.WeeklyMealTypePreferenceAdapter
import com.eat_healthy.tiffin.databinding.WeeklyMealTypePreferenceBinding
import com.eat_healthy.tiffin.genericFiles.ListItem
import com.eat_healthy.tiffin.models.WeekelyMealType
import com.eat_healthy.tiffin.utils.AppUtils.rsAppendedValue
import com.eat_healthy.tiffin.utils.AppUtils.rsAppendedValueWithRs
import com.eat_healthy.tiffin.utils.DateAndTimeUtils
import com.eat_healthy.tiffin.viewmodels.SharedViewModel
import com.eat_healthy.tiffin.viewmodels.MonthlyFoodSelectionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeeklyMealTypePreferenceFragment : ListViewFragment<WeeklyMealTypePreferenceAdapter,
        WeeklyMealTypePreferenceBinding>() {

    private val viewModel: MonthlyFoodSelectionViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )
    private val sharedViewModel: SharedViewModel by activityViewModels()
    override val fragmentLayoutResId: Int
        get() = R.layout.weekly_meal_type_preference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.vegPrice=sharedViewModel.regularMealPrice.toInt()
        viewModel.vegSpecialPrice=sharedViewModel.vegSpecialPrice.toInt()
        viewModel.nonVegPrice=sharedViewModel.nonVegPrice.toInt()
        Glide.with(this).load(sharedViewModel.defaultNormalSabjiImageUrl).into(binding.ivItem)
        Glide.with(this).load(sharedViewModel.defaultVegSpecialImageUrl).into(binding.ivItemSpecial)
        Glide.with(this).load(sharedViewModel.defaultNonVegImageUrl).into(binding.ivItemNonVeg)
        binding.tvRegularPrice.text= rsAppendedValueWithRs(sharedViewModel.regularMealPrice)
        binding.tvVegSpecialPrice.text= rsAppendedValueWithRs(sharedViewModel.vegSpecialPrice)
        binding.tvNonVegSpecialPrice.text= rsAppendedValueWithRs(sharedViewModel.nonVegPrice)
        adapter.setOnClickListener(this)
        adapter.setItems(viewModel.weeklyMealTypeList)
        viewModel.apiResponseLivedata.observe(viewLifecycleOwner,observer)
         // Remember one important thing if we subtract two dates 1/03/2021 to 30/03/2021 , it will
        // give 29 rather than 30 , so take the subscription start date as Calender.getInstance()

        // Below  line are for testing
       // viewModel.weekdaysInUpcomingAndPastWeeks("21/08/2023", "01/04/2023")

        if (viewModel.subcriptionStartDate != null && viewModel.subscriptionEndDate != null) {
            viewModel.weekdaysInUpcomingAndPastWeeks(
                viewModel.subcriptionStartDate!!,
                viewModel.subscriptionEndDate!!
            )
        } else {
            viewModel.subcriptionStartDate = DateAndTimeUtils.getCurrentDate()
            viewModel.subscriptionEndDate =
                DateAndTimeUtils.getSubscriptionEndDate(viewModel.subcriptionStartDate)
            viewModel.weekdaysInUpcomingAndPastWeeks(
                viewModel.subcriptionStartDate!!,
                viewModel.subscriptionEndDate!!
            )
        }

        viewModel.walletMoney =
            ((viewModel.lastMonthlySubscriptionPrice).minus(
                viewModel.subscriptionMoneySpentInPastWeeks()
            )
                    )
        viewModel.moneyToBePaid = viewModel.latestMonthlySubscriptionPrice - viewModel.walletMoney
        bindLayout()
        binding.llNext.setOnClickListener {
            if (viewModel.moneyToBePaid > 0) {
                (parentFragment as MonthlyFoodSelectionFragment)?.changeViewpagerPosition(1)
            } else {
                showToast("Please Select Meal For Atleast One Day OR Make The Net Total Positive")
            }
        }
    }
    private fun bindLayout() {
        if (viewModel.noOfDaysBetweenTodayToSubscriptionEndDate > 0 && !sharedViewModel.subscriptionExpired) {
            if (viewModel.walletMoney > 0) {
                binding.llOneWeekPrice.visibility = View.GONE
                binding.llMonthlyPrice.visibility = View.GONE
                binding.llwallet.visibility = View.VISIBLE
                binding.tvWalletMoney.text = rsAppendedValue(viewModel.walletMoney.toString())
            }
            if (viewModel.moneyToBePaid > 0) {
                binding.tvNetTotalPrice.visibility = View.VISIBLE
                binding.tvNetTotalPrice.text =
                    getString(R.string.net_payable).plus(rsAppendedValue(viewModel.moneyToBePaid.toString()))
            }
            binding.tvWeekPrice.text = rsAppendedValue(viewModel.totalPriceInWeek.toString())
            binding.tvMonthPrice.text =
                rsAppendedValue((viewModel.latestMonthlySubscriptionPrice).toString())
            binding.tvMonthPriceHeader.text =
                "Total In ".plus(viewModel.noOfDaysBetweenTodayToSubscriptionEndDate)
                    .plus(" Days =")
        } else {
            binding.llwallet.visibility = View.VISIBLE
            binding.tvWalletHeader.text = getString(R.string.subscription_expired)
            binding.llNext.visibility = View.VISIBLE
            binding.tvNetTotalPrice.visibility = View.VISIBLE
            binding.tvNetTotalPrice.text = "Renew"
            binding.llOneWeekPrice.visibility = View.GONE
            binding.llMonthlyPrice.visibility = View.GONE
        }
    }

    override fun receivedResponse(item: Any?) {

    }

    override fun onClickItem(position: Int, item: ListItem?, id: String?) {
        item?.let {
            if (it is WeekelyMealType) {
                callFunctionAfterAnyClicks(it,position,id)
            }
        }
    }

    private fun callFunctionAfterAnyClicks(weekelyMealType: WeekelyMealType, position: Int, id: String?){
        binding.llMonthlyPrice.visibility = View.VISIBLE
        viewModel.setPreference(id, weekelyMealType)
        adapter.notifyItemChanged(position)
        viewModel.updateThePrice(adapter.getItemsList())
        binding.tvMonthPriceHeader.text =
            "Total In ".plus(viewModel.noOfDaysBetweenTodayToSubscriptionEndDate)
                .plus(" Days =")
        binding.tvWeekPrice.text = rsAppendedValue(viewModel.totalPriceInWeek.toString())
        viewModel.moneyToBePaid = viewModel.latestMonthlySubscriptionPrice-viewModel.walletMoney
        binding.tvMonthPrice.text = rsAppendedValue((viewModel.latestMonthlySubscriptionPrice).toString())
        binding.tvNetTotalPrice.text = getString(R.string.net_payable).plus(rsAppendedValue(viewModel.moneyToBePaid.toString()))
        binding.tvNetTotalPrice.visibility=View.VISIBLE
        if (viewModel.moneyToBePaid > 0) {
            binding.llNext.visibility = View.VISIBLE
        }
    }
}
