package com.eat_healthy.tiffin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.eat_healthy.tiffin.adapter.GenericStringListAdapter
import com.eat_healthy.tiffin.databinding.GenericStringListBottomsheetBinding
import com.eat_healthy.tiffin.models.StringText
import com.eat_healthy.tiffin.utils.Constants
import com.eat_healthy.tiffin.viewmodels.SharedViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GenericStringListBottomSheet : BottomSheetDialogFragment() {
    val sharedViewModel: SharedViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = GenericStringListBottomsheetBinding.inflate(inflater, container, false)
        val stringList = arguments?.getStringArrayList(Constants.GENERIC_STRING_ARRAYLIST)
        val header = arguments?.getString(Constants.GENERIC_HEADER)
        val genericStringListAdapter = GenericStringListAdapter()
        binding.rvParent.adapter = genericStringListAdapter
        val stringListItem = mutableListOf<StringText>()
        stringList?.forEach {
            stringListItem.add(StringText(it))
        }
        genericStringListAdapter.setItems(stringListItem)
        binding.tvHeader.text = header
        binding.ivClose.setOnClickListener {
            dismissAllowingStateLoss()
        }
        return binding.root
    }
}