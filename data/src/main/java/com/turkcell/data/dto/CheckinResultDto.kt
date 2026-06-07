package com.turkcell.data.dto

import kotlinx.serialization.Serializable


@Serializable
data class CheckinResultDto(
    val ticketId: String,
    val ticketType: String,
    val event: EventInCheckinDto,
    val checkedInAt: String,
)
