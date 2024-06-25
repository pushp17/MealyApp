package com.eat_healthy.tiffin.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.eat_healthy.tiffin.adapter.SelectBaseCourseItemsAdapter
import com.eat_healthy.tiffin.databinding.MainCourseSelectionBottomsheetBinding
import com.eat_healthy.tiffin.genericFiles.ListItem
import com.eat_healthy.tiffin.models.ExtrasV2
import com.eat_healthy.tiffin.models.Header
import com.eat_healthy.tiffin.models.ItemsInCart
import com.eat_healthy.tiffin.models.Meal
import com.eat_healthy.tiffin.utils.AppUtils
import com.eat_healthy.tiffin.utils.Constants
import com.eat_healthy.tiffin.utils.RecyclerviewItemClicklistener
import com.eat_healthy.tiffin.viewmodels.SharedViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class BaseCourseSelectionBottomSheet : BottomSheetDialogFragment(),
    RecyclerviewItemClicklistener<ListItem> {
    private lateinit var selectBaseCourseItemsAdapter: SelectBaseCourseItemsAdapter
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val itemList = mutableListOf<ListItem>()
    lateinit var binding: MainCourseSelectionBottomsheetBinding
    private var mealType: String? = null
    var meal : String? = null
    private var mainMealPrice : Int ? = 0
    private var numberOfItems = 0
    private var totalPrice = 0
    val itemsSelected = mutableListOf<ItemsInCart>()
    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainCourseSelectionBottomsheetBinding.inflate(inflater, container, false)
        mealType = arguments?.getString("mealType","")
        meal = arguments?.getString("meal","")
        mainMealPrice = arguments?.getString("price","")?.toInt()
        binding.tvHeader.text = meal
        binding.tvPrice.text = AppUtils.rsAppendedValue(mainMealPrice.toString())
        selectBaseCourseItemsAdapter = SelectBaseCourseItemsAdapter()
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        gridLayoutManager.spanSizeLookup =
            object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val item = selectBaseCourseItemsAdapter.getItemsList().get(position)
                    return if (item is Meal && item.type.equals("base")) {
                        1
                    } else {
                        2 //item will take 1/2 space of row
                    }
                }
            }
        binding.rvSelectMeal.layoutManager = gridLayoutManager
        binding.rvSelectMeal.adapter = selectBaseCourseItemsAdapter
        selectBaseCourseItemsAdapter.setOnClickListener(this)
        selectBaseCourseItemsAdapter.setItems(setListItem())
        binding.ivClose.setOnClickListener {
            numberOfItems = 0
            totalPrice = 0
            callbackFromCartListItemsBottomSheet()
        }

        binding.tvAdd.setOnClickListener {
            binding.tvAdd.startAnimation(clickAnimation())
            addItems()
            sharedViewModel.cartItemList.addAll(itemsSelected)
            callbackFromCartListItemsBottomSheet()
        }


