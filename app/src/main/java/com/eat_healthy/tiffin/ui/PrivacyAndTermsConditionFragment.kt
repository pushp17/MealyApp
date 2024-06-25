package com.eat_healthy.tiffin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.eat_healthy.tiffin.R
import com.eat_healthy.tiffin.databinding.PrivacyPolicyTermsConditionBinding
import com.eat_healthy.tiffin.databinding.ReferalLayoutBinding
import com.eat_healthy.tiffin.databinding.WalletLayoutBinding

class PrivacyAndTermsConditionFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = PrivacyPolicyTermsConditionBinding.inflate(inflater,container,false)
        return binding.root
    }

}