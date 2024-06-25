package com.eat_healthy.tiffin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.eat_healthy.tiffin.adapter.DeliverySlotAdapter
import com.eat_healthy.tiffin.databinding.OrderTimmingSlotBottomsheetBinding
import com.eat_healthy.tiffin.utils.Constants
import com.eat_healthy.tiffin.viewmodels.SharedViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OrderTimmingSlotBottomSheet : BottomSheetDialogFragment() {
    val sharedViewModel:SharedViewModel by activityViewModels()
    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = OrderTimmingSlotBottomsheetBinding.inflate(inflater, container, false)
        val timeOutString = sharedViewModel.apiResponse?.afterStartOrderTimeout ?: ""
        if(timeOutString.contains("thanks",true)){
            binding.tvLunchTime.text="Lunch  ".plus("09:00 AM - 01:45 PM")
            binding.tvDinnerTime.text="Dinner ".plus("06:00 PM - 08:45 PM")
        }else{
            binding.tvLunchTime.text="Lunch  ".plus(sharedViewModel.lunchStartTime).plus(" - ").plus(sharedViewModel.lunchEndTime)
            binding.tvDinnerTime.text="Dinner ".plus(sharedViewModel.dinnerStartTime).plus(" - ").plus(sharedViewModel.dinnerEndTime)
        }
        val deliverySlotAdapter = DeliverySlotAdapter()
        binding.rvParent.adapter=deliverySlotAdapter
        deliverySlotAdapter.setItems(sharedViewModel.deliverySlotTimming)
        firebaseAnalytics.logEvent(Constants.TIMESLOT_BOTTOMSHEET_OPENED,null)
        return binding.root
    }
}