package com.turkcell.ticketapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkcell.core.domain.event.Event
import com.turkcell.core.domain.event.EventRepository
import com.turkcell.core.domain.event.Ticket
import com.turkcell.core.domain.event.TicketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class HomeUiState(
    val events: List<Event> = emptyList(),
    val tickets: List<Ticket> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val selectedTab: Int = 0
)


class HomeViewModel(
    private val ticketRepository: TicketRepository,
    private val eventRepository: EventRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    init {
        loadEvent()
        loadTicket()

    }

    fun loadEvent() {

        _state.update { it.copy(isLoading = true, errorMessage = null) }


        viewModelScope.launch {
            eventRepository.getEvents().fold(
                onSuccess = { list ->
                    _state.update { it.copy(events = list, isLoading = false, errorMessage = null) }
                },
                onFailure = { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.message ?: "Etkinlikler yüklenemedi."
                        )
                    }
                }
            )
        }
    }
    fun loadTicket() {

        _state.update { it.copy(isLoading = true, errorMessage = null) }


        viewModelScope.launch {
            ticketRepository.getMyTickets().fold(
                onSuccess = { list ->
                    _state.update { it.copy(tickets = list, isLoading = false, errorMessage = null) }
                },
                onFailure = { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.message ?: "Biletler yüklenemedi."
                        )
                    }
                }
            )
        }
    }

    fun onTabSelected(index: Int) = _state.update { it.copy(selectedTab = index) }


}
