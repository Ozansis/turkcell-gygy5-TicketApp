package com.turkcell.ticketapp.navigation

import kotlinx.serialization.Serializable


@kotlinx.serialization.Serializable
object Login

@kotlinx.serialization.Serializable
object Register

@Serializable
object Home


@Serializable
data class EventDetail(val id: String)

@Serializable
data class TicketDetail(val id: String)