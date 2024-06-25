package com.eat_healthy.tiffin.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.eat_healthy.tiffin.R
import com.eat_healthy.tiffin.databinding.OtpScreenBinding
import com.eat_healthy.tiffin.models.MOtp
import com.eat_healthy.tiffin.models.OtpVerification
import com.eat_healthy.tiffin.models.User
import com.eat_healthy.tiffin.models.UserDetail
import com.eat_healthy.tiffin.utils.AppUtils
import com.eat_healthy.tiffin.utils.AppUtils.hideSoftKeyboard
import com.eat_healthy.tiffin.utils.Constants
import com.eat_healthy.tiffin.viewmodels.LoginViewModel
import com.eat_healthy.tiffin.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OtpFragment : BaseFragment() {
    private var binding: OtpScreenBinding? = null
    private var otp: String? = null
    private var user:User?=null
    private val loginViewModel: LoginViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var navigationController:NavController
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = OtpScreenBinding.inflate(inflater, container, false)
        user = arguments?.getParcelable<User>("userData")
        loginViewModel.otpVerificationLiveData.observe(viewLifecycleOwner,observer)
        navigationController=findNavController()
        binding?.pinviewotp?.let { AppUtils.showSoftKeyboard(it) }
        firebaseAnalytics.logEvent(Constants.OTP_SUBMITTED_SUCCESSFULY, null)
        otp=binding?.pinviewotp?.getText().toString()
        binding?.pinviewotp?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                otp = binding?.pinviewotp?.text.toString()
                if (otp?.length == 4) {
                    hideSoftKeyboard(requireActivity())
                }
            }
        })
        binding?.tvProceed?.setOnClickListener {
            if (otp?.length == 4) {
                loginViewModel.verifyOtp(MOtp(user?.username, user?.mobileno, otp))
            } else {
                showToast("Please enter 4 digit OTP")
            }
        }
        return binding?.root
    }

    override fun receivedResponse(item: Any?) {
        item?.let { response ->
            when (response) {
                is OtpVerification -> {
                    if(response.statusCode == 200) {
                        firebaseAnalytics.logEvent(Constants.OTP_SUBMITTED_SUCCESSFULY, null)
                        val screenEntryPoint = arguments?.getString(Constants.SCREEN_ENTRY_POINT)
                        user?.let {
                            sharedViewModel.userDetail=UserDetail(it.username,it.mobileno,
                                true,sharedViewModel.monthlyUser,0.0)
                            sharedPrefManager.addModelClass(Constants.USER_INFO,sharedViewModel.userDetail)
                        }
                        val bundle= bundleOf("isFirstTimeUser" to response.firstTimeUser)

                        when (screenEntryPoint) {
                            Constants.HOME_PAGE_TAB -> {
                                if (sharedViewModel.monthlyUser == true) {
                                    if (response.firstTimeUser == true) {
                                        navigationController.navigate(
                                            R.id.action_otpFragment_to_completeAddressFragment, bundle
                                        )
                                    } else {
                                        navigationController.popBackStack()
                                    }
                                } else {
                                    navigationController.navigate(
                                        R.id.action_otpFragment_to_completeAddressFragment, bundle
                                    )
                                }
                            }
                            Constants.MONTHLY_USER_TAB -> {
                                if (response.firstTimeUser == true) {
                                    navigationController.navigate(
                                        R.id.action_otpFragment_to_completeAddressFragment, bundle
                                    )
                                } else {
                                    navigationController.popBackStack()
                                }
                            }
                            Constants.ACCOUNT -> {
                                showToast("Logged In Successfully")
                                navigationController.popBackStack(R.id.navigation_home, false)
                            }
                            Constants.REFER_EARN_FROM_HOME
                            -> {
                                navigationController.navigate(R.id.action_otpFragment_to_referalFragment)
                            }
                            else -> navigationController.popBackStack()
                        }


                    }else if (response.statusCode==201){
                        showToast("Invalid Otp")
                    }else {
                        showToast("Something went wrong")
                    }
                }

                else -> {}
            }
        }
    }
}
