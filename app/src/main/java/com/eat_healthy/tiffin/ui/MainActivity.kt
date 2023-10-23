package com.eat_healthy.tiffin.ui

import android.R.id
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.eat_healthy.tiffin.R
import com.eat_healthy.tiffin.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity()  {
    lateinit var activityMainBinding: ActivityMainBinding
    var navView: BottomNavigationView?=null
    var navController: NavController?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        installSplashScreen()
        super.onCreate(savedInstanceState)
        activityMainBinding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        navView = activityMainBinding.navView

        navController = findNavController(R.id.nav_host_fragment_activity_main)
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
                else -> navView?.visibility = View.VISIBLE
            }
        }
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

//    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
//        Log.d("sxhsbxs","success")
//        val navHostFragment: Fragment? = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)
//        (navHostFragment?.childFragmentManager?.fragments?.getOrNull(0) as OrderSummaryFragment).placeOrder(p1?.orderId)
//    }
//
//    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
//        Log.d("sxhsbxs","error")
//    }

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