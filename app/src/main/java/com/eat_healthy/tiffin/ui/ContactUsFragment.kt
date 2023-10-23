package com.eat_healthy.tiffin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.eat_healthy.tiffin.databinding.LayoutContactUsBinding
import com.eat_healthy.tiffin.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactUsFragment: BaseFragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       val binding= LayoutContactUsBinding.inflate(inflater,container,false)
        binding.tvCall.text="Call : "+(sharedViewModel.contact?:"9113584599")
        return binding.root
    }

    override fun receivedResponse(item: Any?) {
    }
}