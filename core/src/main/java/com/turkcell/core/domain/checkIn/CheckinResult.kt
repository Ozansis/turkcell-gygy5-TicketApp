package com.turkcell.core.domain.checkIn

data class CheckinResult(
    val ticketId: String,
    val ticketType: String,
    val eventName: String,
    val venue: String,
    val startsAt: String,
    val checkedInAt: String
) {
}