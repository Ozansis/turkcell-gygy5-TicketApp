package com.turkcell.core.domain.purchase

data class Purchase(
    val id: String,
    val status: PurchaseStatus,
    val totalCents: Long,
    val paidAt: String?,
    val items: List<PurchaseItem>

)
