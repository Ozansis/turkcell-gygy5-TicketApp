package com.turkcell.ticketapp

import androidx.activity.result.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.turkcell.core.domain.AuthRepository
import kotlinx.coroutines.launch
import org.koin.dsl.module

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    fun login() {
        viewModelScope.launch {

            val result = authRepository.login("test@test.com", "123456")

            result.onSuccess { session ->
                println("Giriş Başarılı: ${session.accessToken}")
            }.onFailure { error ->
                println("Hata Oluştu: ${error.message}")
            }
        }
    }
}


//val appModule = module {
//    viewModel { LoginViewModel(get()) }
//
//
//}