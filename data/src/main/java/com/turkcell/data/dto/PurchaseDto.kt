package com.turkcell.data.dto

import android.content.ClipData
import com.turkcell.core.domain.purchase.PurchaseStatus
import kotlinx.serialization.Serializable

@Serializable
data class PurchaseDto(
    val id : String,
    val status: String,
    val totalCents : Long,
    val paidAt : String?,
    val items:List<PurchaseItemDto>
) {
}