package com.eat_healthy.tiffin.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.eat_healthy.tiffin.R
import com.eat_healthy.tiffin.databinding.FragmentAccountBinding
import com.eat_healthy.tiffin.utils.Constants

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val navigationController=findNavController()
        val root: View = binding.root
        binding.llPrivacy.setOnClickListener {
            val uri: Uri = Uri.parse("https://www.notion.so/Privacy-Policy-for-Mealy-1d02b4e077ce808dba6ef109d449890b?pvs=4") // missing 'http://' will cause crashed
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
        binding.llTerms.setOnClickListener {
            navigationController.navigate(R.id.action_navigation_account_to_privacyAndTermsConditionFragment)
        }
        binding.llAddress.setOnClickListener {
            val bundle = bundleOf(Constants.SCREEN_ENTRY_POINT to Constants.ACCOUNT)
            navigationController.navigate(R.id.action_navigation_account_to_completeAddressFragment,bundle)
        }

        binding.llOrder.setOnClickListener {
            navigationController.navigate(R.id.action_navigation_account_to_myOrderHisoryFragment)
        }

        binding.llProfile.setOnClickListener {
            navigationController.navigate(R.id.action_navigation_account_to_profileFragment)
        }

        binding.llContactUs.setOnClickListener {
            navigationController.navigate(R.id.action_navigation_account_to_contactUsFragment)
        }
        binding.llSuggestion.setOnClickListener {
            navigationController.navigate(R.id.action_navigation_account_to_userSuggestionFragment)
        }

        binding.llMonthly.setOnClickListener {
            navigationController.navigate(R.id.action_navigation_account_to_monthlyFoodSelectionFragment)
        }

        binding.llUserReview.setOnClickListener {
            navigationController.navigate(R.id.action_navigation_account_to_userReviewFragment)
        }

        binding.llDelivery.setOnClickListener {
            navigationController.navigate(R.id.action_navigation_account_to_deliveryFragment)
        }


        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun requestPermission(){
        //  READ and WRITE external storage permission runtime permission
    }
}