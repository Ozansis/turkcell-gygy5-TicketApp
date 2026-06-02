package com.turkcell.data.mapper

import com.turkcell.core.domain.purchase.Purchase
import com.turkcell.core.domain.purchase.PurchaseItem
import com.turkcell.core.domain.purchase.PurchaseStatus
import com.turkcell.data.dto.PurchaseDto
import com.turkcell.data.dto.PurchaseItemDto


internal fun PurchaseDto.toDomain(): Purchase = Purchase(
    id = id,
    status = PurchaseStatus.fromApi(status),
    totalCents = totalCents,
    paidAt = paidAt,
    items = items.map { it.toDomain() }
)

internal fun PurchaseItemDto.toDomain(): PurchaseItem = PurchaseItem(
    id = id,
    ticketTypeId = ticketTypeId,
    quantity = quantity,
    unitPriceCents = unitPriceCents
)




