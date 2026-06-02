package com.turkcell.ticketapp.viewmodel

import android.util.Log.i
import androidx.compose.runtime.currentComposer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkcell.core.domain.event.Event
import com.turkcell.core.domain.event.EventRepository
import com.turkcell.core.domain.event.Ticket
import com.turkcell.core.domain.event.TicketType
import com.turkcell.core.domain.purchase.PurchaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class EventDetailUiState(
    val event: Event? = null,
    val isLoading: Boolean = false,
    val isPaid: Boolean = false,
    val errorMessage: String? = null,
    val pendingPurchaseId: String? = null,
    val ticketQuantities: Map<String, Int> = emptyMap(),
) {
    val totalCents: Long
        get() = ticketQuantities.entries.sumOf { (ticketId, adet) ->
            val ticketType = event?.ticketTypes?.find { it.id == ticketId }
            (ticketType?.priceCents ?: 0L) * adet
        }

}

class EventDetailViewModel(private val eventRepository: EventRepository,private val purchaseRepository: PurchaseRepository, id: String) : ViewModel() {


    private val _state = MutableStateFlow(EventDetailUiState())
    val state: StateFlow<EventDetailUiState> = _state.asStateFlow()

    init {
        loadEvent(id)
    }

    fun loadEvent(id: String) {

        _state.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            eventRepository.getEventById(id).fold(
                onSuccess = { event ->
                    _state.update { it.copy(event = event, isLoading = false) }

                },
                onFailure = { error ->
                    _state.update {
                        it.copy(isLoading = false, errorMessage = error.toUserMessage())
                    }

                }
            )
        }

    }

    fun increaseTicket(ticketType: TicketType) {
        _state.update { current ->
            val currentQty = current.ticketQuantities[ticketType.id] ?: 0
            val max = minOf(20, ticketType.remaining.toInt())
            if (currentQty >= max) return@update current
            val updated = current.ticketQuantities + (ticketType.id to currentQty + 1)
            current.copy(
                ticketQuantities = updated
            )


        }

    }

    fun decreaseTicket(ticket: TicketType) {
        _state.update { current ->
            val qty = current.ticketQuantities[ticket.id] ?: 0
            if (qty == 0) return@update current
            val updated = current.ticketQuantities + (ticket.id to qty - 1)

            current.copy(
                ticketQuantities = updated,
            )
        }
    }

    fun createPurchase() {
        val quantities = _state.value.ticketQuantities
        if (quantities.isEmpty()) return

        _state.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            purchaseRepository.createPurchase(quantities).fold(
                onSuccess = { purchase ->
                    _state.update { it.copy(isLoading = false, pendingPurchaseId = purchase.id) }
                },
                onFailure = { error ->
                    _state.update { it.copy(isLoading = false, errorMessage = error.toUserMessage()) }
                }
            )
        }
    }

    fun pay(purchaseId: String) {
        _state.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            purchaseRepository.pay(purchaseId).fold(
                onSuccess = {
                    _state.update { it.copy(isLoading = false, isPaid = true) }
                },
                onFailure = { error ->
                    _state.update { it.copy(isLoading = false, errorMessage = error.toUserMessage()) }
                }
            )
        }
    }


}