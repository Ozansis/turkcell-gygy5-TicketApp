package com.turkcell.core.domain.event

interface EventRepository{
    suspend fun getEvents(upcoming: Boolean = true  ): Result<List<Event>>
}