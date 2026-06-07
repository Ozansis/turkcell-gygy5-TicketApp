package com.turkcell.ticketapp.di

import com.turkcell.ticketapp.viewmodel.EventDetailViewModel
import com.turkcell.ticketapp.viewmodel.HomeViewModel
import com.turkcell.ticketapp.viewmodel.LoginViewModel
import com.turkcell.ticketapp.viewmodel.RegisterViewModel
import com.turkcell.ticketapp.viewmodel.StaffViewModel
import com.turkcell.ticketapp.viewmodel.TicketDetailViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


val appModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::StaffViewModel)
    viewModel { params -> EventDetailViewModel(get(), get(), get(), params.get()) }
    viewModel { params -> TicketDetailViewModel(get(), get(), params.get()) }
}
