package com.turkcell.core.domain.event

// Kullanıcının satın aldığı bileti temsil ediyor
// ticketType içindeki iç içe yapıyı düzleştirdim, ekranda direkt kullanabileyim diye
data class Ticket(
    val id: String,
    val qrCode: String,
    val status: TicketStatus,
    val usedAt: String?, // bilet kullanıldıysa dolu, kullanılmadıysa null

    // bilet türü bilgileri
    val ticketTypeName: String,
    val ticketTypePriceCents: Int,

    // etkinlik bilgileri — ticketType içinden çıkardım
    val eventId: String,
    val eventName: String,
    val venue: String,
    val startsAt: String
)