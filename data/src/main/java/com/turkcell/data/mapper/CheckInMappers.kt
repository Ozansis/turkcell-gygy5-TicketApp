package com.turkcell.data.mapper

import com.turkcell.core.domain.checkIn.CheckinResult
import com.turkcell.data.dto.CheckinResultDto
import com.turkcell.data.dto.EventInCheckinDto


internal fun CheckinResultDto.toDomain(): CheckinResult = CheckinResult(
    ticketId = ticketId,
    ticketType = ticketType,
    eventName = event.name,
    venue = event.venue,
    startsAt = event.startsAt,
    checkedInAt = checkedInAt


)