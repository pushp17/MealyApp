package com.eat_healthy.tiffin.utils

sealed class DataState <out T> (val data: T?=null,val statusCode:Int=0,val errorMsg:String?=null){
    class Success<T>(data: T, statusCode:Int=0) : DataState<T>(data,statusCode)
    class Error<T>(statusCode:Int=0, data: T?=null,errorMsg:String?) : DataState<T>(data,statusCode,errorMsg)
    object Loading : DataState<Nothing>()
    object Default : DataState<Nothing>()
}