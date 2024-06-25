package com.eat_healthy.tiffin.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.eat_healthy.tiffin.adapter.CartItemListAdapter
import com.eat_healthy.tiffin.databinding.CartItemsListBottomsheetBinding
import com.eat_healthy.tiffin.genericFiles.ListItem
import com.eat_healthy.tiffin.models.ItemsInCart
import com.eat_healthy.tiffin.models.Meal
import com.eat_healthy.tiffin.utils.Constants
import com.eat_healthy.tiffin.utils.RecyclerviewItemClicklistener
import com.eat_healthy.tiffin.viewmodels.SharedViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CartListItemsBottomSheet: BottomSheetDialogFragment(),
    RecyclerviewItemClicklistener<ListItem> {
    @Inject
    lateinit var adapter: CartItemListAdapter
    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var ifAnyItemRemoved = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = CartItemsListBottomsheetBinding.inflate(inflater, container, false)
        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.rvCartItemList.layoutManager=linearLayoutManager
        binding.rvCartItemList.adapter = adapter
        adapter.setOnClickListener(this)
        adapter.setItems(sharedViewModel.cartItemList)
        binding.ivClose.setOnClickListener {
            callbackFromCartListItemsBottomSheet()
        }
        firebaseAnalytics.logEvent(Constants.CART_BOTTOMSHEET_OPENED,null)
        return binding.root
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        callbackFromCartListItemsBottomSheet()
    }

    private fun callbackFromCartListItemsBottomSheet() {
        requireParentFragment().childFragmentManager.fragments.forEach {
            if (it is HomeFragment) {
                it.callbackFromCartBottomSheet(ifAnyItemRemoved)
            }
            if (it is OrderSummaryFragment) {
                it.callbackFromCartListItemsBottomSheet()
            }
        }
        dismiss()
    }

    override fun onClickItem(position: Int, item: ListItem?) {
        item?.let {
            if(item is ItemsInCart)  {
                sharedViewModel.noOfItemAddedInCart -= 1
                sharedViewModel.totalPrice -= (item.price).toInt()
                sharedViewModel.adapterList.forEach {
                    if (it is Meal && item.itemName.equals(it.name, true)) {
                        it.addButtonCountText.set(it.addButtonCountText.get() - 1)
                        ifAnyItemRemoved = true
                    }
                }
                sharedViewModel.cartItemList.removeAt(position)
                adapter.setItems(sharedViewModel.cartItemList)
                if (sharedViewModel.noOfItemAddedInCart <= 0) {
                    callbackFromCartListItemsBottomSheet()
                }
            }
        }
    }

    override fun onClickItem(position: Int, item: ListItem?, id: String?) {
    }
}
