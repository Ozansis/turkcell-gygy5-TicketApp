package com.turkcell.data.dto

import kotlinx.serialization.Serializable

// Bilet içinden gelen bilet türü bilgisi — içinde etkinlik bilgisi de var
@Serializable
data class TicketTypeInTicketDto(
    val id: String,
    val name: String,
    val priceCents: Int,
    val event: EventInTicketDto
)
