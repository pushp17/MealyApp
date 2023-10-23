package com.eat_healthy.tiffin.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.eat_healthy.tiffin.R
import com.eat_healthy.tiffin.adapter.AddressAdapter
import com.eat_healthy.tiffin.adapter.OrderHistoryAdapter
import com.eat_healthy.tiffin.databinding.FragmentOrderHistoryBinding
import com.eat_healthy.tiffin.models.SingleMealUserOrderHistory
import com.eat_healthy.tiffin.models.SingleMealUserOrderHistoryResponse
import com.eat_healthy.tiffin.models.User
import com.eat_healthy.tiffin.viewmodels.OrderHistoryViewmodel
import com.eat_healthy.tiffin.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyOrderHisoryFragment:ListViewFragment<OrderHistoryAdapter, FragmentOrderHistoryBinding>() {
    override val fragmentLayoutResId: Int
        get() = R.layout.fragment_order_history
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val orderHistoryViewmodel: OrderHistoryViewmodel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        orderHistoryViewmodel.orderHistoryResponseLiveData.observe(viewLifecycleOwner, observer)
        orderHistoryViewmodel.getSingleUserOrdersHistory(User(
            sharedViewModel.userDetail?.username,
            sharedViewModel.userDetail?.mobileno
        ))
    }
    override fun receivedResponse(item: Any?) {
        item?.let {
            when(it){
                is SingleMealUserOrderHistoryResponse ->
                    if (it.statusCode == 200) {
                        val orderHistoryList = mutableListOf<SingleMealUserOrderHistory>()
                        binding.ivNewDeliveryimage.visibility = View.VISIBLE
                        binding.tvHeader.visibility = View.VISIBLE
                        it.itemsInCart?.forEach {
                            orderHistoryList.add(SingleMealUserOrderHistory(
                                it.orderId ?: "", it.dateAndTime ?: "",
                                it.subTotalPrice, it.grandTotalPrice.toString(), it.itemsInCart
                            ))
                        }
                        adapter.setItems(orderHistoryList)
                    } else {
                        binding.emptyTextview.visibility = View.VISIBLE
                        binding.errorMsg.visibility = View.VISIBLE
                    }
            }
        }
    }
}

