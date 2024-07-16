package com.eat_healthy.tiffin.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.eat_healthy.tiffin.R
import com.eat_healthy.tiffin.adapter.DeliveryAdapter
import com.eat_healthy.tiffin.databinding.FragmentDeliveryBinding
import com.eat_healthy.tiffin.models.DeliveryDetailsResponse
import com.eat_healthy.tiffin.utils.DateAndTimeUtils
import com.eat_healthy.tiffin.viewmodels.DeliveryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeliveryFragment : ListViewFragment<DeliveryAdapter, FragmentDeliveryBinding>() {
    override val fragmentLayoutResId: Int
        get() = R.layout.fragment_delivery

    private val todaysDeliveryDetailModel: DeliveryViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        todaysDeliveryDetailModel.deliveryResponseLiveData.observe(getViewLifecycleOwner(),observer)
        todaysDeliveryDetailModel.getTodaysDeliveryDetailsList(DateAndTimeUtils.getCurrentDate())
    }

    override fun receivedResponse(item: Any?) {
        item?.let {
            if (item is DeliveryDetailsResponse) {
                adapter.setItems(item.deliveryDetailsList)
            }
        }
    }
}
