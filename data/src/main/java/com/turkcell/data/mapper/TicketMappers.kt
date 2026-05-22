package com.turkcell.data.mapper

import com.turkcell.core.domain.event.Ticket
import com.turkcell.data.dto.TicketDto
import com.turkcell.core.domain.event.TicketStatus

internal fun TicketDto.toDomain(): Ticket = Ticket(
    id = id,
    qrCode = qrCode,
    status = TicketStatus.fromApi(status),
    usedAt = usedAt,
    ticketTypeName = ticketType.name,
    ticketTypePriceCents = ticketType.priceCents,

    eventId = ticketType.event.id,
    eventName = ticketType.event.name,
    venue = ticketType.event.place,
    startsAt = ticketType.event.startsAt
)