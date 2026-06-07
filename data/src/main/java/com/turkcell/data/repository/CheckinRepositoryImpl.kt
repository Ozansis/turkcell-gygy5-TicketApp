package com.turkcell.data.repository

import com.turkcell.core.domain.checkIn.CheckinRepository
import com.turkcell.core.domain.checkIn.CheckinResult
import com.turkcell.core.domain.event.Event
import com.turkcell.data.dto.CheckinScanRequestDto
import com.turkcell.data.mapper.toDomain
import com.turkcell.data.remote.CheckInApi
import com.turkcell.data.util.runCatchingApi

class CheckinRepositoryImpl(
    private val checkinApi: CheckInApi

) : CheckinRepository {
    override suspend fun getAssignedEvents(): Result<List<Event>> =
        runCatchingApi { checkinApi.getAssignedEvents() }.map { list -> list.map { it.toDomain() } }

    override suspend fun scan(qrCode: String): Result<CheckinResult> =
        runCatchingApi {
            checkinApi.scanTicket(CheckinScanRequestDto(qrCode))
        }.map { it -> it.toDomain() }

}
