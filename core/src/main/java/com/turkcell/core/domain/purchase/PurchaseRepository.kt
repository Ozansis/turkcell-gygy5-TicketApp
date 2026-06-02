package com.turkcell.core.domain.purchase

interface PurchaseRepository {

    suspend fun createPurchase(quantities:Map<String, Int>) : Result<Purchase>

    suspend fun pay(purchaseId: String): Result<Purchase>

    suspend fun getPurchase(id: String) : Result<Purchase>

}