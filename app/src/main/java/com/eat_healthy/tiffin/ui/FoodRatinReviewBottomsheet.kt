package com.eat_healthy.tiffin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.eat_healthy.tiffin.databinding.FoodReviewRatingBottomsheetBinding
import com.eat_healthy.tiffin.models.FoodReview
import com.eat_healthy.tiffin.utils.Constants
import com.eat_healthy.tiffin.utils.DateAndTimeUtils
import com.eat_healthy.tiffin.utils.SharedPrefManager
import com.eat_healthy.tiffin.viewmodels.FoodReviewAndSuggestionViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class FoodRatinReviewBottomsheet : BottomSheetDialogFragment() {
    private val foodReviewAndSuggestionViewModel: FoodReviewAndSuggestionViewModel by activityViewModels()
    var binding: FoodReviewRatingBottomsheetBinding? = null
    @Inject
    lateinit var sharedPrefManager: SharedPrefManager
    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FoodReviewRatingBottomsheetBinding.inflate(inflater, container,false)
        val userLastReview = sharedPrefManager.getModelClass<FoodReview>(Constants.FOOD_REVIEW)
        binding?.tvItem?.text = (userLastReview?.foodOrderList?.getOrNull(0))
        if (userLastReview?.foodOrderList?.size!! > 1) {
            binding?.tvItem?.text = (userLastReview.foodOrderList.getOrNull(0))
            binding?.tvItem2?.visibility = View.VISIBLE
            binding?.tvItem2?.text = (userLastReview.foodOrderList.getOrNull(1))
            if(userLastReview?.foodOrderList?.size!! > 2){
                binding?.tvItem2?.text = (userLastReview.foodOrderList.getOrNull(1).plus(" .....") )
            }
        }
//        binding?.smileRating?.setSmileySelectedListener { type ->
//            val rating = type.rating
//        }
        binding?.ivClose?.setOnClickListener {
            firebaseAnalytics.logEvent(Constants.FOOD_REVIEW_BOTTOMSHEET_CANCELED,null)
            dismiss()
        }

        binding?.tvSubmitFeedback?.setOnClickListener {
            val type = binding?.smileRating?.selectedSmiley?.rating
            if (type != null && type < 1) {
                Toast.makeText(requireContext(), "Please select rating", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            binding?.progressBar?.visibility = View.VISIBLE
                userLastReview.apply {
                    foodReview = binding?.etFeedback?.text.toString()
                    foodRating = binding?.smileRating?.selectedSmiley?.rating.toString()
                    foodReviewDate = DateAndTimeUtils.getCurrentDate()
                }
                foodReviewAndSuggestionViewModel.postFoodReview(userLastReview)
                sharedPrefManager.clearASpecificPreference(Constants.FOOD_REVIEW)
                viewLifecycleOwner.lifecycleScope.launch {
                    delay(2000L)
                    firebaseAnalytics.logEvent(Constants.FOOD_REVIEW_SUBMITTED,null)
                    binding?.progressBar?.visibility = View.GONE
                    Toast.makeText(requireContext(), "Thank you for providing Review", Toast.LENGTH_LONG).show()
                    dismiss()
                }
        }
        firebaseAnalytics.logEvent(Constants.FOOD_REVIEW_BOTTOMSHEET_OPENED,null)
        return binding?.root
    }


    override fun onStart() {
        super.onStart()
        val view = view
        if (view != null) {
            val behavior = BottomSheetBehavior.from(view.parent as View)
            behavior.peekHeight= 1800
        }
    }
}