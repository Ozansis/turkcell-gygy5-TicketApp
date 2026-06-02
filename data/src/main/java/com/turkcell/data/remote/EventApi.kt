package com.turkcell.data.remote

import com.turkcell.core.domain.event.Event
import com.turkcell.data.dto.EventDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EventApi {

    @GET("/events")
    suspend fun getEvents(): List<EventDto>

    @GET("/events/{id}")
    suspend fun getEvent(@Path("id") id: String): EventDto
}