package com.eat_healthy.tiffin.utils

import androidx.fragment.app.Fragment
import com.eat_healthy.tiffin.ui.MainActivity

fun Fragment.showLoading() {
    when (activity) {
        is MainActivity -> (activity as MainActivity).showLoading()
    }
}

fun Fragment.hideLoading() {
    when (activity) {
        is MainActivity -> (activity as MainActivity).hideLoading()
    }
}
