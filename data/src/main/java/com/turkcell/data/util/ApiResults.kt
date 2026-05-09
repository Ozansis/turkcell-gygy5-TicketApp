package com.turkcell.data.util

import android.R.attr.data


// tüm api isteklerim tek kalıpta ilerlesin

// .success
// .complete
// .onSuccess

sealed interface ApiResults<out T> {

    data class Success<T>(val data: T) : ApiResults<T>
    data class Error(val error: Throwable) : ApiResults<Nothing>


}



inline fun <T, R> ApiResults<T>.fold(
    onSuccess: (T) -> R,
    onError: (Throwable) -> R
): R= when(this){
    is ApiResults.Success -> onSuccess(data)
    is ApiResults.Error -> onError(error)}