package com.eat_healthy.tiffin.ui

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.eat_healthy.tiffin.R
import com.eat_healthy.tiffin.adapter.OrderHistoryAdapter
import com.eat_healthy.tiffin.databinding.FragmentOrderHistoryBinding
import com.eat_healthy.tiffin.genericFiles.ListItem
import com.eat_healthy.tiffin.models.SingleMealUserOrderDetail
import com.eat_healthy.tiffin.models.SingleMealUserOrderHistory
import com.eat_healthy.tiffin.models.SingleMealUserOrderHistoryResponse
import com.eat_healthy.tiffin.models.User
import com.eat_healthy.tiffin.utils.Constants
import com.eat_healthy.tiffin.utils.RecyclerviewItemClicklistener
import com.eat_healthy.tiffin.viewmodels.OrderHistoryViewmodel
import com.eat_healthy.tiffin.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyOrderHisoryFragment:ListViewFragment<OrderHistoryAdapter, FragmentOrderHistoryBinding>(),
    RecyclerviewItemClicklistener<ListItem> {
    override val fragmentLayoutResId: Int
        get() = R.layout.fragment_order_history
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val orderHistoryViewmodel: OrderHistoryViewmodel by viewModels()
    private var singleMealUserOrdeDetail : List<SingleMealUserOrderDetail>?=null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        orderHistoryViewmodel.orderHistoryResponseLiveData.observe(viewLifecycleOwner, observer)
        adapter.setOnClickListener(this)
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
                        singleMealUserOrdeDetail = it.itemsInCart?.asReversed()
                        it.itemsInCart?.asReversed()?.forEach {
                            orderHistoryList.add(SingleMealUserOrderHistory(
                                it.orderId ?: "", it.dateAndTime ?: "",
                                it.subTotalPrice, it.grandTotalPrice.toString(), it.itemsInCart,
                                it.orderTime,it.estimatedDeliveryTime,
                                it.paymentSuccessDone
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

    override fun onClickItem(position: Int, item: ListItem?, id: String?) {
        when (item) {
            is SingleMealUserOrderHistory -> {
                when (id) {
                    "orderDetail" -> {
                        val itemListStr = mutableListOf<String>()
                        singleMealUserOrdeDetail?.get(position)?.itemsInCart?.forEach {
                            itemListStr.add(it.yourMeal)
                        }
                        val bundle = bundleOf(
                            Constants.GENERIC_STRING_ARRAYLIST to itemListStr,
                            Constants.GENERIC_HEADER to "Item in cart"
                        )
                        navigationController?.navigate(
                            R.id.action_myOrderHisoryFragment_to_genericStringListBottomSheet,
                            bundle
                        )
                    }
                    "deliverySlot" -> {
                        navigationController?.navigate(
                            R.id.action_myOrderHisoryFragment_to_orderTimmingSlotBottomSheet
                        )
                    }
                }
            }
        }
    }
}

