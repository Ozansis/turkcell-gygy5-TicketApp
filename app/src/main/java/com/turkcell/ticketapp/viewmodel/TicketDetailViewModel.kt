package com.turkcell.ticketapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkcell.core.domain.event.Ticket
import com.turkcell.core.domain.event.TicketRepository
import com.turkcell.ticketapp.util.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TicketDetailUiState(
    val ticket: Ticket? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class TicketDetailViewModel(
    private val application: Application,
    private val ticketRepository: TicketRepository,
    private val ticketId: String
) : ViewModel() {

    private val _state = MutableStateFlow(TicketDetailUiState())
    val state: StateFlow<TicketDetailUiState> = _state.asStateFlow()

    init {
        loadTicket()
    }

    fun loadTicket() {
        _state.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            ticketRepository.getTicket(ticketId).fold(
                onSuccess = { ticket ->
                    _state.update { it.copy(ticket = ticket, isLoading = false) }
                },
                onFailure = { error ->
                    _state.update { it.copy(isLoading = false, errorMessage = error.toUserMessage(application)) }
                }
            )
        }
    }
}
