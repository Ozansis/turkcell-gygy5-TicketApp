package com.turkcell.data.repository

import com.turkcell.core.domain.purchase.Purchase
import com.turkcell.core.domain.purchase.PurchaseRepository
import com.turkcell.data.dto.CreatePurchaseRequestDto
import com.turkcell.data.dto.PurchaseItemRequestDto
import com.turkcell.data.mapper.toDomain
import com.turkcell.data.remote.PurchaseApi
import com.turkcell.data.util.runCatchingApi


class PurchaseRepositoryImpl(
    private val purchaseApi: PurchaseApi
) : PurchaseRepository {
    override suspend fun createPurchase(quantities: Map<String, Int>): Result<Purchase> {
        val items = quantities.map { (ticketTypeId, quantity) ->
            PurchaseItemRequestDto(ticketTypeId, quantity)
        }
        return runCatchingApi {
            purchaseApi.createPurchase(CreatePurchaseRequestDto(items))
        }.map{it.toDomain()}
    }

    override suspend fun pay(purchaseId: String): Result<Purchase> = runCatchingApi {
        purchaseApi.pay(purchaseId)

    }.map { it -> it.toDomain() }

    override suspend fun getPurchase(id: String): Result<Purchase> =runCatchingApi {
        purchaseApi.getPurchase(id)

    }.map { it -> it.toDomain() }

}