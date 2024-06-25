package com.eat_healthy.tiffin.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.eat_healthy.tiffin.R
import com.eat_healthy.tiffin.adapter.WalletAdapter
import com.eat_healthy.tiffin.databinding.WalletLayoutBinding
import com.eat_healthy.tiffin.models.RefrerReferalHistoryResponse
import com.eat_healthy.tiffin.models.User
import com.eat_healthy.tiffin.models.UserDetailResponse
import com.eat_healthy.tiffin.utils.AppUtils
import com.eat_healthy.tiffin.viewmodels.ReferalViewModel
import com.eat_healthy.tiffin.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WalletFragment : ListViewFragment<WalletAdapter,WalletLayoutBinding>() {
    private val referalViewModel : ReferalViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    override val fragmentLayoutResId: Int
        get() = R.layout.wallet_layout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        referalViewModel.refrerReferalHistoryResponseLiveData.observe(viewLifecycleOwner,observer)
        sharedViewModel.userDetailsLiveData.observe(viewLifecycleOwner,observer)

        binding.tvReceivedMoney.text = AppUtils.rsAppendedValue(String.format("%.1f", sharedViewModel.userDetail?.totalReferalMoneyReceived?:0.0))
        binding.tvRedemeedMoney.text = AppUtils.rsAppendedValue(
            String.format("%.1f", (sharedViewModel.userDetail?.totalReferalMoneyReceived?.minus(
                sharedViewModel.userDetail?.referalMoney ?: 0.0
            ))?:0.0)
        )
        binding.tvWalletAmount.text= AppUtils.rsAppendedValue(sharedViewModel.userDetail?.referalMoney.toString()?:"0.0")
        referalViewModel.getRefrerReferalHistory(User(sharedViewModel.userDetail?.username,sharedViewModel.userDetail?.mobileno))
        if (sharedViewModel.userDetail?.isUserSignedIn == true) {
            sharedViewModel.getUserDetails(
                User(
                    sharedViewModel.userDetail?.username,
                    sharedViewModel.userDetail?.mobileno
                ), sharedPrefManager
            )
        }
    }

    override fun receivedResponse(item: Any?) {
        item.let {
            when(it){
                is RefrerReferalHistoryResponse -> {
                    if (it.statusCode == 200) {
                        adapter.setItems(it.refrerReferalHistory.refereeOrdersDetails)
                        binding.tvRefereHistoryHeader.visibility = View.VISIBLE
                    }
                }
                is UserDetailResponse ->{
                    if (it.statusCode == 200) {
                        binding.tvReceivedMoney.text =
                            AppUtils.rsAppendedValue(sharedViewModel.userDetail?.totalReferalMoneyReceived.toString())
                        binding.tvRedemeedMoney.text = AppUtils.rsAppendedValue(
                            (sharedViewModel.userDetail?.totalReferalMoneyReceived?.minus(
                                sharedViewModel.userDetail?.referalMoney ?: 0.0
                            )).toString()
                        )
                        binding.tvWalletAmount.text= AppUtils.rsAppendedValue(sharedViewModel.userDetail?.referalMoney.toString())
                    }
                }
            }
        }
    }
}