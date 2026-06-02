package com.turkcell.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PurchaseItemRequestDto(
    val ticketTypeId: String,
    val quantity: Int
) {
}