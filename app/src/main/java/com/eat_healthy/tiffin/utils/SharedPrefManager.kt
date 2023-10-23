package com.eat_healthy.tiffin.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPrefManager
constructor(context: Context)
{
    companion object {
        private const val MESSO_SHARED_PREFERENCE="tiffinSharedPreference"
    }
    private var sharedPreferences: SharedPreferences
    init {
        sharedPreferences= context.getSharedPreferences(MESSO_SHARED_PREFERENCE, Context.MODE_PRIVATE)
    }
    fun addStringToPreference(key:String, value:String){
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun addStringListToPreference(key: String, value: List<String>) {
        val set = mutableSetOf<String>()
        set.addAll(value)
        sharedPreferences.edit().putStringSet(key, set).apply()
    }

    fun getStringListFromPreference(key: String): List<String>? {
        return sharedPreferences.getStringSet(key, null)?.toList()
    }

    fun addIntToPreference(key:String, value:Int){
        sharedPreferences.edit().putInt(key, value).apply()
    }
    fun addLongToPreference(key:String, value:Long){
        sharedPreferences.edit().putLong(key, value).apply()
    }

    fun addBooleanToPreference(key:String, value:Boolean){
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    inline fun <reified T> addModelClass(key:String,genricModelClass: T){
        addStringToPreference(key,AppUtils.toJson(genricModelClass))
    }

    inline fun <reified T> getModelClass(key:String): T?{
       return AppUtils.fromJson(getStringFromPreference(key,""))
    }

    fun getBooleanFromPreference(key:String):Boolean{
        return sharedPreferences.getBoolean(key,false)
    }

    fun getStringFromPreference(key:String, defValue:String): String? {
        return sharedPreferences.getString(key,defValue)
    }

    fun getIntFromPreference(key:String):Int{
        return sharedPreferences.getInt(key,0)
    }

    fun getLongFromPreference(key:String):Long{
        return  sharedPreferences.getLong(key,0L)
    }

    fun clearAllData(){
        sharedPreferences.edit().clear().apply()
    }

}
