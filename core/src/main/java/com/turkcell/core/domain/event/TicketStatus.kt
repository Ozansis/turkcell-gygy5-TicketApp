package com.turkcell.core.domain.event


enum class TicketStatus {

    VALID, USED;


    companion object {

        fun fromApi(value: String?): TicketStatus = when (value?.uppercase()) {

            "VALID" -> TicketStatus.VALID
            "USED" -> TicketStatus.USED
            else -> TicketStatus.VALID


        }
    }
}