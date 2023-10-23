package com.eat_healthy.tiffin.ui

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.eat_healthy.tiffin.R
import com.eat_healthy.tiffin.adapter.AddressAdapter
import com.eat_healthy.tiffin.databinding.CompleteAddressFragmentBinding
import com.eat_healthy.tiffin.genericFiles.ListItem
import com.eat_healthy.tiffin.models.*
import com.eat_healthy.tiffin.utils.AppUtils.generateRandomNumber
import com.eat_healthy.tiffin.utils.Constants
import com.eat_healthy.tiffin.viewmodels.SharedViewModel
import com.eat_healthy.tiffin.viewmodels.UserAddressViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class CompleteAddressFragment:ListViewFragment<AddressAdapter,CompleteAddressFragmentBinding>() {
    override val fragmentLayoutResId: Int
        get() = R.layout.complete_address_fragment
    private val userAddressViewModel: UserAddressViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var addressList = arrayListOf<UserAddress>()
    private var monthlyUser: Boolean? = false
    private var selectedCityAndLocality1 = ""
    private var selectedCityAndLocality2 = ""
    private var isAccountEntryPoint: Boolean? = false
    private var availableLocation: List<String>? = null
    private var isFirstTimeUser: Boolean? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        monthlyUser = sharedViewModel.monthlyUser
        isAccountEntryPoint = arguments?.getString(Constants.SCREEN_ENTRY_POINT)?.equals(Constants.ACCOUNT)
        if (isAccountEntryPoint == true) {
            binding.tvContinue.visibility = View.INVISIBLE
            binding.tvContinue.text = "Update Address"
        }
        isFirstTimeUser = arguments?.getBoolean("isFirstTimeUser")
        availableLocation = sharedPrefManager.getStringListFromPreference(Constants.AVAILABLE_LOCALITY_LIST)
        userAddressViewModel.userAddressLiveData.observe(viewLifecycleOwner, observer)
        userAddressViewModel.availableLocalityLivedata.observe(viewLifecycleOwner, observer)
        userAddressViewModel.addressUploadLiveData.observe(viewLifecycleOwner, observer)
        if (availableLocation.isNullOrEmpty()) {
            userAddressViewModel.availablelocalistList()
        } else {
            setLayoutBasedOnUserType()
        }

        setflAddItem()
    }

    private fun setLayoutBasedOnUserType() {
        if (isFirstTimeUser == true) {
            setFirstTimeUserLayout()
        } else {
            userAddressViewModel.fetchUserAddress(
                User(
                    sharedViewModel.userDetail?.username,
                    sharedViewModel.userDetail?.mobileno
                )
            )
            this.let {
                adapter.setOnClickListener(it)
            }
        }
    }

//    var selectedLocality: String? = null

    private fun setFirstTimeUserLayout() {
        binding.cvAddAddress.visibility = View.VISIBLE
        availableLocation?.let {
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                it
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinlocality1.adapter = adapter
            }
        }
        binding.spinlocality1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                // your code here
                selectedCityAndLocality1 = availableLocation?.getOrNull(position)!!
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
            }
        }