//        binding.llContinue.setOnClickListener {
//            sharedViewModel.cartItemList.addAll(itemsSelected)
//            callbackFromCartListItemsBottomSheet()
//        }
        firebaseAnalytics.logEvent(Constants.BASE_COURSE_BOTTOMSHEET_OPENED,null)
        return binding.root
    }

    private fun clickAnimation(): AlphaAnimation {
        return AlphaAnimation(1f, .01f) // Change "0.4F" as per your recruitment.
    }

    private fun callbackFromCartListItemsBottomSheet() {
        selectBaseCourseItemsAdapter.getItemsList().forEach {
            if (it is Meal) {
                it.selected = false
            } else if (it is ExtrasV2) {
                it.selected = false
            }
        }

        requireParentFragment().childFragmentManager.fragments.forEach {
            if (it is HomeFragment) {
                it.callbackFromBaseCourseSelectionBottomSheet(numberOfItems,totalPrice)
            }
        }
        dismiss()
    }

    private fun addItems() {
        Toast.makeText(requireActivity(), "Item Added In Cart", Toast.LENGTH_SHORT).show()
        numberOfItems++
        val completeMeal = StringBuilder()
        var extraMealPrice = 0
        completeMeal.append(meal).append(" . ")
        selectBaseCourseItemsAdapter.getItemsList().forEach {
            if (it is Meal && it.selected) {
                completeMeal.append(it.name).append(" . ")
            }
            if (it is ExtrasV2 && it.selected) {
                completeMeal.append(it.name).append(" . ")
                if ((it.type.equals("checkbox") || it.type.equals("radio")) && it.price?.isNotEmpty() == true) {
                    mainMealPrice?.plus(it.price.toDouble())
                    extraMealPrice += it.price.toInt()
                }
            }
        }
        totalPrice += mainMealPrice!! + extraMealPrice
        itemsSelected.add(
            ItemsInCart(
                completeMeal.toString(),
                mainMealPrice?.plus(extraMealPrice).toString(),
                meal
            )
        )
        binding.tvItemPrice.text = numberOfItems.toString().plus(" Meal").plus(" | ")
            .plus(AppUtils.rsAppendedValue(totalPrice.toString()))
    }

    override fun onStart() {
        super.onStart()
        val view = view
        if (view != null) {
            val behavior = BottomSheetBehavior.from(view.parent as View)
            behavior.peekHeight= 2100
        }
    }


    private fun setListItem(): MutableList<ListItem> {
        if (mealType.equals("thali")) {
            itemList.add(Header("Additional side portion"))
            itemList.addAll(sharedViewModel.extrasMutableList.filter { it.type.equals("radio") })

            if (sharedViewModel.extrasMutableList.filter { it.type.equals("checkbox") }.size > 0) {
                itemList.add(Header("Something Extra"))
                itemList.addAll(sharedViewModel.extrasMutableList.filter { it.type.equals("checkbox") })
            }
            return itemList
        }
        itemList.add(Header("Sabji/Curries"))
        itemList.addAll(sharedViewModel.mealMutableList.filter {
            when (mealType) {
                "curries" -> (it.type.equals("sabji") && (it.category?.equals("silver") == true))
                "sabji" -> it.type.equals("curries")
                "egg" -> it.type.equals("curries")
                else -> false
            }
        })
        itemList.add(Header("Select a base"))
        itemList.addAll(sharedViewModel.mealMutableList.filter {   it.type.equals("base")})

        itemList.add(Header("Additional side portion"))
        itemList.addAll(sharedViewModel.extrasMutableList.filter { it.type.equals("radio") })

        if (sharedViewModel.extrasMutableList.filter { it.type.equals("checkbox") }.size > 0) {
            itemList.add(Header("Something Extra"))
            itemList.addAll(sharedViewModel.extrasMutableList.filter { it.type.equals("checkbox") })
        }

        return itemList
    }

    override fun onClickItem(position: Int, item: ListItem?) {
        when (item) {
            is Meal -> {
                selectBaseCourseItemsAdapter.getItemsList().filter { (it is Meal) && it.type.equals(item.type) }
                    .map {
                        (it as Meal).selected = false
                    }
                item.selected = true
                calculatePrice()
            }
            is ExtrasV2 -> {
                when (item.type) {
                    "radio" -> {
                        selectBaseCourseItemsAdapter.getItemsList().filter { (it is ExtrasV2) && it.type.equals("radio")  }
                            .map {
                                (it as ExtrasV2).selected = false
                            }
                        item.selected = true
                    }
                    "checkbox" -> {
                        item.selected = !item.selected
                    }
                }
                calculatePrice()
            }
            else -> {
            }
        }
        selectBaseCourseItemsAdapter.notifyDataSetChanged()
    }

    private fun calculatePrice() {
        var extraMealPrice = 0
        var totalPrice = 0
        selectBaseCourseItemsAdapter.getItemsList().forEach {
            if (it is ExtrasV2 && it.selected) {
                if ((it.type.equals("checkbox") || it.type.equals("radio")) && it.price?.isNotEmpty() == true) {
                    mainMealPrice?.plus(it.price.toDouble())
                    extraMealPrice += it.price.toInt()
                }
            }
        }
        totalPrice += mainMealPrice!! + extraMealPrice
        binding.tvPrice.text = AppUtils.rsAppendedValue(totalPrice.toString())
    }

    override fun onClickItem(position: Int, item: ListItem?, id: String?) {
    }
}