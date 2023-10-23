package com.eat_healthy.tiffin.ui
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails
import com.eat_healthy.tiffin.R
import com.eat_healthy.tiffin.adapter.ViewPagerHomeAdapter
import com.eat_healthy.tiffin.databinding.FragmentHomeBinding
import com.eat_healthy.tiffin.models.MealsApiRespone
import com.eat_healthy.tiffin.utils.AppUtils.rsAppendedValue
import com.eat_healthy.tiffin.utils.Constants
import com.eat_healthy.tiffin.utils.Constants.HOME_PAGE
import com.eat_healthy.tiffin.viewmodels.SharedViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class HomeFragment : BaseFragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    lateinit var navigationController: NavController
    private val sharedViewModel: SharedViewModel by activityViewModels()
    var window: Window? =null
    private lateinit var referrerClient: InstallReferrerClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  sharedViewModel.homePageData()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        navigationController = findNavController()
        window = activity?.window

        firebaseAnalytics.logEvent(HOME_PAGE,null)
        sharedViewModel.homePageData()
        sharedViewModel.userDetail=sharedPrefManager.getModelClass(Constants.USER_INFO)
        sharedViewModel.mealsLivedata.observe(viewLifecycleOwner, observer)
        binding.flAddItem.setOnClickListener {
            if (sharedPrefManager.getStringFromPreference(Constants.USER_CITY_LOCALITY, "")
                    ?.isNotEmpty() == true
            ) {
                navigationController.navigate(R.id.action_navigation_home_to_foodSelectionFragment)
            }else{
                navigationController.navigate(R.id.action_navigation_home_to_currentLocationMapFragment)
            }
        }

        referrerClient = InstallReferrerClient.newBuilder(requireActivity()).build()
        referrerClient.startConnection(object : InstallReferrerStateListener {

            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                when (responseCode) {
                    InstallReferrerClient.InstallReferrerResponse.OK -> {
                        // Connection established.
                        lifecycleScope.launch {
                            delay(9000)
                            val referrerDetails: ReferrerDetails = referrerClient.installReferrer
                            val referrerUrl = referrerDetails.installReferrer
                            Log.d("smdcnxsdhgfc","link:  "+referrerUrl)
                            showToast("link:  "+referrerUrl)
                        }
                    }
                    InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
                        // API not available on the current Play Store app.
                        Log.d("smdcnxsdhgfc","api not available")
                        showToast("api not available")
                    }
                    InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
                        // Connection couldn't be established.
                        Log.d("smdcnxsdhgfc","couldn't connect")
                        showToast("couldn't connect")
                    }
                }
            }

            override fun onInstallReferrerServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })

      //  val image:String?=null
       // createNotification("Subscription Expired","Please Subscribe to not stop you meal. Please Subscribe to not stop you meal. Please Subscribe to not stop you meal. Please Subscribe to not stop you meal.",image?.toUri())
        return root
    }


    private fun startViewPagerAdapter() {
        binding.homeViewpager.adapter = ViewPagerHomeAdapter(this)
        val tabTitle = arrayListOf("VEG- ".plus(rsAppendedValue(sharedViewModel.regularMealPrice)),
            "VEG- ".plus(rsAppendedValue(sharedViewModel.vegSpecialPrice)), "NON-VEG- ".plus(rsAppendedValue(sharedViewModel.nonVegPrice)))
        TabLayoutMediator(binding.homeTabLayout, binding.homeViewpager) { tab, position ->
            tab.text = tabTitle[position]
        }.attach()

        binding.homeViewpager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.homeTabLayout.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.colorPrimary))
                window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                 if(window !=null){
                     when (position) {
                         1 -> {
                             binding.homeTabLayout.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.colorPrimary_3))
                             binding.tvHeading.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.colorPrimary_3))
                             window?.statusBarColor = ContextCompat.getColor(requireContext(),R.color.colorPrimary_3)
                             sharedViewModel.selectedPrice=sharedViewModel.vegSpecialPrice
                         }
                         2 -> {
                             binding.homeTabLayout.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.colorPrimary_2))
                             binding.tvHeading.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.colorPrimary_2))
                             window?.statusBarColor = ContextCompat.getColor(requireContext(),R.color.colorPrimary_2)
                             sharedViewModel.selectedPrice=sharedViewModel.nonVegPrice
                         }
                         else -> {
                             binding.homeTabLayout.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.colorPrimary))
                             binding.tvHeading.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.colorPrimary))
                             window?.statusBarColor = ContextCompat.getColor(requireContext(),R.color.colorPrimary)
                             sharedViewModel.selectedPrice=sharedViewModel.regularMealPrice
                         }
                     }
                 }
                sharedViewModel.mealCategoryTabPosition=position
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })
    }

    override fun receivedResponse(item: Any?) {
        item?.let { response ->
            when (response) {
                is MealsApiRespone -> {
                    updateUI(response)
                }
            }
        }
    }

    private fun updateUI(response:MealsApiRespone){
        if (!sharedViewModel.doesShowlandingpageAsRegisterValueUpdated) {
            sharedViewModel.doesShowlandingpageAsRegisterValueUpdated = true
            sharedViewModel.showlandingpageAsRegister = response.showRegisterPage
        }
        sharedViewModel.statusMsg = response.statusMsg
        sharedViewModel.contact = response.contactNo
        sharedViewModel.status2 = response.statusMsg2
        sharedViewModel.monthlySubscriptionMsg = response.monthlySubscriptionMsg
        sharedViewModel.afterStartOrderTimeout = response.afterStartOrderTimeout
        sharedViewModel.aStartSingleMealStatusLunchTime = response.aStartSingleMealStatusLunchTime
        sharedViewModel.afterStartSingleMealStatusDinnerTime = response.afterStartSingleMealStatusDinnerTime
        sharedViewModel.afterRegistrationStatusMsg = response.afterRegistrationStatusMsg
        sharedViewModel.registerSuccessMsg = response.registerSuccessMsg
        sharedViewModel.isServiceStarted = response.isServiceStarted

        if((getVersionName() < response.appVersionName) && response.playStoreLink !=null){
            showAppUpdateDialog(Constants.PLAYSTORE_LINK)
        }
        sharedViewModel.regularMealPrice=response.regularMealPrice?:"59"
        sharedViewModel.vegSpecialPrice=response.vegSpecialPrice?:"79"
        sharedViewModel.nonVegPrice=response.nonVegPrice?:"89"
        sharedViewModel.selectedPrice=sharedViewModel.regularMealPrice
        sharedViewModel.frameDataForHomePage(response)
        startViewPagerAdapter()
    }

    private fun showAppUpdateDialog(playStoreLink:String){
        val alertDialog = SweetAlertDialogV2(
            requireActivity(),
            SweetAlertDialogV2.CUSTOM_IMAGE_TYPE
        )
        alertDialog.setCancelable(false)
        alertDialog.setCustomImage(R.drawable.info_1)
        alertDialog.titleText = "Update"
        alertDialog.contentText = "We are pleased to inform you that we have started our services now so we request you to please UPDATE the app to start ordering."
        alertDialog.show()
        alertDialog.setConfirmClickListener {
            val uri: Uri =
                Uri.parse(playStoreLink)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }
    private fun  getVersionName() = requireActivity().packageManager.getPackageInfo(requireActivity().packageName,0).versionName.toDoubleOrNull()?:0.0

    override fun onPause() {
        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window?.statusBarColor = ContextCompat.getColor(requireContext(),R.color.colorPrimary)
        super.onPause()
    }
    override fun onDestroyView() {
        referrerClient.endConnection()
        super.onDestroyView()
        _binding = null
    }
}
