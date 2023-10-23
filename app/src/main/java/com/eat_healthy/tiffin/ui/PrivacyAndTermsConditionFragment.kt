package com.eat_healthy.tiffin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.eat_healthy.tiffin.R
import com.eat_healthy.tiffin.databinding.ReferalLayoutBinding

class PrivacyAndTermsConditionFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = ReferalLayoutBinding.inflate(inflater,container,false)
        requireActivity().window?.statusBarColor = ContextCompat.getColor(requireContext(),R.color.referalBackgroundColour)
        return binding.root
    }

    override fun onPause() {
        requireActivity().window?.statusBarColor = ContextCompat.getColor(requireContext(),R.color.colorPrimary)
        super.onPause()
    }

    override fun onResume() {
        requireActivity().window?.statusBarColor = ContextCompat.getColor(requireContext(),R.color.referalBackgroundColour)
        super.onResume()
    }
}