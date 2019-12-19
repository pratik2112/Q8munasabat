package com.q8munasabat.imagepicker.listener

internal interface ResultListener<T> {

    fun onResult(t: T?)
}