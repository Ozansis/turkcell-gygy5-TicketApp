package com.turkcell.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class EventInCheckinDto(
    val id: String,
    val name: String,
    val venue: String,
    val startsAt: String,
)

