package com.turkcell.core.domain.checkIn

import com.turkcell.core.domain.event.Event


interface CheckinRepository {
    suspend fun getAssignedEvents(): Result<List<Event>>
    suspend fun scan(qrCode: String): Result<CheckinResult>
}
