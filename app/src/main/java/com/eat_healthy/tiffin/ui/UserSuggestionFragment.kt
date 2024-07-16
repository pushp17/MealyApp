package com.eat_healthy.tiffin.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.eat_healthy.tiffin.R
import com.eat_healthy.tiffin.adapter.UserSuggestionAdapter
import com.eat_healthy.tiffin.databinding.FragmentUsersSuggestionBinding
import com.eat_healthy.tiffin.models.ApiResponse
import com.eat_healthy.tiffin.models.SuggestionQuestionAnswer
import com.eat_healthy.tiffin.models.UsersSuggestionQuestions
import com.eat_healthy.tiffin.models.UsersSuggestionUpload
import com.eat_healthy.tiffin.utils.Constants
import com.eat_healthy.tiffin.viewmodels.FoodReviewAndSuggestionViewModel
import com.eat_healthy.tiffin.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserSuggestionFragment :ListViewFragment<UserSuggestionAdapter,FragmentUsersSuggestionBinding>() {
    override val fragmentLayoutResId: Int
        get() = R.layout.fragment_users_suggestion

    private val foodAndReviewSuggestionViewModel:FoodReviewAndSuggestionViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.rvParent.layoutManager = LinearLayoutManager(requireContext())
        super.onViewCreated(view, savedInstanceState)
        navigationController=findNavController()
        adapter.setOnClickListener(this)
        foodAndReviewSuggestionViewModel.usersSuggestionUploadResponseLiveData.observe(viewLifecycleOwner,observer)
        foodAndReviewSuggestionViewModel.suggestionQuestionResponseLiveData.observe(viewLifecycleOwner,observer)
        foodAndReviewSuggestionViewModel.getUserSuggestionQuestions()
        binding.tvSubmitFeedback.setOnClickListener {
            uploadUserSuggestions()
        }

        firebaseAnalytics.logEvent(Constants.LUNCH_REMINDER_CLICKED,null)
    }

    private fun uploadUserSuggestions() {
        val usersSuggestionQuestionAnswer = mutableListOf<SuggestionQuestionAnswer>()
        adapter.getItemsList().forEach {
            if (it is SuggestionQuestionAnswer && !it.answer.isNullOrEmpty()) {
                usersSuggestionQuestionAnswer.add(it)
            }
        }
        foodAndReviewSuggestionViewModel.uploadUserSuggestion(
            UsersSuggestionUpload(
                sharedViewModel.userDetail?.username,
                sharedViewModel.userDetail?.mobileno,
                usersSuggestionQuestionAnswer
            )
        )
        firebaseAnalytics.logEvent(Constants.FEEDBACK_FORM_OPENED,null)
    }

    override fun receivedResponse(item: Any?) {
        item?.let {
            when (it) {
                is UsersSuggestionQuestions -> {
                    binding.tvHeader.text = it.header
                    binding.tvSubmitFeedback.visibility=View.VISIBLE
                    binding.tvSubHeader.visibility = View.VISIBLE
                    it.listOfSuggestionQuestionAnswer?.let { it1 -> adapter.setItems(it1.sortedBy { it.position }) }
                    firebaseAnalytics.logEvent(Constants.FEEDBACK_FORM_OPENED_SUCCESSFULLY,null)
                }

                is ApiResponse -> {
                    val alertDialog = SweetAlertDialogV2(
                        requireActivity(),
                        SweetAlertDialogV2.SUCCESS_TYPE
                    )
                    alertDialog.setCancelable(false)
                    alertDialog.titleText = "Successfully submitted"

                    alertDialog.contentText = "Thank you for providing feedback. It will help us improve the service"
                    alertDialog.show()
                    alertDialog.setConfirmClickListener {
                        alertDialog.dismissWithAnimation()
                        navigationController?.popBackStack(R.id.navigation_home, false)

                    }
                    firebaseAnalytics.logEvent(Constants.FEEDBACK_FORM_SUBMITTED,null)
                }

                else -> {}
            }
        }
    }

}