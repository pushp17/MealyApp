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
import com.eat_healthy.tiffin.models.MainMeal
import com.eat_healthy.tiffin.models.Sabji
import com.eat_healthy.tiffin.utils.Constants
import com.eat_healthy.tiffin.utils.RecyclerviewItemClicklistener
import com.eat_healthy.tiffin.viewmodels.SharedViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CartListItemsBottomSheet: BottomSheetDialogFragment(),
    RecyclerviewItemClicklistener<ListItem> {
    @Inject
    lateinit var adapter: CartItemListAdapter
    private val sharedViewModel: SharedViewModel by activityViewModels()
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
        return binding.root
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        callbackFromCartListItemsBottomSheet()
    }

    private fun callbackFromCartListItemsBottomSheet(){
        requireParentFragment().childFragmentManager.fragments.forEach {
            if (it is FoodSelectionFragment) {
                it.callbackFromCartListItemsBottomSheet()
            }
        }
        dismiss()
    }

    override fun onClickItem(position: Int, item: ListItem?) {
        item?.let {
            if(item is ItemsInCart)  {
                if (item.itemType.equals(Constants.normalMealCategory) || item.itemType.equals(
                        Constants.silverMealCategory
                    )
                    || item.itemType.equals(Constants.goldMealCategory)
                ) {
                    sharedViewModel.specialMealCategoryHeaderEnable = false
                    sharedViewModel.specialMealSelected=false
                }
                sharedViewModel.noOfItemAddedInCart = sharedViewModel.noOfItemAddedInCart-1
                sharedViewModel.totalPrice = sharedViewModel.totalPrice - (item.price).toInt()
                sharedViewModel.cartItemList.removeAt(position)
                adapter.setItems(sharedViewModel.cartItemList)
                if (sharedViewModel.noOfItemAddedInCart <= 0) {
                    sharedViewModel.selectedMealCategoryMutableList.forEach {
                        if (it is Sabji) {
                            it.selected = false
                        } else if (it is MainMeal) {
                            it.selected = false
                        }
                    }
                    callbackFromCartListItemsBottomSheet()
                }
            }
        }
    }

    override fun onClickItem(position: Int, item: ListItem?, id: String?) {
    }
}
