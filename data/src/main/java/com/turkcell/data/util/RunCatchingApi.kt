package com.turkcell.data.util

import androidx.datastore.core.IOException
import com.turkcell.data.network.ApiException
import com.turkcell.data.network.NetworkException
import retrofit2.HttpException

suspend inline fun <T> runCatchingApi(crossinline block: suspend () -> T): Result<T> = try {
    Result.success(block())
} catch (e: HttpException) {
    Result.failure(ApiException(code = e.code(), errorMessage = e.message(), cause = e))
} catch (e: IOException) {
    Result.failure(NetworkException(e))
} catch (e: Exception) {
    Result.failure(e)
}