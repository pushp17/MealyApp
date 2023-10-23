package com.eat_healthy.tiffin.ui

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.eat_healthy.tiffin.databinding.AddAddressBottomsheetBinding
import com.eat_healthy.tiffin.models.UserAddress
import com.eat_healthy.tiffin.utils.AppUtils
import com.eat_healthy.tiffin.utils.Constants
import com.eat_healthy.tiffin.utils.SharedPrefManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddAddressBottomSheet: BottomSheetDialogFragment() {
    @Inject
    lateinit var sharedPrefManager:SharedPrefManager
    var navigationController: NavController?=null
    companion object{
        const val TAG="AddAddressBottomSheet"
    }
    lateinit var binding:AddAddressBottomsheetBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddAddressBottomsheetBinding.inflate(inflater, container, false)
        navigationController=findNavController()
        val availableLocation = sharedPrefManager.getStringListFromPreference(Constants.AVAILABLE_LOCALITY_LIST)
        var selectedCityAndLocality: String = ""
        availableLocation?.let {
            ArrayAdapter(
                requireContext(),
                R.layout.simple_spinner_item,
                it
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinlocality.adapter = adapter
            }
        }
        binding.spinlocality.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                // your code here
                selectedCityAndLocality = availableLocation?.getOrNull(position)!!
            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {
            }
        })
        binding.tvProceed.setOnClickListener {
            var  userAddress:UserAddress?=null
            val addressType =
                if ((binding.cbLunch.isChecked && binding.cbDinner.isChecked) || (!binding.cbLunch.isChecked && !binding.cbDinner.isChecked))
                    "Lunch & Dinner" else if (binding.cbLunch.isChecked) {
                    "Lunch"
                } else "Dinner"
            if (binding.etStreet.text.toString().isNotEmpty()) {
                 userAddress = UserAddress(
                     addressId= AppUtils.generateRandomNumber(),
                     streetNo=binding.etStreet.text.toString(),
                     landmark="near "+binding.etLandmark.text.toString(),
                     cityAndLocality=selectedCityAndLocality,
                     addressType=addressType,
                     selected=true
                )
            }
            requireParentFragment().childFragmentManager.fragments.forEach {
                if (it is CompleteAddressFragment) {
                    it.callbackFromAddressBottomSheet(userAddress)
                }
            }
           // ((reparentFragment?.parentFragment) as CompleteAddressFragment).callbackFromAddressBottomSheet(userAddress)
            dismiss()
        }
        return binding.root
    }
}