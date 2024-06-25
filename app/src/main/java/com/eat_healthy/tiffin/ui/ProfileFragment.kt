package com.eat_healthy.tiffin.ui

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.eat_healthy.tiffin.R
import com.eat_healthy.tiffin.databinding.ProfileLayoutBinding
import com.eat_healthy.tiffin.models.MonthlyUserPreferenceResponse
import com.eat_healthy.tiffin.models.User
import com.eat_healthy.tiffin.utils.AppUtils.rsAppendedValue
import com.eat_healthy.tiffin.viewmodels.MonthlyFoodSelectionViewModel
import com.eat_healthy.tiffin.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment: BaseFragment() {
    private val viewModel: MonthlyFoodSelectionViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    var binding: ProfileLayoutBinding? = null
    private var window: Window? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ProfileLayoutBinding.inflate(inflater, container, false)
        val navigationController = findNavController()
        window = activity?.window
        window?.statusBarColor = ContextCompat.getColor(requireContext(),R.color.windowBackgroundNight)
        binding?.llMonthlyuser?.visibility = View.GONE
        if (sharedViewModel.userDetail != null) {
            binding?.tvUser?.text = sharedViewModel.userDetail?.username
            binding?.tvMobile?.text = sharedViewModel.userDetail?.mobileno
        } else {
            navigationController.navigate(R.id.action_profileFragment_to_loginFragment)
        }
        viewModel.monthlyUserPreferenceLivedata.observe(viewLifecycleOwner,observer)
        viewModel.getMonthlyUserPreference(
            User(
                sharedViewModel.userDetail?.username,
                sharedViewModel.userDetail?.mobileno
            )
        , sharedViewModel
        )
        binding?.cvRenewSubcription?.setOnClickListener {
        navigationController.navigate(R.id.action_profileFragment_to_monthlyFoodSelectionFragment)
        }
        return binding?.root
    }
    override fun receivedResponse(item: Any?) {
     item?.let {
         when (it) {
             is MonthlyUserPreferenceResponse -> {
                 if (it.statusCode == 200L) {
                     binding?.llMonthlyuser?.visibility = View.VISIBLE
                     binding?.llIndicator?.visibility = View.VISIBLE
                     binding?.apply {
                         llIndicator.visibility = View.VISIBLE
                         tvSubscriptionStartDate.text = "Subcription Start Date : "+it.monthlyUserPreference?.subscriptionStartDate
                         tvSubscriptionEndDate.text = "Subcription End Date : "+it.monthlyUserPreference?.subscriptionEndDate
                         tvSubscriptionPrice.text = "Subcription Price : "+rsAppendedValue(it.monthlyUserPreference?.latestMonthlySubscriptionPrice)
                         if (sharedViewModel.subscriptionExpired) {
                             cvRenewSubcription.visibility = View.VISIBLE
                             tvMonthlyActiveUser.text = getString(R.string.subscription_expired)
                             indicator.backgroundTintList =
                                 ColorStateList.valueOf(requireActivity().getColor(R.color.colorPrimary))
                         }
                     }
                 }
             }
             else -> {
             }
         }
     }
    }

    override fun onPause() {
        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
        super.onPause()
    }
}
