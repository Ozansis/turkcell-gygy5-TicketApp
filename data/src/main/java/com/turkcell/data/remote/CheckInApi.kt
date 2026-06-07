package com.turkcell.data.remote

import com.turkcell.data.dto.CheckinResultDto
import com.turkcell.data.dto.CheckinScanRequestDto
import com.turkcell.data.dto.EventDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CheckInApi {

    @GET("/checkin/events")
    suspend fun getAssignedEvents():List<EventDto>

    @POST("/checkin/scan")
    suspend fun scanTicket(@Body body: CheckinScanRequestDto) : CheckinResultDto
}

