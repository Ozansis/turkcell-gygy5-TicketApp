package com.turkcell.data.dto

import kotlinx.serialization.Serializable

// /me/tickets endpoint'inden gelen ham bilet verisi
@Serializable
data class TicketDto(
    val id: String,
    val qrCode: String,
    val status: String,
    val usedAt: String?,
    val ticketType: TicketTypeInTicketDto
)
