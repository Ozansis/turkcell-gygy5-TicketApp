package com.turkcell.data.dto

import kotlinx.serialization.Serializable

// Bilet içinden gelen etkinlik bilgisi — /me/tickets endpoint'i için
@Serializable
data class EventInTicketDto(
    val id: String,
    val name: String,
    val place: String,
    val startsAt: String
)
