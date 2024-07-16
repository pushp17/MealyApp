package com.eat_healthy.tiffin.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.eat_healthy.tiffin.R
import com.eat_healthy.tiffin.adapter.UserReviewAdapter
import com.eat_healthy.tiffin.databinding.UserReviewFragmentLayoutBinding
import com.eat_healthy.tiffin.genericFiles.ListItem
import com.eat_healthy.tiffin.models.FoodReview
import com.eat_healthy.tiffin.utils.RecyclerviewItemClicklistener
import com.eat_healthy.tiffin.viewmodels.FoodReviewAndSuggestionViewModel
import com.eat_healthy.tiffin.viewmodels.UserReviewViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserReviewFragment  : ListViewFragment<UserReviewAdapter, UserReviewFragmentLayoutBinding>() ,
    RecyclerviewItemClicklistener<ListItem> {
    override val fragmentLayoutResId: Int
        get() = R.layout.user_review_fragment_layout
    private val viewModel: UserReviewViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.userReviewLivedata.observe(viewLifecycleOwner,observer)
        viewModel.callUserReview()
    }

    override fun receivedResponse(item: Any?) {
        item?.let {
            adapter.setItems(viewModel.usersFoodReviewForTracking)
        }
    }
}