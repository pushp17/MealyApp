package com.eat_healthy.tiffin.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.eat_healthy.tiffin.R
import com.eat_healthy.tiffin.databinding.LoginBinding
import com.eat_healthy.tiffin.models.LoginResponse
import com.eat_healthy.tiffin.models.User
import com.eat_healthy.tiffin.utils.AppUtils.getStringValueInNextLine
import com.eat_healthy.tiffin.utils.Constants
import com.eat_healthy.tiffin.viewmodels.LoginViewModel
import com.eat_healthy.tiffin.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment() {
    private var binding : LoginBinding?=null
    private val loginViewModel: LoginViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var navigationController:NavController
    private val mobileNoValidityRegex=Regex("^[0-9]{10}$")
    private var user: User? = null
    private var screenEntryPoint: String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LoginBinding.inflate(inflater, container, false)
        navigationController = findNavController()
        firebaseAnalytics.logEvent(Constants.LOGIN_PAGE,null)
        if (sharedViewModel.showlandingpageAsRegister == true){
            binding?.rlStatus?.visibility=View.VISIBLE
            binding?.tvStatus?.text = getStringValueInNextLine(sharedViewModel.statusMsg)
        }
        loginViewModel.userLoginResponseLiveData.observe(viewLifecycleOwner, observer)
        screenEntryPoint = arguments?.getString(Constants.SCREEN_ENTRY_POINT)
        binding?.privacyPolicy?.setOnClickListener {
             val uri: Uri = Uri.parse("https://uday-mallikarjun.in/mealy/privacy-policy.html") // missing 'http://' will cause crashed
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
        if (screenEntryPoint?.equals(Constants.HOME_PAGE) == true){
            binding?.tvProceed?.text=getString(R.string.register)
            binding?.tvLoginHeader?.text=getString(R.string.register)
            firebaseAnalytics.logEvent(Constants.LOGIN_PAGE_FROM_HOME_FOR_RAJNI,null)
            firebaseAnalytics.logEvent(Constants.LOGIN_PAGE_FROM_HOME,null)
        }
        binding?.tvProceed?.setOnClickListener {
            if (binding?.etMobileno?.text.toString().matches(mobileNoValidityRegex)) {
                if (binding?.etUsername?.text.toString().isNotEmpty()) {
                    user = User(
                        binding?.etUsername?.text.toString(),
                        "91"+binding?.etMobileno?.text.toString()
                    )
                    user?.let { loginViewModel.login(it) }
                    firebaseAnalytics.logEvent(Constants.REGISER_BUTTON_CLICK,null)
                } else {
                    showToast("Please Enter a your name.")
                }
            } else {
                showToast("Please Enter a valid 10 digit number.")
            }
        }
        return binding?.root
    }
    override fun receivedResponse(item: Any?) {
        item?.let { response->
            when(response){
                is LoginResponse->{
                    if(response.statusCode==200){
                        sharedViewModel.showlandingpageAsRegister=false
                        firebaseAnalytics.logEvent(Constants.REGISTERED_SUCCESSFULLY,null)
                            val bundle = bundleOf(
                                "userData" to user,
                                Constants.SCREEN_ENTRY_POINT to screenEntryPoint
                            )
                            navigationController.navigate(
                                R.id.action_loginFragment_to_otpFragment,
                                bundle
                            )

                    }
                }
            }
        }
    }
}