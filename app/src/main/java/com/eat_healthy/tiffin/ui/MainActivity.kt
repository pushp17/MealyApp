package com.eat_healthy.tiffin.ui

import android.R.id
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.eat_healthy.tiffin.R
import com.eat_healthy.tiffin.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() , PaymentResultWithDataListener {
    lateinit var activityMainBinding: ActivityMainBinding
    var navView: BottomNavigationView?=null
    //var navController: NavController?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        activityMainBinding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        navView = activityMainBinding.navView
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        //navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.monthlyFoodSelectionFragment, R.id.navigation_account
            )
        )
      //  setupActionBarWithNavController(navController, appBarConfiguration)
        navController?.let { navView?.setupWithNavController(it) }
        navController?.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.foodSelectionFragment -> navView?.visibility = View.GONE
                R.id.completeAddressFragment -> navView?.visibility = View.GONE
                R.id.cartListItemsBottomSheet -> navView?.visibility = View.GONE
                R.id.loginFragment -> navView?.visibility = View.GONE
                R.id.otpFragment -> navView?.visibility = View.GONE
                R.id.orderSummaryFragment -> navView?.visibility = View.GONE
                R.id.privacyAndTermsConditionFragment->navView?.visibility = View.GONE
                R.id.referalFragment->navView?.visibility = View.GONE
                R.id.walletFragment->navView?.visibility = View.GONE
                R.id.orderSuccessFragment->navView?.visibility = View.GONE
                R.id.userSuggestionFragment->navView?.visibility = View.GONE
                else -> navView?.visibility = View.VISIBLE
            }
        }
        Checkout.preload(applicationContext)
    }

    fun showLoading() {
        activityMainBinding.pbProgress.let {
            it.visibility = View.VISIBLE
        }
    }

    fun hideLoading() {
        activityMainBinding.pbProgress.let {
            if(it.isVisible)
                it.visibility = View.GONE
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onPaymentSuccess(paymentId: String?, paymentData: PaymentData?) {
        val navHostFragment: Fragment? = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)
        (navHostFragment?.childFragmentManager?.fragments?.getOrNull(0) as OrderSummaryFragment).invokePlaceOrder(paymentData?.orderId,paymentId)
       // (navHostFragment?.childFragmentManager?.fragments?.getOrNull(0) as OrderSummaryFragment).placeOrder(paymentData?.orderId,paymentId,true)
    }

    override fun onPaymentError(p0: Int, p1: String?, paymentData: PaymentData?) {
        val navHostFragment: Fragment? = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)
        (navHostFragment?.childFragmentManager?.fragments?.getOrNull(0) as OrderSummaryFragment).invokePlaceOrder(paymentData?.orderId,paymentData?.paymentId)
       // (navHostFragment?.childFragmentManager?.fragments?.getOrNull(0) as OrderSummaryFragment).placeOrder(paymentData?.orderId,paymentData?.paymentId,false)
    }

//    override fun onResume() {
//        super.onResume()
//        val navController = findNavController(R.id.nav_host_fragment)
//        val appBarConfiguration = AppBarConfiguration(navController.graph)
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
//        supportActionBar!!.title = getString(R.string.news)
//    }
//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment)
//        return navController.navigateUp() || super.onSupportNavigateUp()
//    }
}