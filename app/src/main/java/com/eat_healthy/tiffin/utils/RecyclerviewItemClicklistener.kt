package com.eat_healthy.tiffin.utils

interface RecyclerviewItemClicklistener<T> {
    fun onClickItem(position:Int,item: T?)
    fun onClickItem(position:Int,item: T?,id: String?)
}