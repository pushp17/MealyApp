package com.eat_healthy.tiffin.ui

import android.R
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.eat_healthy.tiffin.utils.DataState
import com.eat_healthy.tiffin.utils.SharedPrefManager
import com.eat_healthy.tiffin.utils.hideLoading
import com.eat_healthy.tiffin.utils.showLoading
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject


abstract class BaseFragment : Fragment() {
    @Inject
    lateinit var sharedPrefManager: SharedPrefManager
    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    open val observer = Observer<DataState<*>> {
        when (it) {
            is DataState.Loading -> {
                showLoading()
            }
            is DataState.Error<*> -> {
                // Different error codes has been handled in BaseDataSource class .
                showToast("Something went wrong , please try after some time")
                hideLoading()
                receivedResponse(it.data)
            }
            is DataState.Success<*> -> {
                hideLoading()
                receivedResponse(it.data)
            }
        }
    }

    fun showToast(msg: String) {
      Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()


//        val view: View ? = toast.view
//
////Gets the actual oval background of the Toast then sets the colour filter
//
////Gets the actual oval background of the Toast then sets the colour filter
//        view?.background?.setColorFilter(YOUR_BACKGROUND_COLOUR, PorterDuff.Mode.SRC_IN)
//
////Gets the TextView from the Toast so it can be editted
//
////Gets the TextView from the Toast so it can be editted
//        val text = view.findViewById<TextView>(R.id.message)
//        text.setTextColor(YOUR_TEXT_COLOUR)
    }
    fun showLongToast(msg: String) {
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_LONG).show()
    }
    abstract fun receivedResponse(item: Any?)
}