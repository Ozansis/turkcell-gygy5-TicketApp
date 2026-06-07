package com.turkcell.ticketapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkcell.core.domain.auth.AuthRepository
import com.turkcell.core.domain.event.Event
import com.turkcell.core.domain.event.EventRepository
import com.turkcell.core.domain.event.Ticket
import com.turkcell.core.domain.event.TicketRepository
import com.turkcell.ticketapp.R
import com.turkcell.ticketapp.util.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class HomeUiState(
    val events: List<Event> = emptyList(),
    val tickets: List<Ticket> = emptyList(),
    val isEventsLoading: Boolean = false,
    val isTicketsLoading: Boolean = false,
    val errorMessage: String? = null,
    val selectedTab: Int = 0
)


class HomeViewModel(
    private val application: Application,
    private val ticketRepository: TicketRepository,
    private val eventRepository: EventRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    init {
        loadEvent()
        loadTicket()

    }

    fun loadEvent() {

        _state.update { it.copy(isEventsLoading = true, errorMessage = null) }


        viewModelScope.launch {
            eventRepository.getEvents().fold(
                onSuccess = { list ->
                    _state.update { it.copy(events = list, isEventsLoading = false, errorMessage = null) }
                },
                onFailure = { e ->
                    _state.update {
                        it.copy(
                            isEventsLoading = false,
                            errorMessage = e.toUserMessage(application)
                        )
                    }
                }
            )
        }
    }
    fun loadTicket() {

        _state.update { it.copy(isTicketsLoading = true, errorMessage = null) }


        viewModelScope.launch {
            ticketRepository.getMyTickets().fold(
                onSuccess = { list ->
                    _state.update { it.copy(tickets = list, isTicketsLoading = false, errorMessage = null) }
                },
                onFailure = { e ->
                    _state.update {
                        it.copy(
                            isTicketsLoading = false,
                            errorMessage = e.toUserMessage(application)
                        )
                    }
                }
            )
        }
    }

    fun onTabSelected(index: Int) = _state.update { it.copy(selectedTab = index) }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }


}
