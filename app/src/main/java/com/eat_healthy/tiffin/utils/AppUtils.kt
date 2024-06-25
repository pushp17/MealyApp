package com.eat_healthy.tiffin.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.gson.Gson
import java.util.*

object AppUtils {
    /* To convert a class object into json string. */
    @JvmStatic
    inline fun <reified T> toJson(genricModelClass: T): String {
       val gson= Gson()
        return gson.toJson(genricModelClass,T::class.java)
    }

    inline fun <reified T> fromJson(json: String?): T? {
        val gson= Gson()
        return gson.fromJson(json,T::class.java)
    }

    @JvmStatic
    fun showSoftKeyboard(view: View) {
        val imm = view.context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    @JvmStatic
    fun hideSoftKeyboard(activity: Activity) {
        val inputManager = activity
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = activity.currentFocus
        if (view != null) {
            inputManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    @JvmStatic
    fun hideSoftKeyboard(view: View?) {
        if (view == null) return
        val inputManager = view.context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    fun generateRandomNumber():String{
        val numbers = "0123456789"
        val random = Random()
        val randomChar = CharArray(4)
        for (i in 0..3) {
            randomChar[i] = numbers[random.nextInt(numbers.length)]
        }
        return randomChar.toString()
    }

    fun rsAppendedValue(value:String?):String{
        return if(value==null) "" else "₹".plus(value)
    }
    fun rsAppendedValueWithRs(value:String?):String{
        return if(value==null) "" else "Rs ".plus(value)
    }
    fun getStringValueInNextLine(value:String?):String?{
        val formatedValue = value?.replace("\\n", System.lineSeparator());
        return formatedValue
    }

    fun joinStringListWithComma(strings: List<String>): String {
        return strings.joinToString(", ")
    }
}