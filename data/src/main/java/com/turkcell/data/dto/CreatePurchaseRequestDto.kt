package com.turkcell.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreatePurchaseRequestDto(
    val items: List<PurchaseItemRequestDto>


) {
}