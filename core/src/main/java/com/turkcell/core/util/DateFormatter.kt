package com.turkcell.core.util

import java.text.SimpleDateFormat
import java.util.Locale


fun formatEventDate(isoDate: String): String {
    return try {
        val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val output = SimpleDateFormat("d MMMM yyyy, HH:mm", Locale("tr"))
        val date = input.parse(isoDate)
        output.format(date!!)
    } catch (e: Exception) {
        isoDate
    }
}