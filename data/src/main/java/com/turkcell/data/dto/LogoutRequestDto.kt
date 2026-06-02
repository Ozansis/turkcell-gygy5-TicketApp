package com.turkcell.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class LogoutRequestDto(val refreshToken: String)
