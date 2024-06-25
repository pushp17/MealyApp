package com.eat_healthy.tiffin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.eat_healthy.tiffin.R
import com.eat_healthy.tiffin.databinding.FragmentOrderSuccesBinding
import com.eat_healthy.tiffin.utils.Constants
import com.eat_healthy.tiffin.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class OrderSuccessFragment : BaseFragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentOrderSuccesBinding.inflate(inflater, container, false)
        val navigationController = findNavController()
        val estimatedDeliveryTime = arguments?.getString("estimatedDeliveryTime")
        sharedViewModel.apiResponse = null
        val calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.MINUTE, 240)
        sharedPrefManager.addLongToPreference(
            Constants.ADD_4_HOURS_TO_ORDER_TIME,
            calendar.timeInMillis
        )

        binding.deliveryTime.text =
            getString(R.string.expected_delivery_time).plus(estimatedDeliveryTime)

        binding.tvGoToHome.setOnClickListener {
            navigationController.popBackStack(R.id.navigation_home, false)
        }
        binding.tvGiveSuggestion.setOnClickListener {
            navigationController.navigate(R.id.action_orderSuccessFragment_to_userSuggestionFragment)
            firebaseAnalytics.logEvent(Constants.FEEDBACK_BTN_CLICKED_FROM_SUCCESSS_PAGE,null)
        }

        binding.deliverySlot.setOnClickListener {
            navigationController.navigate(R.id.action_orderSuccessFragment_to_orderTimmingSlotBottomSheet)
            firebaseAnalytics.logEvent(Constants.ORDER_SUCCESSS_PAGE,null)
            firebaseAnalytics.logEvent(Constants.TIMESLOT_BTN_CLICKED_FROM_SUCCESSS_PAGE,null)
        }
        firebaseAnalytics.logEvent(Constants.ORDER_SUCCESSS_PAGE,null)
        return binding.root
    }

    override fun receivedResponse(item: Any?) {
    }
}