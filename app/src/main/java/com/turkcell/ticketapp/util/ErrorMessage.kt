package com.turkcell.ticketapp.util

import android.content.Context
import com.turkcell.data.network.ApiException
import com.turkcell.data.network.NetworkException
import com.turkcell.ticketapp.R

internal fun Throwable.toUserMessage(context: Context): String = when (this) {
    is ApiException -> when (code) {
        401 -> context.getString(R.string.error_invalid_credentials)
        403 -> context.getString(R.string.error_forbidden)
        404 -> context.getString(R.string.error_not_found)
        409 -> context.getString(R.string.error_conflict)
        in 500..599 -> context.getString(R.string.error_server)
        else -> context.getString(R.string.error_unexpected)
    }
    is NetworkException -> context.getString(R.string.error_no_internet)
    else -> message ?: context.getString(R.string.error_unknown)
}
