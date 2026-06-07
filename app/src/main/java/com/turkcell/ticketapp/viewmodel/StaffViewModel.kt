package com.turkcell.ticketapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.turkcell.core.domain.checkIn.CheckinRepository
import com.turkcell.core.domain.checkIn.CheckinResult
import com.turkcell.core.domain.event.Event
import androidx.lifecycle.viewModelScope

import com.turkcell.ticketapp.util.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class StaffUiState(

    val events: List<Event> = emptyList(),
    val isLoading: Boolean = false,
    val scanResult: CheckinResult? = null,
    val errorMessage: String? = null


)

class StaffViewModel(
    private val application: Application,
    private val checkinRepository: CheckinRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(StaffUiState())
    val state: StateFlow<StaffUiState> = _state.asStateFlow()

    init {
        loadAssignedEvents()
    }

    fun loadAssignedEvents() {
        _state.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            checkinRepository.getAssignedEvents().fold(
                onSuccess = { list ->
                    _state.update { it.copy(events = list, isLoading = false, errorMessage = null) }

                },
                onFailure = { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.toUserMessage(application)
                        )
                    }
                }
            )
        }
    }

    fun scan(qrcode: String){

        _state.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            checkinRepository.scan(qrcode).fold(
                onSuccess = { qr ->
                    _state.update { it.copy(scanResult = qr, isLoading = false, errorMessage = null) }
                },
                onFailure = { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.toUserMessage(application)
                        )
                    }
                }
            )
        }


    }


}