//        var userAddress1: UserAddress? = null
        if (monthlyUser == true) {
            // Show "Add Address 2" layout
            binding.llAddAddressSuggestionForMonthlyUser.visibility = View.VISIBLE
            binding.llAddressType1.visibility = View.VISIBLE
            binding.llAddress2.visibility = View.VISIBLE
            availableLocation?.let {
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    it
                ).also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinlocality2.adapter = adapter
                }
            }
            binding.spinlocality2.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parentView: AdapterView<*>?,
                        selectedItemView: View?,
                        position: Int,
                        id: Long
                    ) {
                        // your code here
                        selectedCityAndLocality2 = availableLocation?.getOrNull(position)!!
                    }

                    override fun onNothingSelected(parentView: AdapterView<*>?) {
                    }
                }
        }
        setProccedClickListener()
    }

    override fun receivedResponse(item: Any?) {
        item?.let { response ->
            when (response) {
                is UserAddressResponse -> {
                    if (response.statusCode == 200) {
                        binding.rlUserAddressList.visibility = View.VISIBLE
                        // binding.cvAddAddress.visibility = View.VISIBLE
                        //  binding.rvParent.visibility = View.VISIBLE
                        response.mUserAddress?.let {
                            adapter.setItems(it.userAddressList)
                            addressList.addAll(it.userAddressList)
                        }
                        adapter.setOnClickListener(this)
                        if (isAccountEntryPoint != true) {
                            binding.tvContinue.visibility = View.VISIBLE
                        } else if (response.mUserAddress?.userAddressList?.isEmpty() == true) {
                            binding.ivEmptyScreen.visibility = View.VISIBLE
                            binding.tvEmptyScreen.visibility = View.VISIBLE
                        }
                        setContinueClickListener()
                    } else {
                        setFirstTimeUserLayout()
                    }
                }
                is AvailableLocalityResponse -> {
                    response.availableLocalityLIst?.let {
                        sharedPrefManager.addStringListToPreference(
                            Constants.AVAILABLE_LOCALITY_LIST,
                            it
                        )
                        availableLocation = it
                            setLayoutBasedOnUserType()
                    }
                }
                else -> {
                    showToast("Address updated successfully.")
                }
            }
        }
    }

    private fun setContinueClickListener() {
        val addressList = mutableListOf<UserAddress>()
        binding.tvContinue.setOnClickListener {
            addressList.addAll(
                (adapter.getItemsList()
                    .filter { (it as UserAddress).selected }) as List<UserAddress>
            )
            if (isAccountEntryPoint == true) {
                userAddressViewModel.updateUserAddress(
                    MUserAddress(
                        sharedViewModel.userDetail?.username!!,
                        sharedViewModel.userDetail?.mobileno!!,
                        addressList
                    )
                )
                return@setOnClickListener
            } else {
                val bundle = bundleOf(
                    "address" to MUserAddress(
                        sharedViewModel.userDetail?.username,
                        sharedViewModel.userDetail?.mobileno,
                        addressList
                    )
                )
                navigationController?.navigate(
                    R.id.action_completeAddressFragment_to_orderSummaryFragment,
                    bundle
                )
            }
        }
    }

    private fun setProccedClickListener() {
        binding.tvProceed.setOnClickListener {
            val userAddress1: UserAddress?
            val userAddress2: UserAddress?
            val addressType =
                if ((binding.cbLunch1.isChecked && binding.cbDinner1.isChecked) || (!binding.cbLunch1.isChecked && !binding.cbDinner1.isChecked))
                    Constants.LUNCH_DINNER
                else if (binding.cbLunch1.isChecked) {
                    Constants.LUNCH
                } else Constants.DINNER

            if (binding.etStreet1.text.toString().isNotEmpty()) {
                userAddress1 = UserAddress(
                    addressId = generateRandomNumber(),
                    streetNo = binding.etStreet1.text.toString(),
                    landmark = "near "+binding.etLandmark1.text.toString(),
                    cityAndLocality = selectedCityAndLocality1,
                    addressType = addressType,
                    selected = true
                )
                addressList.add(userAddress1)
            }

            if (monthlyUser == true) {
                val addressType =
                    if ((binding.cbLunch2.isChecked && binding.cbDinner2.isChecked) || (!binding.cbLunch2.isChecked && !binding.cbDinner2.isChecked))
                        Constants.LUNCH_DINNER
                    else if (binding.cbLunch2.isChecked) {
                        Constants.LUNCH
                    } else Constants.DINNER

                if (binding.etStreet2.text.toString().isNotEmpty()) {
                    userAddress2 = UserAddress(
                        addressId = generateRandomNumber(),
                        streetNo = binding.etStreet2.text.toString(),
                        landmark = "near "+binding.etLandmark2.text.toString(),
                        cityAndLocality = selectedCityAndLocality2,
                        addressType = addressType,
                        selected = true
                    )
                    addressList.add(userAddress2)
                }
            }
            if (isAccountEntryPoint == true) {
                userAddressViewModel.updateUserAddress(
                    MUserAddress(
                        sharedViewModel.userDetail?.username!!,
                        sharedViewModel.userDetail?.mobileno!!,
                        addressList
                    )
                )
                return@setOnClickListener
            } else {
                val bundle = bundleOf(
                    "address" to MUserAddress(
                        sharedViewModel.userDetail?.username,
                        sharedViewModel.userDetail?.mobileno,
                        addressList
                    )
                )
                navigationController?.navigate(
                    R.id.action_completeAddressFragment_to_orderSummaryFragment,
                    bundle
                )
            }
            // val bundle= bundleOf()
            // send addressList
            //  navigationController.navigate(R.id.)
        }
    }

    private fun setflAddItem() {
        binding.flAddItem.setOnClickListener {
            navigationController?.navigate(R.id.action_completeAddressFragment_to_addAddressBottomSheet)
        }
    }

    fun callbackFromAddressBottomSheet(userAddress: UserAddress?) {
        userAddress?.let { nonNullablesAddress ->
            if (addressList.isNotEmpty()) {

                // Checking here the old selected address with new Address and then updating the selection
                val addressList = addressList.apply {
                    when (nonNullablesAddress.addressType) {
                        Constants.LUNCH_DINNER -> {
                            forEach { it.selected = false }
                        }

                        Constants.LUNCH -> {
                            filter { it.addressType == Constants.LUNCH || it.addressType == Constants.LUNCH_DINNER }.forEach {
                                it.selected = false
                            }
                        }

                        Constants.DINNER -> {
                            filter { it.addressType == Constants.DINNER || it.addressType == Constants.LUNCH_DINNER }.forEach {
                                it.selected = false
                            }
                        }
                    }
                }
                addressList.add(0, nonNullablesAddress)
                adapter.setItems(addressList)
            } else {
                binding.ivEmptyScreen.visibility = View.GONE
                binding.tvEmptyScreen.visibility = View.GONE
                addressList.add(nonNullablesAddress)
                adapter.setItems(addressList)
            }
        }
        if (isAccountEntryPoint == true) {
            userAddressViewModel.updateUserAddress(
                MUserAddress(
                    sharedViewModel.userDetail?.username!!,
                    sharedViewModel.userDetail?.mobileno!!,
                    addressList
                )
            )
        }
    }

    override fun onClickItem(position: Int, item: ListItem?) {
        binding.tvContinue.visibility = View.VISIBLE
        item?.let { item ->
            if (item is UserAddress) {
                when (item.addressType) {
                    Constants.LUNCH_DINNER -> {
                        adapter.getItemsList().forEachIndexed { index, listItem ->
                            if ((listItem is UserAddress)) {
                                listItem.selected = false
                                adapter.notifyItemChanged(index)
                            }
                        }
                        item.selected = true
                        adapter.notifyItemChanged(position)
                    }
                    Constants.LUNCH -> {
                        adapter.getItemsList().forEachIndexed { index, listItem ->
                            if ((listItem as UserAddress).addressType.equals(Constants.LUNCH)
                                || listItem.addressType.equals(Constants.LUNCH_DINNER)) {
                                listItem.selected = false
                                adapter.notifyItemChanged(index)
                            }
                        }
                        item.selected = true
                        adapter.notifyItemChanged(position)
                    }

                    Constants.DINNER -> {
                        adapter.getItemsList().forEachIndexed { index, listItem ->
                            if ((listItem as UserAddress).addressType.equals(Constants.DINNER)
                                || listItem.addressType.equals(Constants.LUNCH_DINNER)) {
                                listItem.selected = false
                                adapter.notifyItemChanged(index)
                            }
                        }
                        item.selected = true
                        adapter.notifyItemChanged(position)
                    }
                }

            }
        }
    }
}


